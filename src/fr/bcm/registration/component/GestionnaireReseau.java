package fr.bcm.registration.component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.registration.port.RegistrationInboundPort;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.classes.Noeud;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

/**
 * classe GestionnaireReseau qui représente le gestionnaire du réseau 
 * @author Nguyen, Belkhir
 **/

@OfferedInterfaces(offered= {RegistrationCI.class})
public class GestionnaireReseau extends AbstractComponent{
	
	/**l'URI du composant**/
	public static final String GS_URI="gs-uri";
	
	/**le port entrant du composant**/
	protected RegistrationInboundPort rip;
	
	/**la table du gestionnaire du réseau**/
    Set<ConnectionInfoI> mySet= new HashSet<>();
    
    /**
	 * Constructeur qui crée une instance du gestionnaire du réseau
	 * @throws Exception
	 */
	protected GestionnaireReseau() throws Exception {
		super(1, 0);
		this.rip =new RegistrationInboundPort(GS_URI,this);
		this.rip.publishPort();
		this.toggleLogging();
		this.toggleTracing();
	}
	

	/**--------------------------------------------------
	 *--------------  Component life-cycle -------------
	  --------------------------------------------------**/
	
	
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
	

	/** ------------------------- Services ------------------------**/
	
	/**
	 * 
	 * @param address : l'adresse du noeud terminal
	 * @param communicationInboundPortURI:  l’URI du port entrant de communication
	 * @param initialPosition : la position du noeud
	 * @param initialRange : la portée 
	 * @return : une nouvelle table
	 * @throws Exception
	 */
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		
		Noeud n =new Noeud(address,communicationInboundPortURI,initialPosition,initialRange);
		mySet.add(n);
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
	    
	    this.logMessage("New node has registered");
	    return (Set<ConnectionInfoI>) portee;
	}

	/**
	 * 
	 * @param address : l'adresse du noeud Access Point
	 * @param communicationInboundPortURI : l’URI du port entrant de communication
	 * @param initialPosition : la position du noeud
	 * @param initialRange : la portée
	 * @param routingInboundPortURI : l’URI du port entrant du routage
	 * @return : une nouvelle table
	 * @throws Exception
	 */
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
		mySet.add(n);
		this.logMessage("New node has registered");
		return (Set<ConnectionInfoI>) portee;		
	}


	/**
	 * 
	 * @param address : l'adresse du noeud Access Point
	 * @param communicationInboundPortURI : l’URI du port entrant de communication
	 * @param initialPosition : la position du noeud
	 * @param initialRange : la portée
	 * @param routingInboundPortURI : l’URI du port entrant du routage
	 * @return : une nouvelle table
	 */
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
		
		mySet.add(n);
		this.logMessage("New node has registered");
		return (Set<ConnectionInfoI>) portee;		
	}

	/**
	 * 
	 * @param address : l'adresse du noeud qui va etre dérengistrer
	 * @return null
	 */
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
