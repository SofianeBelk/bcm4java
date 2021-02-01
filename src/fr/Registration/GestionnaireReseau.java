package fr.Registration;

import java.util.ArrayList;
import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered= {RegistrationCI.class})
public class GestionnaireReseau extends AbstractComponent {
	
	public static final String GS_URI="gs-uri";
	protected RegistrationInboundPort rip;
    ArrayList<ConnectionInfo> maListe= new ArrayList<>();
    
	protected GestionnaireReseau() throws Exception {
		super(1, 0);
		this.rip =new RegistrationInboundPort(GS_URI,this);
		this.rip.publishPort();
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
	public Set<ConnectionInfo> catalogue(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange, boolean isRouting){
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange,isRouting);
		maListe.add(n);
	    ArrayList<ConnectionInfo> portee= new ArrayList<>();
	    for(ConnectionInfo ci : maListe) {
	    	if(ci instanceof Noeud) {
	    		if(((Noeud) ci).getinitialPosition().distance(n.getinitialPosition())<=n.getinitialRange()) {
	    			portee.add(ci);
	    		}
	    	}
	    }
		return (Set<ConnectionInfo>) portee;
		
	}
	
	@SuppressWarnings("unchecked")
	public Set<ConnectionInfo> catalogueAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange ){
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange,true);
		ArrayList<ConnectionInfo> copie=(ArrayList<ConnectionInfo>) maListe.clone();
		maListe.add(n);
		return (Set<ConnectionInfo>) copie;
	}



}
