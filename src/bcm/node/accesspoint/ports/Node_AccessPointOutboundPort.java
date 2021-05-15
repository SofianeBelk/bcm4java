package bcm.node.accesspoint.ports;

import java.util.Set;

import bcm.connexion.interfaces.ConnectionInfoI;
import bcm.node.accesspoint.interfaces.Node_AccessPointCI;
import bcm.node.terminal.interfaces.Node_TerminalCI;
import bcm.registration.interfaces.RegistrationCI;
import bcm.utils.address.classes.NodeAddress;
import bcm.utils.address.interfaces.AddressI;
import bcm.utils.address.interfaces.NodeAddressI;
import bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * classe du port sortant "Node_AccessPointOutboundPort"
 * @author Nguyen, belkhir
 *
 */
public class Node_AccessPointOutboundPort extends AbstractOutboundPort implements Node_AccessPointCI{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur du port sortant
	 * @param owner : le composant utilisant ce port
	 * @throws Exception
	 */
	public Node_AccessPointOutboundPort(ComponentI owner) throws Exception {
		super(Node_AccessPointCI.class, owner);
	}
	
	/** appel au service d'enregistrement  d'un Node_AccessPoint au prés du gestionnaire  */
	@Override
	public Set<ConnectionInfoI> registerAccessPoint(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange, String routingInboundPortURI) throws Exception {
		// TODO Auto-generated method stub
		return ((Node_AccessPointCI)this.getConnector()).registerAccessPoint(
				address, 
				communicationInboundPortURI, 
				initialPosition, 
				initialRange,
				routingInboundPortURI);
	}

	/** appel au service de dérengistrement  d'un Node_AccessPoint au prés du gestionnaire  */
	@Override
	public void unregister(AddressI address) throws Exception {
		((Node_AccessPointCI)this.getConnector()).unregister(address);
		
	}





	

	

}
