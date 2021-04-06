package fr.bcm.node.routing.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingOutboundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NetworkAddress;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.classes.Message;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.bcm.utils.routing.classes.RoutingInfo;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

/**
 * classe Node_Routing qui représente le nœud routing dans notre réseau
 * @author Nguyen, Belkhir
 **/

@RequiredInterfaces(required = {Node_RoutingCI.class, CommunicationCI.class, RoutingCI.class})
@OfferedInterfaces (offered = {RoutingCI.class, CommunicationCI.class})

public class Node_Routing extends AbstractComponent{
	
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
	private NodeAddress address = new NodeAddress();
	
	/** la liste des adresses accessible à partir du nœud routing **/
	private List<ConnectionInformation> addressConnected = new ArrayList<>();
	
	/** la liste des adresses de la table de routage **/
	private Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	
	/** l'adresse de l'access Point**/
	private RouteInfoI routeToAccessPoint;
	
	/**l'adresse a qui on envoi le message**/
	public static AddressI addressToSendMessage;
	
	/** le nombre e tentative pour envoyais un message **/
	private int NumberOfNeighboorsToSend = 2;
	
	/** la position initial du nœud routing **/
	private PositionI pointInitial;
	
	/**
	 * Constructeur qui crée une instance du nœud routing
	 * @throws Exception
	 */
	protected Node_Routing() throws Exception {
		super(2,0);
		this.id = node_routing_id;
		node_routing_id += 1;
		this.pointInitial = new Position(0,this.id);
		this.nrop = new Node_RoutingOutBoundPort(this);
		this.nrcip = new Node_RoutingCommInboundPort(this);
		this.nrrip = new Node_RoutingRoutingInboundPort(this);
		this.nrop.publishPort();
		this.nrcip.publishPort();
		this.nrrip.publishPort();
		this.doPortConnection(
				this.nrop.getPortURI(), 
				GestionnaireReseau.GS_URI, 
				NodeConnector.class.getCanonicalName());
		
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node_Routing " + this.address.getAdress() + " " + this.id + " Position initiale : (" + pointInitial.getX() + ", " + pointInitial.getY() + ")");
	}

	/**--------------------------------------------------
	 *--------------  Component life-cycle -------------
	  --------------------------------------------------**/

