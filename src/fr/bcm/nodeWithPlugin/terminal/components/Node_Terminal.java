package fr.bcm.nodeWithPlugin.terminal.components;


import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.nodeWithPlugin.terminal.plugins.NodeTerminalplugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;


@RequiredInterfaces(required = {CommunicationCI.class, Node_TerminalCI.class})
@OfferedInterfaces (offered = {CommunicationCI.class})

public class Node_Terminal extends AbstractComponent{
	
	
	protected String URIportCommunication; 
	protected String URIportNodeTerminale; 
	protected final static String MY_PLUGIN_URI = "map-Node_Terminal-plugin-uri" ;
	
	
	protected Node_Terminal(String URIportNodeTerminale,String URIportCommunication) throws Exception {
		super(1,0); 
		this.URIportCommunication=URIportCommunication;
		this.URIportNodeTerminale=URIportNodeTerminale;
		
	}


	
	@Override
	public void execute() throws Exception {
		super.execute();
		NodeTerminalplugin plugin = new NodeTerminalplugin(URIportNodeTerminale,URIportCommunication);
		plugin.setPluginURI(MY_PLUGIN_URI);
		this.installPlugin(plugin); 
		
	}


	
}
