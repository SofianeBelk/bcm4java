package fr.bcm.node.routing.interfaces;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * <p>
 * 	Interface de Composant pour le composant Node_Routing definissant quelques les foncationnalitée offertes par ce composant  
 * </p>
 * @author  Nguyen, Belkhir
 *
 */
public interface Node_RoutingCI extends RequiredCI, OfferedCI{
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée d'enregistrement d'un Node_Routing
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet d'enregistrer un Node_Routing dans la table du gestionnaire
	 * 	</em>
	 * </p> 
	 * 
	 */
	public Set<ConnectionInfoI> registerRoutingNode(
			NodeAddressI address, 
			String communicationInboundPortURI,
			PositionI initialPosition, 
			double initialRange,
			String routingInBoundPortURI) throws Exception;
	
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée de dérengistrement d'un Node_Routing 
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet de  dérengistrer un Node_Routing dans la table du gestionnaire 
	 * 	</em>
	 * </p> 
	 * 
	 */
	public void unregister(AddressI address) throws Exception;
}
