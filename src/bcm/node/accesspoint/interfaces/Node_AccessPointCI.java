package bcm.node.accesspoint.interfaces;

import java.util.Set;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * <p>
 * 	Interface de Composant pour le composant Node_AccessPoint definissant toutes les foncationnalitée offertes par ce composant  
 * </p>
 * @author  Nguyen, Belkhir
 *
 */
public interface Node_AccessPointCI extends RequiredCI{
	
	
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée d'enregistrement d'un Node_AccessPoint
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet d'enregistrer un Node_AccessPoint dans la table du gestionnaire
	 * 	</em>
	 * </p> 
	 * 
	 */
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception;
	
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée de dérengistrement d'un Node_AccessPoint
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet de  dérengistrer un Node_AccessPoint dans la table du gestionnaire 
	 * 	</em>
	 * </p> 
	 * 
	 */
	public void unregister(AddressI address) throws Exception;
}
