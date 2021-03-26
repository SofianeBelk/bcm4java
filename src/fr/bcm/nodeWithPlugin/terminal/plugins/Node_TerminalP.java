package fr.bcm.nodeWithPlugin.terminal.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import fr.bcm.connexion.classes.ConnectionInformation;
import fr.bcm.connexion.connectors.CommunicationConnector;
import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.ports.Node_TerminalCommOutboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalOutBoundPort;
import fr.bcm.nodeWithPlugin.terminal.ports.Node_TerminalCommInboundPort;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.bcm.registration.interfaces.RegistrationCI;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class Node_TerminalP extends AbstractPlugin {
	public static final long 			serialVersionUID = 1L ;
	
	
	
	private int id;
	protected ComponentI owner;
	protected String URIportCommunication; 
	protected String URIportNodeTerminale; 
	protected Node_TerminalOutBoundPort ntop;
	protected Node_TerminalCommInboundPort ntcip;
	protected List<Node_TerminalCommOutboundPort> node_CommOBP = new ArrayList<>();
	private NodeAddress address = new NodeAddress();
	private List<ConnectionInformation> addressConnected= new ArrayList<>();
	
	private int NumberOfNeighboorsToSend = 2;
	private PositionI pointInitial;
	
	
	// // Thread 
	
	// Thread URI
	public static final String			ConnectRouting_URI 		= "Connexion_via_les_tables_de_routing" ;
	public static final String			Connect_URI            	= "Connexion" ;
	public static final String		   	Transmit_MESSAGES_URI	= "filtrer_des_messages" ;
	public static final String         	Has_Routes_URI			= "Has_routes_for";
	public static final String         	Ping_URI 				= "ping";
	
	// Thread Distribution
	private int nbThreadConnect = 1;
	private int nbThreadConnectRouting = 1;
	private int nbThreadTransmitMessage = 1;
	private int nbThreadHasRouteFor = 1;
	private int nbThreadPing = 1;
	
	// Locks
	protected ReentrantReadWriteLock lockForArrays = new ReentrantReadWriteLock();
	
	
	public Node_TerminalP() {
		
	}
	
	public Node_TerminalP(int C, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing) {
		this.nbThreadPing=nbThreadPing;
		this.nbThreadConnectRouting=nbThreadConnectRouting;
		this.nbThreadTransmitMessage=nbThreadTransmitMessage;
		this.nbThreadHasRouteFor=nbThreadHasRouteFor;
		this.nbThreadPing=nbThreadPing;
	}
	
	@Override
	public void	 installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		System.out.println("plugin node terminal started");

		this.owner=owner;

		// Add interfaces 
		this.addRequiredInterface(CommunicationCI.class);
		this.addRequiredInterface(Node_TerminalCI.class);
        this.addOfferedInterface(CommunicationCI.class);
       
		//ExecutoreServices
        this.createNewExecutorService(ConnectRouting_URI, nbThreadConnectRouting, false);
        this.createNewExecutorService(Connect_URI, nbThreadConnect, false);
		
		this.createNewExecutorService(Transmit_MESSAGES_URI, nbThreadTransmitMessage, false);
		this.createNewExecutorService(Has_Routes_URI, nbThreadHasRouteFor, false);
		
		this.createNewExecutorService(Ping_URI, nbThreadPing, false);
		
		
		// Enable logs
		this.owner.toggleLogging();
		this.owner.toggleTracing();
		this.owner.logMessage("Node Terminal " + this.address.getAdress());
        
	}

	
	@Override
	public void	initialise() throws Exception {
        super.initialise();
        
        this.id = Node_Terminal.node_terminal_id;
		Node_Terminal.node_terminal_id += 1;
		this.pointInitial = new Position(0,this.id);
        //add port 
        this.ntop = new Node_TerminalOutBoundPort(this.owner);
		this.ntcip = new Node_TerminalCommInboundPort(this.owner,this.getPluginURI());
		this.ntop.publishPort();
		this.ntcip.publishPort();
		
		this.owner.doPortConnection(
				this.ntop.getPortURI(),
				GestionnaireReseau.GS_URI,
				NodeConnector.class.getCanonicalName());
	}
	
	public void start() throws Exception {
		 this.logMessage("Tries to log in the manager");
			Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, ntcip.getPortURI(),this.pointInitial , 1.5);
			this.logMessage("Logged");
			
			// Current node connects to others nodes
			
			for(ConnectionInfoI CInfo: devices) {
				ConnectionInformation ciToAdd = new ConnectionInformation(CInfo.getAddress());
				Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this.owner);
				ntcop.publishPort();
				
				
				try {
					this.owner.doPortConnection(
							ntcop.getPortURI(),
							CInfo.getCommunicationInboundPortURI(),
							CommunicationConnector.class.getCanonicalName()
					);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				ntcop.connect(address, this.ntcip.getPortURI());
				lockForArrays.writeLock().lock();
				node_CommOBP.add(ntcop);
				
				ciToAdd.setcommunicationInboundPortURI(CInfo.getCommunicationInboundPortURI());
				ciToAdd.setNtcop(ntcop);
				this.addressConnected.add(ciToAdd);
				lockForArrays.writeLock().unlock();
			}
			this.logMessage("Connected to all nearby devices");
	}

	
	@Override
	public void	finalise() throws Exception
	{		
		super.finalise();
		this.owner.doPortDisconnection(this.ntop.getPortURI());


	}

	
	@Override
	public void	uninstall() throws Exception
	{
		super.uninstall();

		this.ntop.unpublishPort();		
		this.ntcip.unpublishPort();
		
		this.ntop.destroyPort() ;
		this.ntcip.destroyPort() ;
		
	}


	public Object connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {	
		this.logMessage("Someone asked for connection");
		ConnectionInformation CInfo = new ConnectionInformation(address);
		CInfo.setCommunicationInboundPortURI(communicationInboundPortURI);
		
		this.logMessage("Creating comm port");
		Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this.owner);
		ntcop.publishPort();
		
		this.owner.doPortConnection(
				ntcop.getPortURI(), 
				communicationInboundPortURI, 
				CommunicationConnector.class.getCanonicalName());
		this.logMessage("Connected comm port to device");
		
		lockForArrays.writeLock().lock();
		node_CommOBP.add(ntcop);
		CInfo.setNtcop(ntcop);
		this.addressConnected.add(CInfo);
		lockForArrays.writeLock().unlock();
		this.logMessage("Added " + address.getAdress() + " to connections");

		return null;
	}

	public Object connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		return null;
	}

	public Object transmitMessage(MessageI m) throws Exception {
		
		m.decrementHops();
		m.addAddressToHistory(this.address);
		
		
		
		if(m.getAddress().equals(this.address)) {
			this.logMessage("Received a message : " + m.getContent());
		}
		else {
			if(m.stillAlive()) {
				int numberSent = 0;
				
				lockForArrays.readLock().lock();
				for(int i = 0; numberSent < NumberOfNeighboorsToSend && i < this.addressConnected.size(); i++) {					
					AddressI addressToTransmitTo = this.addressConnected.get(i).getAddress();
					if(!m.isInHistory(addressToTransmitTo)) {
						this.logMessage("Transmitting a Message to " + this.addressConnected.get(i).getAddress().getAdress());
						MessageI messageToSend = m.copy();
						this.node_CommOBP.get(i).transmitMessage(messageToSend);
						numberSent += 1;
					}
					else {
						this.logMessage("No node to send message to");
					}
				}
				lockForArrays.readLock().unlock();
			}
			else {
				this.logMessage("Message dead");
			}
		}
		
		return null;
	}

	public boolean hasRouteFor(AddressI address) throws Exception{
		return false;
	}

	public Object ping() throws Exception{
		return null;
	}
	


	
	

}
