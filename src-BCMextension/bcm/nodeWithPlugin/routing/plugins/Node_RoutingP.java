package bcm.nodeWithPlugin.routing.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import bcm.connexion.classes.ConnectionInformation;
import bcm.connexion.connectors.CommunicationConnector;
import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.connectors.NodeConnector;
import bcm.node.routing.components.Node_Routing;
import bcm.node.routing.connectors.RoutingConnector;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import bcm.node.routing.ports.Node_RoutingOutBoundPort;
import bcm.node.routing.ports.Node_RoutingRoutingOutboundPort;
import bcm.nodeWithPlugin.routing.components.Ordinateur;
import bcm.nodeWithPlugin.routing.ports.Node_RoutingCommInboundPort;
import bcm.nodeWithPlugin.routing.ports.Node_RoutingRoutingInboundPort;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.address.classes.NetworkAddress;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.classes.Message;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.classes.Position;
import bcm.utils.nodeInfo.interfaces.PositionI;
import bcm.utils.routing.classes.RoutingInfo;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;


/**
 * classe Node_RoutingP qui  est le plugin "greffons" qui correspand au  nœud routing dans notre réseau
 * @author Nguyen, Belkhir
 **/
public class Node_RoutingP extends AbstractPlugin{

	private static final long serialVersionUID = 1L;
	
	/** un compteur qui permet d'identifier notre noeud **/
    public static int node_routing_id = 1;
	
	/** l'identifiant de notre nœud routing **/
	private int id;
	
	/** le port sortant du nœud routing **/
	protected Node_RoutingOutBoundPort nrop;
	
	/** le port entrant "CommunicationCI" du nœud routing **/
	protected Node_RoutingCommInboundPort nrcip;
	
	/** le port entrant du nœud routing **/
	protected Node_RoutingRoutingInboundPort nrrip;
	
	/** l'adresse du nœud routing **/
	protected NodeAddress address = new NodeAddress();
	
	/** la liste des adresses accessible à partir du nœud routing **/
	protected List<ConnectionInformation> addressConnected = new ArrayList<>();
	
	/** la liste des adresses de la table de routage **/
	protected Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	
	/** l'adresse de l'access Point**/
	protected RouteInfoI routeToAccessPoint;
	
	/**l'adresse a qui on envoi le message**/
	public static AddressI addressToSendMessage;
	
	/** le nombre e tentative pour envoyais un message **/
	private int NumberOfNeighboorsToSend = 2;
	
	/** la position initial du nœud routing **/
	private PositionI pointInitial;
	
	
	protected ComponentI owner;
	
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
	public static final String		   	Transmit_MESSAGES_URI	= "Transmettre_messages" ;
	
	/**l'URI de la méthode HasRoutesFor**/
	public static final String         	Has_Routes_URI			= "Has_routes_for";
	
	/**l'URI de la méthode Ping**/
	public static final String         	Ping_URI 				= "ping";
		
	
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
	
	// Locks
	protected ReentrantReadWriteLock lockForArrays = new ReentrantReadWriteLock();
	protected ReentrantReadWriteLock lockForCI = new ReentrantReadWriteLock();
	protected ReentrantReadWriteLock lockForRoutes = new ReentrantReadWriteLock();
	
	protected ReentrantReadWriteLock lockForRouteToAP = new ReentrantReadWriteLock();

	/**
	 * Constructeur vide
	 */
	public Node_RoutingP() {
		
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
	public Node_RoutingP(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor,int nbThreadPing, int nbThreadUpdateAccessPoint) {
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
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_RoutingCI.class);
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
		
		// Enable logs
        this.id = node_routing_id;
		node_routing_id += 1;
		this.pointInitial = new Position();
		
		
		// Enables logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.logMessage("Node_Routing " + this.address.getAdress() + " " + this.pointInitial.toString());
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        //add port
		this.nrop = new Node_RoutingOutBoundPort(this.owner);
		this.nrcip = new Node_RoutingCommInboundPort(this.owner,this.getPluginURI());
		this.nrrip = new Node_RoutingRoutingInboundPort(this.owner, this.getPluginURI());
		this.nrop.publishPort();
		this.nrcip.publishPort();
		this.nrrip.publishPort();
		this.owner.doPortConnection(
				this.nrop.getPortURI(), 
				GestionnaireReseau.GS_URI, 
				NodeConnector.class.getCanonicalName());
		
	}
	
