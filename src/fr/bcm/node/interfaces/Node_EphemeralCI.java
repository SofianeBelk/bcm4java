package fr.bcm.node.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_EphemeralCI extends RequiredCI{
	public Set<ConnectionInfo> registre(AddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
	public Set<ConnectionInfo> registreAccessPoint(AddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
}
