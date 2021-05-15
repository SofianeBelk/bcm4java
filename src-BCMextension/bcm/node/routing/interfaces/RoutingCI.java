package bcm.node.routing.interfaces;

import java.util.Set;

import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * <p>
 * 	Interface de Composant pour le composant Node_Routing definissant quelques les foncationnalitée offertes par ce composant  
 * </p>
 * @author  Nguyen, Belkhir
 *
 */

public interface RoutingCI extends OfferedCI , RequiredCI{
	
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée de mise a jour
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet  la  mise a jour de la table de routage  si cette route est plus intéressante que la précédente
	 * 	</em>
	 * </p> 
	 * 
	 */
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception;
	
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée de mise a jour
	 * 	</strong>
	 * 	<em> 
	 * 		ce service  permet la mise à jour la route vers le point d’accès le plus proche
	 * 	</em>
	 * </p> 
	 * 
	 */
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception;

}
