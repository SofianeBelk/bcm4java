package fr.bcm.registration.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.registration.port.RegistrationInboundPort;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.ConnectionInfo;
import fr.bcm.utils.nodeInfo.classes.Noeud;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered= {RegistrationCI.class})
public class GestionnaireReseau extends AbstractComponent{
	
	public static final String GS_URI="gs-uri";
	protected RegistrationInboundPort rip;
    Set<ConnectionInfo> mySet= new HashSet<>();
    
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
	public Set<ConnectionInfo> register(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, boolean isRouting) throws Exception {
		
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange,isRouting);
		mySet.add(n);
	    Set<ConnectionInfo> portee= new HashSet<ConnectionInfo>();
	    for(ConnectionInfo ci : mySet) {
	    	if(ci instanceof Noeud) {
	    		if(((Noeud) ci).getinitialPosition().distance(n.getinitialPosition())<=n.getinitialRange()) {
	    			portee.add(ci);
	    		}
	    	}
	    }
	    
	    this.logMessage("New node has registered");
	    return (Set<ConnectionInfo>) portee;
	}

	@SuppressWarnings("unchecked")
	public Set<ConnectionInfo> registerAccessPoint(AddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange,true);
		Set<ConnectionInfo> copie = new HashSet<>();
		copie.addAll(mySet);
		mySet.add(n);
		return (Set<ConnectionInfo>) copie;
	}



}
