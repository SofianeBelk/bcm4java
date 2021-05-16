package bcm.nodeWithPlugin.routing.ports;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import bcm.connexion.interfaces.CommunicationCI;
import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.routing.interfaces.Node_RoutingCI;
import bcm.node.routing.interfaces.RoutingCI;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.nodeWithPlugin.routing.components.Ordinateur;
import bcm.nodeWithPlugin.routing.interfaces.Node_RoutingI;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.registration.interfaces.RegistrationCI;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * Classe Node_RoutingRoutingInboundPort definissant le comportement du port entrant obeissant a l'interface de RoutingCI
 * @author Nguyen, belkhir
 *
 */
public class Node_RoutingRoutingInboundPort extends AbstractInboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;

	/**
	 * Constructeur du port entrant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_RoutingRoutingInboundPort(ComponentI owner) throws Exception {
		super(RoutingCI.class, owner);
	}
	
	/**
	 * une variante du premiéer constructeur 
	 * @param owner : le composant utilisant ce port
	 * @param pluginURI : l'URI du plugin
	 * @throws Exception
	 */
	public Node_RoutingRoutingInboundPort(ComponentI owner, String pluginURI) throws Exception{
		super(CommunicationCI.class, owner);
		this.pluginURI = pluginURI;
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de mise a jour
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet de mettre a jour de la table de routage  
	 *      si cette route est plus intéressante que la précédente
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void updateRouting(NodeAddressI neighbour, Set<RouteInfoI> routes) throws Exception {
		this.getOwner().runTask(Node_RoutingP.UpdateRouting_URI,				
			nr -> {
				try {
					((Node_RoutingI)nr).getPlugin().updateRouting(neighbour, routes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}

	/**
	 * <p>
	 * 	<strong> 
	 * 		demande de mise a jour
	 * 	</strong>
	 * 	<em> 
	 * 		ce service permet de mettre à jour la route vers le point d’accès le plus proche
	 * 	</em>
	 * </p> 
	 * 
	 **/
	@Override
	public void updateAccessPoint(NodeAddressI neighbour, int numberOfHops) throws Exception{
		this.getOwner().runTask(Node_RoutingP.UpdateAccessPoint_URI,				
				nr -> {
					try {
						((Node_RoutingI)nr).getPlugin().updateAccessPoint(neighbour, numberOfHops);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			);
	}



	

	

}