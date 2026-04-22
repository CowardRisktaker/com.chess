# User Stories — Méthode Scrum
## Projet : ChessEngine
**Date** : 2026-04-15

---

## Organisation

Le backlog est découpé en 4 sprints de durée indicative d'une semaine chacun (projet solo). Chaque user story suit le format standard : **"En tant que [rôle], je veux [action] afin de [bénéfice]"**, accompagné de critères d'acceptation vérifiables et d'une estimation en points de complexité (1 = trivial, 2 = facile, 3 = moyen, 5 = complexe, 8 = très complexe).

---

## Product Backlog (toutes les stories)

---

### SPRINT 1 — Fondations techniques et authentification

**Objectif du sprint** : L'utilisateur peut lancer l'application, se connecter avec des identifiants fournis, et accéder à un écran d'accueil minimal. L'architecture MVC est en place.

---

**US-01 — Connexion à la base de données**
> En tant que développeur, je veux que l'application se connecte à MySQL via un fichier de configuration afin que les identifiants ne soient pas écrits en dur dans le code.

Critères d'acceptation :
- Le fichier `config.properties` contient l'URL, le login et le mot de passe de la BDD
- `DAOAcces` lit ce fichier au démarrage via `getResourceAsStream`
- Si la connexion échoue, un message d'erreur s'affiche dans la console (pas un crash silencieux)
- Les identifiants n'apparaissent dans aucun fichier `.java`

Estimation : 2 | MVP associé : M04 | Statut : ❌ À faire

---

**US-02 — Création des comptes mock**
> En tant que joueur, je veux disposer d'un compte utilisateur en base de données afin de pouvoir me connecter à l'application.

Critères d'acceptation :
- Le script SRD crée la table `utilisateur`
- Au moins 2 comptes sont insérés avec des mots de passe hashés BCrypt
- Les mots de passe en clair sont documentés séparément (pas en BDD)

Estimation : 1 | MVP associé : M04 | Statut : ❌ À faire

---

**US-03 — Classe Session**
> En tant que développeur, je veux une classe Session singleton afin que l'utilisateur connecté soit accessible dans toute l'application sans variables statiques dans Main.

Critères d'acceptation :
- `Session.getInstance()` retourne toujours la même instance
- `Session` expose `getUtilisateurConnecte()`, `setUtilisateurConnecte()`, `getStage()`, `setStage()`
- `Main.java` ne contient plus de champs `static` autres que `main()`

Estimation : 1 | MVP associé : M06 | Statut : ❌ À faire

---

**US-04 — Formulaire de connexion**
> En tant que joueur, je veux voir un formulaire de connexion au démarrage de l'application afin de m'authentifier avant d'accéder au jeu.

Critères d'acceptation :
- `VueConnexion` s'affiche au lancement à la place de `VueAccueil`
- La vue contient un champ login, un champ mot de passe masqué, et un bouton "Se connecter"
- La vue ne contient aucune logique d'authentification (délégation au contrôleur)
- La fenêtre a les dimensions définies dans le cahier des charges

Estimation : 2 | MVP associé : M05 | Statut : ❌ À faire

---

**US-05 — Vérification des identifiants**
> En tant que joueur, je veux que mes identifiants soient vérifiés contre la base de données afin que seuls les utilisateurs autorisés accèdent au jeu.

Critères d'acceptation :
- `ControllerConnexion` interroge `DAOUtilisateur.findByLogin(login)`
- Le mot de passe est comparé avec `BCrypt.checkpw()`
- En cas d'échec : un message d'erreur s'affiche dans la vue sans préciser si c'est le login ou le mot de passe qui est faux
- En cas de succès : `Session` est alimentée et `VueAccueil` s'ouvre

Estimation : 3 | MVP associé : M05 | Statut : ❌ À faire

---

**US-06 — Page d'accueil**
> En tant que joueur connecté, je veux voir une page d'accueil avec mon identifiant et les actions disponibles afin de savoir ce que je peux faire.

Critères d'acceptation :
- `VueAccueil` affiche le login du joueur issu de `Session`
- Les boutons "Nouvelle partie", "Reprendre" et "Quitter" sont présents
- "Reprendre" est grisé (`setDisable(true)`) si aucune partie `sauvegardee` n'existe pour ce joueur
- "Quitter" ferme l'application proprement (fermeture de la connexion BDD)

