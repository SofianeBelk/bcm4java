package fr.bcm.connexion.classes;

import fr.bcm.connexion.interfaces.CommunicationCI;
import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingCommOutboundPort;
import fr.bcm.node.routing.ports.Node_RoutingRoutingOutboundPort;
import fr.bcm.utils.address.interfaces.AddressI;
import fr.bcm.utils.nodeInfo.interfaces.PositionI;

public class ConnectionInformation{
	private AddressI address;
	private String communicationInboundPortURI;
	private String routingInboundPortURI;
	private Node_RoutingCommOutboundPort nrcop;
	private Node_RoutingRoutingOutboundPort nrrop;
	private Node_AccessPointCommOutboundPort napcop;
	private boolean isRouting = false;
	private boolean isAccessPoint = false;
	
	public ConnectionInformation(AddressI address){
		this.address = address;
	}
	
	
	public AddressI getAddress(){
		return address;
	}
	
	
	public String getRoutingInboundPortURI() {
		return routingInboundPortURI;
	}
	
	public Node_RoutingCommOutboundPort getNrcop() {
		return nrcop;
	}


	public void setNrcop(Node_RoutingCommOutboundPort nrcop) {
		this.nrcop = nrcop;
	}


	public Node_RoutingRoutingOutboundPort getNrrop() {
		return nrrop;
	}


	public void setNrrop(Node_RoutingRoutingOutboundPort nrrop) {
		this.nrrop = nrrop;
	}


	public void setCommunicationInboundPortURI(String communicationInboundPortURI) {
		this.communicationInboundPortURI = communicationInboundPortURI;
	}


	public void setRoutingInboundPortURI(String routingInboundPortURI) {
		this.routingInboundPortURI = routingInboundPortURI;
	}

	public String getCommunicationInboundPortURI() {
		return communicationInboundPortURI;
	}

	public boolean getisRouting() {
		return isRouting;
	}

	public void setisRouting(boolean isRouting) {
		this.isRouting = isRouting;
	}
	
	public boolean getisAccessPoint() {
		return isAccessPoint;
	}
	

	public void setisAccessPoint(boolean isAccessPoint) {
		this.isAccessPoint = isAccessPoint ;
	}

	public void setAddress(AddressI a) {
		this.address = a;
	}

	public void setcommunicationInboundPortURI(String a) {
		this.communicationInboundPortURI = a;
	}


	public Node_AccessPointCommOutboundPort getNapcop() {
		return napcop;
	}


	public void setNapcop(Node_AccessPointCommOutboundPort napcop) {
		this.napcop = napcop;
	}	
	
}
