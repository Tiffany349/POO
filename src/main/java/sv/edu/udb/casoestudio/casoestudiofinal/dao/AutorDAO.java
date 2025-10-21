package sv.edu.udb.casoestudio.casoestudiofinal.dao;

import sv.edu.udb.casoestudio.casoestudiofinal.models.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Importa la clase de conexión a la base de datos
import sv.edu.udb.casoestudio.casoestudiofinal.dao.ConexionDB;

public class AutorDAO {

    // Método que lista todos los autores
    public List<Autor> listarTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT id_autor, nombre, email FROM Autor";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Autor a = new Autor();
                a.setId(rs.getInt("id_autor"));
                a.setNombre(rs.getString("nombre"));
                a.setEmail(rs.getString("email"));
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
