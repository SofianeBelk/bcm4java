package bcm.utils.address.interfaces;

public interface AddressI {
	public boolean isNodeAdress();
	public boolean isNetworkAdress();
	public boolean equals(AddressI a);
	public String getAdress();
}
