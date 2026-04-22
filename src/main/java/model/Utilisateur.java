package model;

public class Utilisateur {

    private int id;
    private String login;
    private String motDePasse;

    public Utilisateur(int id, String login, String motDePasse) {
        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getMotDePasse() { return motDePasse; }
}
