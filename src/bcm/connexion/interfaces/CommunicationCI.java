package bcm.connexion.interfaces;

import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface CommunicationCI extends OfferedCI , RequiredCI  {
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception;
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI , String routingInboundPortURI) throws Exception;
	public void transmitMessage(MessageI m) throws Exception;
	public boolean hasRouteFor(AddressI address) throws Exception;
	public Void ping() throws Exception;
}
