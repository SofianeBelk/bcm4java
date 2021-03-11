package fr.bcm.node.terminal.components;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalInboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalOutBoundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.Address;
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

import java.util.List;
import java.util.Set;
import java.awt.Point;
import java.util.ArrayList;
import java.util.UUID;


@RequiredInterfaces(required = {CommunicationCI.class, Node_TerminalCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_Terminal extends AbstractComponent{
	
	protected Node_TerminalOutBoundPort ntop;
	protected Node_TerminalInboundPort ntip;
	
	protected List<Node_TerminalCommOutboundPort> node_CommOBP = new ArrayList<>();
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected= new ArrayList<>();
	
	private int NumberOfNeighboorsToSend = 2;
	
	protected Node_Terminal() throws Exception {
		super(1,0); 
		this.ntop = new Node_TerminalOutBoundPort(this);
		this.ntip = new Node_TerminalInboundPort(this);
		this.ntop.publishPort();
		this.ntip.publishPort();
		
		
		this.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enable logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node Terminal " + this.address.getAdress());
		
	}


	@Override
	public synchronized void finalise() throws Exception {
		if(this.ntop.connected()) {
			this.doPortDisconnection(ntop.getPortURI());
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			if(ntop.connected()) {
				this.ntop.doDisconnection();
			}
			if(ntip.connected()) {
				this.ntip.doDisconnection();
			}
			
			
			this.ntop.unpublishPort();
			this.ntip.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		Position pointInitial= new Position(10,10);
		Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, ntip.getPortURI(),pointInitial , 20.00);
		this.logMessage("Logged");
		
		// Current node connects to others nodes
		
		for(ConnectionInfoI CInfo: devices) {
			Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this);
			ntcop.publishPort();
			
			this.addressConnected.add(CInfo);
			try {
				this.doPortConnection(
						ntcop.getPortURI(),
						CInfo.getCommunicationInboundPortURI(),
						CommunicationConnector.class.getCanonicalName()
				);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			ntcop.connect(address, this.ntip.getPortURI());
			node_CommOBP.add(ntcop);
		}
		this.logMessage("Connected to all nearby devices");
		
	}

	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		
		ConnectionInformation CInfo = new ConnectionInformation(address);
		
		Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this);
		ntcop.publishPort();
		
		this.doPortConnection(
				ntcop.getPortURI(), 
				communicationInboundPortURI, 
				CommunicationConnector.class.getCanonicalName());
		this.logMessage("Added new devices to connections");
		
		node_CommOBP.add(ntcop);
		this.addressConnected.add(CInfo);
		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
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
					AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
					if(!m.isInHistory(addressToTransmitTo)) {
						this.logMessage("Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
						MessageI messageToSend = m.copy();
						this.node_CommOBP.get(i).transmitMessage(messageToSend);
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
