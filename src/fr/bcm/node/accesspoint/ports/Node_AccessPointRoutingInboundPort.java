package fr.bcm.node.accesspoint.ports;

import java.util.Set;
import fr.bcm.node.accesspoint.components.Node_AccessPoint;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;


/**
 * Classe Node_AccessPointRoutingInboundPort definissant le comportement du port entrant obeissant a l'interface de RoutingCI
 * @author Nguyen, belkhir
 *
 */
public class Node_AccessPointRoutingInboundPort extends AbstractInboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur du port entrant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_AccessPointRoutingInboundPort(ComponentI owner) throws Exception {
		super(RoutingCI.class, owner);
	}


	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de mise a jour
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet de mettre a jour de la table de routage  
	 *      si cette route est plus intéressante que la précédente
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_AccessPoint)this.getServiceProviderReference()).updateRouting(neighbour, routes);
						return null;
					}
				}
		);
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de mise a jour
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet de mettre à jour la route vers le point d’accès le plus proche
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Node_AccessPoint)this.getServiceProviderReference()).updateAccessPoint(neighbour, numberOfHops);
						return null;
					}
				}
		);
	}



	

	

}
