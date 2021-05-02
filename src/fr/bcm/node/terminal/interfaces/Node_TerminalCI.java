package fr.bcm.node.terminal.interfaces;

import java.util.Set;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * <p>
 * 	Interface de Composant pour le composant Node_Terminal  definissant toutes les foncationnalitées  require par ce composant  
 * </p>
 * @author  Nguyen, Belkhir
 *
 */
public interface Node_TerminalCI extends RequiredCI{
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée qui permet de s'enregistrer au prés du gestionnaire du réseau
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet a un noeud terminal de s'enregistrer auprés du gestionnaire du réseau
	 * 	</em>
	 * </p> 
	*/

	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI, PositionI initialPosition, double initialRange) throws Exception;
	
	/**
	 * <p>
	 * 	<strong> 
	 * 		fonctionnalitée qui permet de désenregister du gestionnaire de réseau
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet a un noeud terminal de désenregister du gestionnaire de réseau
	 * 	</em>
	 * </p> 
	*/
	public void unregister(AddressI address) throws Exception;
}
