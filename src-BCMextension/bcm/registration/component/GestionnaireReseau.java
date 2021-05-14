package bcm.registration.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.registration.interfaces.RegistrationCI;
import bcm.registration.port.RegistrationInboundPort;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.classes.Noeud;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered= {RegistrationCI.class})
public class GestionnaireReseau extends AbstractComponent{
	
	public static final String GS_URI="gs-uri";
	protected RegistrationInboundPort rip;
	protected ReentrantReadWriteLock lockforArray = new ReentrantReadWriteLock();
    Set<ConnectionInfoI> mySet= new HashSet<>();
    
	protected GestionnaireReseau() throws Exception {
		super(1, 0);
		this.rip =new RegistrationInboundPort(GS_URI,this);
		this.rip.publishPort();
		this.toggleLogging();
		this.toggleTracing();
	}
	

	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException{
		try {
			this.rip.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}
	

	@SuppressWarnings("unchecked")
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange);
		
		lockforArray.readLock().lock();;
	    Set<ConnectionInfoI> portee= new HashSet<ConnectionInfoI>();
	    for(ConnectionInfoI ci : mySet) {
	    	if(ci instanceof Noeud) {
	    		if(!ci.getAddress().equals(address)) {
    				if(((Noeud) ci).getinitialPosition().distance(n.getinitialPosition())<=n.getinitialRange()) {
		    			portee.add(ci);
		    		}
	    		}
	    	}
	    }
	    lockforArray.readLock().unlock();;
		
	    lockforArray.writeLock().lock();;
	    mySet.add(n);
	    lockforArray.writeLock().unlock();
	    
	    this.logMessage("New node has registered");
	    return (Set<ConnectionInfoI>) portee;
	}

	@SuppressWarnings("unchecked")
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		Noeud n =new Noeud(
				address,
				communicationInboundPortURI,
				initialPosition,
				initialRange,
				true,
				routingInboundPortURI,
				true
		);
		lockforArray.readLock().lock();
		Set<ConnectionInfoI> portee= new HashSet<ConnectionInfoI>();
		for(ConnectionInfoI ci : mySet) {
	    	if(ci instanceof Noeud) {
	    		if(!ci.getAddress().equals(address)) {
	    			// Connects accessPoint
	    			if(((Noeud)ci).getisAccessPoint()){
	    				portee.add(ci);
	    			}
	    			else {
	    				if(((Noeud) ci).getinitialPosition().distance(n.getinitialPosition())<=n.getinitialRange()) {
			    			portee.add(ci);
			    		}
	    			}
	    		}
	    	}
	    }
		lockforArray.readLock().unlock();
		
		lockforArray.writeLock().lock();
		mySet.add(n);
		lockforArray.writeLock().unlock();
		this.logMessage("New node has registered");
		return (Set<ConnectionInfoI>) portee;		
	}


	public Object registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) {
		Noeud n = new Noeud(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange, 
				true, 
				routingInboundPortURI
		);
		lockforArray.readLock().lock();
		Set<ConnectionInfoI> portee= new HashSet<ConnectionInfoI>();
		for(ConnectionInfoI ci : mySet) {
	    	if(ci instanceof Noeud) {
	    		if(!ci.getAddress().equals(address)) {	    			
    				if(((Noeud) ci).getinitialPosition().distance(n.getinitialPosition())<=n.getinitialRange()) {
		    			portee.add(ci);
		    		}
	    		}
	    	}
	    }
		lockforArray.readLock().unlock();
		
		lockforArray.writeLock().lock();
		mySet.add(n);
		lockforArray.writeLock().unlock();
		this.logMessage("New node has registered");
		return (Set<ConnectionInfoI>) portee;		
	}
	
	public ConnectionInfoI getRandomConn() {
		Random rand = new Random();
		int index;
		
		lockforArray.readLock().lock();
		if(mySet.size() == 1) {
			index = 0;
		}
		else {
			index = rand.nextInt(mySet.size());
		}
		
		Iterator<ConnectionInfoI> iter = this.mySet.iterator();
		int cpt = 0;
		while(iter.hasNext()) {
			if(cpt == index) {
				lockforArray.readLock().unlock();
				return iter.next();
			}
			else {
				iter.next();
				cpt ++;
			}
		}
		lockforArray.readLock().unlock();
		return null;
		
	}

	public void unregister(AddressI address) {
		lockforArray.writeLock().lock();
		Iterator<ConnectionInfoI> iter = this.mySet.iterator();
		while(iter.hasNext()) {
			ConnectionInfoI cinfo = (ConnectionInfoI)iter.next();
			if (cinfo.getAddress().equals(address)){
				this.mySet.remove(cinfo);
				this.logMessage("A node has unregistered");
				break;
			}
		}
		lockforArray.writeLock().unlock();
	}




}
