package fr.Registration;

import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface RegistrationCI extends OfferedCI{
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
	public Set<ConnectionInfo> registreAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
	
}
