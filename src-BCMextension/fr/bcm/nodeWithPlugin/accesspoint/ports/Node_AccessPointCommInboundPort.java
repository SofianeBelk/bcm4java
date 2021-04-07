package fr.bcm.nodeWithPlugin.accesspoint.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;
import fr.bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import fr.bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import fr.bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.bcm.node.accesspoint.components.Node_AccessPoint;
import fr.bcm.node.routing.components.Node_Routing;

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
	public void ping() throws Exception{
		this.getOwner().handleRequest(Node_AccessPointP.Ping_URI,c -> ((Node_AccessPoint)c).ping());
	}


	

	

}
