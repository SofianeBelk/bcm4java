package fr.bcm.node.ports;

import java.util.Set;

import fr.bcm.node.interfaces.Node_EphemeralCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_EphemeralOutBoundPort extends AbstractOutboundPort implements Node_EphemeralCI{

	private static final long serialVersionUID = 1L;

	public Node_EphemeralOutBoundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, Node_EphemeralCI.class, owner);
	}

	@Override
	public Set<ConnectionInfo> registre(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		return ((Node_EphemeralCI)this.getConnector()).registre(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange, 
				isRouting);
	}

	@Override
	public Set<ConnectionInfo> registreAccessPoint(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return ((Node_EphemeralCI)this.getConnector()).registreAccessPoint(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange);
	}


	

	

}
