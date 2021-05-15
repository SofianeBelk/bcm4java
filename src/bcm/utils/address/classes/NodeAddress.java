package bcm.utils.address.classes;

import java.io.Serializable;

import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;

public class NodeAddress implements NodeAddressI, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	
	public NodeAddress() {
		this.address = Address.generateRandomAdress();
	}
	
	public String getAdress() {
		return address;
	}
	
	public boolean equals(AddressI a) {
		return address.equals(a.getAdress());
	}
	
}
