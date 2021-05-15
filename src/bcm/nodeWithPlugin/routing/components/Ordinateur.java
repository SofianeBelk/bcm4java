package bcm.nodeWithPlugin.routing.components;


import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;

/**
 * classe Ordinateur qui est un  nœud routing dans notre réseau
 * @author Nguyen, Belkhir
 **/
public class Ordinateur extends AbstractComponent implements Node_RoutingI{
	
	/** l'URI de l'ordinateur**/
	protected final static String MY_PLUGIN_URI = "map-Node_Routing-plugin-uri" ;
	
	/**plugin noeud ROuting**/
	private Node_RoutingP plugin;

	/**
	 * constructeur Ordinateur
	 * @throws Exception
	 */
	protected Ordinateur() throws Exception {
		super(1,0);
		this.plugin = new Node_RoutingP();
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
	protected Ordinateur(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing, int nbThreadUpdateAccessPoint) throws Exception {
		super(2,0); 
		this.plugin = new Node_RoutingP(nbThreadUpdateRouting,nbThreadConnect,nbThreadConnectRouting,nbThreadTransmitMessage,nbThreadHasRouteFor,nbThreadPing,nbThreadUpdateAccessPoint);
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
	public Node_RoutingP getPlugin() {
		return this.plugin;
	}
	

				

}
