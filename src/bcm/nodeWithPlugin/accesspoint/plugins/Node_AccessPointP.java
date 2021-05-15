package bcm.nodeWithPlugin.accesspoint.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import bcm.connexion.classes.ConnectionInformation;
import bcm.connexion.connectors.CommunicationConnector;
import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import bcm.node.accesspoint.ports.Node_AccessPointRoutingOutboundPort;
import bcm.node.connectors.NodeConnector;
import bcm.node.routing.connectors.RoutingConnector;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.routing.ports.Node_RoutingCommInboundPort;
import bcm.node.routing.ports.Node_RoutingOutBoundPort;
import bcm.node.routing.ports.Node_RoutingRoutingInboundPort;
import bcm.nodeWithPlugin.accesspoint.ports.Node_AccessPointCommInboundPort;
import bcm.nodeWithPlugin.accesspoint.ports.Node_AccessPointRoutingInboundPort;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.classes.Message;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.classes.Position;
import bcm.utils.nodeInfo.interfaces.PositionI;
import bcm.utils.routing.classes.RoutingInfo;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

/**
 * classe Node_AccessPointP qui  est le plugin "greffons" qui correspand au  nœud AccessPoint dans notre réseau
 * @author Nguyen, Belkhir
 **/
public class Node_AccessPointP extends AbstractPlugin {
	
	/** le port sortant du nœud access point **/
	protected Node_AccessPointOutboundPort napop;
	
	/** le port entrant "CommunicationCI" du nœud access point **/
	protected Node_AccessPointCommInboundPort napip;
	
	/** le port entrant du nœud access point **/
	protected Node_AccessPointRoutingInboundPort naprip;
	
	/** la liste des ports sortant "CommunicationCI" du nœud access point  **/
	protected List<Node_AccessPointCommOutboundPort> node_CommOBP = new ArrayList<>();
	
	/** l'adresse du nœud access point **/
	private NodeAddress address = new NodeAddress();
	
	/** la liste des adresses accessible à partir du nœud access point **/
	private List<ConnectionInformation> addressConnected = new ArrayList<>();
	
	/** la liste des adresses de la table de routage **/
	private Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	PositionI pointInitial;
	
	/** le nombre e tentative pour envoyais un message **/
	private int NumberOfNeighboorsToSend = 2;
	
	protected ComponentI owner;
	
	/**le nombre initial de thread pour la méthode UpdateRouting**/
	private int nbThreadUpdateRouting =1;
	
	/**le nombre initial de thread pour la méthode Connect**/
	private int nbThreadConnect = 1;
	
	/**le nombre initial de thread pour la méthode ConnectRouting**/
	private int nbThreadConnectRouting = 1;
	
	/**le nombre initial de thread pour la méthode TransmitMessage**/
	private int nbThreadTransmitMessage = 1;
	
	/**le nombre initial de thread pour la méthode HasRouteFor**/
	private int nbThreadHasRouteFor = 1;
	
	/**le nombre initial de thread pour la méthode Ping**/
	private int nbThreadPing = 1;
	
	/**le nombre initial de thread pour la méthode UpdateAccessPoint**/
	private int nbThreadUpdateAccessPoint =1 ;
	
	// Thread URI
	/**l'URI de la méthode UpdateRouting**/
	public static final String          UpdateRouting_URI       = "Update_Routing";
	
	/**l'URI de la méthode UpdateAccessPoint**/
	public static final String          UpdateAccessPoint_URI   = "Uodate_Access_Point";
	
	/**l'URI de la méthode ConnectRouting**/
	public static final String			ConnectRouting_URI 		= "Connexion_via_les_tables_de_routing" ;
	
	/**l'URI de la méthode Connect**/
	public static final String			Connect_URI            	= "Connexion" ;
	
	/**l'URI de la méthode TransmitMESSAGES**/
	public static final String		   	Transmit_MESSAGES_URI	= "Transmettre_Message" ;
	
	/**l'URI de la méthode HasRoutesFor**/
	public static final String         	Has_Routes_URI			= "Has_routes_for";
	
	/**l'URI de la méthode Ping**/
	public static final String         	Ping_URI 				= "ping";
	
