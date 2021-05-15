package bcm.node.terminal.ports;

import java.util.Set;

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
 * classe du port sortant "Node_TerminalCI"
 * @author Nguyen, belkhir
 *
 */

public class Node_TerminalOutBoundPort extends AbstractOutboundPort implements Node_TerminalCI{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur du port sortant
	 * @param owner
	 * @throws Exception
	 */
	public Node_TerminalOutBoundPort(ComponentI owner) throws Exception{
		super(Node_TerminalCI.class, owner);
	}
	
	/**
	 * une variante du constructeur avec une URI 
	 * @param ntopUri : l'URI
	 * @param owner
	 * @throws Exception
	 */
	public Node_TerminalOutBoundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, Node_TerminalCI.class, owner);
	}

	/** appel au service d'enrengistrement d'un noeud terminal au gestionnaire */
	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		
		return ((Node_TerminalCI)this.getConnector()).registerTerminalNode(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange);
	}

	/** appel au service de dérengistrement  d'un nœud terminal  */
	@Override
	public void unregister(AddressI address) throws Exception {
		((Node_TerminalCI)this.getConnector()).unregister(address);
	}

}
