# BIMIOT

## Présentation
Le projet consiste à lier le monde de l’IOT (Internet Of Things) et celui du BIM (Building information modeling). Concrètement, le but est d’afficher les dernières valeurs reçues d’un capteur IOT sur un bâtiment provenant du BIM. L’affichage peut se faire à l’aide d’un logiciel BIM ou bien d’un viewer BIM, le principal étant de pouvoir actualiser régulièrement, environ toutes les minutes, ces informations sur le logiciel de l’utilisateur.

Les données IOT ne sont pas fournies par de réels capteurs, il faut donc utiliser ou élaborer un simulateur de données IOT. Cet outil permettra de simuler une journée de données IOT provenant de différents capteurs fictifs à partir d’une certaine configuration. Ces données peuvent être de n’importe quelle nature : température, présence de personnes, luminosité …


## Simulateur
  * Il faudra lancer un simulateur à côté. Le simulateur en question peut soit être en localhost soit hébergé.
  
  * Un cas d'utilisation permet de changer l'adresse et le port du simulateur utilisé via l'interface graphique ou via de la swagger dont le lien figure plus loin.

  * Un simulateur est présent dans le repository du projet qui _IoT-data-simulator-1.1.0_. Pour lancé le simulateur, il suffit de se placer dans le répertoire du simulateur via la command __`cd`__.
Puis il faut exécuter la commande __`docker-compose up`__.

## Lancement de BIMIOT
  * Pour build l'application, par exemple en cas de changement dans le code __`docker-compose build`__ avant __`docker-compose up`__
  
![docker-compose build](https://user-images.githubusercontent.com/45150352/223394549-b37f513a-544b-4452-b836-302ff5f011c5.png)


  * Pour démarrer l'application _BIMIOT_ __`docker-compose up`__
  
![docker-compose up](https://user-images.githubusercontent.com/45150352/223390863-6a126171-8457-41a0-b087-753d466c96a6.png)



## URLs
  * [API BIMIOT](http://localhost/swagger-ui/#/)
  
![swagger-ui](https://user-images.githubusercontent.com/45150352/223388428-8793554f-27b3-4b44-b15a-b529bf56929f.png)

  * [Interface graphique](http://localhost)
  
![homepage-ui](https://user-images.githubusercontent.com/45150352/223390301-44f2f818-1285-4788-8d05-5b94f87ebacc.png)

  
![simulation-ui](https://user-images.githubusercontent.com/45150352/223389844-d386eb0c-57c6-4c4f-b809-0ed27b6f012a.png)

  * [Simulateur fourni]() 

  * [Base de données MongoDB](http://localhost:27020)
    * No auth
    
Il n'y pas d'interface graphique dans notre application permettant de visualiser le contenu de la base de données. Mais il possible d'utiliser une application tiers qui vous permet de visualiser les données stockées via l'URL qui suit : **mongodb://localhost:27020**
