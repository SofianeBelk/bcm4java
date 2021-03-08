package fr.bcm.node.terminal.ports;

import java.util.Set;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.terminal.interfaces.Node_TerminalCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class Node_TerminalInboundPortPlugin extends AbstractInboundPort implements Node_TerminalCI{
	private static final long serialVersionUID = 1L;

	
	public Node_TerminalInboundPortPlugin(String uri, ComponentI owner)
			throws Exception {
		super(uri, CommunicationCI.class, owner);
	}


	@Override
	public Set<ConnectionInfoI> registerTerminalNode(NodeAddressI address, String communicationInboundPortURI,
			PositionI initialPosition, double initialRange) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void unregister(AddressI address) throws Exception {
		// TODO Auto-generated method stub
		
	}


	


}
