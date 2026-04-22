# Cahier des Charges
## Projet : ChessEngine
**Version** : 2.0 (simplifiée) | **Date** : 2026-04-15

---

## 1. Contexte et objectifs

ChessEngine est une application desktop Java/JavaFX permettant à un utilisateur authentifié de jouer aux échecs, seul contre une IA basique ou contre un second joueur sur la même machine. L'application persiste les parties en MySQL et permet de les sauvegarder et reprendre.

Projet académique individuel. Le périmètre est volontairement restreint au strict fonctionnel.

---

## 2. Périmètre fonctionnel

**Dans le périmètre :**
- Connexion avec identifiants fournis (comptes mock, pas d'inscription)
- Accueil avec accès à une nouvelle partie ou à la reprise d'une partie sauvegardée
- Configuration d'une partie (mode JvJ ou JvIA)
- Jeu complet avec validation des règles d'échecs
- Détection de l'échec et du mat
- Sauvegarde et reprise d'une partie

**Hors périmètre :**
- Inscription utilisateur
- Profil, ELO, avatar, historique des parties
- Multijoueur réseau
- IA avancée (Minimax) — une IA aléatoire suffit pour le MVP
- Export PGN, notation algébrique visible

---

## 3. Acteurs

Un seul acteur : le **Joueur connecté**. Pas d'administrateur dans l'interface.

---

## 4. Cas d'utilisation

| ID  | Cas d'utilisation         | Priorité  |
|-----|---------------------------|-----------|
| UC1 | Se connecter              | Critique  |
| UC2 | Lancer une nouvelle partie| Critique  |
| UC3 | Jouer un coup             | Critique  |
| UC4 | Sauvegarder une partie    | Critique  |
| UC5 | Reprendre une partie      | Haute     |
| UC6 | Abandonner une partie     | Moyenne   |
| UC7 | Se déconnecter            | Basse     |

---

## 5. Exigences fonctionnelles

**EF01** — Au démarrage, l'application affiche un formulaire de connexion.  
**EF02** — La connexion valide le login/mot de passe contre la table `utilisateur` (BCrypt).  
**EF03** — En cas d'échec, un message d'erreur est affiché sans préciser si c'est le login ou le mot de passe qui est faux.  
**EF04** — Après connexion, le joueur accède à VueAccueil avec les boutons : Nouvelle partie, Reprendre, Quitter.  
**EF05** — "Reprendre" n'est actif que si une partie `sauvegardee` existe pour ce joueur.  
**EF06** — Avant le lancement, le joueur choisit le mode (JvJ ou JvIA).  
**EF07** — Chaque coup joué est validé selon les règles des échecs (mouvements légaux par type de pièce, interdiction de se mettre soi-même en échec).  
**EF08** — Le plateau indique visuellement la case sélectionnée et les cases accessibles.  
**EF09** — L'application détecte et signale l'état d'échec.  
**EF10** — L'application détecte le mat et termine la partie automatiquement.  
**EF11** — Le bouton "Sauvegarder" met la partie en statut `sauvegardee` et revient à l'accueil.  
**EF12** — "Reprendre" reconstruit l'état du plateau en rejouant les coups sauvegardés.  
**EF13** — L'architecture respecte le pattern MVC : aucune logique métier dans les vues.

---

## 6. Exigences non fonctionnelles

**ENF01** — Les mots de passe sont stockés hashés avec BCrypt (factor ≥ 12). Jamais en clair.  
**ENF02** — La connexion BDD est configurée dans `config.properties`, pas dans le code.  
**ENF03** — Le plateau se redessine en moins de 100 ms après un coup.  
**ENF04** — L'application tourne sur Windows 10+ et macOS 12+ avec JDK 17+.

---

## 7. Maquettes simplifiées

Les maquettes graphiques sont dans `docs/maquettes_vues.html`. Ce document décrit uniquement le contenu minimal attendu par vue.

### V01 — VueConnexion
Champ login, champ mot de passe masqué, bouton "Se connecter", label d'erreur conditionnel.

### V02 — VueAccueil
Nom du joueur connecté, bouton "Nouvelle partie", bouton "Reprendre" (grisé si aucune partie sauvegardée), bouton "Quitter".

### V03 — VueGame
Choix du mode (JvJ / JvIA) par deux boutons radio ou toggles, bouton "Lancer", bouton "Retour".

### V04 — VueBoard
Plateau 8x8, surbrillance de la case sélectionnée, surbrillance des cases accessibles, indicateur "À qui de jouer", bouton "Sauvegarder", bouton "Abandonner".

---

## 8. Contraintes techniques

| Élément        | Contrainte                                              |
|----------------|---------------------------------------------------------|
| Langage        | Java 17 LTS (préféré à Java 25 pour stabilité Maven)   |
| GUI            | JavaFX 17+                                              |
| Base de données| MySQL 8.x                                               |
| Connecteur     | MySQL Connector/J via Maven                             |
| Hashage        | BCrypt — dépendance `org.mindrot:jbcrypt:0.4`           |
| Architecture   | MVC strict — packages `model/`, `vue/`, `controller/`   |
| Config BDD     | Fichier `src/main/resources/config.properties`          |
