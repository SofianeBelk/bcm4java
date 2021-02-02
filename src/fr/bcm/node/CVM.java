package fr.bcm.node;

import fr.bcm.node.components.Node_Terminal;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

	public CVM() throws Exception {

	}
	
	@Override
	public void deploy() throws Exception{
		AbstractComponent.createComponent(Node_Terminal.class.getCanonicalName(), new Object[]{});
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
