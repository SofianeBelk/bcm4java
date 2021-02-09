package fr.bcm.connexion.classes;

import fr.bcm.utils.address.interfaces.AddressI;

public class ConnectionInformation {
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
	
	public String getCommunicationIBPU(){
		return communicationInboundPortURI;
	}
	
	public String getRoutingIBPU() {
		return routingInboundPortURI;
	}
	
	
}
