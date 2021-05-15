package bcm.utils.message.interfaces;
import java.io.Serializable;
import java.util.List;

import bcm.utils.address.interfaces.AddressI;

public interface MessageI {

	public AddressI getAddress();
	public Serializable getContent();
	public boolean stillAlive();
	public void decrementHops();
	public MessageI copy();
}

