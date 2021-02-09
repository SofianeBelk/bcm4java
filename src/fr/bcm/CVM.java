package fr.bcm;

import fr.bcm.node.accesspoint.components.Node_AccessPoint;
import fr.bcm.node.connectors.NodeConnector;
import fr.bcm.node.routing.components.Node_Routing;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.registration.component.GestionnaireReseau;
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

	
		// String nacURI=AbstractComponent.createComponent(Node_AccessPoint.class.getCanonicalName(), new Object[]{});
		// String nrURI=AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
		String ntURI1=AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
		String ntURI2=AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
		String ntURI3=AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});

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
