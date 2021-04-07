package fr.bcm.utils.address.classes;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;

public class NodeAddress implements NodeAddressI {
	
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
