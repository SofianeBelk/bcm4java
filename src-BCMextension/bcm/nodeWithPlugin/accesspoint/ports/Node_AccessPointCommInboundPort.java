package bcm.nodeWithPlugin.accesspoint.ports;


import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.routing.components.Node_Routing;
import bcm.node.terminal.components.Node_Terminal;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;
import bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Classe Node_AccessPointCommInboundPort definissant le comportement du port entrant obeissant a l'interface de CommunicationCI
 * @author Nguyen, belkhir
 *
 */
public class Node_AccessPointCommInboundPort extends AbstractInboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;
	
	/**
	 * Constructeur du port entrant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_AccessPointCommInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}
	
	/**
	 * une variante du premiéer constructeur 
	 * @param owner : le composant utilisant ce port
	 * @param pluginURI : l'URI du plugin
	 * @throws Exception
	 */
	public Node_AccessPointCommInboundPort(ComponentI owner, String pluginURI) throws Exception {
		super(CommunicationCI.class, owner);
		this.pluginURI = pluginURI;
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de connexion 
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet de se connecter
	 *      a un voisin.   
	 * 	 
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		this.getOwner().runTask(Node_AccessPointP.Connect_URI,		
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().connect(address, communicationInboundPortURI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de connexion 
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet la connexion  
	 * entre un noeud et son voisin lorsque celui-ci à la capacité à router des messages
	 *       
	 * 	 
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		
		this.getOwner().runTask(Node_AccessPointP.ConnectRouting_URI,			
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de trasmmition de message
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet de transmettre
	 * 	 un message
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void transmitMessage(MessageI m) throws Exception {
		this.getOwner().runTask(Node_AccessPointP.Transmit_MESSAGES_URI,
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().transmitMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		savoir si il existe une route.
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet de découvrir si il existe
	 * une route pour arriver a une adresse particuliére
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(Node_AccessPointP.Has_Routes_URI,c -> ((Node_AccessPoint)c).hasRouteFor(address));
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		savoir si le voisin est toujours la
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet de découvrir si un voisin
	 * est toujours présent ou pas !
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public Void ping() throws Exception{
		return this.getOwner().handleRequest(Node_AccessPointP.Ping_URI,c -> ((Node_AccessPointI)c).getPlugin().ping());
	}


	

	

}
