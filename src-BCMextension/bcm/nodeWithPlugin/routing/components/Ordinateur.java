package bcm.nodeWithPlugin.routing.components;

import java.util.Set;

import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;

public class Ordinateur extends AbstractComponent implements Node_RoutingI{
	
	protected final static String MY_PLUGIN_URI = "map-Node_Routing-plugin-uri" ;
	private Node_RoutingP plugin;

	
	protected Ordinateur() throws Exception {
		super(1,0);
		this.plugin = new Node_RoutingP();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	
	protected Ordinateur(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing, int nbThreadUpdateAccessPoint) throws Exception {
		super(2,0); 
		this.plugin = new Node_RoutingP(nbThreadUpdateRouting,nbThreadConnect,nbThreadConnectRouting,nbThreadTransmitMessage,nbThreadHasRouteFor,nbThreadPing,nbThreadUpdateAccessPoint);
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
		
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
	}
	
	public Node_RoutingP getPlugin() {
		return this.plugin;
	}
	

				

}
