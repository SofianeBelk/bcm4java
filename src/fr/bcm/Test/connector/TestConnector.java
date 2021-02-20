package fr.bcm.Test.connector;

import java.util.Set;

import fr.bcm.Test.GestionnaireResTest.interfaces.RegistrationCITest;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class TestConnector extends AbstractConnector implements RegistrationCITest {

	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCITest)this.offering).registerTerminalNode(address, communicationInboundPortURI, initialPosition, initialRange);
	}

	@Override
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCITest)this.offering).registerAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange, routingInboundPortURI);

	}

	@Override
	public Set<ConnectionInfoI> registreRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCITest)this.offering).registreRoutingNode(address, communicationInboundPortURI, initialPosition, initialRange, routingInBoundPortURI);

	}

	@Override
	public void unregister(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		((RegistrationCITest)this.offering).unregister(address);
	}

}
