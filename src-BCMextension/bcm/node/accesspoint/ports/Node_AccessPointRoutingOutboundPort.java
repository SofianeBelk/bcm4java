package bcm.node.accesspoint.ports;

import java.util.Set;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.registration.interfaces.RegistrationCI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_AccessPointRoutingOutboundPort extends AbstractOutboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;

	public Node_AccessPointRoutingOutboundPort(ComponentI owner) throws Exception{
		super(RoutingCI.class, owner);
	}
	
	public Node_AccessPointRoutingOutboundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, RoutingCI.class, owner);
	}
	
	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		((RoutingCI)this.getConnector()).updateRouting(neighbour, routes);
	}
	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
		((RoutingCI)this.getConnector()).updateAccessPoint(neighbour, numberOfHops);
	}


	


	

	

}
