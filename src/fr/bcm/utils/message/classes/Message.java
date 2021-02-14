package fr.bcm.utils.message.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.message.interfaces.MessageI;

public class Message implements MessageI {
	
	private int hops = 10;
	private AddressI address;
	private String content = "";
	private List<AddressI> listAddress = new ArrayList<>();
	
	
	public Message(AddressI address, String content) {
		this.address = address;
		this.content = content;
	}
	
	public Message(AddressI address, String content, int hops) {
		this.address = address;
		this.content = content;
		this.hops = hops;
	}
	
	private Message(AddressI address, String content, int hops, List<AddressI> listAddress) {
		this.address = address;
		this.content = content;
		this.hops = hops;
		this.listAddress.addAll(listAddress);
	}

	@Override
	public AddressI getAddress() {
		return this.address;
	}

	@Override
	public Serializable getContent() {
		return this.content;
	}

	@Override
	public boolean stillAlive() {
		if (this.hops <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public void decrementHops() {
		this.hops -= 1;
	}
	
	public MessageI copy() {
		return new Message(this.address, this.content, this.hops, this.listAddress);
	}

	@Override
	public void addAddressToHistory(AddressI a) {
		this.listAddress.add(a);
	}


	@Override
	public boolean isInHistory(AddressI a) {
		for(AddressI address: this.listAddress) {
			if (address.equals(a)){
				return true;
			}
		}
		return false;
	}
}
