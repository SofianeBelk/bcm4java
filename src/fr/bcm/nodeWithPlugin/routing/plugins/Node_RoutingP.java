package fr.bcm.nodeWithPlugin.routing.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.components.Node_Routing;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
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
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class Node_RoutingP extends AbstractPlugin{

	private static final long serialVersionUID = 1L;
    public static int node_routing_id = 1;
	
	private int id;
	protected Node_RoutingOutBoundPort nrop;
	protected Node_RoutingCommInboundPort nrcip;
	protected Node_RoutingRoutingInboundPort nrrip;
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected = new ArrayList<>();
	private Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	private RouteInfoI routeToAccessPoint;
	
	public static AddressI addressToSendMessage;
	private int NumberOfNeighboorsToSend = 2;
	
	private PositionI pointInitial;
	
	
	protected ComponentI owner;
	
	

	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		System.out.println("plugin node routing started");
		
		System.out.println(owner.getTotalNumberOfThreads());
		System.out.println(owner);
		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_RoutingCI.class);
		this.addRequiredInterface(RoutingCI.class);

        this.addOfferedInterface(CommunicationCI.class);
        this.addOfferedInterface(RoutingCI.class);
		
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
		
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrcip.getPortURI() , this.pointInitial, 1.5, nrrip.getPortURI());
		this.logMessage("Logged");
		
		
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
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
			
		
			// Routing connection
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
				System.out.println("TEST");
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
		routes.add(new RoutingInfo(address,address));
		this.addressConnected.add(CInfo);
		
		this.logMessage("Routing tables after connection " + this.routingTableToString());
				
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

	public boolean hasRouteFor(AddressI address) throws Exception{
		return false;
	}

	public Object ping() throws Exception{
		return null;
	}
	
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
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
