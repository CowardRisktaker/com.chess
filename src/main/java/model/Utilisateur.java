package model;

public class Utilisateur {

    private int id;
    private String login;

    public Utilisateur(){

    }

    public Utilisateur(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
}
