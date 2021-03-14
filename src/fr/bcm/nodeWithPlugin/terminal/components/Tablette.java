package fr.bcm.nodeWithPlugin.terminal.components;


import fr.bcm.nodeWithPlugin.terminal.plugins.Node_TerminalP;
import fr.sorbonne_u.components.AbstractComponent;




public class Tablette extends AbstractComponent{
	
	
 
	protected final static String MY_PLUGIN_URI = "map-Node_Terminal-plugin-uri" ;
	private Node_TerminalP plugin;
	
	protected Tablette() throws Exception {
		super(2,0); 
		this.plugin = new Node_TerminalP();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
		
	}


	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
		
	}


	
}
