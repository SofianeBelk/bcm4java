package bcm.utils.address.interfaces;

public interface NodeAddressI extends AddressI{
	public default boolean isNodeAdress() {
		return true;
	}
	
	public default boolean isNetworkAdress() {
		return false;
	}
}
