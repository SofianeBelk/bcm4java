package fr.bcm.utils.nodeInfo.classes;


import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;

public class Noeud implements ConnectionInfoI {
	private AddressI address;
	private String communicationInboundPortURI;
	private PositionI initialPosition;
	private double initialRange;
	private boolean isRouting = false;
	private String routingInboundPortURI;
	
	public Noeud(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) {
		this.address=address;
		this.communicationInboundPortURI=communicationInboundPortURI;
		this.initialPosition=initialPosition;
		this.initialRange=initialRange;
		this.isRouting=isRouting;
	}
	
	
	public Noeud(AddressI address, String communicationInboundPortURI, PositionI initialPosition,
			double initialRange, boolean isRouting, String routingInboundPortURI) {
		this.address=address;
		this.communicationInboundPortURI=communicationInboundPortURI;
		this.initialPosition=initialPosition;
		this.initialRange=initialRange;
		this.isRouting=isRouting;
		this.routingInboundPortURI = routingInboundPortURI;
	}

	


	public AddressI getAddress(){
		return this.address;
	}
	

	public PositionI getinitialPosition(){
		return initialPosition;
	}
	public double getinitialRange(){
		return initialRange;
	}
	
	public boolean getisRouting() {
		return this.isRouting;
	}
	public void setAddress(AddressI a) {
		this.address.equals(a);
	}
	
	public void setcommunicationInboundPortURI(String a) {
		this.communicationInboundPortURI=a;
	}
	
	public void setinitialPosition(PositionI a) {
		this.initialPosition=a;
	}
	
	public void setinitialRange(double a) {
		this.initialRange=a;
	}

	@Override
	public String getCommunicationInboundPortURI() {
		return communicationInboundPortURI;
	}


	public String getRoutingInboundPortURI() {
		return routingInboundPortURI;
	}


}
