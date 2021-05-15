package bcm.utils.message.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import bcm.utils.address.interfaces.AddressI;
import bcm.utils.message.interfaces.MessageI;

public class Message implements MessageI, Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Variables pour tracer les messages et garder des statistiques dessus
	public static int MessageSent = 0;
	public static int MessageLost = 0;
	public static int MessageReceived = 0;
	public static int MessageDuplicated = 0;
	public static int TransmittedviaInnondation = 0;
	public static int TransmittedviaRoutingTable = 0;
	
	public static ReentrantLock lock = new ReentrantLock();
	
	// Repr�sente le nombre de bonds que le message peut faire
	private int hops = 3;
	private AddressI address;
	private String content = "";
	
	
	public Message(AddressI address, String content) {
		this.address = address;
		this.content = content;
	}
	
	public Message(AddressI address, String content, int hops) {
		this.address = address;
		this.content = content;
		this.hops = hops;
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
		return new Message(this.address, this.content, this.hops);
	}

	// Permet de tracer les messages et d'avoir des statistiques dessus.
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
