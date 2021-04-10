package bcm.node.terminal.interfaces;

import java.util.Set;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_TerminalCI extends RequiredCI{
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange) throws Exception;
	public void unregister(AddressI address) throws Exception;
}