	public void start() throws Exception {
		
		this.logMessage("Tries to log in the manager");
		// Retrieve the list of devices to connect with
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrcip.getPortURI() , this.pointInitial, 2.5, nrrip.getPortURI());
		this.logMessage("Logged");
		

		// Connects to every device
		for(ConnectionInfoI CInfo: devices) {
			
			// Default connection
			// ciToAdd holds all the information on the device connection
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this.owner);
			this.logMessage("Creating communication Port");
			nrcop.publishPort();
			this.logMessage("Publish Port");
			this.owner.doPortConnection(
					nrcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			this.logMessage("Do Port connection");
			this.logMessage(CInfo.getAddress().getAdress());
			
		
			// Routing connection if the device is routing
			if(CInfo.getisRouting()) {
				
				this.logMessage("Ask for routing connection");
				Node_RoutingRoutingOutboundPort nrrop = new Node_RoutingRoutingOutboundPort(this.owner);
				nrrop.publishPort();
				try {
					this.owner.doPortConnection(
							nrrop.getPortURI(), 
							CInfo.getRoutingInboundPortURI(),
							RoutingConnector.class.getCanonicalName());
				}catch(Exception e) {
					e.printStackTrace();
				}
				nrcop.connectRouting(address, this.nrcip.getPortURI(), this.nrrip.getPortURI());
				ciToAdd.setRoutingInboundPortURI(CInfo.getRoutingInboundPortURI());

				ciToAdd.setNrrop(nrrop);
				this.logMessage("Accepted connection to communication + routing port");
			}
			else {
				this.logMessage("Ask for connection");
				nrcop.connect(address, this.nrcip.getPortURI());
				this.logMessage("Accepted connection to communication");
			}
			
			ciToAdd.setcommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
			ciToAdd.setNrcop(nrcop);
			
			// This blocks locks the array to add a new connection
			lockForCI.writeLock().lock();
			this.addressConnected.add(ciToAdd);
			lockForCI.writeLock().unlock();

			// Route to an access point
			if(CInfo.getisAccessPoint()) {
				this.lockForRouteToAP.writeLock().lock();
				this.routeToAccessPoint = new RoutingInfo(null, CInfo.getAddress());
				this.lockForRouteToAP.writeLock().unlock();
				ciToAdd.setisAccessPoint(CInfo.getisAccessPoint());
			}
		}

		this.logMessage("Connected to all nearby devices");
		
		// Init routes with connected nodes
		lockForCI.readLock().lock();
		
		for(ConnectionInformation a : addressConnected) {
			lockForRoutes.writeLock().lock();
			routes.add(new RoutingInfo(a.getAddress(),a.getAddress()));
			lockForRoutes.writeLock().unlock();
		}
		
		lockForCI.readLock().unlock();

		// Update routes
		lockForCI.readLock().lock();
		for(ConnectionInformation a : addressConnected) {
			if(!(a.getNrrop() == null)) {
				a.getNrrop().updateRouting(this.address, copyRouteInfo());
				
				this.lockForRouteToAP.readLock().lock();
				if(this.routeToAccessPoint != null) {
					a.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
				}
				this.lockForRouteToAP.readLock().unlock();
			}
		}
		lockForCI.readLock().unlock();
		
		
		
	
		
