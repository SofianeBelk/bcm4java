package fr.bcm.nodeWithPlugin.routing.ports;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.nodeWithPlugin.routing.components.Ordinateur;
import fr.bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_RoutingRoutingInboundPort extends AbstractInboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;

	public Node_RoutingRoutingInboundPort(ComponentI owner) throws Exception {
		super(RoutingCI.class, owner);
	}
	
	public Node_RoutingRoutingInboundPort(ComponentI owner, String pluginURI) throws Exception{
		super(CommunicationCI.class, owner);
		this.pluginURI = pluginURI;
	}


	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		this.getOwner().runTask(Node_RoutingP.UpdateRouting_URI,				
			nr -> {
				try {
					((Node_RoutingI)nr).getPlugin().updateRouting(neighbour, routes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}


	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		this.getOwner().runTask(Node_RoutingP.UpdateAccessPoint_URI,				
				nr -> {
					try {
						((Node_RoutingI)nr).getPlugin().updateAccessPoint(neighbour, numberOfHops);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			);
	}



	

	

}
