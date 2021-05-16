package bcm.nodeWithPlugin;


import java.util.ArrayList;
import java.util.Random;

import bcm.nodeWithPlugin.accesspoint.components.PC_Terminal;
import bcm.nodeWithPlugin.routing.components.Ordinateur;
import bcm.nodeWithPlugin.routing.plugins.Node_RoutingP;
import bcm.nodeWithPlugin.terminal.components.Tablette;
import bcm.registration.component.GestionnaireReseau;
import bcm.utils.message.classes.Message;
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
		
		
		int nb_accesspoint = 2;
		int nb_routing = 5;
		int nb_terminal = 3;
		
		
		// Répartition aléatoire des noeuds pour qu'ils soient a 
		Random rand = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		for(int i=0;i<3;i++) {
			list.add(i);
		}
		
		
		while(list.size() > 0) {
			int index;
			if(list.size() == 1) {
				index = 0;
			}
			else {
				index = rand.nextInt(list.size() - 1);
			}
			
            if(list.get(index) == 0) {
            	if(nb_accesspoint > 0) {
            		uri =AbstractComponent.createComponent(
    					PC_Terminal.class.getCanonicalName(),
    					new Object[]{});
        			assert	this.isDeployedComponent(uri);
        			nb_accesspoint -= 1;
            	}
            	else {
            		list.remove(index);
            	}
            }
            
            if(list.get(index) == 1) {
            	if(nb_routing > 0) {
            		uri= AbstractComponent.createComponent(
        					Ordinateur.class.getCanonicalName(),
        					new Object[]{});
        			assert	this.isDeployedComponent(uri);
        			nb_routing -= 1;
            	}
            	else {
            		list.remove(index);
            	}
            }
            
            if(list.get(index) == 2) {
            	if(nb_terminal > 0) {
            		uri = AbstractComponent.createComponent(
        					Tablette.class.getCanonicalName(),
        					new Object[]{}) ;
        			assert	this.isDeployedComponent(uri);
        			nb_terminal -= 1;
            	}
            	else {
            		list.remove(index);
            	}
            }
        }

		super.deploy();
	}
	
	
	public static void main(String[] args) {
		try {
			CVM c = new CVM() ;
			c.startStandardLifeCycle(20000L) ;
			
			System.out.println("Message sent : " + Message.MessageSent);
			System.out.println("Message received : " + Message.MessageReceived);
			System.out.println("Message lost : " + Message.MessageLost);
			System.out.println("Message duplicated : " + Message.MessageDuplicated);
			
			System.out.println("Transmitted via Innondation : " + Message.TransmittedviaInnondation);
			System.out.println("Transmitted via Routing Table : " + Message.TransmittedviaRoutingTable);
			

			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	
}
