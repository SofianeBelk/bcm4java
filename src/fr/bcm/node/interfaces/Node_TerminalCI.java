package fr.bcm.node.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.connectednodes.interfaces.ConnectionInfo;
import fr.bcm.utils.connectednodes.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_TerminalCI extends RequiredCI{
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
}
