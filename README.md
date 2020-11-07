# Server FTP

FTP veut dire « File Transfert Protocol » ou Protocole de transfert de Fichier.

C’est donc un langage qui va permettre l’échange de fichiers entre deux ordinateurs, et plus exactement entre un serveur et un client.
On parle alors de :

    Serveur FTP
    Client FTP

Voici les différentes commandes que nous avons établis : 
- USER
- PASS
- PASV
- STOR 
- PORT 
- MLSD
- RETR
- SYST
- FEAT
- PWD
- TYPE
- LIST
- CWD
- MKD
- NLST
- DELE
- RMD
- SIZE
- MDTM
- RNFR
- RNTO
- CDUP
- QUIT

Descriptif des différentes commandes : https://fr.wikipedia.org/wiki/Liste_des_commandes_ftp

Vous avez plusieurs possibilités pour teste le projet, soit vous téléchargez un client FTP comme Filezilla ou Windows ... ou soit vous utilisez netcat à partir du terminal. Vous devez impérativement rentre un nom (USER) et un mot de passe (PASS). Une fois lancer le code vous aurez un dossier "ROOT" qui sera crée dans le dossier où se trouve le projet (dans eclipse-workspace), puis vous trouverai un autre répertoire dans ce répertoire ROOT avec le nom que vous avez saisi pour le USER. N'hesitez pas à mettre des fichiers comme une image, un texte, un répertoire ... dans le répertoire USER afin de tester les commandes.

Avec Netcat sur Terminal : 
	- Lancez le code (en utilissant le .jar ou copié/collé sur eclipse) 
	- Puis tapez nc localhost 1234 dans votre terminal afin de se connecter sur netcat
        - Et enfin tapez les commandes que vous voulez teste comme en TP en ayant tout d'abord rentre un user et un pass  


Avec Filezilla Client FTP : 
	- Téléchargez le logiciel : https://filezilla-project.org/download.php?type=client
	- Tout d'abord nous devons configurer le client FTP : 
	  Allez jusqu'à l'étape 5 : http://filezilla.fr/tuto-rapide/
	
	 Mettez dans les champs : 

	 Hôte : 127.0.0.1         Port: 1234 ou 21
	 Protocole : FTP- Protocole de Transfert de Fichiers
         Chiffrement : Connexion FTP simple (non sécurisé)
	 Type d'authentification : Demande le mot de passe 

	 Dans l'onglet Paramètres de transfert : Testez les differents mode de transfert (Actif ou passif ou par default)

	 Dans l'onglet Jeu de caractères : Cochez "Forcer l'UTF8"

	 Et enfin cliquez sur "Valide".

         - Lancez le code
         - Sur le même site passez directement à l'étape 14.
