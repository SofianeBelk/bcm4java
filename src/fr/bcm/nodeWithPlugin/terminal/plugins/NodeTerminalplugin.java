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
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class NodeTerminalplugin extends AbstractPlugin implements CommunicationCI{
	private static final long serialVersionUID = 1L ;
	protected ComponentI owner;
	protected String URIportCommunication; 
	protected String URIportNodeTerminale; 
	protected Node_TerminalOutBoundPort ntop;
	protected Node_TerminalInboundPort ntip;
	protected List<Node_TerminalCommOutboundPort> node_CommOBP = new ArrayList<>();
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected= new ArrayList<>();

	
	public NodeTerminalplugin(String URIportNodeTerminale,String URIportCommunication) {
		this.URIportCommunication=URIportCommunication;
		this.URIportNodeTerminale=URIportNodeTerminale;
	}
	
	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		System.out.println("plugin node terminal started");

		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_TerminalCI.class);
        this.addOfferedInterface(RegistrationCI.class);
        
        //add port
        this.ntop = new Node_TerminalOutBoundPort(this.owner);
		this.ntip = new Node_TerminalInboundPort(this.owner);
		this.ntop.publishPort();
		this.ntip.publishPort();
		
		
		
		// Enable logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.owner.logMessage("Node Terminal " + this.address.getAdress());
        
	}

	
	@Override
	public void	initialise() throws Exception {
		System.out.println("Plugin Initialise ");
        super.initialise();
        this.owner.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		
	}

	
	@Override
	public void	finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.ntop.getPortURI());
		this.owner.doPortDisconnection(this.ntip.getPortURI());
		super.finalise();

	}

	
	@Override
	public void	uninstall() throws Exception
	{
		this.ntop.unpublishPort();
		this.ntip.destroyPort() ;
		
		this.ntip.unpublishPort();
		this.ntip.destroyPort() ;
		
		super.uninstall();
	}


	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		this.ntip.connect(address, communicationInboundPortURI);
	}


	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI)
			throws Exception {
		// TODO Auto-generated method stub
		this.ntip.connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}


	@Override
	public void transmitMessage(MessageI m) throws Exception {
		// TODO Auto-generated method stub
		this.ntip.transmitMessage(m);
	}


	@Override
	public boolean hasRouteFor(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		return this.ntip.hasRouteFor(address);
	}


	@Override
	public void ping() throws Exception {
		// TODO Auto-generated method stub
		this.ntip.ping();
	}
	
	

}
