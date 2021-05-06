package fr.bcm;

import fr.bcm.node.accesspoint.components.Node_AccessPoint;
import fr.bcm.node.routing.components.Node_Routing;
import fr.bcm.node.terminal.components.Node_Terminal;
import fr.bcm.registration.component.GestionnaireReseau;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;

public class DistributedCVM extends AbstractDistributedCVM{
	
	protected static final String NodeTerminal_JVM_URI = "NodeTerminaljvm";
	protected static final String NodeRouting_JVM_URI = "NodeRoutingjvm";
	protected static final String NodeAccessPoint_JVM_URI = "NodeAccessPointjvm";
	protected static final String GestionnaireReseau_JVM_URI = "GestionnaireReseaujvm";

	public DistributedCVM(String[] args) throws Exception {
		super(args);
	}

	@Override
	public void instantiateAndPublish() throws Exception {
		if(AbstractCVM.getThisJVMURI().equals(NodeTerminal_JVM_URI)) {
			
		}else {
			if(AbstractCVM.getThisJVMURI().equals(NodeRouting_JVM_URI)) {
				
			}else {
				if(AbstractCVM.getThisJVMURI().equals(NodeAccessPoint_JVM_URI)) {
					
				}else {
					if(AbstractCVM.getThisJVMURI().equals(GestionnaireReseau_JVM_URI)) {
						
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
	AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
		}else {
			if(AbstractCVM.getThisJVMURI().equals(NodeRouting_JVM_URI)) {
				AbstractComponent.createComponent(Node_Routing.class.getCanonicalName(), new Object[]{});

			}else {
				if(AbstractCVM.getThisJVMURI().equals(NodeAccessPoint_JVM_URI)) {
					AbstractComponent.createComponent(Node_AccessPoint.class.getCanonicalName(), new Object[]{});

				}else {
					if(AbstractCVM.getThisJVMURI().equals(GestionnaireReseau_JVM_URI)) {
						AbstractComponent.createComponent(GestionnaireReseau.class.getCanonicalName(), new Object[]{});

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
			Thread.sleep(5000L);
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
