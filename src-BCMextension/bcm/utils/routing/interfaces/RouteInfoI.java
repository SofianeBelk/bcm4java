package bcm.utils.routing.interfaces;

import bcm.utils.address.interfaces.AddressI;

public interface RouteInfoI {
	
	public AddressI getDestination();
	public int getNumberOfHops();
	//récupérer l'adresse intermédiaire
	public AddressI getIntermediate();
	public void setDestination(AddressI a);
	public void setIntermediate(AddressI a);
	public void setHops(int hops);
	public RouteInfoI clone();

}
