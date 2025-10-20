package sv.edu.udb.casoestudio.casoestudiofinal.dao;

import sv.edu.udb.casoestudio.casoestudiofinal.models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public List<Material> listarPorTipoYAntiguedad(String tipo, int maxAnios) {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiales WHERE tipo=? AND anoPublicacion>=YEAR(CURDATE())-?";
        try(Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ps.setInt(2, maxAnios);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id_material"));
                m.setTipo(rs.getString("tipo"));
                m.setTitulo(rs.getString("titulo"));
                m.setAnoPublicacion(rs.getInt("anoPublicacion"));
                m.setNumEdicion(rs.getString("numEdicion"));
                m.setPais(rs.getString("pais"));
                m.setCiudad(rs.getString("ciudad"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setPrecio(rs.getDouble("precio"));
                m.setDescripcion(rs.getString("descripcion"));

                // Cargar editorial y autores
                m.setEditorial(cargarEditorial(rs.getInt("id_editorial")));
                List<Autor> autores = cargarAutores(rs.getInt("id_material"));
                m.setAutores(autores);
                if(!autores.isEmpty()) m.setAutorPrincipal(autores.get(0));

                // Tags, URL y contacto tesis
                m.setTags(cargarTags(rs.getInt("id_material")));
                m.setUrlTesis(rs.getString("urlTesis"));
                m.setContactoTesis(rs.getString("contactoTesis"));

                lista.add(m);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Material> buscarPorParametros(String titulo, String autor, String editorial, String tag) {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM materiales m " +
                "LEFT JOIN Material_Autor ma ON m.id_material = ma.id_material " +
                "LEFT JOIN Autor a ON ma.id_autor = a.id_autor " +
                "LEFT JOIN Material_Tag mt ON m.id_material = mt.id_material " +
                "LEFT JOIN Tag t ON mt.id_tag = t.id_tag " +
                "WHERE m.titulo LIKE ? AND a.nombre LIKE ? AND m.id_editorial LIKE ? AND t.nombre LIKE ?";
        try(Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%"+titulo+"%");
            ps.setString(2, "%"+autor+"%");
            ps.setString(3, "%"+editorial+"%");
            ps.setString(4, "%"+tag+"%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id_material"));
                m.setTipo(rs.getString("tipo"));
                m.setTitulo(rs.getString("titulo"));
                m.setAnoPublicacion(rs.getInt("anoPublicacion"));
                m.setNumEdicion(rs.getString("numEdicion"));
                m.setPais(rs.getString("pais"));
                m.setCiudad(rs.getString("ciudad"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setPrecio(rs.getDouble("precio"));
                m.setDescripcion(rs.getString("descripcion"));

                m.setEditorial(cargarEditorial(rs.getInt("id_editorial")));
                List<Autor> autores = cargarAutores(rs.getInt("id_material"));
                m.setAutores(autores);
                if(!autores.isEmpty()) m.setAutorPrincipal(autores.get(0));

                m.setTags(cargarTags(rs.getInt("id_material")));
                m.setUrlTesis(rs.getString("urlTesis"));
                m.setContactoTesis(rs.getString("contactoTesis"));

                lista.add(m);
            }

        } catch(SQLException e) { e.printStackTrace(); }

        return lista;
    }

    // Métodos auxiliares
    private Editorial cargarEditorial(int idEditorial) {
        return new Editorial("Editorial " + idEditorial);
    }

    private List<Autor> cargarAutores(int idMaterial) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT a.id_autor, a.nombre, a.email FROM Autor a " +
                "JOIN Material_Autor ma ON a.id_autor = ma.id_autor " +
                "WHERE ma.id_material = ?";
        try(Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaterial);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Autor a = new Autor();
                a.setId(rs.getInt("id_autor"));
                a.setNombre(rs.getString("nombre"));
                a.setEmail(rs.getString("email"));
                autores.add(a);
            }

        } catch(SQLException e) { e.printStackTrace(); }
        return autores;
    }

    private List<Tag> cargarTags(int idMaterial) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.id_tag, t.nombre FROM Tag t " +
                "JOIN Material_Tag mt ON t.id_tag = mt.id_tag " +
                "WHERE mt.id_material = ?";
        try(Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaterial);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Tag t = new Tag();
                t.setId(rs.getInt("id_tag"));
                t.setNombre(rs.getString("nombre"));
                tags.add(t);
            }

        } catch(SQLException e) { e.printStackTrace(); }
        return tags;
    }
    public void agregarMaterial(Material material){
        String sql = "INSERT INTO materiales (tipo, titulo, numEdicion, cantidad, precio, urlTesis, contactoTesis, id_editorial) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = ConexionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, material.getTipo());
            ps.setString(2, material.getTitulo());
            ps.setString(3, material.getNumEdicion());
            ps.setInt(4, material.getCantidad());
            ps.setDouble(5, material.getPrecio());
            ps.setString(6, material.getUrlTesis());
            ps.setString(7, material.getContactoTesis());
            ps.setInt(8, material.getEditorial().getId());

            ps.executeUpdate();

            // Obtener id generado para relacionar autores y tags
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int idMaterial = rs.getInt(1);

                // Insertar autores
                for(Autor a : material.getAutores()){
                    String sqlAutor = "INSERT INTO Material_Autor (id_material, id_autor) VALUES (?, ?)";
                    try(PreparedStatement psAutor = conn.prepareStatement(sqlAutor)){
                        psAutor.setInt(1, idMaterial);
                        psAutor.setInt(2, a.getId());
                        psAutor.executeUpdate();
                    }
                }

                // Insertar tags
                for(Tag t : material.getTags()){
                    String sqlTag = "INSERT INTO Material_Tag (id_material, id_tag) VALUES (?, ?)";
                    try(PreparedStatement psTag = conn.prepareStatement(sqlTag)){
                        psTag.setInt(1, idMaterial);
                        psTag.setInt(2, t.getId());
                        psTag.executeUpdate();
                    }
                }
            }

        } catch(Exception e){ e.printStackTrace(); }
    }

}
