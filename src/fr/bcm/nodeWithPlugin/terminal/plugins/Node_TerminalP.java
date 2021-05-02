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
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

/**
 * classe Node_TerminalP qui est le plugin "greffons" qui correspand au noeud terminal
 * @author Nguyen, Belkhir
 **/
public class Node_TerminalP extends AbstractPlugin {
	public static final long 			serialVersionUID = 1L ;
	
	
	/** l'identifiant de notre nœud terminal **/
	private int id;
	
	protected ComponentI owner;
	
	/** l'URI du port de communication**/
	protected String URIportCommunication;
	
	/**l'URI du port terminal**/
	protected String URIportNodeTerminale; 
	
	/** le port sortant du nœud terminal **/
	protected Node_TerminalOutBoundPort ntop;
	
	/** le port entrant "CommunicationCI" du nœud terminal **/
	protected Node_TerminalCommInboundPort ntcip;
	
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
	
	
	// // Thread 
	
	// Thread URI
	/**l'URI de la méthode connect**/
	public static final String			Connect_URI            	= "Connexion" ;
	
	/**l'URI de la méthode HasRouteFor**/
	public static final String         	Has_Routes_URI			= "Has_routes_for";
	
	/**l'URI de la méthode Ping**/
	public static final String         	Ping_URI 				= "ping";
	
	// Thread Distribution
	/**le nombre initial de thread pour la méthode connect**/
	private int nbThreadConnect = 1;
	
	/**le nombre initial de thread pour la méthode connectRouting**/
	private int nbThreadConnectRouting = 1;
	
	/**le nombre initial de thread pour la méthode transmitMessage**/
	private int nbThreadTransmitMessage = 1;
	
	/**le nombre initial de thread pour la méthode HasRouteFor**/
	private int nbThreadHasRouteFor = 1;
	
	/**le nombre de thread pour la méthode ping**/
	private int nbThreadPing = 1;
	
	// Locks
	protected ReentrantReadWriteLock lockForArrays = new ReentrantReadWriteLock();
	
	/**
	 * Constructeur vide
	 */
	public Node_TerminalP() {
		
	}
	
	/**
	 * Constructeur qui permet d'initialiser le nombre maximum de thread pour chaque méthode
	 * @param C
	 * @param nbThreadConnectRouting
	 * @param nbThreadTransmitMessage
	 * @param nbThreadHasRouteFor
	 * @param nbThreadPing
	 */
	public Node_TerminalP(int C, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing) {
		this.nbThreadPing=nbThreadPing;
		this.nbThreadConnectRouting=nbThreadConnectRouting;
		this.nbThreadTransmitMessage=nbThreadTransmitMessage;
		this.nbThreadHasRouteFor=nbThreadHasRouteFor;
		this.nbThreadPing=nbThreadPing;
	}
	
	/**--------------------------------------------------
	 *--------------  Component life-cycle -------------
	  --------------------------------------------------**/
	
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
        this.createNewExecutorService(Connect_URI, nbThreadConnect, false);
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
		 
		 	// Retrieve the list of devices to connect with
			Set<ConnectionInfoI> devices = this.ntop.registerTerminalNode(address, ntcip.getPortURI(),this.pointInitial , 1.5);
			this.logMessage("Logged");
			
			// Connects to every device
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
				
				// This blocks lock arrays to add a new connection into it
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
		Node_TerminalCommOutboundPort ntcop = new Node_TerminalCommOutboundPort(this.owner);
		ntcop.publishPort();
		this.owner.doPortConnection(
				ntcop.getPortURI(), 
				communicationInboundPortURI, 
				CommunicationConnector.class.getCanonicalName());
		this.logMessage("Connected comm port to device");
		
		
		// This blocks lock arrays to add a new connection into it
		lockForArrays.writeLock().lock();
		node_CommOBP.add(ntcop);
		CInfo.setNtcop(ntcop);
		this.addressConnected.add(CInfo);
		lockForArrays.writeLock().unlock();
		
		this.logMessage("Added " + address.getAdress() + " to connections");

		return null;
	}

	// Doesn't have a routing ability
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
	}
	
	// Doesn't transmit message
	public void transmitMessage(MessageI m) throws Exception {
	}

	public boolean hasRouteFor(AddressI address) throws Exception{
		return false;
	}

	/**
	 * Cette méthode permet de vérifier le voisin est encore présent
	 * @return null
	 * @throws Exception
	 */
	public Object ping() throws Exception{
		return null;
	}
	


	
	

}
