package fr.bcm.node.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_TerminalOutBoundPort extends AbstractOutboundPort implements Node_TerminalCI{

	private static final long serialVersionUID = 1L;

	public Node_TerminalOutBoundPort(ComponentI owner) throws Exception{
		super(Node_TerminalCI.class, owner);
	}
	public Node_TerminalOutBoundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, Node_TerminalCI.class, owner);
	}

	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		
		return ((Node_TerminalCI)this.getConnector()).registerTerminalNode(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange, 
				isRouting);
	}

	@Override
	public void unregister(AddressI address) throws Exception {
		((Node_TerminalCI)this.getConnector()).unregister(address);
	}

}
