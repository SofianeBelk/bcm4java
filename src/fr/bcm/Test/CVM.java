package fr.bcm.Test;

import fr.bcm.Test.GestionnaireResTest.component.cTestReseau;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

	public CVM() throws Exception {
		
	}
	
	public void deploy() throws Exception{

		AbstractComponent.createComponent(GestionnaireReseau.class.getCanonicalName(), new Object[]{});	
		AbstractComponent.createComponent(cTestReseau.class.getCanonicalName(), new Object[]{});
		super.deploy();
	}
	
	public static void main(String[] args) {
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(10000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
