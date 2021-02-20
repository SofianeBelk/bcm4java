package fr.bcm.Test.GestionnaireResTest.ports;

import java.util.Set;

import fr.bcm.Test.GestionnaireResTest.interfaces.RegistrationCITest;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class cTestReseauOutBoundPort extends AbstractOutboundPort implements RegistrationCITest{
	private static final long serialVersionUID = 1L;

	public cTestReseauOutBoundPort(ComponentI owner)
			throws Exception {
		super(RegistrationCITest.class, owner);
	}
	
	public cTestReseauOutBoundPort(String ntopUri, ComponentI owner)
			throws Exception {
		super(ntopUri,RegistrationCITest.class, owner);
	}


	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCITest)this.getConnector()).registerTerminalNode(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange);
	}

	@Override
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCITest)this.getConnector()).registerAccessPoint(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange,
				routingInboundPortURI);
	}

	@Override
	public Set<ConnectionInfoI> registreRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCITest)this.getConnector()).registreRoutingNode(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange,
				routingInBoundPortURI);
	}

	@Override
	public void unregister(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		((RegistrationCITest)this.getConnector()).unregister(address);

	}
}