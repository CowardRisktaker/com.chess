# Dictionnaire des Données (DDD)
## Projet : ChessEngine
**Méthode** : MERISE | **Version** : 2.0 (simplifiée) | **Date** : 2026-04-15

---

## Périmètre retenu

L'application couvre 4 fonctionnalités : connexion avec identifiants mock, accueil, jeu, sauvegarde/reprise de partie. Tout ce qui sort de ce périmètre (inscription, profil, ELO, avatar, historique) est hors scope.

---

## Entité UTILISATEUR

| Code attribut    | Désignation          | Type     | Longueur | Contrainte                   | Remarque                          |
|------------------|----------------------|----------|----------|------------------------------|-----------------------------------|
| `id_utilisateur` | Identifiant          | INT      | 11       | PK, AUTO_INCREMENT, NOT NULL | Clé primaire                      |
| `login`          | Nom d'utilisateur    | VARCHAR  | 50       | UNIQUE, NOT NULL             | Identifiant de connexion          |
| `mot_de_passe`   | Mot de passe hashé   | VARCHAR  | 255      | NOT NULL                     | BCrypt — jamais stocké en clair   |

Les comptes sont créés manuellement en base (pas d'écran d'inscription).

---

## Entité PARTIE

| Code attribut    | Désignation              | Type  | Longueur | Contrainte                          | Remarque                                      |
|------------------|--------------------------|-------|----------|-------------------------------------|-----------------------------------------------|
| `id_partie`      | Identifiant de la partie | INT   | 11       | PK, AUTO_INCREMENT, NOT NULL        |                                               |
| `date_debut`     | Date/heure de début      | DATETIME | —     | NOT NULL, DEFAULT NOW()             |                                               |
| `date_fin`       | Date/heure de fin        | DATETIME | —     | NULL                                | NULL si partie en cours                       |
| `statut`         | État de la partie        | ENUM  | —        | NOT NULL                            | `en_cours`, `sauvegardee`, `terminee`         |
| `mode_jeu`       | Mode de partie           | ENUM  | —        | NOT NULL                            | `joueur_vs_joueur`, `joueur_vs_ia`            |
| `id_joueur`      | Joueur connecté          | INT   | 11       | FK → UTILISATEUR, NOT NULL          | Toujours le joueur Blanc (connecté)           |
| `id_vainqueur`   | Vainqueur                | INT   | 11       | FK → UTILISATEUR, NULL              | NULL si partie en cours ou nulle              |

---

## Entité COUP

| Code attribut    | Désignation             | Type     | Longueur | Contrainte                           | Remarque                                       |
|------------------|-------------------------|----------|----------|--------------------------------------|------------------------------------------------|
| `id_coup`        | Identifiant du coup     | INT      | 11       | PK, AUTO_INCREMENT, NOT NULL         |                                                |
| `id_partie`      | Partie associée         | INT      | 11       | FK → PARTIE, NOT NULL                |                                                |
| `numero_coup`    | Ordre du coup           | SMALLINT | 3        | NOT NULL                             | Commence à 1, incrémenté à chaque demi-coup    |
| `couleur_joueur` | Couleur qui joue        | ENUM     | —        | NOT NULL                             | `Blanc`, `Noir`                                |
| `type_piece`     | Type de pièce déplacée  | ENUM     | —        | NOT NULL                             | `Pion`,`Tour`,`Cavalier`,`Fou`,`Reine`,`Roi`  |
| `case_depart`    | Case d'origine          | CHAR     | 2        | NOT NULL                             | Notation algébrique : `a1` à `h8`             |
| `case_arrivee`   | Case de destination     | CHAR     | 2        | NOT NULL                             | Notation algébrique : `a1` à `h8`             |

La table `coup` sert de **mécanisme de sauvegarde** : rejouer tous les coups d'une partie reconstruit son état exact.

---

## Règles de gestion

**RG01** — Le login doit être unique. Les comptes sont insérés manuellement avec un hash BCrypt.  
**RG02** — Une partie `sauvegardee` peut être reprise ; son `statut` repasse à `en_cours` à la reprise.  
**RG03** — Chaque coup joué déclenche immédiatement un INSERT en base (pas de batch).  
**RG04** — `numero_coup` est séquentiel par partie : 1, 2, 3… (un coup = un demi-coup, Blanc puis Noir).  
**RG05** — En mode `joueur_vs_ia`, `id_vainqueur` peut référencer l'utilisateur connecté ou rester NULL si l'IA gagne (pas de compte IA).
