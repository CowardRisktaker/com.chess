# MCD — MLD — SRD
## Projet : ChessEngine
**Méthode** : MERISE | **Version** : 2.0 (simplifiée) | **Date** : 2026-04-15

---

# PARTIE 1 — MCD (Modèle Conceptuel des Données)

## Entités

```
┌──────────────────────────┐
│       UTILISATEUR        │
├──────────────────────────┤
│ # id_utilisateur         │
│   login                  │
│   mot_de_passe           │
└──────────────────────────┘

┌──────────────────────────┐
│          PARTIE          │
├──────────────────────────┤
│ # id_partie              │
│   date_debut             │
│   date_fin               │
│   statut                 │
│   mode_jeu               │
└──────────────────────────┘

┌──────────────────────────┐
│           COUP           │
├──────────────────────────┤
│ # id_coup                │
│   numero_coup            │
│   couleur_joueur         │
│   type_piece             │
│   case_depart            │
│   case_arrivee           │
└──────────────────────────┘
```

## Associations et cardinalités

```
UTILISATEUR (0,N) ──── joue ──── (1,1) PARTIE
    Un utilisateur joue 0 à N parties.
    Une partie est jouée par exactement 1 utilisateur (le joueur connecté).

UTILISATEUR (0,N) ──── remporte ──── (0,1) PARTIE
    Un utilisateur remporte 0 à N parties.
    Une partie a 0 ou 1 vainqueur (0 si en cours, sauvegardée, ou nulle).

PARTIE (1,N) ──── contient ──── (1,1) COUP
    Une partie contient 1 à N coups.
    Un coup appartient à exactement 1 partie.
```

## Schéma

```
  ┌────────────────┐  (0,N)  joue     (1,1)  ┌────────────────┐
  │  UTILISATEUR   │ ──────────────────────> │     PARTIE     │
  │                │  (0,N)  remporte  (0,1)  │                │
  │ id_utilisateur │ ──────────────────────> │  id_partie     │
  │ login          │                          │  date_debut    │
  │ mot_de_passe   │                          │  date_fin      │
  └────────────────┘                          │  statut        │
                                              │  mode_jeu      │
                                              └───────┬────────┘
                                                      │ (1,N) contient
                                                      ▼ (1,1)
                                            ┌─────────────────────┐
                                            │        COUP         │
                                            │ id_coup             │
                                            │ numero_coup         │
                                            │ couleur_joueur      │
                                            │ type_piece          │
                                            │ case_depart         │
                                            │ case_arrivee        │
                                            └─────────────────────┘
```

---

# PARTIE 2 — MLD (Modèle Logique des Données)

```
UTILISATEUR (
    #id_utilisateur : INT,
    login           : VARCHAR(50),
    mot_de_passe    : VARCHAR(255)
)

PARTIE (
    #id_partie      : INT,
    date_debut      : DATETIME,
    date_fin        : DATETIME,
    statut          : ENUM('en_cours','sauvegardee','terminee'),
    mode_jeu        : ENUM('joueur_vs_joueur','joueur_vs_ia'),
    id_joueur       : INT  => UTILISATEUR(id_utilisateur)  [NOT NULL]
    id_vainqueur    : INT  => UTILISATEUR(id_utilisateur)  [NULL]
)

COUP (
    #id_coup        : INT,
    id_partie       : INT  => PARTIE(id_partie)            [NOT NULL],
    numero_coup     : SMALLINT,
    couleur_joueur  : ENUM('Blanc','Noir'),
    type_piece      : ENUM('Pion','Tour','Cavalier','Fou','Reine','Roi'),
    case_depart     : CHAR(2),
    case_arrivee    : CHAR(2)
)
```

**Dépendances fonctionnelles :**
- `id_utilisateur` → login, mot_de_passe
- `id_partie` → date_debut, date_fin, statut, mode_jeu, id_joueur, id_vainqueur
- `id_coup` → id_partie, numero_coup, couleur_joueur, type_piece, case_depart, case_arrivee

