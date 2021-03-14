package fr.bcm.nodeWithPlugin.routing.components;

import fr.bcm.nodeWithPlugin.routing.plugins.Ordinateur;
import fr.sorbonne_u.components.AbstractComponent;

public class Node_Routing extends AbstractComponent{
	
	protected final static String MY_PLUGIN_URI = "map-Node_Routing-plugin-uri" ;
	private Ordinateur plugin;

	
	protected Node_Routing() throws Exception {
		super(1,0);
		this.plugin = new Ordinateur();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
		
	}



}
