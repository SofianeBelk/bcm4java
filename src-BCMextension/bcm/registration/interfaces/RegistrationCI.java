package bcm.registration.interfaces;

import java.util.Set;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;



public interface RegistrationCI extends OfferedCI{
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange) throws Exception;
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception;
	public Set<ConnectionInfoI> registreRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception;
	public void unregister(AddressI address) throws Exception;
	
}
