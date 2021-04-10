package bcm.nodeWithPlugin.accesspoint.ports;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.routing.components.Node_Routing;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;
import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
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
	private String pluginURI;
	
	public Node_AccessPointRoutingInboundPort(ComponentI owner) throws Exception {
		super(RoutingCI.class, owner);
	}

	public Node_AccessPointRoutingInboundPort(ComponentI owner, String pluginURI) throws Exception {
		super(CommunicationCI.class, owner);
		this.pluginURI = pluginURI;
	}

	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		this.getOwner().runTask(			
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().updateRouting(neighbour, routes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}


	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		this.getOwner().runTask(				
				nr -> {
					try {
						((Node_AccessPointI)nr).getPlugin().updateAccessPoint(neighbour, numberOfHops);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			);
	}



	

	

}
