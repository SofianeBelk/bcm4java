package fr.bcm.utils.nodeInfo.interfaces;

import fr.bcm.utils.address.interfaces.AddressI;

public interface ConnectionInfo {
	
	public AddressI getAddress();
	public String getCommunicationInboundPortURI();
}
