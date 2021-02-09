package fr.bcm.node.components;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.interfaces.Node_AccessPointCI;
import fr.bcm.node.ports.Node_AccessPointOutBoundPort;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;


@RequiredInterfaces(required = {Node_AccessPointCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_AccessPoint extends AbstractComponent{
	
	public static final String neop_uri = "neop_uri";
	protected Node_AccessPointOutBoundPort neop;
	
	private NodeAddress address = new NodeAddress();
	
	
	protected Node_AccessPoint() throws Exception {
		super(1,0);
		this.neop = new Node_AccessPointOutBoundPort(neop_uri, this);
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
