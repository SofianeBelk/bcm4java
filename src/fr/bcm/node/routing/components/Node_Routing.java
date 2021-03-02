package fr.bcm.node.routing.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.connectors.RoutingConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingOutboundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NetworkAddress;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.classes.Message;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.bcm.utils.routing.classes.RoutingInfo;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;


@RequiredInterfaces(required = {Node_RoutingCI.class, CommunicationCI.class, RoutingCI.class})
@OfferedInterfaces (offered = {RoutingCI.class, CommunicationCI.class})

public class Node_Routing extends AbstractComponent{
	

	public static int node_routing_id = 1;
	
	private int id;
	protected Node_RoutingOutBoundPort nrop;
	
	protected Node_RoutingCommInboundPort nrcip;
	protected List<Node_RoutingCommOutboundPort> node_CommOBP = new ArrayList<>();
	
	protected Node_RoutingRoutingInboundPort nrrip;
	protected List<Node_RoutingRoutingOutboundPort> node_RoutingOBP = new ArrayList<>();
	
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected = new ArrayList<>();
	private Set<RouteInfoI> routes = new HashSet<RouteInfoI>();
	
	private AddressI addressToSendMessage;
	private int NumberOfNeighboorsToSend = 2;
	
	
	protected Node_Routing() throws Exception {
		super(1,0);
		this.id = node_routing_id;
		node_routing_id += 1;
		this.nrop = new Node_RoutingOutBoundPort(this);
		this.nrcip = new Node_RoutingCommInboundPort(this);
		this.nrrip = new Node_RoutingRoutingInboundPort(this);
		this.nrop.publishPort();
		this.nrcip.publishPort();
		this.nrrip.publishPort();
		this.doPortConnection(
				this.nrop.getPortURI(), 
				GestionnaireReseau.GS_URI, 
				NodeConnector.class.getCanonicalName());
		
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node_Routing " + this.address.getAdress() + " " + this.id);
	}


	@Override
	public synchronized void finalise() throws Exception {
		if(this.nrop.connected()) {
			this.doPortDisconnection(nrop.getPortURI());
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			if(nrop.connected()) {
				this.nrop.doDisconnection();
			}
			if(nrcip.connected()) {
				this.nrcip.doDisconnection();
			}
			if(nrrip.connected()) {
				this.nrrip.doDisconnection();
			}
			for (Node_RoutingCommOutboundPort port : this.node_CommOBP) {
				port.doDisconnection();
				port.unpublishPort();
			}
			for (Node_RoutingRoutingOutboundPort port : this.node_RoutingOBP) {
				port.doDisconnection();
				port.unpublishPort();
			}
			
			this.nrop.unpublishPort();
			this.nrcip.unpublishPort();
			this.nrrip.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		Thread.yield();
		Thread.sleep(1000);
		this.logMessage("Tries to log in the manager");
		PositionI pointInitial = new Position(10,5);
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrcip.getPortURI() , pointInitial, 25.00, nrrip.getPortURI());
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
			
			this.addressConnected.add(CInfo);
			Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
			this.logMessage("Creating routing Port");
			nrcop.publishPort();
			this.logMessage("Publish Port");
			this.doPortConnection(
					nrcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			this.logMessage("Do Port connection");
			this.logMessage(CInfo.getAddress().getAdress());
			
			
			this.logMessage("Ask for connection");
			
			// Routing connection
			if(!CInfo.getRoutingInboundPortURI().isEmpty()) {
				Node_RoutingRoutingOutboundPort nrrop = new Node_RoutingRoutingOutboundPort(this);
				nrrop.publishPort();
				try {
					this.doPortConnection(
							nrrop.getPortURI(), 
							CInfo.getRoutingInboundPortURI(),
							RoutingConnector.class.getCanonicalName());
				}catch(Exception e) {
					e.printStackTrace();
				}
				nrcop.connectRouting(address, this.nrcip.getPortURI(), this.nrrip.getPortURI());
				node_RoutingOBP.add(nrrop);
				this.logMessage("Successful connection to communication + routing port");
			}
			
			// Normal connection
			else {
				nrcop.connect(address, this.nrcip.getPortURI());
				this.logMessage("Successful connection to communication");
			}
			
			node_CommOBP.add(nrcop);
			
		}

		this.logMessage("Connected to all nearby devices");
		
		Thread.yield();
		Thread.sleep(3000);
		if(this.id == 1) {
			Thread.yield();
			if(this.addressConnected.size()>0) {
				this.addressToSendMessage = this.addressConnected.get(0).getAddress();
			}
			Message m = new Message(this.addressToSendMessage,"Hello from : " + this.address.getAdress());
			this.logMessage("Sending message to " + this.addressToSendMessage.getAdress());
			this.transmitMessage(m);
		}
		
		
		if(this.id == 2) {
			Thread.yield();
			if(this.addressConnected.size()>0) {
				NetworkAddress toSend = new NetworkAddress();
				Message m = new Message(toSend,"Hello to Network from : " + toSend.getAdress());						
				this.logMessage("Sending message to network " + toSend.getAdress());
				this.transmitMessage(m);
			}
		}
		
		// Init routes
		for(ConnectionInfoI a : addressConnected) {
			routes.add(new RoutingInfo(a.getAddress(),address));
		}
	}

	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.logMessage("Someone asked connection");
		ConnectionInfoI CInfo = new ConnectionInformation(address, communicationInboundPortURI);
		this.addressConnected.add(CInfo);
		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
		nrcop.publishPort();

		try {
			this.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		node_CommOBP.add(nrcop);
		this.logMessage("Added new devices to connections");
		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		this.logMessage("Someone asked connection");
		ConnectionInfoI CInfo = new ConnectionInformation(address, communicationInboundPortURI);
		this.addressConnected.add(CInfo);

		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
		Node_RoutingRoutingOutboundPort nrrop = new Node_RoutingRoutingOutboundPort(this);
		nrcop.publishPort();
		nrrop.publishPort();
		
		// Connection for comm + routing
		this.logMessage("Trying connexion to communication + router port");
		try {
			this.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
			this.doPortConnection(
					nrrop.getPortURI(), 
					routingInboundPortURI, 
					RoutingConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.logMessage("Successful connection to communication + routing port");
		node_CommOBP.add(nrcop);
		node_RoutingOBP.add(nrrop);
		this.logMessage("Added new devices to connections");
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		m.decrementHops();
		m.addAddressToHistory(this.address);
		// Message par innondation
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
	
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception{
		//UNE premiére version
		boolean add;// pour savoir si on l'a déja ajouter ou pas
		for(RouteInfoI riExt : routes) {
			
			add = true;
			for(RouteInfoI riInt : this.routes) {	
				if(riInt.getDestination()==riExt.getDestination()) {
					if(riInt.getNumberOfHops() > riExt.getNumberOfHops()+1 ) {
						//route interessante on modifier notre table
						riInt.setHops(riExt.getNumberOfHops() + 1);
						riInt.setIntermediate(neighbour);
					}
					add = false;
				}
			}
			
			if(add) {
				RouteInfoI riAdd = riExt.clone();
				riAdd.setHops(riAdd.getNumberOfHops() + 1);
				this.routes.add(riAdd);
			}
		}
	}
	
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		// TO-DO !!!
	}
	
}