	protected ReentrantReadWriteLock lockForArrays = new ReentrantReadWriteLock();

	/**
	 * Constructeur vide
	 */
	public Node_AccessPointP() {
		
	}

	/**
	 * Constructeur qui permet d'initialiser le nombre maximum de thread pour chaque méthode
	 * @param nbThreadUpdateRouting
	 * @param nbThreadConnect
	 * @param nbThreadConnectRouting
	 * @param nbThreadTransmitMessage
	 * @param nbThreadHasRouteFor
	 * @param nbThreadPing
	 * @param nbThreadUpdateAccessPoint
	 */
	public Node_AccessPointP(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing, int nbThreadUpdateAccessPoint) {
		this.nbThreadUpdateRouting=nbThreadUpdateRouting;
		this.nbThreadConnect=nbThreadConnect;
		this.nbThreadConnectRouting= nbThreadConnectRouting;
		this.nbThreadTransmitMessage=nbThreadTransmitMessage;
		this.nbThreadHasRouteFor=nbThreadHasRouteFor;
		this.nbThreadPing=nbThreadPing;
		this.nbThreadUpdateAccessPoint=nbThreadUpdateAccessPoint;
	}
	
	
	/**--------------------------------------------------
	 *--------------  Component life-cycle -------------
	  --------------------------------------------------**/
	
	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);

		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(Node_AccessPointCI.class);
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(RoutingCI.class);

        this.addOfferedInterface(CommunicationCI.class);
        this.addOfferedInterface(RoutingCI.class);
		
        //Executor Service
        this.createNewExecutorService(UpdateRouting_URI, nbThreadUpdateRouting, false);
        this.createNewExecutorService(UpdateAccessPoint_URI, nbThreadUpdateAccessPoint, false);

        this.createNewExecutorService(ConnectRouting_URI, nbThreadConnectRouting, false);
        this.createNewExecutorService(Connect_URI, nbThreadConnect, false);
		
		this.createNewExecutorService(Transmit_MESSAGES_URI, nbThreadTransmitMessage, false);
		this.createNewExecutorService(Has_Routes_URI, nbThreadHasRouteFor, false);
		
		this.createNewExecutorService(Ping_URI, nbThreadPing, false);

        
		// Enables logs
		this.owner.toggleLogging();
		// this.owner.toggleTracing();
		this.pointInitial = new Position();
		
		this.logMessage("Node_AccessPoint " + this.address.getAdress() + " " + this.pointInitial.toString());
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        this.napop = new Node_AccessPointOutboundPort(this.owner);
		this.napip = new Node_AccessPointCommInboundPort(this.owner,this.getPluginURI());
		this.naprip = new Node_AccessPointRoutingInboundPort(this.owner, this.getPluginURI());
		
		this.napop.publishPort();
		this.napip.publishPort();
		this.naprip.publishPort();
		this.owner.doPortConnection(
				this.napop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
	}
	
	public void start() throws Exception {
		this.logMessage("Tries to log in the manager");
		
		// Retrieve the list of devices to connect with
		Set<ConnectionInfoI> devices = this.napop.registerAccessPoint(address,napip.getPortURI() , this.pointInitial, 1.5, naprip.getPortURI());
		this.logMessage("Logged");
		
		// Connects to every device
		for(ConnectionInfoI CInfo: devices) {
			// Default connection
			// ciToAdd holds all the information on the device connection
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			
			
			Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this.owner);
			napcop.publishPort();
			this.owner.doPortConnection(
					napcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			napcop.connect(address, this.napip.getPortURI());
			lockForArrays.writeLock().lock();
			node_CommOBP.add(napcop);
			
			ciToAdd.setCommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
			ciToAdd.setNapcop(napcop);
			this.addressConnected.add(ciToAdd);
			
			// Init routes with connected nodes
			for(ConnectionInformation a : addressConnected) {
				routes.add(new RoutingInfo(a.getAddress(),a.getAddress()));
			}
			lockForArrays.writeLock().unlock();

			// Update routes
			for(ConnectionInformation a : addressConnected) {
				if(!(a.getNaprop() == null)) {
					a.getNaprop().updateRouting(this.address, this.copyRouteInfo());
					a.getNaprop().updateAccessPoint(this.address, 0);
				}
			}
		}

		this.logMessage("Connected to all nearby devices");
		
	}
	
	@Override
	public void	finalise() throws Exception
	{		
		super.finalise();
		this.owner.doPortDisconnection(this.napop.getPortURI());
		

	}

	
	@Override
	public void	uninstall() throws Exception
	{
		super.uninstall();

		this.napop.unpublishPort();		
		this.napip.unpublishPort();
		this.naprip.unpublishPort();
		
		this.napop.destroyPort() ;
		this.napip.destroyPort() ;
		this.naprip.destroyPort() ;

		
	}
	
	/** ------------------------- Services ------------------------**/

	/**
	 * cette méthode permet a un voisin de se connecter 
	 * @param address : l'adresse du voisin
	 * @param communicationInboundPortURI
	 * @return null
	 * @throws Exception
	 */
	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setcommunicationInboundPortURI(communicationInboundPortURI);
		
		
		Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this.owner);
		napcop.publishPort();

		try {
			this.owner.doPortConnection(
					napcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		lockForArrays.writeLock().lock();
		node_CommOBP.add(napcop);
		CInfo.setNapcop(napcop);
		this.addressConnected.add(CInfo);
		lockForArrays.writeLock().unlock();
		this.logMessage("Added new devices to connections");
		return null;
	}

	
	/**
	 * Cette méthode est appeler par le nœud routing s'il a la capacité à router des messages 
	 * @param address
	 * @param communicationInboundPortURI
	 * @param routingInboundPortURI
	 * @return null
	 * @throws Exception
	 */
	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		this.logMessage("Someone asked connection");
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setcommunicationInboundPortURI(communicationInboundPortURI);
		CInfo.setRoutingInboundPortURI(routingInboundPortURI);
		CInfo.setisRouting(true);
		

		Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this.owner);
		Node_AccessPointRoutingOutboundPort naprop = new Node_AccessPointRoutingOutboundPort(this.owner);
		napcop.publishPort();
		naprop.publishPort();
		
		// Connection for comm + routing
		this.logMessage("Trying connexion to communication + router port");
		try {
			this.owner.doPortConnection(
					napcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
			this.owner.doPortConnection(
					naprop.getPortURI(), 
					routingInboundPortURI, 
					RoutingConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Successful connection to communication + routing port");
		this.logMessage("Added new devices to connections");
		lockForArrays.writeLock().lock();
		routes.add(new RoutingInfo(address,address));
		this.logMessage("Sending routing informations");
		
		
		CInfo.setNapcop(napcop);
		CInfo.setNaprop(naprop);
		CInfo.getNaprop().updateRouting(this.address, this.copyRouteInfo());
		CInfo.getNaprop().updateAccessPoint(this.address, 0);
		this.addressConnected.add(CInfo);
		lockForArrays.writeLock().unlock();
		return null;
	}

	/**
	 * cette méthode permet de trasmettre un message
	 * @param m : le message à transmettre
	 * @return null
	 * @throws Exception
	 */
	public void transmitMessage(MessageI m) throws Exception {
		m.decrementHops();
		
		if(m.getAddress().isNetworkAdress()) {
			this.logMessage("Sent " + m.getContent() + " to network");
			Message.newMessageReceived();
			return;
		}
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
			Message.newMessageReceived();
		}
		else {
			if(m.stillAlive()) {
				// Try to send it from routing tables
				if(sendMessageViaRouting(m)) {
					Message.newMessageViaTableRouting();
					return;
				}
				
				// Message par Innondation 
				this.sendMessageViaInnondation(m);
				Message.newMessageViaInnondation();
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return;
	}
	
	/**
	 * cette méthode nous permet d'envyer un message via le routeur
	 * @param m : le message
	 * @return true si on réussit a envoyer un message via le routeur
	 * @throws Exception
	 */
	public boolean sendMessageViaRouting(MessageI m) throws Exception {
		this.lockForArrays.readLock().lock();
		for(RouteInfoI ri: this.routes) {
			if(ri.getDestination().getAdress() == m.getAddress().getAdress()) {
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().getAdress() == ri.getIntermediate().getAdress()) {
						this.logMessage("(Via routing tables) Transmitting a Message to " + ci.getAddress().getAdress());
						ci.getNapcop().transmitMessage(m);
						this.lockForArrays.readLock().unlock();
						return true;
					}
				}
			}
		}
		this.lockForArrays.readLock().unlock();
		return false;
	}
	
	/**
	 * cette méthode nous permets d'envoyer un message via l'innondation
	 * @param m : le message
	 * @throws Exception
	 */
	public void sendMessageViaInnondation(MessageI m) throws Exception{
		int numberSent = 0;
		this.lockForArrays.readLock().lock();
		for(int i = 0; numberSent < NumberOfNeighboorsToSend && i < this.addressConnected.size(); i++) {
			// No blocking mechanism
			Message.newMessageDuplicated();
			
			AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
			this.logMessage("(Via innondation) Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
			
			this.addressConnected.get(i).getNapcop().transmitMessage(m);
			numberSent += 1;
		
		}
		this.lockForArrays.readLock().unlock();
	}

	
	/**
	 * Cette méthode vérifie s'il existe une route vers une adresse particulière
	 * @param address : l'adresse a vérifié
	 * @return true si il existe une route
	 * @throws Exception
	 */
	public int hasRouteFor(AddressI address) throws Exception{
		if(address.isNetworkAdress()) {
			return 1;
		}
		else {
			int hops = -1;
			lockForArrays.readLock().lock();
			for(RouteInfoI riInt : this.routes) {	
				// If route exists in current table
				if(riInt.getDestination().equals(address)) {
					// If the route is worth using
					hops = riInt.getNumberOfHops();
					break;
				}
			}
			lockForArrays.readLock().unlock();
			return hops;
		}
	}

	/**
	 * Cette méthode permet de vérifier le voisin est encore présent
	 * @return null
	 * @throws Exception
	 */
	public Void ping() throws Exception{
		return null;
	}

	/**
	 * cette méthode nous permet de mettre a jours notre table de routage
	 * @param neighbour
	 * @param routes
	 * @throws Exception
	 */
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception{
		boolean add;
		
		
		
		// CORE
		lockForArrays.writeLock().lock();
		for(RouteInfoI riExt : routes) {	
			add = true;
			if(riExt.getDestination().equals(this.address)) {
				continue;
			}
			
			for(RouteInfoI riInt : this.routes) {	
				
				// If route exists in current table
				if(riInt.getDestination().equals(riExt.getDestination())) {
					// If the route is worth using
					if(riInt.getNumberOfHops() > riExt.getNumberOfHops()+1 ) {
						//route interessante on modifier notre table
						riInt.setHops(riExt.getNumberOfHops() + 1);
						riInt.setIntermediate(neighbour);
					}
					
					// Route already exists and is not needed to be added in the current table
					add = false;
				}
			}

			// Adds a new route to the table
			if(add) {
				RouteInfoI riAdd = riExt.clone();
				riAdd.setHops(riAdd.getNumberOfHops() + 1);
				riAdd.setIntermediate(neighbour);
				this.routes.add(riAdd);
			}
		}
		lockForArrays.writeLock().unlock();
		
	}
	
	/**
	 * cette méthode nous permet de mettre à jour la route vers le point d’accès le plus proche 
	 * @param neighbour
	 * @param numberOfHops
	 * @throws Exception
	 */
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		return;
	}
	/**
	 * Permet de faire une copie de la table de routage
	 */
	public Set<RouteInfoI> copyRouteInfo(){
		this.lockForArrays.readLock().lock();;
		Set<RouteInfoI> copy = new HashSet<RouteInfoI>();
		for(RouteInfoI ri: this.routes) {
			copy.add(ri);
		}
		this.lockForArrays.readLock().unlock();
		return copy;
	}
	
	/**
	 * Permet de transformer en string une table de routage 
	 */
	public String routingTableToString() {
		String toString = "Routes :";
		toString += "[";
		for(RouteInfoI riDisplay : this.routes) {
			toString += "\n";
			toString += riDisplay.getDestination().getAdress() + ", " + riDisplay.getIntermediate().getAdress() + ", " +riDisplay.getNumberOfHops();
		}
		toString += "]";
		return toString;
	}
		

}
