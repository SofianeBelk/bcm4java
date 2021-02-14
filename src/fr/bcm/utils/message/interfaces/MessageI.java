package fr.bcm.utils.message.interfaces;
import java.io.Serializable;
import java.util.List;

import fr.bcm.utils.address.interfaces.AddressI;

public interface MessageI {

	public AddressI getAddress();
	public Serializable getContent();
	public boolean stillAlive();
	public void decrementHops();
	public MessageI copy();
	public void addAddressToHistory(AddressI a);
	public boolean isInHistory(AddressI a);
}

