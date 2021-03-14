package fr.bcm.nodeWithPlugin.accesspoint.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommInboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointRoutingInboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointRoutingOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingInboundPort;
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

public class OrdinateurFixe extends AbstractPlugin {
	protected Node_AccessPointOutboundPort napop;
	protected Node_AccessPointCommInboundPort napip;
	protected Node_AccessPointRoutingInboundPort naprip;
	protected List<Node_AccessPointCommOutboundPort> node_CommOBP = new ArrayList<>();
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected = new ArrayList<>();
	private Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	
	private int NumberOfNeighboorsToSend = 2;
	protected ComponentI owner;
	
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
		
        
		// Enables logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.logMessage("Node_AccessPoint " + this.address.getAdress());
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        //add port
        //
        this.napop = new Node_AccessPointOutboundPort(this.owner);
		this.napip = new Node_AccessPointCommInboundPort(this.owner);
		this.naprip = new Node_AccessPointRoutingInboundPort(this.owner);
		
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
		Set<ConnectionInfoI> devices = this.napop.registerAccessPoint(address,napip.getPortURI() , pointInitial, 25.00, naprip.getPortURI());
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			
			
			Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this.owner);
			napcop.publishPort();
			this.owner.doPortConnection(
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
		this.napip.connect(address, communicationInboundPortURI);
		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		this.napip.connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		this.napip.transmitMessage(m);
		return null;
		
	}

	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.napip.hasRouteFor(address);
	}

	public Object ping() throws Exception{
		this.napip.ping();
		return null;
	}


	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception{
		this.naprip.updateRouting(neighbour, routes);
	}
	
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		this.naprip.updateAccessPoint(neighbour, numberOfHops);
	}
		

}
