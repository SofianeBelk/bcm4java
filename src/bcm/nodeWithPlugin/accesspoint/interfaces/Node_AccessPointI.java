package bcm.nodeWithPlugin.accesspoint.interfaces;

import bcm.nodeWithPlugin.accesspoint.components.PC_Terminal;
import bcm.nodeWithPlugin.accesspoint.plugins.Node_AccessPointP;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;

public interface Node_AccessPointI {
	public Node_AccessPointP getPlugin();
}
