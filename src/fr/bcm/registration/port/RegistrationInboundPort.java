package fr.bcm.registration.port;

import java.util.Set;

import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.registration.component.GestionnaireReseau;

public class RegistrationInboundPort 
extends AbstractInboundPort
implements RegistrationCI
{

	public RegistrationInboundPort(ComponentI owner) throws Exception {
		super(RegistrationCI.class, owner);
		assert owner instanceof GestionnaireReseau;
	}
		
	public RegistrationInboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,RegistrationCI.class, owner);
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		return (Set<ConnectionInfoI>) this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).registerTerminalNode(address, communicationInboundPortURI, initialPosition, initialRange, false)
				);
		
		
	}

	@Override
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		
		return (Set<ConnectionInfoI>) this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).registerAccessPoint(address, communicationInboundPortURI, initialPosition, initialRange, routingInboundPortURI)
				);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ConnectionInfoI> registreRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception {
		return (Set<ConnectionInfoI>) this.getOwner().handleRequest(
				c -> ((GestionnaireReseau)c).registerRoutingNode(address, communicationInboundPortURI, initialPosition, initialRange, routingInBoundPortURI)
		);
	}

	@Override
	public void unregister(AddressI address) throws Exception {
		this.getOwner().handleRequest(c -> ((GestionnaireReseau)c).unregister(address));
	}

	

}
