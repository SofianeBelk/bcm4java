package fr.bcm.nodeWithPlugin.accesspoint.components;


import fr.bcm.nodeWithPlugin.accesspoint.plugins.OrdinateurFixe;
import fr.sorbonne_u.components.AbstractComponent;



public class Node_AccessPoint extends AbstractComponent{
	
	protected final static String MY_PLUGIN_URI = "map-Node_AccessPoint-plugin-uri" ;
	private OrdinateurFixe plugin;

	
	protected Node_AccessPoint() throws Exception {
		super(1,0);
		this.plugin = new OrdinateurFixe();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
		
	}

	
	
}
