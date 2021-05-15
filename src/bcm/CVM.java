package bcm;

import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.connectors.NodeConnector;
import bcm.node.routing.components.Node_Routing;
import bcm.node.terminal.components.Node_Terminal;
import bcm.registration.component.GestionnaireReseau;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

	public CVM() throws Exception {

	}
	
	@Override
	/*
	 *Connexion entre le composant terminal au composant Gestionnaire Reseau
	 *Connexion entre le composant Ephemeral au composant Gestionnaire Reseau  
	 **/
	public void deploy() throws Exception{

		AbstractComponent.createComponent(GestionnaireReseau.class.getCanonicalName(), new Object[]{});

	
		// String nacURI1=AbstractComponent.createComponent(Node_AccessPoint.class.getCanonicalName(), new Object[]{});
		// String nacURI2=AbstractComponent.createComponent(Node_AccessPoint.class.getCanonicalName(), new Object[]{});
		
		String ntURI1=AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
		// String ntURI2=AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
		
		AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
		// AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
		// AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
		
		
		super.deploy();
	}
	
	public static void main(String[] args) {
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(30000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
