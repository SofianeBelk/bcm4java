package fr.bcm.node.accesspoint.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


public class Node_AccessPointOutboundPort extends AbstractOutboundPort implements Node_AccessPointCI{

	private static final long serialVersionUID = 1L;

	public Node_AccessPointOutboundPort(ComponentI owner) throws Exception {
		super(Node_AccessPointCI.class, owner);
	}

	@Override
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return ((Node_AccessPointCI)this.getConnector()).registerAccessPoint(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange,
				routingInboundPortURI);
	}


	@Override
	public void unregister(AddressI address) throws Exception {
		((Node_AccessPointCI)this.getConnector()).unregister(address);
		
	}





	

	

}
