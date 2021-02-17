package fr.bcm.connexion.interfaces;

import fr.bcm.utils.address.interfaces.AddressI;

public interface ConnectionInfoI {
	
	public AddressI getAddress();
	public String getCommunicationInboundPortURI();
	public String getRoutingInboundPortURI();
}
