package fr.bcm.node.routing.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.routing.interfaces.Node_RoutingCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * classe du port sortant "Node_RoutingOutBoundPort"
 * @author Nguyen, belkhir
 *
 */
public class Node_RoutingOutBoundPort extends AbstractOutboundPort implements Node_RoutingCI{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur du port sortant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_RoutingOutBoundPort(ComponentI owner) throws Exception {
		super(Node_RoutingCI.class, owner);
	}

	/** appel au service d'enregistrement  d'un Node_Routing au prés du gestionnaire  */
	@Override
	public Set<ConnectionInfoI> registerRoutingNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		return ((Node_RoutingCI)this.getConnector()).registerRoutingNode(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange,
				routingInboundPortURI);
	}

	/** appel au service de dérengistrement  d'un Node_Routing au prés du gestionnaire  */
	@Override
	public void unregister(AddressI address) throws Exception {
		((Node_RoutingCI)this.getConnector()).unregister(address);
		
	}



	

	

}
