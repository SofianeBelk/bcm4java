package fr.bcm.nodeWithPlugin;


import fr.bcm.nodeWithPlugin.accesspoint.components.PC_Terminal;
import fr.bcm.nodeWithPlugin.routing.components.Ordinateur;
import fr.bcm.nodeWithPlugin.terminal.components.Tablette;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

	
	public CVM() throws Exception {}
	
	@Override
	public void deploy() throws Exception {
		String uri = AbstractComponent.createComponent(
				GestionnaireReseau.class.getCanonicalName(),
				new Object[]{}) ;
		this.toggleTracing(uri);
		assert	this.isDeployedComponent(uri) ;
		
		
		/*
		uri =AbstractComponent.createComponent(
				PC_Terminal.class.getCanonicalName(),
				new Object[]{});
		assert	this.isDeployedComponent(uri);
		*/
		
		
		uri = AbstractComponent.createComponent(
				Tablette.class.getCanonicalName(),
				new Object[]{}) ;
		assert	this.isDeployedComponent(uri);
		
		/*
		uri= AbstractComponent.createComponent(
				Ordinateur.class.getCanonicalName(),
				new Object[]{});
		assert	this.isDeployedComponent(uri);
		
		
		uri= AbstractComponent.createComponent(
				Ordinateur.class.getCanonicalName(),
				new Object[]{});
		assert	this.isDeployedComponent(uri);
		*/
		
		uri= AbstractComponent.createComponent(
				Ordinateur.class.getCanonicalName(),
				new Object[]{});
		assert	this.isDeployedComponent(uri);

		super.deploy();
	}
	
	
	public static void main(String[] args) {
		try {
			CVM c = new CVM() ;
			c.startStandardLifeCycle(20000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	
}
