package fr.bcm.node.terminal.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class Node_TerminalInboundPort extends AbstractInboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;

	public Node_TerminalInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}

	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.getOwner().handleRequest(c -> ((Node_Terminal)c).connect(address, communicationInboundPortURI));
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		this.getOwner().handleRequest(c -> ((Node_Terminal)c).connectRouting(address, communicationInboundPortURI, routingInboundPortURI));
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		this.getOwner().handleRequest(c -> {((Node_Terminal)c).transmitMessage(m); return null;});
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(c -> ((Node_Terminal)c).hasRouteFor(address));
	}

	@Override
	public void ping() throws Exception{
		this.getOwner().handleRequest(c -> ((Node_Terminal)c).ping());
	}


	

	

}
