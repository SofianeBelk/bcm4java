package fr.bcm.nodeWithPlugin.routing.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.components.Node_Routing;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.nodeWithPlugin.routing.components.Ordinateur;
import fr.bcm.nodeWithPlugin.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.nodeWithPlugin.routing.ports.Node_RoutingRoutingInboundPort;
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
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class Node_RoutingP extends AbstractPlugin{

	private static final long serialVersionUID = 1L;
    public static int node_routing_id = 1;
	
	private int id;
	protected Node_RoutingOutBoundPort nrop;
	protected Node_RoutingCommInboundPort nrcip;
	protected Node_RoutingRoutingInboundPort nrrip;
	protected NodeAddress address = new NodeAddress();
	protected List<ConnectionInformation> addressConnected = new ArrayList<>();
	protected Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	protected RouteInfoI routeToAccessPoint;
	
	public static AddressI addressToSendMessage;
	private int NumberOfNeighboorsToSend = 2;
	
	private PositionI pointInitial;
	
	
	protected ComponentI owner;
	
	// Thread URI
	public static final String          UpdateRouting_URI       = "Update_Routing";
	public static final String          UpdateAccessPoint_URI   = "Uodate_Access_Point";
	public static final String			ConnectRouting_URI 		= "Connexion_via_les_tables_de_routing" ;
	public static final String			Connect_URI            	= "Connexion" ;
	public static final String		   	Transmit_MESSAGES_URI	= "Transmettre_messages" ;
	public static final String         	Has_Routes_URI			= "Has_routes_for";
	public static final String         	Ping_URI 				= "ping";
		
	
	private int nbThreadUpdateRouting =1;
	private int nbThreadConnect = 1;
	private int nbThreadConnectRouting = 1;
	private int nbThreadTransmitMessage = 1;
	private int nbThreadHasRouteFor = 1;
	private int nbThreadPing = 1;
	private int nbThreadUpdateAccessPoint =1 ;
	
	//locks
	protected ReentrantReadWriteLock lockForArrays = new ReentrantReadWriteLock();
	protected ReentrantReadWriteLock lockForRouteToAP = new ReentrantReadWriteLock();

	
	public Node_RoutingP() {
		
	}
	
	public Node_RoutingP(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor,int nbThreadPing, int nbThreadUpdateAccessPoint) {
		this.nbThreadUpdateRouting=nbThreadUpdateRouting;
		this.nbThreadConnect=nbThreadConnect;
		this.nbThreadConnectRouting= nbThreadConnectRouting;
		this.nbThreadTransmitMessage=nbThreadTransmitMessage;
		this.nbThreadHasRouteFor=nbThreadHasRouteFor;
		this.nbThreadPing=nbThreadPing;
		this.nbThreadUpdateAccessPoint=nbThreadUpdateAccessPoint;
	}

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
		this.pointInitial = new Position(0,this.id);
		
		
		// Enables logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.logMessage("Node_Routing " + this.address.getAdress() + " " + this.id);
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        //add port
        //
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
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrcip.getPortURI() , this.pointInitial, 1.5, nrrip.getPortURI());
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
			lockForArrays.writeLock().lock();
			this.addressConnected.add(ciToAdd);
			lockForArrays.writeLock().unlock();

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
		lockForArrays.readLock().lock();
		for(ConnectionInformation a : addressConnected) {
			routes.add(new RoutingInfo(a.getAddress(),a.getAddress()));
		}
		lockForArrays.readLock().unlock();

		// Update routes
		lockForArrays.readLock().lock();
		for(ConnectionInformation a : addressConnected) {
			if(!(a.getNrrop() == null)) {
				a.getNrrop().updateRouting(this.address, this.routes);
				
				this.lockForRouteToAP.readLock().lock();
				if(this.routeToAccessPoint != null) {
					a.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
				}
				this.lockForRouteToAP.readLock().unlock();
			}
		}
		lockForArrays.readLock().unlock();
		
		
		
		Thread.sleep(3000);
		System.out.println("Sending ping");
		for(ConnectionInformation ci: this.addressConnected) {
			ci.getNrcop().ping();
		}
		System.out.println("Ending ping");
		
		/* Testing part, sending messages */
		/*
		// Registering first routing node as the address to send the message to for testing
		if(this.id == 1) {
			Node_Routing.addressToSendMessage = this.address;
		}
		
		Thread.yield();
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
		*/
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


	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {

		boolean add;
	
		
		// CORE
		for(RouteInfoI riExt : routes) {	
			add = true;
			if(riExt.getDestination().equals(this.address)) {
				continue;
			}
			
			lockForArrays.readLock().lock();
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
			lockForArrays.readLock().unlock();
			
			lockForArrays.writeLock().lock();
			// Adds a new route to the table
			if(add) {
				RouteInfoI riAdd = riExt.clone();
				riAdd.setHops(riAdd.getNumberOfHops() + 1);
				riAdd.setIntermediate(neighbour);
				this.routes.add(riAdd);
			}
			lockForArrays.writeLock().unlock();
		}
		
	}
	
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
		
		lockForArrays.writeLock().lock();
		this.addressConnected.add(CInfo);
		routes.add(new RoutingInfo(address,address));
		lockForArrays.writeLock().unlock();

		// this.logMessage("Routing tables after connection " + this.routingTableToString());
				
		return null;
	}

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
		lockForArrays.writeLock().lock();
		routes.add(new RoutingInfo(address,address));
		lockForArrays.writeLock().unlock();
		this.logMessage("Sending routing informations");
		
		
		
		CInfo.setNrcop(nrcop);
		CInfo.setNrrop(nrrop);
		CInfo.getNrrop().updateRouting(this.address, this.routes);
		this.lockForRouteToAP.readLock().lock();
		if(this.routeToAccessPoint != null) {
			CInfo.getNrrop().updateAccessPoint(this.address, this.routeToAccessPoint.getNumberOfHops());
		}
		this.lockForRouteToAP.readLock().unlock();
		
		lockForArrays.writeLock().lock();
		this.addressConnected.add(CInfo);
		lockForArrays.writeLock().unlock();

		// this.logMessage("Routing tables after connectionR " + this.routingTableToString());
		return null;
	}

	public void transmitMessage(MessageI m) throws Exception {
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
					return;
				}
				
				// Try to send it from routing tables
				if(sendMessageViaRouting(m)) {
					return;
				}
				
				// Message par Innondation 
				sendMessageViaInnondation(m);
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return;
	}
	
	public boolean sendMessageToNetwork(MessageI m) throws Exception {
		
		if(m.getAddress().isNetworkAdress()) {
			this.lockForRouteToAP.readLock().lock();
			if(this.routeToAccessPoint != null) {
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().equals(this.routeToAccessPoint.getIntermediate())) {
						this.logMessage("(Via routing tables) Transmitting a Message to network by " + ci.getAddress().getAdress());
						ci.getNrcop().transmitMessage(m);
						return true;
					}
				}
			}
			this.lockForRouteToAP.readLock().unlock();
		}
		return false;
	}
	
	public boolean sendMessageViaRouting(MessageI m) throws Exception {
		for(RouteInfoI ri: this.routes) {
			if(ri.getDestination().getAdress() == m.getAddress().getAdress()) {
				for(ConnectionInformation ci: this.addressConnected) {
					if(ci.getAddress().getAdress() == ri.getDestination().getAdress()) {
						this.logMessage("(Via routing tables) Transmitting a Message to " + ci.getAddress().getAdress());
						ci.getNrcop().transmitMessage(m);
						return true;
					}
				}
			}
		}
		return false;
	}
	
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

	public int hasRouteFor(AddressI address) throws Exception{
		int hops = -1;
		
		lockForArrays.readLock().lock();
		if(address.equals(routeToAccessPoint.getDestination())) {
			return routeToAccessPoint.getNumberOfHops();
		}
		lockForArrays.readLock().unlock();
		
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

	public Object ping() throws Exception{
		return null;
	}
	
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
