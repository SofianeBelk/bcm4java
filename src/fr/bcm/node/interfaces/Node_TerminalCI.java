package fr.bcm.node.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_TerminalCI extends RequiredCI{
	public Set<ConnectionInfo> registerTerminalNode(NetworkAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
	public void unregister(AddressI address) throws Exception;
}