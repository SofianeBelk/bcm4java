package fr.bcm.node.components;

import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.node.ports.Node_TerminalOutBoundPort;
import fr.bcm.utils.address.classes.Address;
import fr.bcm.utils.address.classes.NetworkAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

import java.awt.Point;
import java.util.UUID;


@RequiredInterfaces(required = {Node_TerminalCI.class})
public class Node_Terminal extends AbstractComponent{
	
	public static final String ntop_uri = "ntop-uri";
	protected Node_TerminalOutBoundPort ntop;
	
	private NetworkAddress address = new NetworkAddress();
	
	
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
		this.ntop.registre(address, ntop_uri,pointInitial , 20.00, true).size();
		this.logMessage("Logged");
	}

	
	
}
