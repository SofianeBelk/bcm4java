package fr.bcm.connexion.interfaces;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;

public interface ConnectionInfoI {
	
	public AddressI getAddress();
	public String getCommunicationInboundPortURI();
	public String getRoutingInboundPortURI();
	

	public PositionI getinitialPosition();
	
	public double getinitialRange();
	
	public boolean getisRouting();
	
	public boolean getisAccessPoint();
	
	public void setAddress(AddressI a);
	
	public void setcommunicationInboundPortURI(String a);
	
	public void setinitialPosition(PositionI a);
	
	public void setinitialRange(double a);

}
