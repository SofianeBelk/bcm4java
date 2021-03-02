package fr.bcm.utils.routing.interfaces;

import fr.bcm.utils.address.interfaces.AddressI;

public interface RouteInfoI {
	
	public AddressI getDestination();
	public int getNumberOfHops();
	//récupérer l'adresse intermédiaire
	public AddressI getIntermediate();
	public void setIntermediate(AddressI a);
	public void setHops(int hops);
	public RouteInfoI clone();

}
