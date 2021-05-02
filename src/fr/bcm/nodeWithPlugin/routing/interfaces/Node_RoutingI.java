package fr.bcm.nodeWithPlugin.routing.interfaces;

import fr.bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;

/**
 * <p>
 * 	Interface de Composant pour le composant Node_Routing "Ordinateur" definissant le méthode qui permet de récupérer le plugin   
 * </p>
 * @author  Nguyen, Belkhir
 *
 */
public interface Node_RoutingI {
	public Node_RoutingP getPlugin();
}
