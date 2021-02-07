package fr.bcm.registration.interfaces;

import java.util.Set;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;



public interface RegistrationCI extends OfferedCI{
	public Set<ConnectionInfo> registerTerminalNode(NetworkAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting) throws Exception;
	public Set<ConnectionInfo> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ) throws Exception;
	public Set<ConnectionInfo> registreRoutingNode(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange, String routingInBoundPortURI) throws Exception;
	public void unregister(AddressI address) throws Exception;
}
