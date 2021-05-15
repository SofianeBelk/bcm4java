package bcm.nodeWithPlugin.accesspoint.components;


import bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;
import bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import fr.sorbonne_u.components.AbstractComponent;


/**
 * classe PC_Terminal qui est un  nœud AccessPoint dans notre réseau
 * @author Nguyen, Belkhir
 **/
public class PC_Terminal extends AbstractComponent implements Node_AccessPointI{
	
	/** l'URI de PC_Terminal**/
	protected final static String MY_PLUGIN_URI = "map-Node_AccessPoint-plugin-uri" ;
	
	/**plugin noeud AceesPoint**/
	private Node_AccessPointP plugin;

	/**
	 * constructeur PC_Terminal
	 * @throws Exception
	 */
	protected PC_Terminal() throws Exception {
		super(1,0);
		this.plugin = new Node_AccessPointP();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	
	/**
	 * constrcuteur avec le nombre de thread maximum pour chaque méthode
	 * @param nbThreadUpdateRouting : le nombre maximum de thread  pour la méthode UpdateRouting
	 * @param nbThreadConnect : le nombre maximum de thread  pour la méthode Connect
	 * @param nbThreadConnectRouting : le nombre maximum de thread  pour la méthode ConnectRouting
	 * @param nbThreadTransmitMessage : le nombre maximum de thread  pour la méthode TransmitMessage
	 * @param nbThreadHasRouteFor : le nombre maximum de thread  pour la méthode HasRoutFor
	 * @param nbThreadPing : le nombre maximum de thread pour la méthode Ping
	 * @param nbThreadUpdateAccessPoint :
	 * @throws Exception
	 */
	protected PC_Terminal(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing, int nbThreadUpdateAccessPoint) throws Exception{
		super(2,0); 
		this.plugin = new Node_AccessPointP(nbThreadUpdateRouting,nbThreadConnect,nbThreadConnectRouting,nbThreadTransmitMessage,nbThreadHasRouteFor,nbThreadPing,nbThreadUpdateAccessPoint);
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
	@Override
	public Node_AccessPointP getPlugin() {
		return this.plugin;
	}

	
	
}
