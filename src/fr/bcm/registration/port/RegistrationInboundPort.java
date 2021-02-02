package fr.bcm.registration.port;

import java.util.Set;

import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.NodeAddressI;
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
		// TODO Auto-generated constructor stub
	}
	
	public RegistrationInboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,RegistrationCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	public Set<ConnectionInfo> registre(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).catalogue(address, communicationInboundPortURI, initialPosition, initialRange, isRouting)
				);
		
		
	}

	@Override
	public Set<ConnectionInfo> registreAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).catalogueAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange)
				);
	}

}
