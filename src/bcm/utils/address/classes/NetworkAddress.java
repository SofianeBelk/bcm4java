package bcm.utils.address.classes;

import java.io.Serializable;

import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;

public class NetworkAddress implements NetworkAddressI, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
