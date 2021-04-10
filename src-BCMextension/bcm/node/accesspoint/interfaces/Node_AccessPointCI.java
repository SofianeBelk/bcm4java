package bcm.node.accesspoint.interfaces;

import java.util.Set;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_AccessPointCI extends RequiredCI{
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception;
	public void unregister(AddressI address) throws Exception;
}
