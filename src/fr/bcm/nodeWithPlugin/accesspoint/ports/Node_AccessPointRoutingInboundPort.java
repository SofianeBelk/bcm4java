package fr.bcm.nodeWithPlugin.accesspoint.ports;

import java.util.Set;


import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.node.routing.interfaces.RoutingCI;
import fr.bcm.nodeWithPlugin.accesspoint.interfaces.Node_AccessPointI;

import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.routing.interfaces.RouteInfoI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * Classe Node_AccessPointRoutingInboundPort definissant le comportement du port entrant obeissant a l'interface de RoutingCI
 * @author Nguyen, belkhir
 *
 */

public class Node_AccessPointRoutingInboundPort extends AbstractInboundPort implements RoutingCI{

	private static final long serialVersionUID = 1L;
	private String pluginURI;
	
	/**
	 * Constructeur du port entrant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_AccessPointRoutingInboundPort(ComponentI owner) throws Exception {
		super(RoutingCI.class, owner);
	}

	/**
	 * une variante du premiéer constructeur 
	 * @param owner : le composant utilisant ce port
	 * @param pluginURI : l'URI du plugin
	 * @throws Exception
	 */
	public Node_AccessPointRoutingInboundPort(ComponentI owner, String pluginURI) throws Exception {
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
		this.getOwner().runTask(			
			nr -> {
				try {
					((Node_AccessPointI)nr).getPlugin().updateRouting(neighbour, routes);
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
		this.getOwner().runTask(				
				nr -> {
					try {
						((Node_AccessPointI)nr).getPlugin().updateAccessPoint(neighbour, numberOfHops);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			);
	}



	

	

}
