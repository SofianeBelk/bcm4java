package fr.bcm.utils.nodeInfo.interfaces;

public interface NetworkAddressI extends AddressI{
	public default boolean isNodeAdress() {
		return false;
	}
	
	public default boolean isNetworkAdress() {
		return true;
	}
	
}
