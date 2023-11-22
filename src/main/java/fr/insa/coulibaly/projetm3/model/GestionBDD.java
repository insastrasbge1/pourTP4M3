/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.coulibaly.projetm3.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 *
 * @author francois
 */
public class GestionBDD {

    public static Connection connectGeneralMySQL(String host,
            int port, String database,
            String user, String pass)
            throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    /**
     * Une base de donnée en mémoire. Ne peut bien sûr servir que pour les
     * tests. Remarquez qu'il faut souvent réinitialiser le schéma 
     * la bdd est en mémoire, et on repart donc avec une BDD vide (ni table ni rien)
     * à chaque exécution du programme.
     *
     * @return
     * @throws SQLException
     */
    public static Connection connectH2EmbededInMemory() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:h2:mem:test");
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        // pour savoir s'il faut recréer le schéma, on teste l'existence de la table li_client
        // attention : les noms sans guillemets sont souvent convertis en majuscule par SQL
        // pour être tranquile on teste li_client ou LI_CLIENT
        DatabaseMetaData meta = con.getMetaData();
        ResultSet table1 = meta.getTables(null, null, "li_client", new String[] {"TABLE"});
        boolean found = table1.next();
        ResultSet table2 = meta.getTables(null, null, "LI_CLIENT", new String[] {"TABLE"});
        found = found || table2.next();
        if (! found) {
            razBDD(con);
        }
        return con;
    }

    public static Connection connectSurServeurM3() throws SQLException {
        return connectGeneralMySQL("92.222.25.165", 3306,
                "ici votre bdd ex : m3_acoulibaly01", "ici votre nom ex : m3_acoulibaly01",
                "ici votre pass ex : pas d'exemple :)");
    }

    /**
     * Creation du schéma. On veut créer tout ou rien, d'où la gestion explicite
     * des transactions.
     *
     * @throws SQLException
     */
    public static void creeSchema(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "create table li_client (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    nom varchar(30) not null unique,\n"
                    + "    pass varchar(30) not null\n"
                    + ")\n"
            );
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Suppression du schéma. Le schéma n'est peut-être pas créé, ou pas
     * entièrement créé, on ne s'arrête donc pas en cas d'erreur : on ne fait
     * que passer à la suite
     *
     * @throws SQLException
     */
    public static void deleteSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate("drop table li_client");
            } catch (SQLException ex) {
            }
        }
    }

    public static void initTest(Connection conn) throws SQLException {
        inscription(conn, "toto");
        inscription(conn, "titi");
    }

    public static void razBDD(Connection conn) throws SQLException {
        deleteSchema(conn);
        creeSchema(conn);
        initTest(conn);
    }

    public static Optional<String> inscription(Connection con, String nom) throws SQLException {
        try {
            con.setAutoCommit(false);
            if (testeLogin(con, nom)) {
                return Optional.empty();
            } else {
                try (PreparedStatement pst = con.prepareStatement(
                        "insert into li_client (nom,pass) values (?,?)")) {
                    String pass = String.format("%08X", new Random().nextInt());
                    pst.setString(1, nom);
                    pst.setString(2, pass);
                    pst.executeUpdate();
                    return Optional.of(pass);
                }
            }
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static boolean testeLogin(Connection con, String nom) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select * from li_client where nom = ?")) {
            pst.setString(1, nom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public static void debut() {
        try (Connection con = connectH2EmbededInMemory()) {
            System.out.println("connecté");
            menuPrincipal(con);
        } catch (SQLException ex) {
            throw new Error("Connection impossible", ex);
        }
    }

    public static void afficheLesUtilisateurs(Connection con)
            throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,nom,pass from li_client")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String pass = rs.getString("pass");
                    System.out.println("Client " + nom + " ; pass : " + pass);
                }
            }
        }
    }

    public static void menuPrincipal(Connection conn) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu principal");
            System.out.println("==============");
            System.out.println((i++) + ") supprimer schéma");
            System.out.println((i++) + ") créer schéma");
            System.out.println((i++) + ") RAZ BDD = supp + crée + init");
            System.out.println((i++) + ") liste des client");
            System.out.println("0) Fin");
            System.out.println("Votre choix : ");
            rep = Lire.i();
            try {
                int j = 1;
                if (rep == j++) {
                    deleteSchema(conn);
                } else if (rep == j++) {
                    creeSchema(conn);
                } else if (rep == j++) {
                    razBDD(conn);
                } else if (rep == j++) {
                    afficheLesUtilisateurs(conn);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    public static void main(String[] args) {
        debut();
    }

}
