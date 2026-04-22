# Liste des MVP (Minimum Viable Products)
## Projet : ChessEngine
**Date** : 2026-04-15

---

## Définition

Un MVP est ici une fonctionnalité atomique et livrable qui apporte une valeur concrète à l'utilisateur. L'ordre reflète la dépendance technique entre les items : chaque MVP peut être démontré indépendamment dès qu'il est terminé.

Les MVPs sont regroupés en deux niveaux :
- **Core** : sans ces items, l'application ne remplit pas son objectif principal
- **Post-MVP** : améliorations qui n'empêchent pas de livrer, mais que l'on souhaite intégrer ensuite

---

## Niveau 1 — Core MVP

| # | MVP | Statut | Dépend de | Description |
|---|-----|--------|-----------|-------------|
| M01 | Affichage du plateau | ✅ Fait | — | Plateau 8x8 avec alternance de couleurs (#69923e / #fff) et bordure brune |
| M02 | Placement initial des pièces | ✅ Fait | M01 | Les 32 pièces sont disposées en position standard à l'ouverture de VueBoard |
| M03 | Déplacement visuel (sans règles) | ✅ Fait | M02 | Clic sur une pièce puis clic sur une case déplace la pièce visuellement |
| M04 | Base de données créée et connectée | ❌ À faire | — | Script SRD exécuté, `config.properties` configuré, `DAOAcces` fonctionnel |
| M05 | Authentification (login mock) | ❌ À faire | M04 | `VueConnexion` + `ControllerConnexion` : vérification BCrypt contre la table `utilisateur` |
| M06 | Session utilisateur | ❌ À faire | M05 | Classe `Session` singleton ; l'utilisateur connecté est accessible dans toute l'application |
| M07 | Page d'accueil fonctionnelle | ❌ À faire | M06 | `VueAccueil` affiche le login du joueur connecté et les boutons Nouvelle partie / Reprendre / Quitter |
| M08 | Configuration et lancement d'une partie | ❌ À faire | M07 | `VueGame` + `ControllerGame` : choix du mode (JvJ / JvIA), création de la `Partie` en BDD, ouverture de `VueBoard` |
| M09 | Validation des mouvements | ❌ À faire | M03 | Méthode `mouvementsValides()` dans chaque `Piece` ; les coups illégaux sont refusés |
| M10 | Surbrillance des cases accessibles | ❌ À faire | M09 | Au clic sur une pièce, les cases atteignables sont surlignées visuellement |
| M11 | Détection de l'échec | ❌ À faire | M09 | `RegleEchecs.estEnEchec()` ; le Roi en échec est signalé visuellement |
| M12 | Détection du mat (fin de partie) | ❌ À faire | M11 | `RegleEchecs.estMat()` ; fin de partie automatique avec popup et UPDATE en BDD |
| M13 | Persistance des coups | ❌ À faire | M08 | Chaque coup valide déclenche un INSERT dans la table `coup` |
| M14 | Sauvegarde d'une partie | ❌ À faire | M13 | Bouton "Sauvegarder" : UPDATE `statut='sauvegardee'`, retour à l'accueil |
| M15 | Reprise d'une partie sauvegardée | ❌ À faire | M14 | `Board.replayCoups()` reconstruit l'état depuis la BDD ; bouton "Reprendre" grisé si aucune partie |
| M16 | Architecture MVC complète | ❌ À faire | M05→M15 | Aucune logique SQL dans les vues, aucun composant JavaFX dans les contrôleurs ; `ControllerBoard` extrait de `VueBoard` |

---

## Niveau 2 — Post-MVP (interface & confort)

Ces items n'affectent pas le comportement fonctionnel. Ils améliorent l'expérience utilisateur.

| # | MVP | Statut | Description |
|---|-----|--------|-------------|
| P01 | Thème sombre de l'application | ❌ Optionnel | Fond #1a1a2e, cohérence visuelle entre toutes les vues |
| P02 | Affichage des coordonnées du plateau | ❌ Optionnel | Labels a-h et 1-8 autour du plateau |
| P03 | Notation algébrique des coups | ❌ Optionnel | Panneau latéral affichant l'historique des coups joués |
| P04 | Chronomètre par joueur | ❌ Optionnel | `Timeline` JavaFX, un compteur par couleur |
| P05 | Surbrillance du dernier coup joué | ❌ Optionnel | Cases départ/arrivée du coup précédent légèrement colorées |
| P06 | IA aléatoire (JvIA basique) | ❌ Optionnel | L'IA choisit un coup au hasard parmi ses coups légaux |

---

## Récapitulatif

```
Fait         :  3 / 16 MVPs core  (M01, M02, M03)
À faire      : 13 / 16 MVPs core
Post-MVP     :  6 items optionnels
```

Le projet est livrable (au sens "démo fonctionnelle") dès M15 terminé. M16 (MVC complet) est une contrainte académique transversale qui doit être respectée tout au long du développement, pas en fin de projet.
