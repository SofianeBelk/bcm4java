package fr.bcm.utils.address.classes;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;

public class NetworkAddress implements NetworkAddressI{
	
	private String address;
	
	public NetworkAddress() {
		this.address = Address.generateRandomAdress();
	}
	
	public String getAdress() {
		return address;
	}
	
	@Override
	public boolean equals(AddressI a) {
		return address.equals(a.getAdress());
	}
	

}