	@Override
	public synchronized void finalise() throws Exception {
		if(this.nrop.connected()) {
			this.doPortDisconnection(nrop.getPortURI());
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			if(nrop.connected()) {
				this.nrop.doDisconnection();
			}
			if(nrcip.connected()) {
				this.nrcip.doDisconnection();
			}
			if(nrrip.connected()) {
				this.nrrip.doDisconnection();
			}
			
			for (ConnectionInformation ci: this.addressConnected) {
				ci.disconnectAll();
			}
			
			this.nrop.unpublishPort();
			this.nrcip.unpublishPort();
			this.nrrip.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		Thread.yield();
		Thread.sleep(2000);
		this.logMessage("Tries to log in the manager");
		
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrcip.getPortURI() , this.pointInitial, 1.5, nrrip.getPortURI());
		this.logMessage("Logged");
		
		
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			
			
			Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
			this.logMessage("Creating communication Port");
			nrcop.publishPort();
			this.logMessage("Publish Port");
			this.doPortConnection(
					nrcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			this.logMessage("Do Port connection");
			this.logMessage(CInfo.getAddress().getAdress());
			
		
			// Routing connection
			if(CInfo.getisRouting()) {
				
				this.logMessage("Ask for routing connection");
				Node_RoutingRoutingOutboundPort nrrop = new Node_RoutingRoutingOutboundPort(this);
				nrrop.publishPort();
				try {
					this.doPortConnection(
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
			
			// Normal connection
			else {
				this.logMessage("Ask for connection");
				nrcop.connect(address, this.nrcip.getPortURI());
				this.logMessage("Accepted connection to communication");
			}
			
			ciToAdd.setcommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
			ciToAdd.setNrcop(nrcop);
			this.addressConnected.add(ciToAdd);
			
			// Route to an access point
			if(CInfo.getisAccessPoint()) {
				this.routeToAccessPoint = new RoutingInfo(null, CInfo.getAddress());
				ciToAdd.setisAccessPoint(CInfo.getisAccessPoint());
			}
		}

		this.logMessage("Connected to all nearby devices");
		
		// Init routes with connected nodes
		for(ConnectionInformation a : addressConnected) {
			routes.add(new RoutingInfo(a.getAddress(),a.getAddress()));
		}
		
		// Update routes
		for(ConnectionInformation a : addressConnected) {
			if(!(a.getNrrop() == null)) {
				a.getNrrop().updateRouting(this.address, this.routes);
				
				if(this.routeToAccessPoint != null) {
					a.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
				}
			}
		}
		
		
		
		
		
		/* Testing part, sending messages */
		
		// Registering first routing node as the address to send the message to for testing
		if(this.id == 1) {
			Node_Routing.addressToSendMessage = this.address;
		}
		
		Thread.sleep(3000);
		// Sending a message to the first routing node from the third routing node
		if(this.id == 3) {
			if(Node_Routing.addressToSendMessage != null) {
				Message m = new Message(Node_Routing.addressToSendMessage,"Hello from : " + this.address.getAdress());
				this.logMessage("Sending message to " + Node_Routing.addressToSendMessage.getAdress());
				this.transmitMessage(m);
			}
		}
		
		// Sending a message to the network from the second routing node
		if(this.id == 2) {
			Thread.yield();
			if(this.addressConnected.size()>0) {
				NetworkAddress toSend = new NetworkAddress();
				Message m = new Message(toSend,"Hello to Network from : " + toSend.getAdress());						
				this.logMessage("Sending message to network " + toSend.getAdress());
				this.transmitMessage(m);
			}
		}
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
		this.logMessage("Someone asked connection");
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setcommunicationInboundPortURI(communicationInboundPortURI);
		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
		nrcop.publishPort();

		try {
			this.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Added new devices to connections");
		CInfo.setNrcop(nrcop);
		routes.add(new RoutingInfo(address,address));
		this.addressConnected.add(CInfo);
		
		this.logMessage("Routing tables after connection " + this.routingTableToString());
				
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
		

		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
		Node_RoutingRoutingOutboundPort nrrop = new Node_RoutingRoutingOutboundPort(this);
		nrcop.publishPort();
		nrrop.publishPort();
		
		// Connection for comm + routing
		this.logMessage("Trying connexion to communication + router port");
		try {
			this.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
			this.doPortConnection(
					nrrop.getPortURI(), 
					routingInboundPortURI, 
					RoutingConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Successful connection to communication + routing port");
		this.logMessage("Added new devices to connections");
		routes.add(new RoutingInfo(address,address));
		this.logMessage("Sending routing informations");
		
		
		
		CInfo.setNrcop(nrcop);
		CInfo.setNrrop(nrrop);
		CInfo.getNrrop().updateRouting(this.address, this.routes);
		if(this.routeToAccessPoint != null) {
			CInfo.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
		}
		this.addressConnected.add(CInfo);
		this.logMessage("Routing tables after connectionR " + this.routingTableToString());
		return null;
	}
	
	/**
	 * cette méthode permet de trasmettre un message
	 * @param m : le message à transmettre
	 * @return null
	 * @throws Exception
	 */
	
	public Object transmitMessage(MessageI m) throws Exception {
		this.logMessage("Routing tables before sending message " + this.routingTableToString());
		m.decrementHops();
		m.addAddressToHistory(this.address);
		
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
		}
		else {
			if(m.stillAlive()) {
				
				// Sending to nearest access point if possible
				if(sendMessageToNetwork(m)) {
					return null;
				}
				
				// Try to send it from routing tables
				if(sendMessageViaRouting(m)) {
					return null;
				}
				
				// Message par Innondation 
				sendMessageViaInnondation(m);
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return null;
	}
	
	/**
	 * cette methode nous permet d'envoyer un message to network
	 * @param m : le message
	 * @return true si on réussi a envoyais le message to Network
	 * @throws Exception
	 */
	public boolean sendMessageToNetwork(MessageI m) throws Exception {
		
		if(m.getAddress().isNetworkAdress()) {
			if(this.routeToAccessPoint != null) {
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().equals(this.routeToAccessPoint.getIntermediate())) {
						this.logMessage("(Via routing tables) Transmitting a Message to network by " + ci.getAddress().getAdress());
						ci.getNrcop().transmitMessage(m);
						return true;
					}
				}
			}
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
		for(RouteInfoI ri: this.routes) {
			if(ri.getDestination().equals(m.getAddress())) {
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().equals(ri.getDestination())) {
						this.logMessage("(Via routing tables) Transmitting a Message to " + ci.getAddress().getAdress());
						ci.getNrcop().transmitMessage(m);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * cette méthode nous permets d'envoyer un message via l'innondation
	 * @param m : le message
	 * @throws Exception
	 */
	public void sendMessageViaInnondation(MessageI m) throws Exception{
		int numberSent = 0;
		for(int i = 0; numberSent < NumberOfNeighboorsToSend && i < this.addressConnected.size(); i++) {
			// No blocking mechanism
			AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
			if(!m.isInHistory(addressToTransmitTo)) {
				this.logMessage("(Via innondation) Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
				this.addressConnected.get(i).getNrcop().transmitMessage(m);
				numberSent += 1;
			}
			else {
				this.logMessage("No node to send message to");
			}
		}
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

	/**
	 * Cette méthode permet de vérifier le voisin est encore présent
	 * @return null
	 * @throws Exception
	 */
	public Object ping() throws Exception{
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
		// Adding a new route to an access point if it doesn't exists
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
	}
	
	// Used to generate a string to display routing table's informations
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
