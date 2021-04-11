package fr.bcm.node.terminal.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Interface du port sortant "Node_TerminalCI"
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
