package fr.bcm.nodeWithPlugin.routing.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingOutboundPort;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalCommInboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalOutBoundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NetworkAddress;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.classes.Message;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class NodeRoutingplugin extends AbstractPlugin implements Node_RoutingCI, RoutingCI, CommunicationCI{

	private static final long serialVersionUID = 1L;
public static int node_routing_id = 1;
	
	private int id;
	protected Node_RoutingOutBoundPort nrop;
	
	protected Node_RoutingCommInboundPort nrcip;
	protected List<Node_RoutingCommOutboundPort> node_CommOBP = new ArrayList<>();
	
	protected Node_RoutingRoutingInboundPort nrrip;
	protected List<Node_RoutingRoutingOutboundPort> node_RoutingOBP = new ArrayList<>();
	
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected = new ArrayList<>();
    
	private Map<NodeAddressI, Set<RouteInfoI>> routes;
    private Map<NodeAddressI, Integer> accessPointsMap;

	
	private AddressI addressToSendMessage;
	private int NumberOfNeighboorsToSend = 2;
	protected ComponentI owner;

	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		System.out.println("plugin node routing started");

		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_RoutingCI.class);
        this.addOfferedInterface(CommunicationCI.class);
        this.addOfferedInterface(RoutingCI.class);
		
		// Enable logs
        this.id = node_routing_id;
		node_routing_id += 1;
		
		
		// Enables logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.logMessage("Node_Routing " + this.address.getAdress() + " " + this.id);
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        //add port
        this.nrop = new Node_RoutingOutBoundPort(this.owner);
		this.nrcip = new Node_RoutingCommInboundPort(this.owner);
		this.nrrip = new Node_RoutingRoutingInboundPort(this.owner);
		this.nrop.publishPort();
		this.nrcip.publishPort();
		this.nrrip.publishPort();
		this.owner.doPortConnection(
				this.nrop.getPortURI(), 
				GestionnaireReseau.GS_URI, 
				NodeConnector.class.getCanonicalName());
		
	}
	
	public void start() throws Exception {
		Thread.yield();
		Thread.sleep(1000);
		this.logMessage("Tries to log in the manager");
		PositionI pointInitial = new Position(10,5);
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrcip.getPortURI() , pointInitial, 25.00, nrrip.getPortURI());
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
			
			this.addressConnected.add(CInfo);
			Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this.owner);
			this.logMessage("Creating routing Port");
			nrcop.publishPort();
			this.logMessage("Publish Port");
			this.owner.doPortConnection(
					nrcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			this.logMessage("Do Port connection");
			this.logMessage(CInfo.getAddress().getAdress());
			
			
			this.logMessage("Ask for connection");
			
			// Routing connection
			if(!CInfo.getRoutingInboundPortURI().isEmpty()) {
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
				node_RoutingOBP.add(nrrop);
				this.logMessage("Successful connection to communication + routing port");
			}
			
			// Normal connection
			else {
				nrcop.connect(address, this.nrcip.getPortURI());
				this.logMessage("Successful connection to communication");
			}
			
			node_CommOBP.add(nrcop);
			
		}

		this.logMessage("Connected to all nearby devices");
		
		Thread.yield();
		Thread.sleep(3000);
		if(this.id == 1) {
			Thread.yield();
			if(this.addressConnected.size()>0) {
				this.addressToSendMessage = this.addressConnected.get(0).getAddress();
			}
			Message m = new Message(this.addressToSendMessage,"Hello from : " + this.address.getAdress());
			this.logMessage("Sending message to " + this.addressToSendMessage.getAdress());
			this.transmitMessage(m);
		}
		
		
		if(this.id == 2) {
			Thread.yield();
			if(this.addressConnected.size()>0) {
				NetworkAddress toSend = new NetworkAddress();
				Message m = new Message(toSend,"Hello to Network from : " + toSend.getAdress());						
				this.logMessage("Sending message to network " + toSend.getAdress());
				this.transmitMessage(m);
			}
		}
		
		// Init routes
		for(ConnectionInfoI a : addressConnected) {
			//routes.put(this.address,a. ); 
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


	@Override
	public Set<ConnectionInfoI> registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return this.nrop.registerRoutingNode(address, communicationInboundPortURI, initialPosition, initialRange, routingInBoundPortURI);
	}

	@Override
	public void unregister(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		this.nrop.unregister(address);
	}

	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		// TODO Auto-generated method stub
		this.nrrip.updateRouting(neighbour, routes);
	}

	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
		// TODO Auto-generated method stub
		this.nrrip.updateAccessPoint(neighbour, numberOfHops);
	}

	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		this.nrcip.connect(address, communicationInboundPortURI);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI)
			throws Exception {
		// TODO Auto-generated method stub
		this.nrcip.connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		// TODO Auto-generated method stub
		this.nrcip.transmitMessage(m);
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		return this.nrcip.hasRouteFor(address);
	}

	@Override
	public void ping() throws Exception {
		// TODO Auto-generated method stub
		this.nrcip.ping();
	}

}
