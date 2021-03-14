package fr.bcm.nodeWithPlugin.terminal.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.ports.*;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class Tablette extends AbstractPlugin {
	private static final long serialVersionUID = 1L ;
	protected ComponentI owner;
	protected String URIportCommunication; 
	protected String URIportNodeTerminale; 
	protected Node_TerminalOutBoundPort ntop;
	protected Node_TerminalCommInboundPort ntcip;
	protected List<Node_TerminalCommOutboundPort> node_CommOBP = new ArrayList<>();
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected= new ArrayList<>();

	

	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		System.out.println("plugin node terminal started");

		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_TerminalCI.class);
        this.addOfferedInterface(CommunicationCI.class);
       
		
		// Enable logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.owner.logMessage("Node Terminal " + this.address.getAdress());
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        //add port
        this.ntop = new Node_TerminalOutBoundPort(this.owner);
		this.ntcip = new Node_TerminalCommInboundPort(this.owner);
		this.ntop.publishPort();
		this.ntcip.publishPort();
		
		this.owner.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		
	}
	
	public void start() throws Exception {
		 this.logMessage("Tries to log in the manager");
			Position pointInitial= new Position(10,10);
			Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, ntcip.getPortURI(),pointInitial , 20.00);
			this.logMessage("Logged");
			
			// Current node connects to others nodes
			
			for(ConnectionInfoI CInfo: devices) {
				Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this.owner);
				ntcop.publishPort();
				
				this.addressConnected.add(CInfo);
				try {
					this.owner.doPortConnection(
							ntcop.getPortURI(),
							CInfo.getCommunicationInboundPortURI(),
							CommunicationConnector.class.getCanonicalName()
					);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				ntcop.connect(address, this.ntcip.getPortURI());
				node_CommOBP.add(ntcop);
			}
			this.logMessage("Connected to all nearby devices");
	}

	
	@Override
	public void	finalise() throws Exception
	{		
		super.finalise();
		this.owner.doPortDisconnection(this.ntop.getPortURI());


	}

	
	@Override
	public void	uninstall() throws Exception
	{
		super.uninstall();

		this.ntop.unpublishPort();		
		this.ntcip.unpublishPort();
		
		this.ntop.destroyPort() ;
		this.ntcip.destroyPort() ;
		
	}


	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		this.ntcip.connect(address, communicationInboundPortURI);
	}


	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI)
			throws Exception {
		// TODO Auto-generated method stub
		this.ntcip.connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}


	public void transmitMessage(MessageI m) throws Exception {
		// TODO Auto-generated method stub
		this.ntcip.transmitMessage(m);
	}


	
	public boolean hasRouteFor(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		return this.ntcip.hasRouteFor(address);
	}


	
	public void ping() throws Exception {
		// TODO Auto-generated method stub
		this.ntcip.ping();
	}


	
	

}
