package roig.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import roig.dto.Usuari;

/**
 *
 * @author Admin
 */
public class DataAccess {

    private Connection getConnection() {
        Connection con = null;

        String connectionString = "jdbc:sqlserver://localhost;database=simulapdb;user=sa;password=Pwd1234;encrypt=false;";
        try {
            con = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public ArrayList<Usuari> getUsuaris() {

        ArrayList<Usuari> usuaris = new ArrayList<>();
        String sql = "SELECT * FROM Usuaris";

        Connection con = getConnection();
        try {
            PreparedStatement selectStatement = con.prepareStatement(sql);
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Usuari user = new Usuari();
                user.setId(rs.getInt("Id"));
                user.setNom(rs.getString("Nom"));
                user.setEmail(rs.getString("Email"));
                user.setPasswordHash(rs.getString("PasswordHash"));
//                user.setFoto(rs.getBytes("Foto"));
                user.setInstructor(rs.getBoolean("Instructor"));
                usuaris.add(user);
            }
            selectStatement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuaris;
    }

    public int registerUser(Usuari u) {
        int newUserId = 0;
        Connection con = getConnection();
        String sql = "INSERT INTO Usuaris(Nom, Email, PasswordHash, Instructor) "
                + " VALUES (?,?,?,?)";

        try {
            PreparedStatement insertStatement = con.prepareStatement(sql);
            insertStatement.setString(1, u.getNom());
            insertStatement.setString(2, u.getEmail());
            insertStatement.setString(3, u.getPasswordHash());
            insertStatement.setBoolean(4, u.isInstructor());
            newUserId = insertStatement.executeUpdate();
            insertStatement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getLastInsertUserId() {
        String sql = "SELECT MAX(Id) FROM Usuaris";
        int userId = 0;
        Connection con = getConnection();
        try {
            PreparedStatement selectStatement = con.prepareStatement(sql);
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                userId = rs.getInt(1);
            }
            selectStatement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userId;
    }

    public Usuari getUsuarisEmail(String email) {
        Usuari user = null;
        String sql = "SELECT * FROM Usuaris WHERE Email=?";

        Connection con = getConnection();
        try {
            PreparedStatement selectStatement = con.prepareStatement(sql);
            selectStatement.setString(1, email);
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                user = new Usuari();
                user.setId(rs.getInt("Id"));
                user.setNom(rs.getString("Nom"));
                user.setEmail(rs.getString("Email"));
                user.setPasswordHash(rs.getString("PasswordHash"));
//                user.setFoto(rs.getBytes("Foto"));
                user.setInstructor(rs.getBoolean("Instructor"));
            }
            selectStatement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
}
