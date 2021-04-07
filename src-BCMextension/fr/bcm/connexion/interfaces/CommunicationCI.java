package fr.bcm.connexion.interfaces;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface CommunicationCI extends OfferedCI , RequiredCI  {
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception;
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI , String routingInboundPortURI) throws Exception;
	public void transmitMessage(MessageI m) throws Exception;
	public boolean hasRouteFor(AddressI address) throws Exception;
	public void ping() throws Exception;
	
}
