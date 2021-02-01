package fr.bcm.utils.connectednodes.interfaces;

import fr.bcm.utils.address.interfaces.NodeAddressI;

public interface ConnectionInfo {
	
	public NodeAddressI getAddress();
	public String getCommunicationInboundPortURI();
}
