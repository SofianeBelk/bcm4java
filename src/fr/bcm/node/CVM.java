package fr.bcm.node;

import fr.bcm.node.components.Node_AccessPoint;
import fr.bcm.node.components.Node_Routing;
import fr.bcm.node.components.Node_Terminal;
import fr.bcm.node.connectors.NodeConnector;
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
		String ntURI=AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
		String nacURI=AbstractComponent.createComponent(Node_AccessPoint.class.getCanonicalName(), new Object[]{});
		String nrURI=AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});

		AbstractComponent.createComponent(GestionnaireReseau.class.getCanonicalName(), new Object[]{});
		
		this.doPortConnection(ntURI, Node_Terminal.ntop_uri, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());
		this.doPortConnection(nacURI, Node_AccessPoint.nacop_uri, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());
		this.doPortConnection(nrURI, Node_Routing.nrop_uri, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());
		
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
