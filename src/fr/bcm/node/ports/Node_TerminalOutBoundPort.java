package fr.bcm.node.ports;

import java.util.Set;

import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.connectednodes.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_TerminalOutBoundPort extends AbstractOutboundPort implements Node_TerminalCI{

	private static final long serialVersionUID = 1L;

	public Node_TerminalOutBoundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, Node_TerminalCI.class, owner);
	}

	@Override
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		return ((RegistrationCI)this.getConnector()).registre(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange, 
				isRouting);
	}


	

	

}
