package fr.bcm.node.terminal.ports;


import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * Classe Node_TerminalCommInboundPort definissant le comportement du port entrant obeissant a l'interface de CommunicationCI
 * @author Nguyen, belkhir
 *
 */

public class Node_TerminalCommInboundPort extends AbstractInboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur du port entrant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_TerminalCommInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
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
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_Terminal)this.getServiceProviderReference()).connect(address, communicationInboundPortURI);
						return null;
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
	 * entre le noeud terminal et un voisin lorsque celui-ci à la capacité à router des messages
	 *       
	 * 	 
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		this.getOwner().handleRequest(c -> ((Node_Terminal)c).connectRouting(address, communicationInboundPortURI, routingInboundPortURI));
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
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_Terminal)this.getServiceProviderReference()).transmitMessage(m);
						return null;
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
	 * 		ce service permet l'execution asynchrone de la fonctionalité, elle permet de découvrir si on peut
	 * 	 trasmettre un message a une certaine adresse
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return this.getOwner().handleRequest(c -> ((Node_Terminal)c).hasRouteFor(address));
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
		this.getOwner().handleRequest(c -> ((Node_Terminal)c).ping());
	}


	

	

}
