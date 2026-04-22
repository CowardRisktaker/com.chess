package DAO;

import java.sql.*;

    public class DAOAcces {
        private Connection conn;
        private Statement statement;
        private String driver;
        private String dbName;
        private String login;
        private String mdp;

        /**
         * constructeur de connexion
         *
         * @param 	driver le driver JDBC : pilote pour faire marcher java avec la bdd
         * 			dbName : Acces
         * 			login: acces
         * 			mdp : 1234
         *
         **/

        public DAOAcces(String driver, String dbName, String login, String mdp) {
            this.driver = driver;
            this.dbName= dbName;
            this.login= login;
            this.mdp = mdp;
            String strUrl = "jdbc:mysql://localhost:3306/" +  dbName + "?autoReconnect=true&useSSL=false&serverTimezone=UTC";

            try {

                Class.forName(driver);
                this.conn = DriverManager.getConnection(strUrl, login, mdp);
                // autorise la modification de la base de données par resultset
                this.statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }
            catch(ClassNotFoundException e) {
                System.out.println("Driver non charg� !!");
                e.printStackTrace();
            }
            catch(SQLException e) {
                System.out.println("Probl�me SQL !!" + e.getMessage());
                e.printStackTrace();
            }
        }


        public Connection getConn() {
            return this.conn;
        }


        public void setConn(Connection conn) {
            this.conn = conn;
        }


        public Statement getStatement() {
            return this.statement;
        }


        public void setStatement(Statement statement) {
            this.statement = statement;
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
         * destructeur de connexion
         *
         *
         **/
        public void closeConnection() {
            try {
                this.conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }	}
    }
