package fr.bcm.node.routing.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.classes.RouteInfo;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface RoutingCI extends OfferedCI , RequiredCI{
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfo> routes) throws Exception;
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception;

}
