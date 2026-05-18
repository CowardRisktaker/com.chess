package DAO;

import model.Sauvegarde;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DAOPartie extends DAOAcces {

    public DAOPartie() throws SQLException {}

    public int creerPartie(int idUser, String modeJeu) throws SQLException {
        String sql = "INSERT INTO `com.chess`.partie (id_joueur, mode_jeu, statut) VALUES (?, ?, 'en_cours')";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idUser);
            ps.setString(2, modeJeu);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Aucun id_partie généré.");
        }
    }
    public boolean sauvegarder(String etat, int idPartie) {
        String sql = "UPDATE `com.chess`.partie SET etat_plateau = ?, statut = 'sauvegardee' WHERE id_partie = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, etat);
            ps.setInt(2, idPartie);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String chargerPartie(int idUser) {
        String sql = "SELECT etat_plateau FROM `com.chess`.partie \n" +
                "WHERE id_joueur = ? AND statut = 'sauvegardee' \n" +
                "ORDER BY date_debut DESC LIMIT 1";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("etat_plateau");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}