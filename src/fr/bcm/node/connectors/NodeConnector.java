package fr.bcm.node.connectors;

import java.util.Set;

import fr.bcm.node.interfaces.Node_EphemeralCI;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.connectednodes.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class NodeConnector extends AbstractConnector implements Node_TerminalCI,Node_EphemeralCI {

	@Override
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		return ((RegistrationCI)this.offering).registre(address, communicationInboundPortURI, initialPosition, initialRange, isRouting);
	}

	@Override
	public Set<ConnectionInfo> registreAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCI)this.offering).registreAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange);
	}
	
	
	
	
}
