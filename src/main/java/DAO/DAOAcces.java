package DAO;

import io.github.cdimascio.dotenv.Dotenv;
import model.Piece;
import model.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

    public class DAOAcces {
        private Connection conn;
        private String driver;
        private String dbName;
        private String login;
        private String mdp;

        /**
         * constructeur de connexion
         *
         *
         **/

        public DAOAcces() throws SQLException {
            Dotenv dotenv = Dotenv.load();
            this.dbName = dotenv.get("DB_NAME");
            this.login = dotenv.get("DB_LOGIN");
            this.mdp = dotenv.get("DB_PASSWORD");
            String port = dotenv.get("DB_PORT");

            String strUrl = "jdbc:mysql://localhost:" + port + "/" + dbName
                    + "?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            this.conn = DriverManager.getConnection(strUrl, login, mdp);
        }


        public Connection getConn() {

            return this.conn;
        }


        public void setConn(Connection conn) {
            this.conn = conn;
        }

        public Utilisateur checkLogin(String login) throws SQLException {
            String sql = "SELECT * FROM `com.chess`.utilisateur WHERE login = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, login);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Utilisateur(rs.getInt("id_utilisateur"), rs.getString("login"), rs.getInt("elo")) ;
                    }
                }
            }

            return null;
        }

        public boolean checkPass(String login, String plainPassword) throws SQLException {
            String sql = "SELECT mot_de_passe FROM `com.chess`.utilisateur WHERE login = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, login);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String hashFromDB = rs.getString("mot_de_passe");
                        return BCrypt.checkpw(plainPassword, hashFromDB);
                    }
                }
            }
            return false;
        }

        public String getDriver() {
            return this.driver;
        }


        public void setDriver(String driver) {
            this.driver = driver;
        }


        public String getDbName() {
            return this.dbName;
        }


        public void setDbName(String dbName) {
            this.dbName = dbName;
        }


        public String getLogin() {
            return this.login;
        }


        public void setLogin(String login) {
            this.login = login;
        }


        public String getMdp() {
            return this.mdp;
        }


        public void setMdp(String mdp) {
            this.mdp = mdp;
        }

        /**
         * Destructeur de connexion
         *
         *
         **/
        public void closeConn() {
            try {
                this.conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }	}
    }
