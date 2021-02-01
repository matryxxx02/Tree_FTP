## Tree FTP 

**Auteur** : Nicolas Fernandes, nicolas.fernandes.etu@univ-lille.fr <br>
01/02/21

### Presentation du projet : 

**Tree FTP** est un programme codé en Java qui permet de lister le contenu d'un serveur FTP distant grâce au protocole [File Transfer Protocole](https://fr.wikipedia.org/wiki/File_Transfer_Protocol) (FTP). 
Le listing du serveur se base sur la [commande](http://www.delafond.org/traducmanfr/man/man1/tree.1.html) `tree` a quelques détails prêt.
Le logiciel peut etre appelé avec des paramètres, comme la profondeur a laquelle nous voulons lister les fichiers,
du serveur ou l'affichage des fichiers cachés.

*(Aperçu du resultat de treeFTP)*
```
├── cdimage
│    ├── bionic
│    │    ├── daily-live
│    │    │    ├── 20200805
│    │    │    ├── 20200806
│    │    │    ├── 20200806.1
│    │    │    ├── 20200806.1
│    │    │    └── 20200806.1
│    │    └── ../source/bionic/source
│    ├── cdicons
│    │    ├── folder.png
│    │    ├── img.png
│    │    ├── iso.png
│    │    ├── jigdo.png
│    │    ├── kubuntu-folder.png
```

### Utilisation :

Pour lancer l'application il suffit de se placer dans le répertoire où se trouve le jar,
et de taper :
```
    java -jar TreeFtp.jar NomDuServerFTP
```
Certains serveurs FTP permettent la connexion en tant que visiteur, cependant, d'autre peuvent nécessiter un login et un mot de passe.
Dans ce cas il faut écrire le login et le mot de passe à la suite du nom du serveur :
```
    java -jar TreeFtp.jar NomDuServerFTP VotreLogin VotreMotDePasse
```

Pour la profondeur souhaitée, il est nécessaire d'ajouter l'option `-p={profondeur}`,
si vous voulez afficher l'arborescence sans limite de profondeur il suffit d'omettre ce paramètre. <br>
**ATTENTION** si votre serveur FTP contient beaucoup de fichier et que vous ne limitez pas la profondeur, le resultat du programme peut-être très long.

```
    java -jar TreeFtp.jar NomDuServerFTP VotreLogin VotreMotDePasse -p=2
```


Vous pouvez activer le l'affichage des fichiers cachés avec l'option `-a` :
```
    java -jar TreeFtp.jar NomDuServerFTP VotreLogin VotreMotDePasse -p=2 -a
```

### Architecture :

### Lister et expliquer la gestion d'erreur :

### Code samples :







-- consignes pour le read me :

Le readme commence par titre, auteur et date. Il est encodé en UTF-8.
Simulation d'un Opérateur de Téléphonie en Java
Romain Rouvoy, John Doe
21/12/21
Introduction 
Puis vient un paragraphe d'introduction du logiciel: but, concepts et algorithmes. 
Par exemple: 
Ce logiciel simule l'évolution de la masse d'un trou noir. L'algorithme utilisé est celui de Jane Doe (<lien>)

Architecture 
Lister et expliquer ici les interfaces, les classes abstraites et les méthodes polymorphiques

Lister et expliquer la gestion d'erreur:
détection d'erreurs throw new X
résolution d'erreurs catch(Y).
Code Samples 
Ensuite, le readme liste 5 exemples de code (code samples) intéressants, par exemple:
une méthode contenant un algorithme intéressant,
une liste de classes impliquées dans un design pattern,
une jolie optimisation.
Un code sample doit être introduit en 2 phrases maximales, l'essentiel des explications vient ensuite dans des commentaires en ligne (//). 

 Un code sample ne doit pas être long (<20 lignes) ne doit pas être trop profond (<3 niveaux d'indentations), peut contenir des "trois petits points" ... pour abstraire les détails.
Les code samples du readme sont indentés avec 2 espaces.

Style du README
Les explications utilisent la troisième personne ("la fonction renvoie"), parfois le passif ("le paramètre est lu par ...."). Les "je"/"vous"/"nous" sont proscrits. Evitez les phrases longues, les fautes d'orthographe et de grammaire.

Format du README
Le readme est encodé en UTF-8. Il est recommandé d'utiliser la syntaxe Markdown ou RST.