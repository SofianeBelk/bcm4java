package fr.bcm.connexion.connectors;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class CommunicationConnector extends AbstractConnector implements CommunicationCI{

	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) {
		// TODO Auto-generated method stub
		((CommunicationCI)this.offering).connect(address, communicationInboundPortURI);
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) {
		// TODO Auto-generated method stub
		((CommunicationCI)this.offering).connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}

	@Override
	public void transmitMessage(MessageI m) {
		// TODO Auto-generated method stub
		((CommunicationCI)this.offering).transmitMessage(m);
	}

	@Override
	public boolean hasRouteFor(AddressI address) {
		// TODO Auto-generated method stub
		return ((CommunicationCI)this.offering).hasRouteFor(address);
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub
		((CommunicationCI)this.offering).ping();
	}

}
