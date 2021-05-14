package bcm.node.routing.interfaces;

import java.util.Set;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_RoutingCI extends RequiredCI, OfferedCI{
	public Set<ConnectionInfoI> registerRoutingNode(
			NodeAddressI address, 
			String communicationInboundPortURI,
			PositionI initialPosition, 
			double initialRange,
			String routingInBoundPortURI) throws Exception;
	public ConnectionInfoI getRandomConn() throws Exception;
	public void unregister(AddressI address) throws Exception;
}
