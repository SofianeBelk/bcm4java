package fr.bcm.node;

import fr.bcm.node.components.Node_Ephemeral;
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
		String neURI=AbstractComponent.createComponent(Node_Ephemeral.class.getCanonicalName(), new Object[]{});
		AbstractComponent.createComponent(GestionnaireReseau.class.getCanonicalName(), new Object[]{});
		//la connexion du coté gauche 
		this.doPortConnection(ntURI, Node_Terminal.ntop_uri, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());
		/*la connexion du coté droit "je crois que le composant 
		gestionnaire reseau a besoin d'une interface required pour se connecter a l'autre composant"*/
		this.doPortConnection(neURI, Node_Ephemeral.neop_uri, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());
		super.deploy();
	}
	
	public static void main(String[] args) {
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(5000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
