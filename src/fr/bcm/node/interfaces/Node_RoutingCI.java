package fr.bcm.node.interfaces;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Node_RoutingCI extends RequiredCI, OfferedCI{
	public Set<ConnectionInfoI> registerRoutingNode(
			NodeAddressI address, 
			String communicationInboundPortURI,
			PositionI initialPosition, 
			double initialRange,
			String routingInBoundPortURI) throws Exception;
	public void unregister(AddressI address) throws Exception;
}
