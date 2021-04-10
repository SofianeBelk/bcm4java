package bcm.nodeWithPlugin.routing.ports;

import java.util.Set;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.routing.components.Node_Routing;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.terminal.components.Node_Terminal;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;
import bcm.registration.interfaces.RegistrationCI;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class Node_RoutingCommInboundPort extends AbstractInboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;

	public Node_RoutingCommInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}
	
	public Node_RoutingCommInboundPort(ComponentI owner, String pluginURI) throws Exception{
		super(CommunicationCI.class, owner);
		this.pluginURI = pluginURI;
	}


	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.getOwner().runTask(Node_RoutingP.Connect_URI,				
			nr -> {
				try {
					((Node_RoutingI)nr).getPlugin().connect(address, communicationInboundPortURI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		
		this.getOwner().runTask(Node_RoutingP.ConnectRouting_URI,				
			nr -> {
				try {
					((Node_RoutingI)nr).getPlugin().connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		this.getOwner().runTask(Node_RoutingP.Transmit_MESSAGES_URI,				
			nr -> {
				try {
					((Node_RoutingI)nr).getPlugin().transmitMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(c -> ((Node_Routing)c).hasRouteFor(address));
	}

	@Override
	public void ping() throws Exception{
		this.getOwner().handleRequest(c -> ((Node_Routing)c).ping());
	}




	

	

}