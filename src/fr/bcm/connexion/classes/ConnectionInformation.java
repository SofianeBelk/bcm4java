package fr.bcm.connexion.classes;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;

public class ConnectionInformation implements ConnectionInfoI{
	private AddressI address;
	private String communicationInboundPortURI;
	private String routingInboundPortURI;
	
	public ConnectionInformation(AddressI address, String communicationInboundPortURI){
		this.address = address;
		this.communicationInboundPortURI = communicationInboundPortURI;
	}
	
	public ConnectionInformation(AddressI address, String communicationInboundPortURI, String routingInboundPortURI){
		this.address = address;
		this.communicationInboundPortURI = communicationInboundPortURI;
		this.routingInboundPortURI = routingInboundPortURI;
	}
	
	public AddressI getAddress(){
		return address;
	}
	
	
	public String getRoutingInboundPortURI() {
		return routingInboundPortURI;
	}

	@Override
	public String getCommunicationInboundPortURI() {
		return communicationInboundPortURI;
	}
	
	
}
