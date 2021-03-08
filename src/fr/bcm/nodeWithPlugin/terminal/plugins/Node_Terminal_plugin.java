package fr.bcm.nodeWithPlugin.terminal.plugins;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.nodeWithPlugin.terminal.ports.Node_TerminalInboundPortPlugin;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class Node_Terminal_plugin extends AbstractPlugin implements CommunicationCI{
	private static final long serialVersionUID = 1L ;
	protected Node_TerminalInboundPortPlugin np;
	protected ComponentI owner;
	
	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		System.out.println("plugin node terminal started");
		super.installOn(owner) ;
		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_TerminalCI.class);
        this.addOfferedInterface(RegistrationCI.class);
        
        //add port
        
        np.publishPort();
	}

	
	@Override
	public void	initialise() throws Exception {
		System.out.println("Plugin Initialise ");
        super.initialise();

		// connect the outbound port.
        this.owner.doPortConnection(
				this.np.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
	}

	
	@Override
	public void	finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.np.getPortURI());
		super.finalise();
	}

	
	@Override
	public void	uninstall() throws Exception
	{
		this.np.unpublishPort() ;
		this.np.destroyPort() ;
		super.uninstall();
	}


	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void transmitMessage(MessageI m) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean hasRouteFor(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void ping() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}
