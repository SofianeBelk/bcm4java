package bcm;

import bcm.node.accesspoint.components.Node_AccessPoint;
import bcm.node.routing.components.Node_Routing;
import bcm.node.terminal.components.Node_Terminal;
import bcm.nodeWithPlugin.accesspoint.components.PC_Terminal;
import bcm.nodeWithPlugin.routing.components.Ordinateur;
import bcm.nodeWithPlugin.terminal.components.Tablette;
import bcm.registration.component.GestionnaireReseau;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;

public class DistributedCVM extends AbstractDistributedCVM{
	
	protected static final String NodeTerminal_JVM_URI = "NodeTerminaljvm";
	protected static final String NodeRouting_JVM_URI = "NodeRoutingjvm";
	protected static final String NodeAccessPoint_JVM_URI = "NodeAccessPointjvm";
	protected static final String GestionnaireReseau_JVM_URI = "GestionnaireReseaujvm";

	protected String ntURI1 = "";
	protected String nrURI1 = "";
	protected String nacURI1 = "";
	protected String gesURI = "";
	
	public DistributedCVM(String[] args) throws Exception {
		super(args);
	}

	@Override
	public void instantiateAndPublish()  throws Exception {
		
		if(AbstractCVM.getThisJVMURI().equals(GestionnaireReseau_JVM_URI)) { 
			
			gesURI = AbstractComponent.createComponent(GestionnaireReseau.class.getCanonicalName(), new Object[]{});
			
		}else {
		
		if(AbstractCVM.getThisJVMURI().equals(NodeTerminal_JVM_URI)) {
				ntURI1=AbstractComponent.createComponent(
    					Tablette.class.getCanonicalName(),
    					new Object[]{}) ;
						//AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
			
		}else {
			if(AbstractCVM.getThisJVMURI().equals(NodeRouting_JVM_URI)) {
				nrURI1 = AbstractComponent.createComponent(
    					Ordinateur.class.getCanonicalName(),
    					new Object[]{}); 
						//AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
				nrURI1 = AbstractComponent.createComponent(
    					Ordinateur.class.getCanonicalName(),
    					new Object[]{});
						//AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});
				nrURI1 = AbstractComponent.createComponent(
    					Ordinateur.class.getCanonicalName(),
    					new Object[]{});
						//AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});

			}else {
				if(AbstractCVM.getThisJVMURI().equals(NodeAccessPoint_JVM_URI)) {
					nacURI1 = AbstractComponent.createComponent(
	    					PC_Terminal.class.getCanonicalName(),
	    					new Object[]{});
							
					//AbstractComponent.createComponent(Node_AccessPoint.class.getCanonicalName(), new Object[]{});

				}else {
					
						System.out.println("JVM URI Inconnu : "+AbstractCVM.getThisJVMURI());
					}
				}
			}
		}
		super.instantiateAndPublish();

	}
	
	@Override
	public void interconnect() throws Exception {
		if(AbstractCVM.getThisJVMURI().equals(NodeTerminal_JVM_URI)) {
			//this.doPortConnection(ntURI1, Node_Terminal.Node_Terminal_URI, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());
		}else {
			if(AbstractCVM.getThisJVMURI().equals(NodeRouting_JVM_URI)) {
				//this.doPortConnection(nrURI1, Node_Routing.Node_Routing_URI, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());

			}else {
				if(AbstractCVM.getThisJVMURI().equals(NodeAccessPoint_JVM_URI)) {
					//this.doPortConnection(nacURI1, Node_AccessPoint.Node_AccessPoint_URI, GestionnaireReseau.GS_URI, NodeConnector.class.getCanonicalName());

				}else {
					if(AbstractCVM.getThisJVMURI().equals(GestionnaireReseau_JVM_URI)) {
						
						
				}else {
						System.out.println("JVM URI Inconnu : "+AbstractCVM.getThisJVMURI());
					}
				}
			}
		}
		super.interconnect();

	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DistributedCVM dcvm = new DistributedCVM(args);
			dcvm.startStandardLifeCycle(30000L);
			Thread.sleep(50000L);
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
