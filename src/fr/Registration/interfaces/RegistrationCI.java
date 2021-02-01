package fr.Registration.interfaces;

import java.util.Set;

import fr.Utils.interfaces.ConnectionInfo;
import fr.Utils.interfaces.NodeAddressI;
import fr.Utils.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface RegistrationCI extends OfferedCI{
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
	public Set<ConnectionInfo> registreAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
	
}
