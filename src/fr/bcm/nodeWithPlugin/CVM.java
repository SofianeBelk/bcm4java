package fr.bcm.nodeWithPlugin;

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
				new Object[]{uri,""}) ;
		assert	this.isDeployedComponent(uri);
		
		
		
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
