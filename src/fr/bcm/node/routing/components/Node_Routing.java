package fr.bcm.node.routing.components;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;


@RequiredInterfaces(required = {Node_RoutingCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {RoutingCI.class, CommunicationCI.class})

public class Node_Routing extends AbstractComponent{
	

	protected Node_RoutingOutBoundPort nrop;
	protected Node_RoutingInboundPort nrip;
	protected Node_RoutingCommOutboundPort nrcop;
	private NodeAddress address = new NodeAddress();
	
	
	protected Node_Routing() throws Exception {
		super(1,0);
		this.nrop = new Node_RoutingOutBoundPort(this);
		this.nrip = new Node_RoutingInboundPort(this);
		this.nrcop = new Node_RoutingCommOutboundPort(this);
		this.nrop.publishPort();
		this.nrip.publishPort();
		this.nrcop.publishPort();
		
		this.doPortConnection(
				this.nrop.getPortURI(), 
				GestionnaireReseau.GS_URI, 
				NodeConnector.class.getCanonicalName());
		
		// Enables logs
		this.toggleLogging();
		this.toggleTracing();
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
			this.nrop.unpublishPort();
			this.nrip.unpublishPort();
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
		this.nrop.registerRoutingNode(address,nrop.getPortURI() , pointInitial, 25.00, nrip.getPortURI());
		this.logMessage("Logged");
		this.nrop.unregister(address);
		this.logMessage("Unregistered");
	}

	
	
}
