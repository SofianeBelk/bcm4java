package fr.bcm.registration.interfaces;

import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.connectednodes.interfaces.ConnectionInfo;
import fr.bcm.utils.connectednodes.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;

import java.util.Set;


public interface RegistrationCI extends OfferedCI{
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting);
	public Set<ConnectionInfo> registreAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange );
	
}
