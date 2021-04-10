package bcm.node.accesspoint.ports;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.routing.components.Node_Routing;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.registration.interfaces.RegistrationCI;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_AccessPointRoutingInboundPort extends AbstractInboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;

	public Node_AccessPointRoutingInboundPort(ComponentI owner) throws Exception {
		super(RoutingCI.class, owner);
	}


	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_AccessPoint)this.getServiceProviderReference()).updateRouting(neighbour, routes);
						return null;
					}
				}
		);
	}


	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_AccessPoint)this.getServiceProviderReference()).updateAccessPoint(neighbour, numberOfHops);
						return null;
					}
				}
		);
	}



	

	

}
