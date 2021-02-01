package fr.Utils.classes;

import fr.Utils.interfaces.ConnectionInfo;
import fr.Utils.interfaces.NodeAddressI;
import fr.Utils.interfaces.PositionI;

public class Noeud implements ConnectionInfo {
	private NodeAddressI address;
	private String communicationInboundPortURI;
	private PositionI initialPosition;
	private double initialRange;
	private boolean isRouting;
	
	public Noeud(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) {
		this.address=address;
		this.communicationInboundPortURI=communicationInboundPortURI;
		this.initialPosition=initialPosition;
		this.initialRange=initialRange;
		this.isRouting=isRouting;
	}
	
	public NodeAddressI getAddress(){
		return address;
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
	public void setAddress(NodeAddressI a) {
		this.address=a;
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
		// TODO Auto-generated method stub
		return communicationInboundPortURI;
	}
}
