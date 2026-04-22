# Diagramme de Vues
## Projet : ChessEngine
**Version** : 2.0 (simplifiée) | **Date** : 2026-04-15

---

## 1. Inventaire des vues

4 vues, architecture MVC stricte. Chaque vue est une classe du package `vue/`, sans aucune logique métier ni SQL.

| ID  | Classe Java          | Statut         | Rôle                                          |
|-----|----------------------|----------------|-----------------------------------------------|
| V01 | `VueConnexion`       | À implémenter  | Formulaire login (identifiants mock)          |
| V02 | `VueAccueil`         | Partielle       | Menu principal : Jouer, Reprendre, Quitter    |
| V03 | `VueGame`            | À implémenter  | Configuration de la partie avant lancement    |
| V04 | `VueBoard`           | Fonctionnelle   | Plateau de jeu + bouton Sauvegarder           |

---

## 2. Flux de navigation

```
  Démarrage de l'application
           │
           ▼
  ┌──────────────────┐
  │  V01             │
  │  VueConnexion    │  ← Vue initiale (remplace VueAccueil au démarrage)
  │                  │
  │  [login]         │
  │  [mot de passe]  │
  │  [Se connecter]  │
  └────────┬─────────┘
           │ Authentification OK
           │ Session.setUtilisateur(u)
           ▼
  ┌──────────────────────────────┐
  │  V02 — VueAccueil            │
  │                              │
  │  [Nouvelle partie]           │──────────────────────────────┐
  │  [Reprendre une partie]      │──────────────────────────┐   │
  │  [Quitter]                   │                          │   │
  └──────────────────────────────┘                          │   │
                                               Partie       │   │ Nouvelle
                                               sauvegardée  │   │ partie
                                               trouvée      │   │
                                                            ▼   ▼
                                               ┌──────────────────────┐
                                               │  V03 — VueGame       │
                                               │  (config partie)     │
                                               │                      │
                                               │  [Mode: JvJ / JvIA]  │
                                               │  [Lancer]            │
                                               │  [Retour]            │
                                               └──────────┬───────────┘
                                                          │ Partie créée en BDD
                                                          │ new Board() initialisé
                                                          ▼
                                               ┌──────────────────────┐
                                               │  V04 — VueBoard      │
                                               │  (plateau de jeu)    │
                                               │                      │
                                               │  [Plateau 8x8]       │
                                               │  [Sauvegarder]       │──► statut='sauvegardee'
                                               │  [Abandonner]        │──► V02
                                               └──────────┬───────────┘
                                                          │ Mat détecté
                                                          │ statut='terminee'
                                                          ▼
                                               ┌──────────────────────┐
                                               │  Popup fin de partie │
                                               │  [Retour accueil]    │──► V02
                                               └──────────────────────┘
```

---

## 3. Responsabilités MVC par vue

Le respect du pattern MVC est une contrainte non négociable du projet. La règle est simple : **une vue ne contient que du code JavaFX** (construction de la scène, positionnement des composants, application du CSS). Toute logique en est absente.

| Couche        | Responsabilité                                                                 | Exemples                                             |
|---------------|---------------------------------------------------------------------------------|------------------------------------------------------|
| **Model**     | État de l'application, règles métier, entités BDD                              | `Board`, `Piece`, `Partie`, `Utilisateur`, `DAOCoup` |
| **Vue**       | Construction de l'interface JavaFX uniquement, aucune logique                  | `VueConnexion`, `VueAccueil`, `VueBoard`             |
| **Controller**| Réception des événements UI, appel du modèle, mise à jour de la vue            | `ControllerConnexion`, `ControllerBoard`             |

### Ce qui est interdit dans une vue
- Requêtes SQL ou appels DAO directs
- Vérification des règles d'échecs
- Logique d'authentification
- Appels à `Session` (uniquement le contrôleur le fait)

### Ce qui est interdit dans un contrôleur
- Construction de composants JavaFX (`new Button(...)`, etc.)
- Appels à `root.getChildren().add(...)` directs (déléguer à la vue)

---

## 4. Classe Session (singleton partagé)

La `Session` est le seul objet partagé entre les couches. Elle évite les variables statiques brutes dans `Main`.

```
Session (singleton)
  ├── utilisateurConnecte : Utilisateur   (positionné par ControllerConnexion)
  ├── partieEnCours       : Partie        (positionné par ControllerGame)
  └── stage               : Stage         (positionné par Main.start())
```

Chaque contrôleur lit `Session.getInstance()` pour accéder au contexte. Aucune vue ne touche directement à la Session.

---

## 5. Flux de sauvegarde / reprise (détail)

**Sauvegarder** : le bouton "Sauvegarder" dans V04 appelle `ControllerBoard.sauvegarder()`. Ce contrôleur fait un UPDATE `statut = 'sauvegardee'` sur la partie courante. Les coups sont déjà persistés coup par coup depuis le début, donc aucun traitement supplémentaire n'est nécessaire.

**Reprendre** : le bouton "Reprendre" dans V02 appelle `ControllerAccueil.reprendrePartie()`. Celui-ci fait un SELECT de la dernière partie `sauvegardee` du joueur, recharge tous ses coups, les rejoue sur un `Board` vierge via `Board.replayCoups(List<Coup>)`, puis ouvre V04 avec ce Board reconstruit.
