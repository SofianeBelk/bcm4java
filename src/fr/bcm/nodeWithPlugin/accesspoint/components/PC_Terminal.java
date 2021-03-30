package fr.bcm.nodeWithPlugin.accesspoint.components;


import fr.bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;
import fr.bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import fr.bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import fr.sorbonne_u.components.AbstractComponent;



public class PC_Terminal extends AbstractComponent implements Node_AccessPointI{
	
	protected final static String MY_PLUGIN_URI = "map-Node_AccessPoint-plugin-uri" ;
	private Node_AccessPointP plugin;

	
	protected PC_Terminal() throws Exception {
		super(1,0);
		this.plugin = new Node_AccessPointP();
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
	}
	
	protected PC_Terminal(int nbThreadUpdateRouting, int nbThreadConnect, int nbThreadConnectRouting, int nbThreadTransmitMessage, int nbThreadHasRouteFor, int nbThreadPing, int nbThreadUpdateAccessPoint) throws Exception{
		super(2,0); 
		this.plugin = new Node_AccessPointP(nbThreadUpdateRouting,nbThreadConnect,nbThreadConnectRouting,nbThreadTransmitMessage,nbThreadHasRouteFor,nbThreadPing,nbThreadUpdateAccessPoint);
		this.plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin);
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		plugin.start();
		
	}

	@Override
	public Node_AccessPointP getPlugin() {
		return this.plugin;
	}

	
	
}