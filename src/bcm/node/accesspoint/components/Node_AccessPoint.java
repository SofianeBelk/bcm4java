package bcm.node.accesspoint.components;

import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bcm.connexion.classes.ConnectionInformation;
import bcm.connexion.connectors.CommunicationConnector;
import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.accesspoint.ports.Node_AccessPointCommInboundPort;
import bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import bcm.node.accesspoint.ports.Node_AccessPointRoutingInboundPort;
import bcm.node.accesspoint.ports.Node_AccessPointRoutingOutboundPort;
import bcm.node.connectors.NodeConnector;
import bcm.node.routing.connectors.RoutingConnector;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import bcm.node.routing.ports.Node_RoutingRoutingOutboundPort;
import bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.classes.Position;
import bcm.utils.nodeInfo.interfaces.PositionI;
import bcm.utils.routing.classes.RoutingInfo;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

/**
 * classe Node_AccessPoint qui représente le nœud Access point dans notre réseau
 * @author Nguyen, Belkhir
 **/

@RequiredInterfaces(required = {Node_AccessPointCI.class, CommunicationCI.class, RoutingCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class, RoutingCI.class})

public class Node_AccessPoint extends AbstractComponent{
	
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
	
	/**un boolean qui nous permet de savoir si notre composant est toujours en vie**/
	private boolean isAlive = false;
	
	/** le nombre e tentative pour envoyais un message **/
	private int NumberOfNeighboorsToSend = 2;
	
	/**
	 * Constructeur qui crée une instance du nœud access point
	 * @throws Exception
	 */
	protected Node_AccessPoint() throws Exception {
		super(2,0);
		this.napop = new Node_AccessPointOutboundPort(this);
		this.napip = new Node_AccessPointCommInboundPort(this);
		this.naprip = new Node_AccessPointRoutingInboundPort(this);
		
		System.out.println("AP PORT URI " + naprip.getPortURI());
		this.napop.publishPort();
		this.napip.publishPort();
		this.naprip.publishPort();
		this.doPortConnection(
				this.napop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node_AccessPoint " + this.address.getAdress());
	}

	/**--------------------------------------------------
	 *--------------  Component life-cycle -------------
	  --------------------------------------------------**/

	@Override
	public synchronized void finalise() throws Exception {
		if(this.napop.connected()) {
			this.doPortDisconnection(napop.getPortURI());
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		
		try {
			if(napop.connected()) {
				this.napop.doDisconnection();
			}
			if(napip.connected()) {
				this.napip.doDisconnection();
			}
			if(this.naprip.connected()) {
				this.naprip.doDisconnection();
			}
			for (ConnectionInformation ci: this.addressConnected) {
				ci.disconnectAll();
			}
			
			this.napop.unpublishPort();
			this.napip.unpublishPort();
			this.naprip.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		PositionI pointInitial = new Position(0,0);
		Set<ConnectionInfoI> devices = this.napop.registerAccessPoint(address,napip.getPortURI() , pointInitial, 25.00, naprip.getPortURI());
		this.isAlive=true;
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			
			
			Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this);
			napcop.publishPort();
			this.doPortConnection(
					napcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			napcop.connect(address, this.napip.getPortURI());
			node_CommOBP.add(napcop);
			
			ciToAdd.setCommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
			ciToAdd.setNapcop(napcop);
			this.addressConnected.add(ciToAdd);
			
			// Init routes with connected nodes
			for(ConnectionInformation a : addressConnected) {
				routes.add(new RoutingInfo(a.getAddress(),a.getAddress()));
			}
			
			// Update routes
			for(ConnectionInformation a : addressConnected) {
				if(!(a.getNaprop() == null)) {
					a.getNaprop().updateRouting(this.address, this.routes);
					a.getNaprop().updateAccessPoint(this.address, 0);
				}
			}
		}

		this.logMessage("Connected to all nearby devices");this.logMessage("Ping neighbour");
		try {
			this.napip.ping();
		}catch(Exception e) {
			this.logMessage("Dead neighbour");
		}
		this.logMessage("neighbour always alive");
		
		this.napop.unregister(this.address);
		this.isAlive = false;
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
		
		
		Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this);
		napcop.publishPort();

		try {
			this.doPortConnection(
					napcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		node_CommOBP.add(napcop);
		CInfo.setNapcop(napcop);
		this.addressConnected.add(CInfo);
		this.logMessage("Added new devices to connections");
		return null;
	}

	/**
	 * Cette méthode est appeler par le nœud  s'il a la capacité à router des messages 
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
		

		Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this);
		Node_AccessPointRoutingOutboundPort naprop = new Node_AccessPointRoutingOutboundPort(this);
		napcop.publishPort();
		naprop.publishPort();
		
		// Connection for comm + routing
		this.logMessage("Trying connexion to communication + router port");
		try {
			this.doPortConnection(
					napcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
			this.doPortConnection(
					naprop.getPortURI(), 
					routingInboundPortURI, 
					RoutingConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Successful connection to communication + routing port");
		this.logMessage("Added new devices to connections");
		routes.add(new RoutingInfo(address,address));
		this.logMessage("Sending routing informations");
		
		this.logMessage("Routing tables after connectionR " + this.routingTableToString());
		
		CInfo.setNapcop(napcop);
		CInfo.setNaprop(naprop);
		CInfo.getNaprop().updateRouting(this.address, this.routes);
		CInfo.getNaprop().updateAccessPoint(this.address, 0);
		this.addressConnected.add(CInfo);
		return null;
	}

	/**
	 * cette méthode permet de trasmettre un message
	 * @param m : le message à transmettre
	 * @return null
	 * @throws Exception
	 */
	public Object transmitMessage(MessageI m) throws Exception {
		

		m.decrementHops();
		
		if(m.getAddress().isNetworkAdress()) {
			this.logMessage("Sent " + m.getContent() + " to network");
			return null;
		}
		
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
		}
		else {
			
			if(m.stillAlive()) {
				int numberSent = 0;
				for(int i = 0; numberSent < NumberOfNeighboorsToSend && i < this.addressConnected.size(); i++) {		
					// No blocking mechanism
					AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
					this.logMessage("Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
					this.node_CommOBP.get(i).transmitMessage(m);
					numberSent += 1;
				
				}
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return null;
	}

	/**
	 * Cette méthode vérifie s'il existe une route vers une adresse particulière
	 * @param address : l'adresse a vérifié
	 * @return true si il existe une route
	 * @throws Exception
	 */
	public boolean hasRouteFor(AddressI address) throws Exception{
		return false;
	}

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
		
		// PRINT BEFORE
		String toString = "BEFORE " + this.routingTableToString();
		this.logMessage(toString);
		
		
		// CORE
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
		
		// PRINT AFTER
		toString = "AFTER " + this.routingTableToString();
		this.logMessage(toString);
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
