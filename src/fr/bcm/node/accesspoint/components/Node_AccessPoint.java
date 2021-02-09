package fr.bcm.node.accesspoint.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointInboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;


@RequiredInterfaces(required = {Node_AccessPointCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_AccessPoint extends AbstractComponent{
	

	protected Node_AccessPointOutboundPort napop;
	protected Node_AccessPointInboundPort napip;
	protected Node_AccessPointCommOutboundPort napcop;
	
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected = new ArrayList<>();
	
	
	protected Node_AccessPoint() throws Exception {
		super(1,0);
		this.napop = new Node_AccessPointOutboundPort(this);
		this.napip = new Node_AccessPointInboundPort(this);
		this.napcop = new Node_AccessPointCommOutboundPort(this);
		this.napop.publishPort();
		this.napip.publishPort();
		this.napcop.publishPort();
		this.doPortConnection(
				this.napop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node_AccessPoint");
	}


	@Override
	public synchronized void finalise() throws Exception {
		if(this.napop.connected()) {
			this.doPortDisconnection(napop.getPortURI());
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			if(napop.connected()) {
				this.napop.doDisconnection();
			}
			if(napip.connected()) {
				this.napip.doDisconnection();
			}
			
			if(napcop.connected()) {
				this.napcop.doDisconnection();
			}
			
			
			this.napop.unpublishPort();
			this.napip.unpublishPort();
			this.napcop.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		PositionI pointInitial = new Position(10,5);
		Set<ConnectionInfoI> devices = this.napop.registerAccessPoint(address,napip.getPortURI() , pointInitial, 25.00);
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
			this.addressConnected.add(CInfo);
			

			System.out.println(this.napcop.connected());
			this.doPortConnection(
					this.napcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			this.napcop.connect(address, this.napip.getPortURI());
		}
		System.out.println("Size devices " + devices.size());
		this.logMessage("Connected to all nearby devices");
				
	}
	
	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		ConnectionInfoI CInfo = new ConnectionInformation(address, communicationInboundPortURI);
		this.addressConnected.add(CInfo);
		System.out.println("Trying job");
		System.out.println(this.napcop.connected());
		try {
			this.doPortConnection(
					this.napcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Added new devices to connections");
		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		ConnectionInformation CInfo = new ConnectionInformation(address, communicationInboundPortURI, routingInboundPortURI);
		this.addressConnected.add(CInfo);
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		return null;
	}

	public boolean hasRouteFor(AddressI address) throws Exception{
		if(this.address.equals(address)) {
			return true;
		}
		
		for(ConnectionInfoI CInfo: this.addressConnected) {
			if(CInfo.getAddress().equals(address)) {
				return true;
			}
		}
		return false;
	}

	public Object ping() throws Exception{
		return null;
	}
	
}
