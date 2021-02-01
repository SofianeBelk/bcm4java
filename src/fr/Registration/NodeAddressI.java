package fr.Registration;

public interface NodeAddressI {
	public default boolean isNodeAdress() {
		return true;
	}
	
	public default boolean isNetworkAdress() {
		return false;
	}
}
