package fr.bcm.connexion.ports;


import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.address.interfaces.NodeAddressI;
import fr.bcm.utils.message.interfaces.MessageI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class CommunicationInboundPort extends AbstractInboundPort implements CommunicationCI{

	protected final int	executorIndex ;
	private static final long serialVersionUID = 1L;
	
	public	CommunicationInboundPort(int executorIndex, ComponentI owner) throws Exception
		{
			super(CommunicationCI.class, owner);
			assert	owner.validExecutorServiceIndex(executorIndex) ;
			this.executorIndex = executorIndex ;
		}

	@Override
	public void connect(NodeAddressI address, String communicationInboundPortURI) {
		// TODO Auto-generated method stub
		 try {
			this.getOwner().handleRequest(
					executorIndex,			// identifies the pool of threads to be used
					new AbstractComponent.AbstractService() {

						@Override
						public Void call() throws Exception {
							// TODO Auto-generated method stub
							return null;
						}
						
						}
					) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void connectRouting(NodeAddressI address, String communicationInboundPortURI, String routingInboundPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transmitMessage(MessageI m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasRouteFor(AddressI address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void ping() {
		// TODO Auto-generated method stub
		
	}

}
