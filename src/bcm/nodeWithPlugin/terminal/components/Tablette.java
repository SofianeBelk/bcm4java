package bcm.nodeWithPlugin.terminal.components;


import bcm.nodeWithPlugin.terminal.interfaces.Node_TerminalI;
import bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;
import fr.sorbonne_u.components.AbstractComponent;


/**
 * classe Tablette qui est un  nœud terminal dans notre réseau
 * @author Nguyen, Belkhir
 **/

public class Tablette extends AbstractComponent implements Node_TerminalI{

	/** l'URI de la tablette**/
	protected final static String MY_PLUGIN_URI = "map-Node_Terminal-plugin-uri" ;
	
	/**plugin noeud terminal**/
	private Node_TerminalP plugin;
	
	/**
	 * constructeur Tablette
	 * @throws Exception
	 */
	protected Tablette() throws Exception {
		super(2,0); 
		this.plugin = new Node_TerminalP();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	/**
	 * constrcuteur avec le nombre de thread maximum pour chaque méthode
	 * @param nbThreadConnect : le nombre maximum de thread  pour la méthode connect
	 * @param nbThreadConnectRouting : le nombre maximum de thread  pour la méthode ConnectRouting
	 * @param nbThreadTransmitMessage : le nombre maximum de thread pout la méthode TransmitMessage
	 * @param nbThreadHasRouteFor : le nombre maximum de thread  pour la méthode HasRoutFor
	 * @param nbThreadPing : le nombre maximum de thread pour la méthode Ping
	 * @throws Exception
	 */
	protected Tablette(int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing) throws Exception {
		super(2,0); 
		this.plugin = new Node_TerminalP(nbThreadConnect,nbThreadConnectRouting,nbThreadTransmitMessage,nbThreadHasRouteFor,nbThreadPing);
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
		
	}

	/**
	 * on redéfinie la méthode execute qui permet de lancer la méthode start implémenter dans le plugin
	 * @throws Exception
	 */
	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
		
	}
	
	/**
	 * retrun : retroune le plugin
	 */
	public Node_TerminalP getPlugin() {
		return this.plugin;
	}


	
}
