package fr.bcm.connexion.utils;

import fr.bcm.utils.address.interfaces.AddressI;

public interface RouteInfo {
	public AddressI getDestination();
	public int getNumberOfHops();
}
