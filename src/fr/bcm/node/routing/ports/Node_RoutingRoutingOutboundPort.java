package fr.bcm.node.routing.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.classes.RouteInfo;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_RoutingRoutingOutboundPort extends AbstractOutboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;

	public Node_RoutingRoutingOutboundPort(ComponentI owner) throws Exception{
		super(RoutingCI.class, owner);
	}
	
	public Node_RoutingRoutingOutboundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, RoutingCI.class, owner);
	}
	
	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfo> routes) throws Exception {
		((RoutingCI)this.getConnector()).updateRouting(neighbour, routes);
		
	}
	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
		((RoutingCI)this.getConnector()).updateAccessPoint(neighbour, numberOfHops);
	}


	


	

	

}
