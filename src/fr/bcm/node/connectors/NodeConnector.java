package fr.bcm.node.connectors;

import java.util.Set;

import fr.bcm.node.interfaces.Node_AccessPointCI;
import fr.bcm.node.interfaces.Node_RoutingCI;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class NodeConnector extends AbstractConnector implements Node_TerminalCI, Node_AccessPointCI, Node_RoutingCI {

	@Override
	public void unregister(AddressI address) throws Exception {
		((RegistrationCI)this.offering).unregister(address);
	}


	@Override
	public Set<ConnectionInfo> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		return ((RegistrationCI)this.offering).registerAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange);
	}


	@Override
	public Set<ConnectionInfo> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		return ((RegistrationCI)this.offering).registerTerminalNode(address, communicationInboundPortURI, initialPosition, initialRange, isRouting);
	}
	

	@Override
	public Set<ConnectionInfo> registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception {
		return ((RegistrationCI)this.offering).registreRoutingNode(address, communicationInboundPortURI, initialPosition, initialRange, routingInBoundPortURI);
	}
	
	
	
	
}
