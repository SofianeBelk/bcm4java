package bcm.utils.nodeInfo.classes;


import java.io.Serializable;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;

public class Noeud implements ConnectionInfoI, Serializable {
	private AddressI address;
	private String communicationInboundPortURI;
	private PositionI initialPosition;
	private double initialRange;
	private boolean isRouting = false;
	private String routingInboundPortURI;
	private boolean isAccessPoint = false;
	
	public Noeud(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) {
		this.address=address;
		this.communicationInboundPortURI=communicationInboundPortURI;
		this.initialPosition=initialPosition;
		this.initialRange=initialRange;
		this.isRouting=isRouting;
	}
	
	public Noeud(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) {
		this.address=address;
		this.communicationInboundPortURI=communicationInboundPortURI;
		this.initialPosition=initialPosition;
		this.initialRange=initialRange;
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
	
	public Noeud(AddressI address, String communicationInboundPortURI, PositionI initialPosition,
			double initialRange, boolean isRouting, String routingInboundPortURI, boolean isAccessPoint) {
		this.address=address;
		this.communicationInboundPortURI=communicationInboundPortURI;
		this.initialPosition=initialPosition;
		this.initialRange=initialRange;
		this.isRouting=isRouting;
		this.routingInboundPortURI = routingInboundPortURI;
		this.isAccessPoint = isAccessPoint;
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
	
	public boolean getisAccessPoint() {
		return this.isAccessPoint;
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
		return this.routingInboundPortURI;
	}


}
