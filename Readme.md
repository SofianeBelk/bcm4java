Veuillez noter que le package "node" contenu dans src/bcm n'est pas à jour. Pour consulter le code le plus à jour, merci de consulter les noeuds plugins présent dans src/bcm/nodeWithPlugin

// Utilisation de la CVM (Audit 1)
Dans src/bcm/CVM.java se trouve la CVM utilisé lors de l'audit 1.

// Utilisation de la CVM (Audit 4)
dans src/bcm/nodeWithPlugin/CVM.java se trouve la CVM la plus à jour avec les noeuds les plus à jour.
Cette CVM créait : 
- 5 noeuds de routing
- 2 noeuds d'AccessPoint
- 2 noeuds terminaux
Toutes les X secondes, des messages sont envoyés à partir des noeuds de routings, ils seront transmit par table de routing ou par innondation selon les connections faites.
Certains noeuds de routing enverrons des messages destinés au réseau classique, d'autres noeuds de routing enverrons des messages à des noeuds aléatoire présent dans le réseau.
Les noeuds de routing ping de temps à autre leur voisins, et mette à jour leur table de routing, ils transmettent également de temps à autre leur table de routings à leur voisins.
Les noeuds terminaux ont une chance de se deconnecté (5% de chance de deconnection, toutes les secondes)

// Utilisation de la distributed CVM
	Pour lancer les 4 JVM, il faut modifer les permissions qui se trouve
dans le fichier dcvm.policy, en indiquant les bons chemins pour la jvm.

Ensuite, il faut ouvrir d'abord 6 terminal dans le dossier 
dÃ©ploiment qui se trouve dans le projet bcm4java, et Ã©xÃ©cuter ces commandes :

			Terminal 1 ==> lancez la commande ./start-gregistry
			Terminal 2 ==> lancez la commande ./start-cyclicbarrier
			Terminal 3 ==> lancez la commande ./start-dcvm GestionnaireReseaujvm
			Terminal 4 ==> lancez la commande ./start-dcvm NodeTerminaljvm
			Terminal 5 ==> lancez la commande ./start-dcvm NoteRoutingjvm
			Terminal 6 ==> lancez la commande ./start-dcvm NodeAcessPointjvm
			
			

			
			
			
			