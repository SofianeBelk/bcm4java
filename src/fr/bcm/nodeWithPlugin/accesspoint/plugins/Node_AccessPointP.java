package fr.bcm.nodeWithPlugin.accesspoint.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointRoutingOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingInboundPort;
import fr.bcm.nodeWithPlugin.accesspoint.ports.Node_AccessPointCommInboundPort;
import fr.bcm.nodeWithPlugin.accesspoint.ports.Node_AccessPointRoutingInboundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.bcm.utils.routing.classes.RoutingInfo;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class Node_AccessPointP extends AbstractPlugin {
	protected Node_AccessPointOutboundPort napop;
	protected Node_AccessPointCommInboundPort napip;
	protected Node_AccessPointRoutingInboundPort naprip;
	protected List<Node_AccessPointCommOutboundPort> node_CommOBP = new ArrayList<>();
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected = new ArrayList<>();
	private Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	
	private int NumberOfNeighboorsToSend = 2;
	protected ComponentI owner;
	
	
	private int nbThreadUpdateRouting =1;
	private int nbThreadConnect = 1;
	private int nbThreadConnectRouting = 1;
	private int nbThreadTransmitMessage = 1;
	private int nbThreadHasRouteFor = 1;
	private int nbThreadPing = 1;
	private int nbThreadUpdateAccessPoint =1 ;
	
	// Thread URI
	public static final String          UpdateRouting_URI       = "Update_Routing";
	public static final String          UpdateAccessPoint_URI   = "Uodate_Access_Point";
	public static final String			ConnectRouting_URI 		= "Connexion_via_les_tables_de_routing" ;
	public static final String			Connect_URI            	= "Connexion" ;
	public static final String		   	Transmit_MESSAGES_URI	= "Transmettre_Message" ;
	public static final String         	Has_Routes_URI			= "Has_routes_for";
	public static final String         	Ping_URI 				= "ping";
	
	protected ReentrantReadWriteLock lockForArrays = new ReentrantReadWriteLock();

	
	public Node_AccessPointP() {
		
	}

	public Node_AccessPointP(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing, int nbThreadUpdateAccessPoint) {
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
		System.out.println("plugin node access point started");

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
		this.owner.toggleTracing();
		this.logMessage("Node_AccessPoint " + this.address.getAdress());
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        this.napop = new Node_AccessPointOutboundPort(this.owner);
		this.napip = new Node_AccessPointCommInboundPort(this.owner,this.getPluginURI());
		this.naprip = new Node_AccessPointRoutingInboundPort(this.owner, this.getPluginURI());
		
		System.out.println("AP PORT URI " + naprip.getPortURI());
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
		PositionI pointInitial = new Position(0,0);
		// Retrieve the list of devices to connect with
		Set<ConnectionInfoI> devices = this.napop.registerAccessPoint(address,napip.getPortURI() , pointInitial, 25.00, naprip.getPortURI());
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
					a.getNaprop().updateRouting(this.address, this.routes);
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
		
		this.logMessage("Routing tables after connectionR " + this.routingTableToString());
		
		CInfo.setNapcop(napcop);
		CInfo.setNaprop(naprop);
		CInfo.getNaprop().updateRouting(this.address, this.routes);
		CInfo.getNaprop().updateAccessPoint(this.address, 0);
		this.addressConnected.add(CInfo);
		lockForArrays.writeLock().unlock();
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		

		m.decrementHops();
		m.addAddressToHistory(this.address);
		
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
					if(!m.isInHistory(addressToTransmitTo)) {
						this.logMessage("Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
						this.node_CommOBP.get(i).transmitMessage(m);
						numberSent += 1;
					}
					else {
						this.logMessage("No node to send message to");
					}
				}
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return null;
	}

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

	public Object ping() throws Exception{
		return null;
	}


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
			lockForArrays.writeLock().lock();
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
			lockForArrays.writeLock().unlock();

		}
		
		// PRINT AFTER
		toString = "AFTER " + this.routingTableToString();
		this.logMessage(toString);
	}
	
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
