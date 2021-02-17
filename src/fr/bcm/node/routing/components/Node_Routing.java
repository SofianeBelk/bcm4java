package fr.bcm.node.routing.components;

import java.util.ArrayList;
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
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
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
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;


@RequiredInterfaces(required = {Node_RoutingCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {RoutingCI.class, CommunicationCI.class})

public class Node_Routing extends AbstractComponent{
	

	public static int node_routing_id = 1;
	
	private int id;
	protected Node_RoutingOutBoundPort nrop;
	protected Node_RoutingCommInboundPort nrip;
	protected List<Node_RoutingCommOutboundPort> node_CommOBP = new ArrayList<>();
	protected String routingInboundPortURI = "";
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected = new ArrayList<>();
	
	private AddressI addressToSendMessage;
	private int NumberOfNeighboorsToSend = 2;
	
	
	protected Node_Routing() throws Exception {
		super(1,0);
		this.id = node_routing_id;
		node_routing_id += 1;
		this.nrop = new Node_RoutingOutBoundPort(this);
		this.nrip = new Node_RoutingCommInboundPort(this);
		this.nrop.publishPort();
		this.nrip.publishPort();
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
			if(nrip.connected()) {
				this.nrip.doDisconnection();
			}
			for (Node_RoutingCommOutboundPort port : this.node_CommOBP) {
				port.doDisconnection();
				port.unpublishPort();
			}
			
			
			
			
			this.nrop.unpublishPort();
			this.nrip.unpublishPort();
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
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrip.getPortURI() , pointInitial, 25.00, routingInboundPortURI);
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
			
			this.addressConnected.add(CInfo);
			
			this.logMessage(String.valueOf(devices.size()));
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
			nrcop.connect(address, this.nrip.getPortURI());
			this.logMessage("Ask for connection");
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
		ConnectionInformation CInfo = new ConnectionInformation(address, communicationInboundPortURI, routingInboundPortURI);
		this.addressConnected.add(CInfo);
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		
		m.decrementHops();
		m.addAddressToHistory(this.address);

		
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
		/*
		if(this.hasRouteFor(m.getAddress())) {
			
			boolean trouverNode=false;
			boolean trouverNetwork=false;
			
			if(this.address==m.getAddress()) {
				System.out.println("Message bien recu : "+m.getContent().toString());
				return null;
			}
			if(m.getAddress().isNetworkAdress()) {
				NetworkAddressI a = (NetworkAddressI) m.getAddress();
				for(Node_RoutingCommOutboundPort c : node_CommOBP) {
					if(c.hasRouteFor(a)) {
						// il faudrait faire appell a la fonction updateRouting
						c.transmitMessage(m);
						trouverNetwork=true;
						break;
					}
				}
			}
			
			if(m.getAddress().isNodeAdress()) {
				NodeAddressI k =(NodeAddressI)m.getAddress();
				for(Node_RoutingCommOutboundPort c : node_CommOBP) {
					if(c.hasRouteFor(k) ){
						// il faudrait faire appell a la fonction updateAccessPoint
						c.transmitMessage(m);
						trouverNode=true;
						break;
					}
				}
		
			}
			
			if(!(trouverNode||trouverNetwork)) {
				int alea =(int) (1+Math.random() * (node_CommOBP.size() - 1));
	            for(int i=0;i<alea;i++) {
	                if(m.stillAlive()) {
	                    m.decrementHops();
	                    node_CommOBP.get(i).transmitMessage(m);
	                }else {
	                    //Destruction
	                    m = null;
	                    System.out.println("Message Detruit");
	                }
	            }
			}
		}
		*/
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
