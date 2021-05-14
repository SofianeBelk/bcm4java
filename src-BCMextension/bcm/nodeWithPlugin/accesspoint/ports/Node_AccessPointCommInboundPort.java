package bcm.nodeWithPlugin.accesspoint.ports;

import java.util.Set;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.routing.components.Node_Routing;
import bcm.node.terminal.components.Node_Terminal;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;
import bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class Node_AccessPointCommInboundPort extends AbstractInboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;
	
	public Node_AccessPointCommInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}
	
	public Node_AccessPointCommInboundPort(ComponentI owner, String pluginURI) throws Exception {
		super(CommunicationCI.class, owner);
		this.pluginURI = pluginURI;
	}

	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.getOwner().runTask(Node_AccessPointP.Connect_URI,		
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().connect(address, communicationInboundPortURI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		
		this.getOwner().runTask(Node_AccessPointP.ConnectRouting_URI,			
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		this.getOwner().runTask(Node_AccessPointP.Transmit_MESSAGES_URI,
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().transmitMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(Node_AccessPointP.Has_Routes_URI,c -> ((Node_AccessPoint)c).hasRouteFor(address));
	}

	@Override
	public Void ping() throws Exception{
		return this.getOwner().handleRequest(Node_AccessPointP.Ping_URI,c -> ((Node_AccessPointI)c).getPlugin().ping());
	}


	

	

}
