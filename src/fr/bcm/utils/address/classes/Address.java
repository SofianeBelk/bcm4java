package fr.bcm.utils.address.classes;

import java.util.UUID;

import fr.bcm.utils.address.interfaces.AddressI;

public class Address implements AddressI{
	
	private String address;
	
	public Address() {
		this.address = Address.generateRandomAdress();
	}
	
	
	public static String generateRandomAdress(){
		return UUID.randomUUID().toString();
	}
	
	@Override
	public boolean isNodeAdress() {
		return false;
	}
	@Override
	public boolean isNetworkAdress() {
		return false;
	}
	
	public String getAdress() {
		return address;
	}
	
	@Override
	public boolean equals(AddressI a) {
		return address.equals(a.getAdress());
	}
	
}
