package sv.edu.udb.casoestudio.casoestudiofinal.dao;

import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    // 1️⃣ Agregar un material y asignar autor principal
    public void agregarMaterial(Material m) {
        String sqlMaterial = "INSERT INTO Material (tipo, titulo, ano_publicacion, num_edicion, id_editorial, cantidad, precio, descripcion, url_tesis, contacto_tesis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAutor = "INSERT INTO Material_Autor (id_material, id_autor, es_principal) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psMaterial = conn.prepareStatement(sqlMaterial, Statement.RETURN_GENERATED_KEYS)) {
                psMaterial.setString(1, m.getTipo());
                psMaterial.setString(2, m.getTitulo());
                psMaterial.setInt(3, m.getAnoPublicacion());
                psMaterial.setString(4, m.getNumEdicion());
                psMaterial.setInt(5, m.getIdEditorial());
                psMaterial.setInt(6, m.getCantidad());
                psMaterial.setDouble(7, m.getPrecio());
                psMaterial.setString(8, m.getDescripcion());
                psMaterial.setString(9, m.getUrlTesis());
                psMaterial.setString(10, m.getContactoTesis());

                psMaterial.executeUpdate();

                try (ResultSet rs = psMaterial.getGeneratedKeys()) {
                    int idMaterial = 0;
                    if (rs.next()) idMaterial = rs.getInt(1);

                    try (PreparedStatement psAutor = conn.prepareStatement(sqlAutor)) {
                        psAutor.setInt(1, idMaterial);
                        psAutor.setInt(2, m.getIdAutorPrincipal());
                        psAutor.setBoolean(3, true);
                        psAutor.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2️⃣ Listar materiales por tipo y antigüedad
    public List<Material> listarPorTipoYAntiguedad(String tipo, int maxAnios) {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM Material WHERE tipo = ? AND ano_publicacion >= YEAR(CURDATE()) - ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ps.setInt(2, maxAnios);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = mapearMaterial(rs);
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 3️⃣ Buscar materiales por parámetros
    public List<Material> buscarPorParametros(String titulo, String autor, String editorial, String tag) {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* " +
                "FROM Material m " +
                "LEFT JOIN Material_Autor ma ON m.id_material = ma.id_material " +
                "LEFT JOIN Autor a ON ma.id_autor = a.id_autor " +
                "LEFT JOIN Editorial e ON m.id_editorial = e.id_editorial " +
                "LEFT JOIN Material_Tag mt ON m.id_material = mt.id_material " +
                "LEFT JOIN Tag t ON mt.id_tag = t.id_tag " +
                "WHERE (m.titulo LIKE ? OR ? = '') " +
                "AND (a.nombre LIKE ? OR ? = '') " +
                "AND (e.nombre LIKE ? OR ? = '') " +
                "AND (t.nombre LIKE ? OR ? = '')";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%");
            ps.setString(2, titulo);
            ps.setString(3, "%" + autor + "%");
            ps.setString(4, autor);
            ps.setString(5, "%" + editorial + "%");
            ps.setString(6, editorial);
            ps.setString(7, "%" + tag + "%");
            ps.setString(8, tag);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = mapearMaterial(rs);
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // 🔹 Método auxiliar para mapear ResultSet a Material
    private Material mapearMaterial(ResultSet rs) throws SQLException {
        Material m = new Material();
        m.setIdMaterial(rs.getInt("id_material"));
        m.setTipo(rs.getString("tipo"));
        m.setTitulo(rs.getString("titulo"));
        m.setNumEdicion(rs.getString("num_edicion"));
        m.setAnoPublicacion(rs.getInt("ano_publicacion"));
        m.setCantidad(rs.getInt("cantidad"));
        m.setPrecio(rs.getDouble("precio"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setUrlTesis(rs.getString("url_tesis"));
        m.setContactoTesis(rs.getString("contacto_tesis"));
        m.setIdEditorial(rs.getInt("id_editorial"));
        return m;
    }
}
