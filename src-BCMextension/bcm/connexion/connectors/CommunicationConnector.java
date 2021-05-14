package bcm.connexion.connectors;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class CommunicationConnector extends AbstractConnector implements CommunicationCI{

	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		((CommunicationCI)this.offering).connect(address, communicationInboundPortURI);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		((CommunicationCI)this.offering).connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
		((CommunicationCI)this.offering).transmitMessage(m);
	}

	@Override
	public boolean hasRouteFor(AddressI address) throws Exception {
		return ((CommunicationCI)this.offering).hasRouteFor(address);
	}

	@Override
	public Void ping() throws Exception {
		return ((CommunicationCI)this.offering).ping();
	}

}
