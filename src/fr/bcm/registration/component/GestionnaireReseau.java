package fr.bcm.registration.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.registration.port.RegistrationInboundPort;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.classes.Noeud;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered= {RegistrationCI.class})
public class GestionnaireReseau extends AbstractComponent{
	
	public static final String GS_URI="gs-uri";
	protected RegistrationInboundPort rip;
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
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange,isRouting);
		mySet.add(n);
	    Set<ConnectionInfoI> portee= new HashSet<ConnectionInfoI>();
	    for(ConnectionInfoI ci : mySet) {
	    	if(ci instanceof Noeud) {
	    		if(!ci.getAddress().equals(address)) {
	    			if(!((Noeud)ci).getisRouting()) {
	    				if(((Noeud) ci).getinitialPosition().distance(n.getinitialPosition())<=n.getinitialRange()) {
			    			portee.add(ci);
			    		}
	    			}
	    		}
	    	}
	    }
	    
	    this.logMessage("New node has registered");
	    return (Set<ConnectionInfoI>) portee;
	}

	@SuppressWarnings("unchecked")
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange,true);
		Set<ConnectionInfoI> copie = new HashSet<>();
		copie.addAll(mySet);
		mySet.add(n);
		this.logMessage("New node has registered");
		return (Set<ConnectionInfoI>) copie;		
	}


	public Object registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) {
		Noeud n = new Noeud(address, communicationInboundPortURI, initialPosition, initialRange, true, routingInboundPortURI);
		Set<ConnectionInfoI> copie = new HashSet<>();
		copie.addAll(mySet);
		mySet.add(n);
		this.logMessage("New node has registered");
		return (Set<ConnectionInfoI>) copie;		
	}

	public Object unregister(AddressI address) {
		Iterator<ConnectionInfoI> iter = this.mySet.iterator();
		while(iter.hasNext()) {
			ConnectionInfoI cinfo = (ConnectionInfoI)iter.next();
			if (cinfo.getAddress().equals(address)){
				this.mySet.remove(cinfo);
				this.logMessage("A node has unregistered");
				break;
			}
		}
		return null;
	}




}
