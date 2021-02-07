package fr.bcm.node.ports;

import java.util.Set;

import fr.bcm.node.interfaces.Node_EphemeralCI;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.classes.NodeAddress;
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
	public Set<ConnectionInfo> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return ((Node_EphemeralCI)this.getConnector()).registerAccessPoint(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange);
	}


	@Override
	public void unregister(AddressI address) throws Exception {
		((Node_EphemeralCI)this.getConnector()).unregister(address);
		
	}


	

	

}
