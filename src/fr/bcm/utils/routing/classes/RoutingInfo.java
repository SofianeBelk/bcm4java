package fr.bcm.utils.routing.classes;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;

public class RoutingInfo implements RouteInfoI{
	private AddressI destination;
	private AddressI intermediate;
	private int numberOfhops;
	
	public RoutingInfo(AddressI destination, AddressI intermediate) {
		this.destination = destination;
		this.intermediate = intermediate;
		this.numberOfhops=1;
	}
	
	public RoutingInfo(AddressI destination, AddressI intermediate, int hops) {
		this.destination = destination;
		this.intermediate = intermediate;
		this.numberOfhops=hops;
	}
	
	@Override
	public AddressI getDestination() {
		return destination;
	}

	@Override
	public int getNumberOfHops() {
		return numberOfhops;
	}

	@Override
	public AddressI getIntermediate() {
		return intermediate;
	}
	
	@Override
	public void setIntermediate(AddressI a) {
		this.intermediate = a;
	}
	
	@Override
	public void setHops(int hops) {
		this.numberOfhops = hops;
	}
	
	public void setNumberOfHops(int numberOfhops) {
		this.numberOfhops=numberOfhops;
	}
	
	public RouteInfoI clone() {
		return new RoutingInfo(this.destination, this.intermediate, this.numberOfhops);
	}

}