Estimation : 2 | MVP associé : M07 | Statut : ❌ À faire (partiel : `VueAccueil` existe mais sans session ni boutons complets)

---

### SPRINT 2 — Lancer une partie et jouer selon les règles

**Objectif du sprint** : Le joueur peut configurer et lancer une partie, déplacer les pièces selon les règles, et voir l'échec détecté.

---

**US-07 — Configuration d'une partie**
> En tant que joueur, je veux choisir le mode de jeu avant de lancer une partie afin de décider si je joue contre l'ordinateur ou contre un autre joueur.

Critères d'acceptation :
- `VueGame` propose deux options : "Joueur vs Joueur" et "Joueur vs IA"
- Un bouton "Lancer" crée une entrée dans la table `partie` (statut `en_cours`) et ouvre `VueBoard`
- Un bouton "Retour" revient à `VueAccueil` sans créer de partie
- `ControllerGame` gère la logique, pas `VueGame`

Estimation : 2 | MVP associé : M08 | Statut : ❌ À faire

---

**US-08 — Validation des mouvements de chaque pièce**
> En tant que joueur, je veux que seuls les coups légaux soient acceptés afin que les règles des échecs soient respectées.

Critères d'acceptation :
- Chaque classe de pièce (`Pion`, `Tour`, `Cavalier`, `Fou`, `Reine`, `Roi`) implémente `mouvementsValides(Board board)`
- Un clic sur une case illégale est ignoré (pas de déplacement)
- Un coup qui laisserait le propre Roi en échec est refusé
- Les mouvements valides correspondent aux règles standard des échecs (sans roque ni en-passant pour le MVP)

Estimation : 8 | MVP associé : M09 | Statut : ❌ À faire

---

**US-09 — Surbrillance des cases accessibles**
> En tant que joueur, je veux voir les cases où ma pièce peut se rendre après l'avoir sélectionnée afin de jouer plus facilement.

Critères d'acceptation :
- Clic sur une pièce : la case sélectionnée est surlignée (couleur distincte)
- Les cases accessibles sont marquées visuellement (cercle ou teinte différente)
- Les surbrillances disparaissent après le coup ou si l'on clique ailleurs
- La logique de calcul est dans le modèle, pas dans la vue

Estimation : 3 | MVP associé : M10 | Statut : ❌ À faire

---

**US-10 — Détection de l'échec**
> En tant que joueur, je veux être averti quand mon Roi est en échec afin de réagir avant de subir un mat.

Critères d'acceptation :
- Après chaque coup, `RegleEchecs.estEnEchec()` est appelé
- Si le Roi adverse est en échec, un signal visuel apparaît (couleur sur la case du Roi ou message)
- L'application ne permet pas de jouer un coup qui laisse son propre Roi en échec

Estimation : 5 | MVP associé : M11 | Statut : ❌ À faire

---

**US-11 — Détection du mat et fin de partie**
> En tant que joueur, je veux que la partie se termine automatiquement quand un joueur est mat afin que l'issue soit claire.

Critères d'acceptation :
- `RegleEchecs.estMat()` est appelé après chaque coup
- Si mat : une popup s'affiche avec le nom du vainqueur
- La table `partie` est mise à jour (`statut='terminee'`, `id_vainqueur` renseigné)
- La popup propose de retourner à l'accueil

Estimation : 5 | MVP associé : M12 | Statut : ❌ À faire

---

### SPRINT 3 — Persistance, sauvegarde et reprise

**Objectif du sprint** : Chaque coup est sauvegardé en BDD. Le joueur peut sauvegarder sa partie et la reprendre plus tard.

---

**US-12 — Persistance des coups en temps réel**
> En tant que joueur, je veux que chaque coup joué soit enregistré en base de données afin que la partie soit reconstituable à tout moment.

Critères d'acceptation :
- Après chaque coup valide, `DAOCoup.insererCoup()` est appelé par `ControllerBoard`
- Le numéro de coup est incrémenté correctement (`numero_coup` = 1, 2, 3…)
- La case de départ et d'arrivée sont stockées en notation algébrique (`e2`, `e4`)
- En cas d'erreur SQL, un message est loggé mais le jeu continue

