package fr.bcm.node.routing.ports;

import java.util.Set;

import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * classe du port sortant "Node_RoutingRoutingOutboundPort"
 * @author Nguyen, belkhir
 *
 */
public class Node_RoutingRoutingOutboundPort extends AbstractOutboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur du port sortant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_RoutingRoutingOutboundPort(ComponentI owner) throws Exception{
		super(RoutingCI.class, owner);
	}
	
	/**
	 * une variante du constructeur avec une URI 
	 * @param ntopUri : l'URI
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_RoutingRoutingOutboundPort(String ntopUri, ComponentI owner) throws Exception {
		super(ntopUri, RoutingCI.class, owner);
	}
	
	/** appel au service de mise a jour de la table de routage  */
	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		((RoutingCI)this.getConnector()).updateRouting(neighbour, routes);
	}
	
	/** appel au service de mise a jour de la  route vers le point d’accès le plus proche */
	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception {
		((RoutingCI)this.getConnector()).updateAccessPoint(neighbour, numberOfHops);
	}


	


	

	

}
