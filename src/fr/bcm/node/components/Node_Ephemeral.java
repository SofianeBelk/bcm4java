package fr.bcm.node.components;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.interfaces.Node_EphemeralCI;
import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.node.ports.Node_EphemeralOutBoundPort;
import fr.bcm.node.ports.Node_TerminalOutBoundPort;
import fr.bcm.utils.address.classes.Address;
import fr.bcm.utils.address.classes.NetworkAddress;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

import java.awt.Point;
import java.util.UUID;


@RequiredInterfaces(required = {Node_EphemeralCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_Ephemeral extends AbstractComponent{
	
	public static final String neop_uri = "neop_uri";
	protected Node_EphemeralOutBoundPort neop;
	
	private NodeAddress address = new NodeAddress();
	
	
	protected Node_Ephemeral() throws Exception {
		super(1,0);
		this.neop = new Node_EphemeralOutBoundPort(neop_uri, this);
		this.neop.publishPort();
		
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
	}


	@Override
	public synchronized void finalise() throws Exception {
		if(this.neop.connected()) {
			this.doPortDisconnection(neop_uri);
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.neop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException();
		}
		super.shutdown();
	}


	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		PositionI pointInitial = new Position(10,5);
		this.neop.registerAccessPoint(address,neop_uri , pointInitial, 25.00);
		this.logMessage("Logged");
		this.neop.unregister(address);
		this.logMessage("Unregistered");
	}

	
	
}
