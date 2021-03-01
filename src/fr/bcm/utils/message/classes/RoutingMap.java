package fr.bcm.utils.message.classes;

import fr.bcm.utils.address.interfaces.AddressI;

public class RoutingMap implements RouteInfo{
	private AddressI distinataire;
	private AddressI intermediare;
	private int numberOfhops;
	
	public RoutingMap(AddressI distinataire, AddressI intermediare) {
		this.distinataire = distinataire;
		this.intermediare = intermediare;
		// Hypothése de départ "Object Constructor"
		this.numberOfhops=0;
	}
	
	@Override
	public AddressI getDestination() {
		// TODO Auto-generated method stub
		return distinataire;
	}

	@Override
	public int getNumberOfHops() {
		// TODO Auto-generated method stub
		return numberOfhops;
	}

	@Override
	public AddressI getIntermediare() {
		// TODO Auto-generated method stub
		return intermediare;
	}
	
	public void setNumberOfHops(int numberOfhops) {
		this.numberOfhops=numberOfhops;
	}

}
