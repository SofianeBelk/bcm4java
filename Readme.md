Veuillez noter que le package "node" contenu dans src/bcm n'est pas � jour. Pour consulter le code le plus � jour, merci de consulter les noeuds plugins pr�sent dans src/bcm/nodeWithPlugin

// Utilisation de la CVM (Audit 1)
Dans src/bcm/CVM.java se trouve la CVM utilis� lors de l'audit 1.

// Utilisation de la CVM (Audit 4)
dans src/bcm/nodeWithPlugin/CVM.java se trouve la CVM la plus � jour avec les noeuds les plus � jour.
Cette CVM cr�ait : 
- 5 noeuds de routing
- 2 noeuds d'AccessPoint
- 2 noeuds terminaux
Toutes les X secondes, des messages sont envoy�s � partir des noeuds de routings, ils seront transmit par table de routing ou par innondation selon les connections faites.
Certains noeuds de routing enverrons des messages destin�s au r�seau classique, d'autres noeuds de routing enverrons des messages � des noeuds al�atoire pr�sent dans le r�seau.
Les noeuds de routing ping de temps � autre leur voisins, et mette � jour leur table de routing, ils transmettent �galement de temps � autre leur table de routings � leur voisins.
Les noeuds terminaux ont une chance de se deconnect� (5% de chance de deconnection, toutes les secondes)

// Utilisation de la distributed CVM
	Pour lancer les 4 JVM, il faut modifer les permissions qui se trouve
dans le fichier dcvm.policy, en indiquant les bons chemins pour la jvm.

Ensuite, il faut ouvrir d'abord 6 terminal dans le dossier 
déploiment qui se trouve dans le projet bcm4java, et éxécuter ces commandes :

			Terminal 1 ==> lancez la commande ./start-gregistry
			Terminal 2 ==> lancez la commande ./start-cyclicbarrier
			Terminal 3 ==> lancez la commande ./start-dcvm GestionnaireReseaujvm
			Terminal 4 ==> lancez la commande ./start-dcvm NodeTerminaljvm
			Terminal 5 ==> lancez la commande ./start-dcvm NoteRoutingjvm
			Terminal 6 ==> lancez la commande ./start-dcvm NodeAcessPointjvm
			
			

			
			
			
			