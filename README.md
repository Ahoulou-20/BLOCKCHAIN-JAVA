### README pour l'application Blockchain en Java  

---

## **Introduction**  
Ce projet est une application de Blockchain développée en **Java** utilisant **IntelliJ IDEA** comme IDE. L'application implémente un serveur Blockchain et un client qui interagit avec celui-ci via des sockets. Elle inclut une interface graphique pour le client permettant d'effectuer des opérations sur la Blockchain.  

---

## **Fonctionnalités**  
- **Serveur Blockchain** :  
  - Stocke la Blockchain.  
  - Gère les connexions des clients et affiche les connexions actives.  
- **Client GUI** :  
  - Affiche une interface graphique permettant :  
    - De visualiser la Blockchain.  
    - D'ajouter des blocs.  
    - De consulter les données stockées.  

---

## **Prérequis**  
1. **JDK 17+** installé.  
2. **IntelliJ IDEA** (Community ou Ultimate).  
3. Bibliothèques nécessaires ajoutées au projet (par exemple : JavaFX pour l'interface graphique).  
4. Une connexion réseau pour permettre la communication via sockets.  

---

## **Structure du projet**  
Le projet est organisé en plusieurs packages :  
- `blockchain` : Contient la logique de la Blockchain (ajout de blocs, validation).  
- `server` : Gère le serveur et les connexions des clients.  
- `client` : Gère le client et son interface graphique (JavaFX).  

---

## **Procédure d'exécution**  

### Étape 1 : Cloner le projet  
Cloner ou télécharger le projet depuis votre dépôt GitHub ou le fichier zip.

```bash
git clone https://github.com/Ahoulou-20/BLOCKCHAIN-JAVA.git
```

### Étape 2 : Ouvrir le projet dans IntelliJ IDEA  
1. Ouvrir IntelliJ IDEA.  
2. Sélectionner **File > Open** et choisir le dossier du projet.  
3. Attendre que IntelliJ importe et configure le projet.  

### Étape 3 : Configurer JavaFX  
1. Télécharger JavaFX depuis [gluonhq.com](https://gluonhq.com/products/javafx/).  
2. Ajouter le chemin de la bibliothèque dans **File > Project Structure > Libraries**.  
3. Ajouter les arguments VM pour JavaFX :  
   - Aller dans **Run > Edit Configurations**.  
   - Ajouter les arguments suivants dans `VM options` :  
     ```bash
     --module-path <chemin_vers_JavaFX> --add-modules javafx.controls,javafx.fxml
     ```

### Étape 4 : Exécuter le serveur  
1. Aller dans le fichier `ServerMain.java`.  
2. Lancer l'application avec le bouton **Run**.  

### Étape 5 : Exécuter le client  
1. Aller dans le fichier `ClientMain.java`.  
2. Lancer l'application avec le bouton **Run**.  

---

## **Utilisation**  
1. Lancez le serveur pour initialiser la Blockchain et écouter les connexions des clients.  
2. Lancez plusieurs clients pour interagir avec le serveur via l'interface graphique.  
3. Ajoutez des blocs, visualisez la Blockchain, ou consultez les transactions.  

---

## **Contributions**  
Les contributions sont les bienvenues ! N'hésitez pas à soumettre des pull requests ou à signaler des problèmes.  

---

## **Licence**  
Ce projet est sous licence (Li@TrX).
