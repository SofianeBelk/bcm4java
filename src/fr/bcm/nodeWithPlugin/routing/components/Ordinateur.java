package fr.bcm.nodeWithPlugin.routing.components;

import java.util.Set;

import fr.bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;

public class Ordinateur extends AbstractComponent{
	
	protected final static String MY_PLUGIN_URI = "map-Node_Routing-plugin-uri" ;
	private Node_RoutingP plugin;

	
	protected Ordinateur() throws Exception {
		super(2,0);
		this.plugin = new Node_RoutingP();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
	}
	

				

}