		int cpt = 0;
		while(true) {
			Random rand = new Random();
			Thread.yield();
			Thread.sleep(rand.nextInt(4) * 250);
			
			// Tous les 5 tours de boucle, nous v�rifions les voisins
			if(cpt % 5 == 0) {
				this.checkPingOthers();
			}
			// Tous les 10 tours de boucle, nous updatons nos voisins sur la table de routing
			if(cpt % 10 == 0) {
				lockForCI.readLock().lock();
				for(ConnectionInformation a : addressConnected) {
					if(!(a.getNrrop() == null)) {
						a.getNrrop().updateRouting(this.address, copyRouteInfo());
						
						this.lockForRouteToAP.readLock().lock();
						if(this.routeToAccessPoint != null) {
							a.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
						}
						this.lockForRouteToAP.readLock().unlock();
					}
				}
				lockForCI.readLock().unlock();
			}
			
			
			// Le noeud � 15% de chance d'envoyer un message � une adresse du r�seau classique
			if(rand.nextFloat() < 0.15) {
				NetworkAddress toSend = new NetworkAddress();
				Message m = new Message(toSend,"Hello to Network from : " + toSend.getAdress());						
				this.logMessage("Sending message to network " + toSend.getAdress());
				Message.newMessageSent();
				this.transmitMessage(m);
			}
			// Le noeud � 10% de chance d'envoyer un message � une adresse al�atoire du r�seau
			if(rand.nextFloat() < 0.10){
				lockForCI.readLock().lock();
				ConnectionInfoI ci;
				do {
					ci = this.nrop.getRandomConn();
				}while(ci.getAddress().equals(this.address));
				lockForCI.readLock().unlock();
				Message m = new Message(ci.getAddress(), "Hello from : " + this.address.getAdress());	
				this.logMessage("Sending message to " + ci.getAddress().getAdress());
				Message.newMessageSent();
				this.transmitMessage(m);
				
			
			}
			
			cpt++;
			
			
		}
		
				
		
	}

	
	@Override
	public void	finalise() throws Exception
	{		
		super.finalise();
		this.owner.doPortDisconnection(this.nrop.getPortURI());
		

	}

	
	@Override
	public void	uninstall() throws Exception
	{
		super.uninstall();

		this.nrop.unpublishPort();		
		this.nrcip.unpublishPort();
		this.nrrip.unpublishPort();
		
		this.nrop.destroyPort() ;
		this.nrcip.destroyPort() ;
		this.nrrip.destroyPort() ;

		
	}

	/** ------------------------- Services ------------------------**/
	
	/**
	 * cette méthode nous permet de mettre a jours notre table de routage
	 * @param neighbour
	 * @param routes
	 * @throws Exception
	 */
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routesExt) throws Exception {
		boolean add;
	
		
		// CORE
		lockForRoutes.writeLock().lock();
		Iterator<RouteInfoI> iterRoutesExt = routesExt.iterator();
		RouteInfoI riExt;
		while(iterRoutesExt.hasNext()) {
			riExt = iterRoutesExt.next();
			add = true;
			if(riExt.getDestination().equals(this.address)) {
				continue;
			}
			
			
			Iterator<RouteInfoI> iterRoutesInt = this.routes.iterator();
			RouteInfoI riInt;
			while(iterRoutesInt.hasNext()) {
				riInt = iterRoutesInt.next();
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
		lockForRoutes.writeLock().unlock();
		
	}
	
	/**
	 * cette méthode permet a un voisin de se connecter 
	 * @param address : l'adresse du voisin
	 * @param communicationInboundPortURI
	 * @return null
	 * @throws Exception
	 */
	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.logMessage("Someone asked connection");
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setcommunicationInboundPortURI(communicationInboundPortURI);
		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this.owner);
		nrcop.publishPort();

		try {
			this.owner.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Added new devices to connections");
		CInfo.setNrcop(nrcop);
		
		lockForCI.writeLock().lock();
		this.addressConnected.add(CInfo);
		lockForCI.writeLock().unlock();
		lockForRoutes.writeLock().lock();
		this.routes.add(new RoutingInfo(address,address));
		lockForRoutes.writeLock().unlock();
		
		// this.logMessage("Routing tables after connection " + this.routingTableToString());
				
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
		

		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this.owner);
		Node_RoutingRoutingOutboundPort nrrop = new Node_RoutingRoutingOutboundPort(this.owner);
		nrcop.publishPort();
		nrrop.publishPort();
		
		// Connection for comm + routing
		this.logMessage("Trying connexion to communication + router port");
		try {
			this.owner.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
			this.owner.doPortConnection(
					nrrop.getPortURI(), 
					routingInboundPortURI, 
					RoutingConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Successful connection to communication + routing port");
		this.logMessage("Added new devices to connections");
		lockForRoutes.writeLock().lock();
		this.routes.add(new RoutingInfo(address,address));
		lockForRoutes.writeLock().unlock();
		this.logMessage("Sending routing informations");
		
		
		
		CInfo.setNrcop(nrcop);
		CInfo.setNrrop(nrrop);
		CInfo.getNrrop().updateRouting(this.address, this.copyRouteInfo());
		this.lockForRouteToAP.readLock().lock();
		if(this.routeToAccessPoint != null) {
			CInfo.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
		}
		this.lockForRouteToAP.readLock().unlock();
		
		lockForCI.writeLock().lock();
		this.addressConnected.add(CInfo);
		lockForCI.writeLock().unlock();

		// this.logMessage("Routing tables after connectionR " + this.routingTableToString());
		return null;
	}
	

	
	
		// this.logMessage("Routing tables before sending message " + this.routingTableToString());
	/**
	 * cette méthode permet de trasmettre un message
	 * @param m : le message à transmettre
	 * @return null
	 * @throws Exception
	 */
	public void transmitMessage(MessageI m) throws Exception {
		// this.logMessage("Routing tables before sending message " + this.routingTableToString());
		m.decrementHops();
		
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
			Message.newMessageReceived();
		}
		else {
			if(m.stillAlive()) {
				
				// Sending to nearest access point if possible
				if(sendMessageToNetwork(m)) {
					Message.newMessageViaTableRouting();
					return;
				}
				
				// Try to send it from routing tables
				if(sendMessageViaRouting(m)) {
					Message.newMessageViaTableRouting();
					return;
				}
				
				// Message par Innondation 
				sendMessageViaInnondation(m);
				Message.newMessageViaInnondation();
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return;
	}
	
	/**
	 * cette methode nous permet d'envoyer un message to network
	 * @param m : le message
	 * @return true si on réussi a envoyais le message to Network
	 * @throws Exception
	 */
	public boolean sendMessageToNetwork(MessageI m) throws Exception {
		MessageI messageToTransmit = m.copy();
		if(m.getAddress().isNetworkAdress()) {
			this.lockForRouteToAP.readLock().lock();
			if(this.routeToAccessPoint != null) {
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().equals(this.routeToAccessPoint.getIntermediate())) {
						this.logMessage("(Via routing tables) Transmitting a Message to network by " + ci.getAddress().getAdress());
						ci.getNrcop().transmitMessage(messageToTransmit);
						this.lockForRouteToAP.readLock().unlock();
						return true;
					}
				}
			}
			this.lockForRouteToAP.readLock().unlock();
		}
		return false;
	}
	
	/**
	 * cette méthode nous permet d'envyer un message via le routeur
	 * @param m : le message
	 * @return true si on réussit a envoyer un message via le routeur
	 * @throws Exception
	 */
	public boolean sendMessageViaRouting(MessageI m) throws Exception {
		MessageI messageToTransmit = m.copy();
		this.lockForRoutes.readLock().lock();
		for(RouteInfoI ri: this.routes) {
			if(ri.getDestination().equals(m.getAddress())) {
				
				this.lockForCI.readLock().lock();
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().getAdress() == ri.getIntermediate().getAdress()) {
						this.logMessage("(Via routing tables) Transmitting a Message to " + ci.getAddress().getAdress());
						ci.getNrcop().transmitMessage(messageToTransmit);
						this.lockForCI.readLock().unlock();
						return true;
					}
				}
				this.lockForCI.readLock().unlock();
			}
		}
		this.lockForRoutes.readLock().unlock();
		return false;
	}
	
	/**
	 * cette méthode nous permets d'envoyer un message via l'innondation
	 * @param m : le message
	 * @throws Exception
	 */
	public void sendMessageViaInnondation(MessageI m) throws Exception{
		
		this.lockForCI.readLock().lock();
		
		List<Integer> listIndex = new ArrayList<Integer>();
		
		int i = 0;
		while(i < NumberOfNeighboorsToSend) {
			
			Random rand = new Random();
			int index = rand.nextInt(this.addressConnected.size());
			
			if(!listIndex.contains(index)) {
				MessageI messageToTransmit = m.copy();
				Message.newMessageDuplicated();
				
				ConnectionInformation addressToTransmitTo = this.addressConnected.get(index);
				
				this.logMessage("(Via innondation) Transmitting a Message to " + addressToTransmitTo.getAddress().getAdress());
				addressToTransmitTo.getNrcop().transmitMessage(messageToTransmit);
				i++;
			}
		}
		this.lockForCI.readLock().unlock();
	}

	/**
	 * Cette méthode vérifie s'il existe une route vers une adresse particulière
	 * @param address : l'adresse a vérifié
	 * @return true si il existe une route
	 * @throws Exception
	 */
	public int hasRouteFor(AddressI address) throws Exception{
		int hops = -1;
		
		lockForRouteToAP.readLock().lock();
		if(address.equals(routeToAccessPoint.getDestination())) {
			return routeToAccessPoint.getNumberOfHops();
		}
		lockForRouteToAP.readLock().unlock();
		
		lockForRoutes.readLock().lock();
		for(RouteInfoI riInt : this.routes) {	
			// If route exists in current table
			if(riInt.getDestination().equals(address)) {
				// If the route is worth using
				hops = riInt.getNumberOfHops();
				break;
			}
		}
		lockForRoutes.readLock().unlock();
		return hops;
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
	 * cette méthode nous permet de mettre à jour la route vers le point d’accès le plus proche 
	 * @param neighbour
	 * @param numberOfHops
	 * @throws Exception
	 */
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
		// Adding a new route to an access point if it doesn't exists
		this.lockForRouteToAP.writeLock().lock();
		if(this.routeToAccessPoint == null) {
			this.routeToAccessPoint = new RoutingInfo(null, neighbour, numberOfHops+1);
		}
		else{
			// If the new route is interesting, we update our route to the accesspoint
			if(numberOfHops + 1 < this.routeToAccessPoint.getNumberOfHops()) {
				this.routeToAccessPoint.setIntermediate(neighbour);
				this.routeToAccessPoint.setHops(numberOfHops + 1);
			}
		}
		this.lockForRouteToAP.writeLock().unlock();
	}
	
	/**
	 * Permet de transformer en string une table de routage 
	 * @param neighbour
	 * @param numberOfHops
	 * @throws Exception
	 */
	public String routingTableToString() {
		this.lockForRoutes.readLock().lock();
		String toString = "Routes :";
		toString += "[";
		for(RouteInfoI riDisplay : this.routes) {
			toString += "\n";
			toString += riDisplay.getDestination().getAdress() + ", " + riDisplay.getIntermediate().getAdress() + ", " +riDisplay.getNumberOfHops();
		}
		toString += "]";
		this.lockForRoutes.readLock().unlock();
		return toString;
	}
	
	/**
	 * Permet de faire une copie de la table de routage
	 */
	public Set<RouteInfoI> copyRouteInfo(){
		this.lockForRoutes.readLock().lock();;
		Set<RouteInfoI> copy = new HashSet<RouteInfoI>();
		for(RouteInfoI ri: this.routes) {
			copy.add(ri);
		}
		this.lockForRoutes.readLock().unlock();
		return copy;
	}
	
	/**
	 * Ping toutes les addresses pour v�rifier que les noeuds sont actifs
	 */
	public void checkPingOthers() {
		this.logMessage("Sending ping");
		
		
		lockForCI.writeLock().lock();
		Iterator<ConnectionInformation> i = this.addressConnected.iterator();
		ConnectionInformation ci;
		while(i.hasNext()) {
			ci = i.next();
			try {
				ci.getNrcop().ping();
			}
			
			// Ne pas d�truire le port, Voir mail
			catch(Exception e) {
				lockForRoutes.writeLock().lock();
				Iterator<RouteInfoI> iri = this.routes.iterator();
				RouteInfoI ri;
				while(iri.hasNext()) {
					ri = iri.next();
					if (ci.getAddress().equals(ri.getIntermediate())){
						iri.remove();
					}
				}
			
				i.remove();
				lockForRoutes.writeLock().unlock();
				// e.printStackTrace();
				this.logMessage("Removed dead connection");
			}
			finally {
				this.logMessage("Ping successful");
			}
		}
		lockForCI.writeLock().unlock();
		
		
	}

}