Estimation : 3 | MVP associé : M13 | Statut : ❌ À faire

---

**US-13 — Sauvegarde d'une partie en cours**
> En tant que joueur, je veux pouvoir sauvegarder ma partie et revenir à l'accueil afin de la reprendre plus tard.

Critères d'acceptation :
- Un bouton "Sauvegarder" est présent dans `VueBoard`
- Le clic met à jour `statut='sauvegardee'` dans la table `partie`
- L'application revient à `VueAccueil` après la sauvegarde
- Le bouton "Reprendre" de `VueAccueil` devient actif

Estimation : 2 | MVP associé : M14 | Statut : ❌ À faire

---

**US-14 — Reprise d'une partie sauvegardée**
> En tant que joueur, je veux reprendre une partie sauvegardée à l'état exact où je l'avais laissée afin de ne pas perdre ma progression.

Critères d'acceptation :
- "Reprendre" charge la dernière partie `sauvegardee` du joueur connecté
- `Board.replayCoups()` rejoue tous les coups dans l'ordre pour reconstituer le plateau
- Le plateau affiché correspond exactement à l'état au moment de la sauvegarde
- Le statut de la partie redevient `en_cours` après reprise
- Si le joueur n'a pas de partie sauvegardée, le bouton est grisé (impossible d'atteindre ce cas normalement)

Estimation : 5 | MVP associé : M15 | Statut : ❌ À faire

---

### SPRINT 4 — Refactoring MVC + Post-MVP

**Objectif du sprint** : Le code respecte strictement MVC. Les améliorations UI sont ajoutées si le temps le permet.

---

**US-15 — Respect strict de l'architecture MVC**
> En tant que développeur, je veux que le code respecte strictement le pattern MVC afin de satisfaire les exigences académiques et de faciliter la maintenance.

Critères d'acceptation :
- `VueBoard` ne contient aucune logique de validation, ni d'accès DAO
- Un `ControllerBoard` gère tous les événements de `VueBoard`
- Aucun composant JavaFX (`Button`, `Label`, etc.) n'est instancié dans un contrôleur
- `Session` est le seul point de partage d'état entre couches

Estimation : 5 | MVP associé : M16 | Statut : ❌ À faire (partiellement cassé : logique dans VueBoard actuellement)

---

**US-16 — IA aléatoire (Post-MVP)**
> En tant que joueur, je veux pouvoir jouer contre l'ordinateur afin de pratiquer seul.

Critères d'acceptation :
- En mode JvIA, après chaque coup du joueur, l'IA joue automatiquement
- L'IA choisit un coup au hasard parmi tous ses coups légaux
- L'IA respecte les mêmes règles que le joueur humain

Estimation : 3 | MVP associé : P06 | Statut : ❌ Optionnel

---

**US-17 — Affichage des coordonnées du plateau (Post-MVP)**
> En tant que joueur, je veux voir les coordonnées (a-h, 1-8) autour du plateau afin de m'orienter plus facilement.

Critères d'acceptation :
- Les lettres a à h s'affichent sous les colonnes
- Les chiffres 1 à 8 s'affichent à gauche des rangées
- Les labels ne chevauchent pas le plateau

Estimation : 1 | MVP associé : P02 | Statut : ❌ Optionnel

---

## Récapitulatif du backlog

| Sprint | Stories | Points totaux | Objectif |
|--------|---------|---------------|----------|
| Sprint 1 | US-01 à US-06 | 11 pts | Connexion + Session + Accueil |
| Sprint 2 | US-07 à US-11 | 23 pts | Règles d'échecs + fin de partie |
| Sprint 3 | US-12 à US-14 | 10 pts | Persistance + sauvegarde/reprise |
| Sprint 4 | US-15 à US-17 | 9 pts  | MVC strict + améliorations UI |
| **Total** | **17 stories** | **53 pts** | |

Les 3 stories déjà couvertes par le code existant (M01, M02, M03) ne sont pas listées comme stories Scrum car elles ne font pas partie du backlog restant — elles constituent la base de départ.
