package fr.bcm.node.accesspoint.interfaces;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_AccessPointCI extends RequiredCI{
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
	public void unregister(AddressI address) throws Exception;
}
