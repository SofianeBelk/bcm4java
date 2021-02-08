package fr.bcm.connexion.utils;
import java.io.Serializable;

import fr.bcm.utils.address.interfaces.AddressI;

public interface MessageI {

	public AddressI getAddress();
	public Serializable getContent();
	public boolean stillAlive();
	public void decrementHops();
	
}

