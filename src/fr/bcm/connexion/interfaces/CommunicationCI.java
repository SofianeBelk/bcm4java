package fr.bcm.connexion.interfaces;

import fr.bcm.connexion.utils.MessageI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface CommunicationCI  extends OfferedCI , RequiredCI  {
	public void connect(NodeAddressI address, String communicationInboundPortURI);
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI , String routingInboundPortURI);
	public void transmitMessage(MessageI m);
	public boolean hasRouteFor(AddressI address);
	public void ping();
	
}
