package fr.bcm.node.routing.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import fr.bcm.node.accesspoint.ports.Node_AccessPointOutboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommInboundPort;
import fr.bcm.node.routing.ports.Node_RoutingOutBoundPort;
import fr.bcm.registration.component.GestionnaireReseau;
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


@RequiredInterfaces(required = {Node_RoutingCI.class, CommunicationCI.class})
@OfferedInterfaces (offered = {RoutingCI.class, CommunicationCI.class})

public class Node_Routing extends AbstractComponent{
	

	protected Node_RoutingOutBoundPort nrop;
	protected Node_RoutingCommInboundPort nrip;
	protected List<Node_RoutingCommOutboundPort> node_CommOBP = new ArrayList<>();
	protected String routingInboundPortURI = "";
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInfoI> addressConnected = new ArrayList<>();
	
	
	protected Node_Routing() throws Exception {
		super(1,0);
		this.nrop = new Node_RoutingOutBoundPort(this);
		this.nrip = new Node_RoutingCommInboundPort(this);
		this.nrop.publishPort();
		this.nrip.publishPort();
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
		Set<ConnectionInfoI> devices = this.nrop.registerRoutingNode(address,nrop.getPortURI() , pointInitial, 25.00, nrip.getPortURI());
		this.logMessage("Logged");
		// Current node connects to others nodes
		for(ConnectionInfoI CInfo: devices) {
			
			this.addressConnected.add(CInfo);
			Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
			nrcop.publishPort();
			this.doPortConnection(
					nrcop.getPortURI(),
					CInfo.getCommunicationInboundPortURI(),
					CommunicationConnector.class.getCanonicalName()
			);
			nrcop.connect(address, this.nrip.getPortURI());
			node_CommOBP.add(nrcop);
		}

		this.logMessage("Connected to all nearby devices");
	}

	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		ConnectionInfoI CInfo = new ConnectionInformation(address, communicationInboundPortURI);
		this.addressConnected.add(CInfo);
		Node_RoutingCommOutboundPort nrcop = new Node_RoutingCommOutboundPort(this);
		nrcop.publishPort();

		try {
			this.doPortConnection(
					nrcop.getPortURI(), 
					communicationInboundPortURI, 
					CommunicationConnector.class.getCanonicalName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		node_CommOBP.add(nrcop);
		this.logMessage("Added new devices to connections");
		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		ConnectionInformation CInfo = new ConnectionInformation(address, communicationInboundPortURI, routingInboundPortURI);
		this.addressConnected.add(CInfo);
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		

		if(this.hasRouteFor(m.getAddress())) {
			
			boolean trouverNode=false;
			boolean trouverNetwork=false;
			
			if(this.address==m.getAddress()) {
				System.out.println("Message bien recu : "+m.getContent().toString());
				return null;
			}
			if(m.getAddress().isNetworkAdress()) {
				NetworkAddressI a = (NetworkAddressI) m.getAddress();
				for(Node_RoutingCommOutboundPort c : node_CommOBP) {
					if(c.hasRouteFor(a)) {
						// il faudrait faire appell a la fonction updateRouting
						c.transmitMessage(m);
						trouverNetwork=true;
						break;
					}
				}
			}
			
			if(m.getAddress().isNodeAdress()) {
				NodeAddressI k =(NodeAddressI)m.getAddress();
				for(Node_RoutingCommOutboundPort c : node_CommOBP) {
					if(c.hasRouteFor(k) ){
						// il faudrait faire appell a la fonction updateAccessPoint
						c.transmitMessage(m);
						trouverNode=true;
						break;
					}
				}
		
			}
			
			if(!(trouverNode||trouverNetwork)) {
				int alea =(int) (1+Math.random() * (node_CommOBP.size() - 1));
	            for(int i=0;i<alea;i++) {
	                if(m.stillAlive()) {
	                    m.decrementHops();
	                    node_CommOBP.get(i).transmitMessage(m);
	                }else {
	                    //Destruction
	                    m = null;
	                    System.out.println("Message Detruit");
	                }
	            }
			}
		}
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
