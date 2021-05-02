package fr.bcm.nodeWithPlugin.terminal.ports;


import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.nodeWithPlugin.terminal.interfaces.Node_TerminalI;
import fr.bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * Classe Node_TerminalCommInboundPort definissant le comportement du port entrant obeissant a l'interface de CommunicationCI
 * @author Nguyen, belkhir
 *
 */

public class Node_TerminalCommInboundPort extends AbstractInboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;

	/**
	 * Constructeur du port entrant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_TerminalCommInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}
	
	/**
	 * une variante du premiéer constructeur 
	 * @param owner : le composant utilisant ce port
	 * @param pluginURI : l'URI du plugin
	 * @throws Exception
	 */
	public Node_TerminalCommInboundPort(ComponentI owner, String pluginURI) throws Exception {
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
		this.getOwner().runTask(Node_TerminalP.Connect_URI,
				nr -> {
					try {
						((Node_TerminalI)nr).getPlugin().connect(address, communicationInboundPortURI);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			);
	}
	
	

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
	}

	@Override
	public void transmitMessage(MessageI m) throws Exception {
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		savoir si il existe une route.
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet de découvrir si on peut
	 * 	 trasmettre un message a une certaine adresse
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(Node_TerminalP.Has_Routes_URI, c -> ((Node_Terminal)c).hasRouteFor(address));
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
	public void ping() throws Exception{
		this.getOwner().handleRequest(Node_TerminalP.Ping_URI, c -> ((Node_Terminal)c).ping());
	}


	

	

}