Les trois tables sont en 3NF : clé primaire simple, pas de dépendance partielle ni transitive.

---

# PARTIE 3 — SRD (Script SQL)

```sql
-- ============================================================
-- ChessEngine — Création de la base de données
-- Version : 2.0 | MySQL 8.x
-- ============================================================

CREATE DATABASE IF NOT EXISTS chessengine
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE chessengine;

-- ------------------------------------------------------------
-- UTILISATEUR
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS utilisateur (
    id_utilisateur INT         NOT NULL AUTO_INCREMENT,
    login          VARCHAR(50) NOT NULL,
    mot_de_passe   VARCHAR(255) NOT NULL COMMENT 'Hash BCrypt',

    CONSTRAINT pk_utilisateur PRIMARY KEY (id_utilisateur),
    CONSTRAINT uk_login       UNIQUE (login)
) ENGINE=InnoDB;

-- Comptes mock (mots de passe hashés avec BCrypt factor 12)
-- Mot de passe en clair : "chess1234"
INSERT INTO utilisateur (login, mot_de_passe) VALUES
    ('joueur1', '$2b$12$KIX7YG7R6v5t9b5tJ2OXXeQzV0pLZ4ZuRfVzTQ7F6mK9aB3D1uHjy'),
    ('joueur2', '$2b$12$KIX7YG7R6v5t9b5tJ2OXXeQzV0pLZ4ZuRfVzTQ7F6mK9aB3D1uHjy');

-- ------------------------------------------------------------
-- PARTIE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS partie (
    id_partie    INT      NOT NULL AUTO_INCREMENT,
    date_debut   DATETIME NOT NULL DEFAULT NOW(),
    date_fin     DATETIME NULL,
    statut       ENUM('en_cours','sauvegardee','terminee') NOT NULL DEFAULT 'en_cours',
    mode_jeu     ENUM('joueur_vs_joueur','joueur_vs_ia')   NOT NULL,
    id_joueur    INT      NOT NULL,
    id_vainqueur INT      NULL,

    CONSTRAINT pk_partie           PRIMARY KEY (id_partie),
    CONSTRAINT fk_partie_joueur    FOREIGN KEY (id_joueur)
        REFERENCES utilisateur(id_utilisateur) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_partie_vainqueur FOREIGN KEY (id_vainqueur)
        REFERENCES utilisateur(id_utilisateur) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- COUP
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS coup (
    id_coup        INT      NOT NULL AUTO_INCREMENT,
    id_partie      INT      NOT NULL,
    numero_coup    SMALLINT NOT NULL,
    couleur_joueur ENUM('Blanc','Noir') NOT NULL,
    type_piece     ENUM('Pion','Tour','Cavalier','Fou','Reine','Roi') NOT NULL,
    case_depart    CHAR(2)  NOT NULL,
    case_arrivee   CHAR(2)  NOT NULL,

    CONSTRAINT pk_coup        PRIMARY KEY (id_coup),
    CONSTRAINT fk_coup_partie FOREIGN KEY (id_partie)
        REFERENCES partie(id_partie) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_depart     CHECK (case_depart  REGEXP '^[a-h][1-8]$'),
    CONSTRAINT chk_arrivee    CHECK (case_arrivee REGEXP '^[a-h][1-8]$')
) ENGINE=InnoDB;

CREATE INDEX idx_coup_partie ON coup (id_partie, numero_coup);
```

## Logique de sauvegarde/reprise

La sauvegarde ne stocke pas un snapshot du plateau : elle s'appuie sur la table `coup`. Pour reprendre une partie sauvegardée, l'application recharge tous les coups de cette partie dans l'ordre (`ORDER BY numero_coup ASC`) et les rejoue sur un `Board` vierge. Cela garantit que l'état reconstitué est exactement identique à l'état au moment de la sauvegarde.

```sql
-- Requête de reprise
SELECT type_piece, couleur_joueur, case_depart, case_arrivee
FROM coup
WHERE id_partie = ?
ORDER BY numero_coup ASC;
```
