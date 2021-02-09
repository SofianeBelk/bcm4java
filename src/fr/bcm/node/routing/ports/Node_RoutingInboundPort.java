package fr.bcm.node.routing.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_RoutingInboundPort extends AbstractInboundPort implements Node_RoutingCI{

	private static final long serialVersionUID = 1L;

	public Node_RoutingInboundPort(ComponentI owner) throws Exception {
		super(Node_RoutingCI.class, owner);
	}


	@Override
	public Set<ConnectionInfoI> registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		return null;
	}


	@Override
	public void unregister(AddressI address) throws Exception {
		return;
	}



	

	

}
