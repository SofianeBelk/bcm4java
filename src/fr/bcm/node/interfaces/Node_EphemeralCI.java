package fr.bcm.node.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_EphemeralCI extends RequiredCI{
	public Set<ConnectionInfo> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
	public void unregister(AddressI address) throws Exception;
}
