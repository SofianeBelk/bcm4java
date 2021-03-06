package fr.bcm.registration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import fr.bcm.connexion.interfaces.ConnectionInfoI;
import fr.bcm.node.accesspoint.ports.Node_AccessPointCommInboundPort;
import fr.bcm.node.terminal.ports.Node_TerminalInboundPort;
import fr.bcm.utils.address.classes.NodeAddress;
import fr.bcm.utils.nodeInfo.classes.Position;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

public class GestionnnaireReseauTest {

    GestionnaireReseau gr;
    Set<ConnectionInfoI> portee1;
    Set<ConnectionInfoI> portee2;
    NodeAddress a =new NodeAddress();
    NodeAddress b =new NodeAddress();

    
   
    public void initialisation() throws Exception {
    	gr = new GestionnaireReseau();
        portee1=gr.registerTerminalNode(a, Node_TerminalInboundPort.generatePortURI(), new Position(10,10), 20.00, false);
        portee2=gr.registerAccessPoint(b,Node_AccessPointCommInboundPort.generatePortURI(), new Position(11,8), 25.00, "");
        gr.start();
    }
    
    @Test
    public void test1() throws Exception {
    	
    	initialisation();
        //start
        assertTrue(gr.isStarted());
        //verification des tailles
        assertEquals(2,gr.mySet.size());
        assertEquals(0,portee1.size());
        assertEquals(1,portee2.size());
        
        //unregister
        gr.unregister(a);
        assertEquals(1,gr.mySet.size());
        
        //unregister
        gr.unregister(b);
        assertEquals(0,gr.mySet.size());
        finalisation();
    }
    
    @Test
    public void test2() throws Exception{
    	assertTrue(true);
    }

    
    public void finalisation() {
        try{
            gr.finalise();
        }catch(Exception e) {
            System.out.println("");
        }
    }
}
