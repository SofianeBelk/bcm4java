package bcm.utils.message.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import bcm.utils.address.interfaces.AddressI;
import bcm.utils.message.interfaces.MessageI;

public class Message implements MessageI, Serializable {
	
	public static int MessageSent = 0;
	public static int MessageLost = 0;
	public static int MessageReceived = 0;
	public static int MessageDuplicated = 0;
	public static int TransmittedviaInnondation = 0;
	public static int TransmittedviaRoutingTable = 0;
	
	public static ReentrantLock lock = new ReentrantLock();
	
	private int hops = 3;
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
			Message.lock.lock();
			Message.MessageLost += 1;
			Message.lock.unlock();
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
	
	public static void newMessageSent() {
		Message.lock.lock();
		Message.MessageSent += 1;
		Message.lock.unlock();
	}
	
	public static void newMessageReceived() {
		Message.lock.lock();
		Message.MessageReceived += 1;
		Message.lock.unlock();
	}
	
	public static void newMessageDuplicated() {
		Message.lock.lock();
		Message.MessageDuplicated += 1;
		Message.lock.unlock();
	}
	
	public static void newMessageViaInnondation() {
		Message.lock.lock();
		Message.TransmittedviaInnondation += 1;
		Message.lock.unlock();
	}
	
	public static void newMessageViaTableRouting() {
		Message.lock.lock();
		Message.TransmittedviaRoutingTable += 1;
		Message.lock.unlock();
	}
}
