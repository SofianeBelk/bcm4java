package bcm.node.terminal.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

import java.util.List;
import java.util.Set;
import java.rmi.ConnectException;
import java.util.ArrayList;

import bcm.connexion.classes.ConnectionInformation;
import bcm.connexion.connectors.CommunicationConnector;
import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.connectors.NodeConnector;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.node.terminal.ports.Node_TerminalCommInboundPort;
import bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import bcm.node.terminal.ports.Node_TerminalOutBoundPort;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.address.classes.Address;
import bcm.utils.address.classes.NetworkAddress;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.classes.Message;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.classes.Position;
import bcm.utils.nodeInfo.interfaces.PositionI;


@RequiredInterfaces(required = {CommunicationCI.class, Node_TerminalCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_Terminal extends AbstractComponent{
	
	/** un compteur qui permet d'identifier notre noeud **/
	public static int node_terminal_id = 1;
	
	/** l'identifiant de notre nœud terminal **/
	private int id;
	
	/** le port sortant du nœud terminal **/
	protected Node_TerminalOutBoundPort ntop;
	
	/** le port entrant "CommunicationCI" du nœud terminal **/
	protected Node_TerminalCommInboundPort ntip;
	
	/** la liste des ports sortant "CommunicationCI" du nœud terminal **/
	protected List<Node_TerminalCommOutboundPort> node_CommOBP = new ArrayList<>();
	
	/** l'adresse du nœud terminal **/
	private NodeAddress address = new NodeAddress();
	
	/** la liste des adresses accessible à partir du nœud terminal **/
	private List<ConnectionInformation> addressConnected= new ArrayList<>();
	
	/** le nombre e tentative pour envoyais un message **/
	private int NumberOfNeighboorsToSend = 2;
	
	/** la position initial du nœud terminal **/
	private PositionI pointInitial;
	
	/** un boolean qui permet de savoir si notre composant est toujours vivant**/
	private boolean isAlive = false;
	
	
	/**
	 * Constructeur qui crée une instance du nœud terminal
	 * @throws Exception
	 */
	protected Node_Terminal() throws Exception {
		super(3,0); 
		this.id = node_terminal_id;
		node_terminal_id += 1;
		this.pointInitial = new Position(0,this.id);
		this.ntop = new Node_TerminalOutBoundPort(this);
		this.ntip = new Node_TerminalCommInboundPort(this);
		this.ntop.publishPort();
		this.ntip.publishPort();
		
		
		this.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
		// Enable logs
		this.toggleLogging();
		this.toggleTracing();
		this.logMessage("Node Terminal " + this.address.getAdress() + this.id + " Position initiale : (" + pointInitial.getX() + ", " + pointInitial.getY() + ")");
		
	}


	/**--------------------------------------------------
	 *--------------  Component life-cycle -------------
	  --------------------------------------------------**/
	
	@Override
	public synchronized void finalise() throws Exception {
		super.finalise();
	}


	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			if(ntop.connected()) {
				this.ntop.doDisconnection();
			}
			if(ntip.connected()) {
				this.ntip.doDisconnection();
			}
			
			
			this.ntop.unpublishPort();
			this.ntip.unpublishPort();
		} catch (Exception e) {
			return;
		}
		super.shutdown();
	}


	@Override
	public void execute() throws Exception {
		super.execute();
		this.logMessage("Tries to log in the manager");
		Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, ntip.getPortURI(),this.pointInitial , 1.5);
		this.isAlive = true;
		this.logMessage("Logged");
		
		// Current node connects to others nodes
		
		for(ConnectionInfoI CInfo: devices) {
			ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
			Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this);
			ntcop.publishPort();
			
			
			try {
				this.doPortConnection(
						ntcop.getPortURI(),
						CInfo.getCommunicationInboundPortURI(),
						CommunicationConnector.class.getCanonicalName()
				);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			ntcop.connect(address, this.ntip.getPortURI());
			node_CommOBP.add(ntcop);
			
			ciToAdd.setcommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
			ciToAdd.setNtcop(ntcop);
			this.addressConnected.add(ciToAdd);
		}
		this.logMessage("Connected to all nearby devices");
		this.logMessage("Ping neighbour");
		try {
			this.ntip.ping();
		}catch(Exception e) {
			this.logMessage("Dead neighbour");
		}
		this.logMessage("neighbour always alive");
		this.ntop.unregister(this.address);
		this.isAlive = false;
	}

	/** ------------------------- Services ------------------------**/
	
	/**
	 * cette méthode permet a un voisin de se connecter 
	 * @param address : l'adresse du voisin
	 * @param communicationInboundPortURI
	 * @return null
	 * @throws Exception
	 */
	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		
		this.logMessage("Someone asked for connection");
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setCommunicationInboundPortURI(communicationInboundPortURI);
		
		this.logMessage("Creating comm port");
		Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this);
		ntcop.publishPort();
		
		this.doPortConnection(
				ntcop.getPortURI(), 
				communicationInboundPortURI, 
				CommunicationConnector.class.getCanonicalName());
		this.logMessage("Connected comm port to device");
		
		node_CommOBP.add(ntcop);
		CInfo.setNtcop(ntcop);
		this.addressConnected.add(CInfo);
		this.logMessage("Added " + address.getAdress() + " to connections");

		return null;
	}

	/**
	 * Cette méthode est appeler par le nœud terminal s'il a la capacité à router des messages 
	 * @param address
	 * @param communicationInboundPortURI
	 * @param routingInboundPortURI
	 * @return null
	 * @throws Exception
	 */
	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		return null;
	}
	
	/**
	 * cette méthode permet de trasmettre un message
	 * @param m : le message à transmettre
	 * @return null
	 * @throws Exception
	 */

	public Object transmitMessage(MessageI m) throws Exception {
		
		m.decrementHops();
		
		
		
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
		}
		else {
			if(m.stillAlive()) {
				int numberSent = 0;
				for(int i = 0; numberSent < NumberOfNeighboorsToSend && i < this.addressConnected.size(); i++) {					
					AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
					this.logMessage("Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
					MessageI messageToSend = m.copy();
					this.node_CommOBP.get(i).transmitMessage(messageToSend);
					numberSent += 1;
				}
			}
			else {
				this.logMessage("Message dead");
			}
		}
		
		return null;
	}
	
	/**
	 * Cette méthode vérifie s'il existe une route vers une adresse particulière
	 * @param address : l'adresse a vérifié
	 * @return true si il existe une route
	 * @throws Exception
	 */

	public boolean hasRouteFor(AddressI address) throws Exception{
		return false;
	}

	public Void ping() throws Exception{
		return null;
	}
	
}
