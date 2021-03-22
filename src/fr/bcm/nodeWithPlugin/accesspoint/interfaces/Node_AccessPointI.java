package fr.bcm.nodeWithPlugin.accesspoint.interfaces;

import fr.bcm.nodeWithPlugin.accesspoint.components.PC_Terminal;
import fr.bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import fr.bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;

public interface Node_AccessPointI {
	public Node_AccessPointP getPlugin();
}
