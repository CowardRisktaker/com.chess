# Roadmap & Guide de codage
## Projet : ChessEngine
**Version** : 2.0 (simplifiée) | **Date** : 2026-04-15

---

## 1. État des lieux

### Fonctionnel aujourd'hui
- Plateau 8x8 affiché avec alternance de couleurs CSS (`#69923e` / `#fff`)
- Pièces placées en position initiale (`Board.initPieces()`)
- Déplacement visuel par double clic (aucune validation)
- Structure MVC en place (packages séparés, mais contrôleurs vides)
- Connexion MySQL prête (`DAOAcces`) mais non utilisée

### Vide ou cassé
- `VueConnexion`, `VueGame`, `ControllerConnexion` : classes vides
- Aucune règle d'échecs
- Aucune persistance des coups
- Variables statiques brutes dans `Main` (à remplacer par `Session`)

---

## 2. Ordre de réalisation recommandé

```
Étape 1  ─  Créer la BDD (exécuter le SRD du doc 02)
Étape 2  ─  config.properties + refactoriser DAOAcces
Étape 3  ─  Créer Session.java (singleton)
Étape 4  ─  Créer Utilisateur.java (POJO) + DAOUtilisateur
Étape 5  ─  Implémenter VueConnexion + ControllerConnexion
Étape 6  ─  Enrichir VueAccueil + ControllerAccueil (boutons, session)
Étape 7  ─  Implémenter VueGame + ControllerGame
Étape 8  ─  Ajouter mouvementsValides() dans chaque Piece
Étape 9  ─  Créer RegleEchecs (estEnEchec, estMat)
Étape 10 ─  Refactoriser VueBoard : validation + surbrillances + détection mat
Étape 11 ─  Créer Partie.java + DAOPartie + DAOCoup
Étape 12 ─  Persister chaque coup (INSERT après chaque mouvement valide)
Étape 13 ─  Implémenter Sauvegarder (UPDATE statut) + Reprendre (replay coups)
Étape 14 ─  (Post-MVP) UI améliorée : thème sombre, chrono, notation algébrique
```

---

## 3. Guides techniques

### 3.1 config.properties

Créer `src/main/resources/config.properties` :
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/chessengine?autoReconnect=true&useSSL=false&serverTimezone=UTC
db.login=root
db.password=votre_mdp
```

Charger dans `DAOAcces` :
```java
Properties props = new Properties();
try (InputStream is = getClass().getResourceAsStream("/config.properties")) {
    props.load(is);
}
String url      = props.getProperty("db.url");
String login    = props.getProperty("db.login");
String password = props.getProperty("db.password");
```

---

### 3.2 Session (singleton)

Créer `src/main/java/app/Session.java`. Remplace les variables statiques de `Main`.

```java
public class Session {
    private static Session instance;
    private Utilisateur utilisateurConnecte;
    private Partie partieEnCours;
    private Stage stage;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }
    // getters / setters
}
```

---

### 3.3 Authentification (VueConnexion + ControllerConnexion)

**Ajouter dans pom.xml :**
```xml
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
```

**VueConnexion** : construit le formulaire JavaFX. Expose `getLogin()`, `getMotDePasse()`, `getBtnConnexion()`, `afficherErreur()`. Rien d'autre.

**ControllerConnexion** — flux :
1. Récupérer `tfLogin.getText()` et `pfMdp.getText()`
2. `DAOUtilisateur.findByLogin(login)` → `Utilisateur` ou `null`
3. Si null → `vue.afficherErreur()`
4. Si trouvé → `BCrypt.checkpw(mdp, utilisateur.getMotDePasse())`
5. Si false → `vue.afficherErreur()`
6. Si true → `Session.getInstance().setUtilisateurConnecte(u)` → charger `VueAccueil`

---

### 3.4 Mouvements valides (méthode abstraite dans Piece)

Ajouter dans `Piece.java` :
```java
public abstract List<Position> mouvementsValides(Board board);
```

Règles par pièce (coordonnées internes : x=colonne 0-7, y=rangée 0-7, y=0 en haut) :

**Pion Blanc** : avance vers y décroissant (y-1). Avance de deux cases depuis y=6. Capture en diagonale (x±1, y-1) si pièce adverse.

**Pion Noir** : identique, y croissant (y+1). Rangée initiale y=1.

**Tour** : 4 directions (+x, -x, +y, -y). Pour chaque direction, avancer case par case. Ajouter si vide. Si pièce adverse : ajouter et stopper. Si pièce alliée : stopper.

**Fou** : identique, 4 diagonales (±x, ±y).

**Reine** : union Tour + Fou.

**Cavalier** : 8 offsets L `{(±1,±2),(±2,±1)}`. Ajouter si vide ou adverse. Pas d'obstacle intermédiaire.

**Roi** : les 8 cases adjacentes, case vide ou adverse. **Filtrer ensuite celles qui mettraient le Roi en échec.**

**Filtre obligatoire après calcul brut :**
```java
// Pour toute pièce, filtrer les coups qui exposent le propre Roi
mouvementsBruts.removeIf(pos -> {
    Board copie = board.copie();
    copie.deplacerPiece(piece.getPosition(), pos);
    return RegleEchecs.estEnEchec(copie, piece.getCouleur());
});
```

`Board.copie()` doit retourner un nouveau `Board` avec le même tableau de pièces (clonage superficiel suffisant — les objets `Piece` ne sont pas modifiés).

---

### 3.5 Détection échec / mat

**`RegleEchecs.estEnEchec(Board board, String couleur)`** :
1. Trouver la position du Roi de la couleur donnée sur le plateau
2. Pour chaque pièce adverse, calculer ses mouvements **bruts** (sans filtrage récursif pour éviter la récursion infinie)
3. Si l'une atteint la case du Roi → retourner `true`

**`RegleEchecs.estMat(Board board, String couleur)`** :
1. Vérifier `estEnEchec(board, couleur)`
2. Pour chaque pièce de la couleur : si `mouvementsValides()` non vide → pas mat
3. Si toutes les pièces n'ont aucun coup → mat confirmé

Appeler après chaque coup : si mat → UPDATE `statut='terminee'` en BDD + popup dans `VueBoard`.

---

### 3.6 Persistance des coups (DAOCoup)

```java
public void insererCoup(int idPartie, int numeroCoup, String couleur,
                         String typePiece, String caseDepart, String caseArrivee)
        throws SQLException {
    String sql = """
        INSERT INTO coup (id_partie, numero_coup, couleur_joueur,
                          type_piece, case_depart, case_arrivee)
        VALUES (?, ?, ?, ?, ?, ?)
        """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idPartie);
        ps.setInt(2, numeroCoup);
        ps.setString(3, couleur);
        ps.setString(4, typePiece);
        ps.setString(5, caseDepart);    // ex: "e2"
        ps.setString(6, caseArrivee);   // ex: "e4"
        ps.executeUpdate();
    }
}
```

**Conversion coordonnées internes → algébriques :**
```java
// x=0→'a' … x=7→'h' | y=0→'8' … y=7→'1'
String col = String.valueOf((char)('a' + x));
String row = String.valueOf(8 - y);
String alg = col + row;   // "e2", "h8", etc.
```

---

### 3.7 Sauvegarde et reprise

**Sauvegarder** (dans `ControllerBoard`) :
```java
// UPDATE partie SET statut='sauvegardee' WHERE id_partie=?
daoPartie.sauvegarder(session.getPartieEnCours().getId());
// Retour à VueAccueil
```

**Reprendre** (dans `ControllerAccueil`) :
```java
Partie p = daoPartie.findDernierePartieSauvegardee(idJoueur);
if (p == null) return; // bouton devrait être grisé, ne devrait pas arriver

