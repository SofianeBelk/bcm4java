package fr.bcm.node.connectors;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class NodeConnector extends AbstractConnector implements Node_TerminalCI, Node_AccessPointCI, Node_RoutingCI {

	@Override
	public void unregister(AddressI address) throws Exception {
		((RegistrationCI)this.offering).unregister(address);
	}


	@Override
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		return ((RegistrationCI)this.offering).registerAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange, routingInboundPortURI);
	}


	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		return ((RegistrationCI)this.offering).registerTerminalNode(address, communicationInboundPortURI, initialPosition, initialRange);
	}
	

	@Override
	public Set<ConnectionInfoI> registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception {
		return ((RegistrationCI)this.offering).registreRoutingNode(address, communicationInboundPortURI, initialPosition, initialRange, routingInBoundPortURI);
	}
	
	
	
	
}
