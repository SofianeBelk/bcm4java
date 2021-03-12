package fr.bcm.nodeWithPlugin;


import fr.bcm.nodeWithPlugin.routing.components.Node_Routing;
import fr.bcm.nodeWithPlugin.terminal.components.Node_Terminal;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

public   static String URI__BROKER_PUBLISHER_MANAGEMENT = "URI_BROKER_PUBLISHER_MANAGEMENT";
	
	public CVM() throws Exception {}
	
	@Override
	public void deploy() throws Exception {
		String uri = AbstractComponent.createComponent(
				GestionnaireReseau.class.getCanonicalName(),
				new Object[]{}) ;
		this.toggleTracing(uri);
		assert	this.isDeployedComponent(uri) ;
		
		uri = AbstractComponent.createComponent(
				Node_Terminal.class.getCanonicalName(),
				new Object[]{}) ;
		assert	this.isDeployedComponent(uri);
		
		
		String nrURI1=AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
		String nrURI2=AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
		
		super.deploy();
	}
	
	
	public static void main(String[] args) {
		try {
			CVM c = new CVM() ;
			c.startStandardLifeCycle(3000L) ;
			Thread.sleep(10000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	
}
