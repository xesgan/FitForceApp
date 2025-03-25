package roig.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public Usuari validateUser(String email, char[] password) {
        Usuari usuari = getUsuarisEmail(email);
        if (usuari != null) {
            String userPasswordHash = usuari.getPasswordHash();
            var result = BCrypt.verifyer().verify(password, userPasswordHash);
            if (result.verified) {
                return usuari; // Usuario válido
            }
        }
        return null; // Usuario no válido o no encontrado
    }

    public int registerUser(Usuari user) {
        int userId = -1;
        String query = "INSERT INTO Usuaris (nom, email, passwordHash, Instructor) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setBoolean(4, user.isInstructor());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1); // Obtener el ID generado
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId; // Devuelve el ID del usuario o -1 si hubo un error
    }

    public int getLastInsertUserId() {
        int userId = -1;
        String query = "SELECT SCOPE_IDENTITY() AS lastId"; // Para MS SQL Server
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                userId = rs.getInt("lastId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public boolean userExists(String email) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM Usuaris WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
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
