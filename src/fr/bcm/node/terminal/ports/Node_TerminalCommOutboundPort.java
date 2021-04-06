package fr.bcm.node.terminal.ports;


import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Interface du port sortant "CommunicationCI"
 * @author Nguyen, belkhir
 *
 */
public class Node_TerminalCommOutboundPort extends AbstractOutboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur du port sortant
	 * @param owner
	 * @throws Exception
	 */

	public Node_TerminalCommOutboundPort(ComponentI owner) throws Exception{
		super(CommunicationCI.class, owner);
	}
	
	/**
	 * une variante du constructeur avec une URI 
	 * @param ntopUri : l'URI
	 * @param owner
	 * @throws Exception
	 */
	public Node_TerminalCommOutboundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, CommunicationCI.class, owner);
	}

	/** appel au service de connexion d'un noeud terminal a un voisin */
	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		((CommunicationCI)this.getConnector()).connect(address, communicationInboundPortURI);
	}

	/** appel au service de connexion d'un noeud terminal  a un voisin qui a la capacité à router des messages  */
	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) throws Exception {
		((CommunicationCI)this.getConnector()).connectRouting(address, communicationInboundPortURI, routingInboundPortURI);
	}
	/** appel au service de trasmittion d'un message*/
	@Override
	public void transmitMessage(MessageI m) throws Exception {
		((CommunicationCI)this.getConnector()).transmitMessage(m);
		
	}
	/** appel au service de test d'existance d'une adresse a partir du noeud terminal */
	@Override
	public boolean hasRouteFor(AddressI address) throws Exception{
		return ((CommunicationCI)this.getConnector()).hasRouteFor(address);
	}
	
	/** appel au service de test de survie d'un voisin */
	@Override
	public void ping() throws Exception{
		((CommunicationCI)this.getConnector()).ping();
	}


	

	

}
