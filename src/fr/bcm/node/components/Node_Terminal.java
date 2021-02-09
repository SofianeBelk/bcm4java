package fr.bcm.node.components;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.node.ports.Node_TerminalOutBoundPort;
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
import java.awt.Point;
import java.util.ArrayList;
import java.util.UUID;


@RequiredInterfaces(required = {Node_TerminalCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_Terminal extends AbstractComponent{
	
	public static final String ntop_uri = "ntop-uri";
	protected Node_TerminalOutBoundPort ntop;
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected= new ArrayList<>();
	
	
	protected Node_Terminal() throws Exception {
		super(1,0);
		this.ntop = new Node_TerminalOutBoundPort(ntop_uri, this);
		this.ntop.publishPort();
		
		// Enable logs
		this.toggleLogging();
		this.toggleTracing();
	}


	@Override
	public synchronized void finalise() throws Exception {
		if(this.ntop.connected()) {
			this.doPortDisconnection(ntop_uri);
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.ntop.unpublishPort();
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
		this.ntop.registerTerminalNode(address, ntop_uri,pointInitial , 20.00, true).size();
		this.logMessage("Logged");
		
		this.ntop.connect(address, ntop_uri);
	}

	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		ConnectionInformation CInfo = new ConnectionInformation(address, communicationInboundPortURI);
		this.addressConnected.add(CInfo);
	}

	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		ConnectionInformation CInfo = new ConnectionInformation(address, communicationInboundPortURI, routingInboundPortURI);
		this.addressConnected.add(CInfo);
	}

	public void transmitMessage(MessageI m) throws Exception {
	}

	public boolean hasRouteFor(AddressI address) throws Exception{
		if(this.address.equals(address)) {
			return true;
		}
		
		for(ConnectionInformation CInfo: this.addressConnected) {
			if(CInfo.getAddress().equals(address)) {
				return true;
			}
		}
		return false;
	}

	public void ping() throws Exception{
	}
	
}
