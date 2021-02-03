package fr.bcm.registration.port;

import java.util.Set;

import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.bcm.registration.component.GestionnaireReseau;

public class RegistrationInboundPort 
extends AbstractInboundPort
implements RegistrationCI
{

	public RegistrationInboundPort(ComponentI owner) throws Exception {
		super(RegistrationCI.class, owner);
	}
	
	public RegistrationInboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,RegistrationCI.class, owner);
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	public Set<ConnectionInfo> registre(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		return (Set<ConnectionInfo>) this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).register(address, communicationInboundPortURI, initialPosition, initialRange, isRouting)
				);
		
		
	}

	@Override
	public Set<ConnectionInfo> registreAccessPoint(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		return (Set<ConnectionInfo>) this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).registerAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange)
				);
	}

}
