package bcm.node.accesspoint.ports;



import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.registration.interfaces.RegistrationCI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NetworkAddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.message.interfaces.MessageI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * classe du port sortant "Node_AccessPointCommOutboundPort"
 * @author Nguyen, belkhir
 *
 */
public class Node_AccessPointCommOutboundPort extends AbstractOutboundPort implements CommunicationCI{

	private static final long serialVersionUID = 1L;


	/**
	 * Constructeur du port sortant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_AccessPointCommOutboundPort(ComponentI owner) throws Exception{
		super(CommunicationCI.class, owner);
	}
	
	/**
	 * une variante du constructeur avec une URI 
	 * @param ntopUri : l'URI
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_AccessPointCommOutboundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, CommunicationCI.class, owner);
	}

	/** appel au service de connexion d'un Node_AccessPoint a un voisin */
	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) throws Exception {
		((CommunicationCI)this.getConnector()).connect(address, communicationInboundPortURI);
	}

	/** appel au service de connexion d'un Node_AccessPoint  a un voisin qui a la capacité à router des messages  */
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
	public Void ping() throws Exception{
		return ((CommunicationCI)this.getConnector()).ping();
	}


	

	

}
