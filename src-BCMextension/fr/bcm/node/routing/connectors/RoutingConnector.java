package fr.bcm.node.routing.connectors;

import java.util.Set;

import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class RoutingConnector extends AbstractConnector implements RoutingCI {

	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		// TODO Auto-generated method stub
		((RoutingCI)this.offering).updateRouting(neighbour, routes);
	}

	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
		// TODO Auto-generated method stub
		((RoutingCI)this.offering).updateAccessPoint(neighbour, numberOfHops);
	}

}
