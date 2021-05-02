package fr.bcm.nodeWithPlugin.terminal.interfaces;

import fr.bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;

/**
 * <p>
 * 	Interface de Composant pour le composant Node_Terminal "tablette" definissant le méthode qui permet de récupérer le plugin   
 * </p>
 * @author  Nguyen, Belkhir
 *
 */
public interface Node_TerminalI {
	public Node_TerminalP getPlugin();
}
