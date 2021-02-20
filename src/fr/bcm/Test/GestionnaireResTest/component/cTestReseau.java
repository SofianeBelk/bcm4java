package fr.bcm.Test.GestionnaireResTest.component;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import fr.bcm.Test.GestionnaireResTest.interfaces.RegistrationCITest;
import fr.bcm.Test.GestionnaireResTest.ports.cTestReseauOutBoundPort;
import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalInboundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;

@RequiredInterfaces(required= {RegistrationCITest.class})
public class cTestReseau extends AbstractComponent{
	
	protected cTestReseauOutBoundPort ntop;
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected= new ArrayList<>();
	
	protected cTestReseau() throws Exception {
		super(1,0);
		this.ntop = new cTestReseauOutBoundPort(this);
		ntop.publishPort();
		this.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enable logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node Test " + this.address.getAdress());
		
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
			this.ntop.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public void execute() throws Exception {
		super.execute();
		this.logMessage("start of the test");
		Position pointInitial= new Position(10,10);
		/*probléme a resoudre*/
		//Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, Node_TerminalInboundPort.generatePortURI(), pointInitial, 10.00);
		//assertTrue(devices.isEmpty());
		this.logMessage("End of the test");
				
	}
}
