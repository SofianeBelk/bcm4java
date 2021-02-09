package fr.bcm.node.components;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.node.ports.Node_TerminalOutBoundPort;
import fr.bcm.node.ports.Node_TerminalInboundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.Address;
import fr.bcm.utils.address.classes.NetworkAddress;
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
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected= new ArrayList<>();
	
	
	protected Node_Terminal() throws Exception {
		super(1,0); 
		this.ntop = new Node_TerminalOutBoundPort(this);
		this.ntip = new Node_TerminalInboundPort(this);
		this.ntop.publishPort();
		this.ntip.publishPort();
		
		System.out.println(this.ntop.getImplementedInterface().getCanonicalName());
		
		this.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enable logs
		this.toggleLogging();
		this.toggleTracing();
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
			this.ntop.unpublishPort();
			this.ntip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException();
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		Position pointInitial= new Position(10,10);
		Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, ntip.getPortURI(),pointInitial , 20.00, true);
		this.logMessage("Logged");
		
		// Current node connects to others nodes
		
		for(ConnectionInfoI CInfo: devices) {
			
			this.addressConnected.add(CInfo);
			System.out.println("TEST1");
			try {
				System.out.println(this.ntop.getPortURI());
				System.out.println(CInfo.getCommunicationInboundPortURI());
				this.doPortConnection(
						this.ntop.getPortURI(),
						CInfo.getCommunicationInboundPortURI(),
						CommunicationCI.class.getCanonicalName()
				);
			} catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("TEST2");
			this.ntop.connect(address, this.ntip.getPortURI());
			System.out.println("TEST3");
		}
		this.logMessage("Connected to all nearby devices");
		
		
	}

	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		System.out.println("TEST4");
		ConnectionInfoI CInfo = new ConnectionInformation(address, communicationInboundPortURI);
		this.addressConnected.add(CInfo);
		this.doPortConnection(
				this.ntop.getPortURI(), 
				communicationInboundPortURI, 
				CommunicationCI.class.getCanonicalName());
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