List<Coup> coups = daoCoup.findByPartie(p.getId());
Board board = new Board();
board.replayCoups(coups); // rejoue tous les coups dans l'ordre

// Repasser le statut à 'en_cours'
daoPartie.reprendrePartie(p.getId());
session.setPartieEnCours(p);

// Ouvrir VueBoard avec ce Board reconstruit
```

**`Board.replayCoups(List<Coup>)`** :
```java
public void replayCoups(List<Coup> coups) {
    for (Coup c : coups) {
        Position depart  = algToPos(c.getCaseDepart());
        Position arrivee = algToPos(c.getCaseArrivee());
        deplacerPiece(depart, arrivee);
    }
}

private Position algToPos(String alg) {
    int x = alg.charAt(0) - 'a';       // 'e' → 4
    int y = 8 - (alg.charAt(1) - '0'); // '2' → 6
    return new Position(x, y);
}
```

---

## 4. Architecture cible (packages)

```
src/main/java/
├── app/
│   ├── Launcher.java          (existant)
│   ├── Main.java              (à modifier : utiliser Session)
│   └── Session.java           ← NOUVEAU
├── DAO/
│   ├── DAOAcces.java          (à refactoriser : config.properties)
│   ├── DAOUtilisateur.java    ← NOUVEAU
│   ├── DAOPartie.java         ← NOUVEAU
│   └── DAOCoup.java           ← NOUVEAU
├── controller/
│   ├── ControllerConnexion.java  (à implémenter)
│   ├── ControllerAccueil.java    (à enrichir)
│   ├── ControllerGame.java       ← NOUVEAU
│   └── ControllerBoard.java      ← NOUVEAU (extraire logique de VueBoard)
├── model/
│   ├── Piece.java             (ajouter mouvementsValides())
│   ├── Position.java          (existant)
│   ├── Board.java             (ajouter copie(), replayCoups(), deplacerPiece())
│   ├── Pion/Tour/Cavalier/Fou/Reine/Roi.java  (implémenter mouvementsValides())
│   ├── Utilisateur.java       ← NOUVEAU (POJO)
│   └── Partie.java            ← NOUVEAU (POJO)
├── service/
│   └── RegleEchecs.java       ← NOUVEAU (estEnEchec, estMat)
└── vue/
    ├── VueConnexion.java      (à implémenter)
    ├── VueAccueil.java        (à enrichir)
    ├── VueGame.java           (à implémenter)
    └── VueBoard.java          (à refactoriser + enrichir)

src/main/resources/
├── config.properties          ← NOUVEAU
├── images/  (existant)
└── style.css (existant)
```
