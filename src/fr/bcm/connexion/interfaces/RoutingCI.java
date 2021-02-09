package fr.bcm.connexion.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.classes.RouteInfo;
import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface RoutingCI extends OfferedCI{
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfo> routes);
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops);

}
