package fr.bcm.node.routing.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_RoutingCommOutboundPort extends AbstractOutboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;

	public Node_RoutingCommOutboundPort(ComponentI owner) throws Exception{
		super(CommunicationCI.class, owner);
	}
	public Node_RoutingCommOutboundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, CommunicationCI.class, owner);
	}


	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		((CommunicationCI)this.getConnector()).connect(address, communicationInboundPortURI);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		((CommunicationCI)this.getConnector()).connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		((CommunicationCI)this.getConnector()).transmitMessage(m);
		
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return ((CommunicationCI)this.getConnector()).hasRouteFor(address);
	}

	@Override
	public void ping() throws Exception{
		((CommunicationCI)this.getConnector()).ping();
	}


	

	

}
