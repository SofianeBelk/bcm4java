package fr.bcm.node.components;

import fr.bcm.node.interfaces.Node_TerminalCI;
import fr.bcm.node.ports.Node_EphemeralOutBoundPort;
import fr.bcm.node.ports.Node_TerminalOutBoundPort;
import fr.bcm.utils.address.classes.Address;
import fr.bcm.utils.address.classes.NetworkAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NetworkAddressI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import java.util.UUID;


@RequiredInterfaces(required = {Node_TerminalCI.class})
public class Node_Ephemeral extends AbstractComponent{
	
	public static final String neop_uri = "neop_uri";
	protected Node_EphemeralOutBoundPort neop;
	
	private NetworkAddress address = new NetworkAddress();
	
	
	protected Node_Ephemeral() throws Exception {
		super(1,0);
		this.neop = new Node_EphemeralOutBoundPort(neop_uri, this);
		this.neop.publishPort();
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
		this.logMessage(String.valueOf(address.isNetworkAdress()));
	}

	
	
}
