package bcm.node.routing.ports;

import java.util.Set;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.routing.components.Node_Routing;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.terminal.components.Node_Terminal;
import bcm.node.terminal.interfaces.Node_TerminalCI;
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

	public Node_RoutingCommInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}


	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_Routing)this.getServiceProviderReference()).connect(address, communicationInboundPortURI);
						return null;
					}
				}
		);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_Routing)this.getServiceProviderReference()).connectRouting(address, communicationInboundPortURI,routingInboundPortURI);
						return null;
					}
				}
		);
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		this.getOwner().handleRequest(
				
				
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_Routing)this.getServiceProviderReference()).transmitMessage(m);
						return null;
					}
				}
		);
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(c -> ((Node_Routing)c).hasRouteFor(address));
	}

	@Override
	public Void ping() throws Exception{
		return this.getOwner().handleRequest(c -> ((Node_Routing)c).ping());
	}




	

	

}
