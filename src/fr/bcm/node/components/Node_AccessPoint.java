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
	
	public static final String nacop_uri = "nacop_uri";
	protected Node_AccessPointOutBoundPort nac;
	
	private NodeAddress address = new NodeAddress();
	
	
	protected Node_AccessPoint() throws Exception {
		super(1,0);
		this.nac = new Node_AccessPointOutBoundPort(nacop_uri, this);
		this.nac.publishPort();
		
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
	}


	@Override
	public synchronized void finalise() throws Exception {
		if(this.nac.connected()) {
			this.doPortDisconnection(nacop_uri);
		}
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.nac.unpublishPort();
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
		this.nac.registerAccessPoint(address,nacop_uri , pointInitial, 25.00);
		this.logMessage("Logged");
		this.nac.unregister(address);
		this.logMessage("Unregistered");
	}

	
	
}
