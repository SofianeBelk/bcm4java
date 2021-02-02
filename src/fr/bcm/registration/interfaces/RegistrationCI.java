package fr.bcm.registration.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.connectednodes.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;



public interface RegistrationCI extends OfferedCI{
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
	public Set<ConnectionInfo> registreAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
	
}
