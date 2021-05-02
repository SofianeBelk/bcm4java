package fr.bcm.nodeWithPlugin.accesspoint.interfaces;

import fr.bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;

/**
* <p>
* 	Interface de Composant pour le composant Node_AccessPoint "PC_Terminal" definissant le méthode qui permet de récupérer le plugin   
* </p>
* @author  Nguyen, Belkhir
*
*/
public interface Node_AccessPointI {
	public Node_AccessPointP getPlugin();
}
