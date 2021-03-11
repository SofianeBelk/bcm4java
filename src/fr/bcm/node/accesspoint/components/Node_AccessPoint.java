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
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommInboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;


@RequiredInterfaces(required = {Node_AccessPointCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class, RoutingCI.class})

public class Node_AccessPoint extends AbstractComponent{
	

	protected Node_AccessPointOutboundPort napop;
	protected Node_AccessPointCommInboundPort napip;
	protected List<Node_AccessPointCommOutboundPort> node_CommOBP = new ArrayList<>();
	protected String routingInboundPortURI = "";
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected = new ArrayList<>();
	
	private int NumberOfNeighboorsToSend = 2;
	
	protected Node_AccessPoint() throws Exception {
		super(1,0);
		this.napop = new Node_AccessPointOutboundPort(this);
		this.napip = new Node_AccessPointCommInboundPort(this);

		this.napop.publishPort();
		this.napip.publishPort();
		this.doPortConnection(
				this.napop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node_AccessPoint " + this.address.getAdress());
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
			
			
			
			
			this.napop.unpublishPort();
			this.napip.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		PositionI pointInitial = new Position(0,0);
		Set<ConnectionInfoI> devices = this.napop.registerAccessPoint(address,napip.getPortURI() , pointInitial, 25.00, routingInboundPortURI);
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			
			
			Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this);
			napcop.publishPort();
			this.doPortConnection(
					napcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			napcop.connect(address, this.napip.getPortURI());
			node_CommOBP.add(napcop);
			
			ciToAdd.setCommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
			ciToAdd.setNapcop(napcop);
			this.addressConnected.add(ciToAdd);
		}

		this.logMessage("Connected to all nearby devices");
				
	}
	
	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setcommunicationInboundPortURI(communicationInboundPortURI);
		
		
		Node_AccessPointCommOutboundPort napcop = new Node_AccessPointCommOutboundPort(this);
		napcop.publishPort();

		try {
			this.doPortConnection(
					napcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		node_CommOBP.add(napcop);
		CInfo.setNapcop(napcop);
		this.addressConnected.add(CInfo);
		this.logMessage("Added new devices to connections");
		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		

		m.decrementHops();
		m.addAddressToHistory(this.address);
		
		if(m.getAddress().isNetworkAdress()) {
			this.logMessage("Sent " + m.getContent() + " to network");
			return null;
		}
		
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
		}
		else {
			
			if(m.stillAlive()) {
				int numberSent = 0;
				for(int i = 0; numberSent < NumberOfNeighboorsToSend && i < this.addressConnected.size(); i++) {		
					// No blocking mechanism
					AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
					if(!m.isInHistory(addressToTransmitTo)) {
						this.logMessage("Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
						this.node_CommOBP.get(i).transmitMessage(m);
						numberSent += 1;
					}
					else {
						this.logMessage("No node to send message to");
					}
				}
			}
			else {
				this.logMessage("Message dead");
			}
		}
		return null;
	}

	public boolean hasRouteFor(AddressI address) throws Exception{
		return false;
	}

	public Object ping() throws Exception{
		return null;
	}
	
}
