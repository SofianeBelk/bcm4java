package bcm.connexion.interfaces;

import bcm.utils.address.interfaces.AddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;

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