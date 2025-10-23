package sv.edu.udb.casoestudio.casoestudiofinal.dao;

import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Editorial;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Autor;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {



    /* ===========================
       BUSCAR CON FILTROS / KEYWORD
       =========================== */
    public List<Material> buscarPorParametros(String titulo, String autor, String editorial, String tag) {
        List<Material> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT " +
                        "  m.id_material           AS id_material, " +
                        "  m.tipo                  AS tipo, " +
                        "  m.titulo                AS titulo, " +
                        "  m.ano_publicacion       AS anoPublicacion, " +
                        "  m.num_edicion           AS numEdicion, " +
                        "  m.cantidad              AS cantidad, " +
                        "  m.precio                AS precio, " +
                        "  m.descripcion           AS descripcion, " +
                        "  m.url_tesis             AS urlTesis, " +
                        "  m.contacto_tesis        AS contactoTesis, " +
                        "  m.id_editorial          AS id_editorial, " +
                        "  ed.nombre               AS editorialNombre, " +
                        "  ed.pais                 AS pais, " +
                        "  ed.ciudad               AS ciudad " +
                        "FROM Material m " +
                        "LEFT JOIN Editorial      ed ON ed.id_editorial = m.id_editorial " +
                        "LEFT JOIN Material_Autor ma ON ma.id_material   = m.id_material " +
                        "LEFT JOIN Autor          a  ON a.id_autor       = ma.id_autor " +
                        "LEFT JOIN Material_Tag   mt ON mt.id_material   = m.id_material " +
                        "LEFT JOIN Tag            t  ON t.id_tag         = mt.id_tag " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (titulo != null && !titulo.isBlank()) {
            sql.append(" AND LOWER(m.titulo) LIKE ? ");
            params.add("%" + titulo.toLowerCase().trim() + "%");
        }
        if (autor != null && !autor.isBlank()) {
            sql.append(" AND EXISTS ( " +
                    "   SELECT 1 FROM Material_Autor ma2 " +
                    "   JOIN Autor a2 ON a2.id_autor = ma2.id_autor " +
                    "   WHERE ma2.id_material = m.id_material " +
                    "     AND LOWER(a2.nombre) LIKE ? " +
                    ") ");
            params.add("%" + autor.toLowerCase().trim() + "%");
        }
        if (editorial != null && !editorial.isBlank()) {
            sql.append(" AND LOWER(ed.nombre) LIKE ? ");
            params.add("%" + editorial.toLowerCase().trim() + "%");
        }
        if (tag != null && !tag.isBlank()) {
            String like = "%" + tag.toLowerCase().trim() + "%";
            sql.append(" AND ( " +
                    "   LOWER(m.titulo) LIKE ? " +
                    "   OR LOWER(ed.nombre) LIKE ? " +
                    "   OR EXISTS ( " +
                    "       SELECT 1 FROM Material_Autor ma3 " +
                    "       JOIN Autor a3 ON a3.id_autor = ma3.id_autor " +
                    "       WHERE ma3.id_material = m.id_material " +
                    "         AND LOWER(a3.nombre) LIKE ? " +
                    "   ) " +
                    "   OR EXISTS ( " +
                    "       SELECT 1 FROM Material_Tag mt2 " +
                    "       JOIN Tag t2 ON t2.id_tag = mt2.id_tag " +
                    "       WHERE mt2.id_material = m.id_material " +
                    "         AND LOWER(t2.nombre) LIKE ? " +
                    "   ) " +
                    ") ");
            params.add(like); // titulo
            params.add(like); // editorial
            params.add(like); // autor
            params.add(like); // tag
        }

        sql.append(" ORDER BY m.titulo ASC ");

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            for (Object p : params) ps.setObject(i++, p);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material m = mapMaterialBasico(rs);
                    // Relacionados
                    m.setEditorial(cargarEditorial(rs.getInt("id_editorial")));
                    m.setAutores(cargarAutores(m.getId()));
                    if (!m.getAutores().isEmpty()) m.setAutorPrincipal(m.getAutores().get(0));
                    m.setTags(cargarTags(m.getId()));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /* ===========================
       LISTAR POR TIPO Y ANTIGÜEDAD
       =========================== */
    public List<Material> listarPorTipoYAntiguedad(String tipo, int maxAnios) {
        List<Material> lista = new ArrayList<>();

        String sql =
                "SELECT m.id_material AS id_material, m.tipo AS tipo, m.titulo AS titulo, " +
                        "       m.ano_publicacion AS anoPublicacion, m.num_edicion AS numEdicion, " +
                        "       m.cantidad AS cantidad, m.precio AS precio, m.descripcion AS descripcion, " +
                        "       m.url_tesis AS urlTesis, m.contacto_tesis AS contactoTesis, " +
                        "       m.id_editorial AS id_editorial, ed.nombre AS editorialNombre, " +
                        "       ed.pais AS pais, ed.ciudad AS ciudad " +
                        "FROM Material m " +
                        "LEFT JOIN Editorial ed ON ed.id_editorial = m.id_editorial " +
                        "WHERE m.tipo = ? AND m.ano_publicacion >= YEAR(CURDATE()) - ? " +
                        "ORDER BY m.ano_publicacion DESC, m.titulo ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ps.setInt(2, maxAnios);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Material m = mapMaterialBasico(rs);
                    m.setEditorial(cargarEditorial(rs.getInt("id_editorial")));
                    m.setAutores(cargarAutores(m.getId()));
                    m.setTags(cargarTags(m.getId()));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /* =========
       CREAR
       ========= */
    public void agregarMaterial(Material material) {
        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            Integer editorialId = null;
            if (material.getEditorial() != null && material.getEditorial().getNombre() != null) {
                editorialId = ensureEditorial(conn, material.getEditorial());
            }

            int materialId = insertMaterial(conn, material, editorialId);

            // Autores (el primero marcado como principal)
            if (material.getAutores() != null) {
                boolean first = true;
                for (Autor a : material.getAutores()) {
                    if (a == null || a.getNombre() == null || a.getNombre().isBlank()) continue;
                    int autorId = ensureAutor(conn, a);
                    insertMaterialAutor(conn, materialId, autorId, first);
                    first = false;
                }
            }

            // Tags
            if (material.getTags() != null) {
                for (Tag t : material.getTags()) {
                    if (t == null || t.getNombre() == null || t.getNombre().isBlank()) continue;
                    int tagId = ensureTag(conn, t);
                    insertMaterialTag(conn, materialId, tagId);
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* =========
       MODIFICAR
       ========= */
    public void actualizarMaterial(Material material) {
        if (material.getId() == 0) {
            throw new IllegalArgumentException("El material debe tener id para actualizarse");
        }

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            Integer editorialId = null;
            if (material.getEditorial() != null && material.getEditorial().getNombre() != null) {
                editorialId = ensureEditorial(conn, material.getEditorial());
            }

            updateMaterial(conn, material, editorialId);

            // Relaciones: borramos y reinsertamos (simple y seguro)
            deleteMaterialAutores(conn, material.getId());
            deleteMaterialTags(conn, material.getId());

            if (material.getAutores() != null) {
                boolean first = true;
                for (Autor a : material.getAutores()) {
                    if (a == null || a.getNombre() == null || a.getNombre().isBlank()) continue;
                    int autorId = ensureAutor(conn, a);
                    insertMaterialAutor(conn, material.getId(), autorId, first);
                    first = false;
                }
            }

            if (material.getTags() != null) {
                for (Tag t : material.getTags()) {
                    if (t == null || t.getNombre() == null || t.getNombre().isBlank()) continue;
                    int tagId = ensureTag(conn, t);
                    insertMaterialTag(conn, material.getId(), tagId);
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ===========================
       HELPERS DE LECTURA RELACIONADOS
       =========================== */
    private Editorial cargarEditorial(int idEditorial) {
        if (idEditorial <= 0) return null;

        String sql = "SELECT id_editorial, nombre, pais, ciudad " +
                "FROM Editorial WHERE id_editorial = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Editorial ed = new Editorial();
                    ed.setId(rs.getInt("id_editorial"));
                    ed.setNombre(rs.getString("nombre"));
                    ed.setPais(rs.getString("pais"));
                    ed.setCiudad(rs.getString("ciudad"));
                    return ed;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Autor> cargarAutores(int idMaterial) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT a.id_autor, a.nombre, a.email, ma.es_principal " +
                "FROM Autor a " +
                "JOIN Material_Autor ma ON a.id_autor = ma.id_autor " +
                "WHERE ma.id_material = ? " +
                "ORDER BY ma.es_principal DESC, a.nombre ASC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaterial);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Autor a = new Autor();
                    a.setId(rs.getInt("id_autor"));
                    a.setNombre(rs.getString("nombre"));
                    a.setEmail(rs.getString("email"));
                    // si tu modelo tiene setPrincipal(Boolean)
                    try {
                        a.getClass().getMethod("setPrincipal", Boolean.class)
                                .invoke(a, rs.getBoolean("es_principal"));
                    } catch (Exception ignore) {}
                    autores.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autores;
    }

    private List<Tag> cargarTags(int idMaterial) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.id_tag, t.nombre " +
                "FROM Tag t " +
                "JOIN Material_Tag mt ON t.id_tag = mt.id_tag " +
                "WHERE mt.id_material = ? " +
                "ORDER BY t.nombre";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMaterial);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tag t = new Tag();
                    t.setId(rs.getInt("id_tag"));
                    t.setNombre(rs.getString("nombre"));
                    tags.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    /* ===========================
       HELPERS DE ESCRITURA/UPSERT
       =========================== */
    private int insertMaterial(Connection conn, Material m, Integer editorialId) throws SQLException {

        String sql = "INSERT INTO Material (tipo, titulo, ano_publicacion, num_edicion, id_editorial, " +
                "cantidad, precio, descripcion, url_tesis, contacto_tesis) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getTipo());
            ps.setString(2, m.getTitulo());

            if (m.getAnoPublicacion() == 0) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, m.getAnoPublicacion());

            if (m.getNumEdicion() == null || m.getNumEdicion().isBlank()) ps.setNull(4, Types.VARCHAR);
            else ps.setString(4, m.getNumEdicion());

            if (editorialId == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, editorialId);

            ps.setInt(6, m.getCantidad());
            ps.setDouble(7, m.getPrecio());

            if (m.getDescripcion() == null || m.getDescripcion().isBlank()) ps.setNull(8, Types.LONGVARCHAR);
            else ps.setString(8, m.getDescripcion());

            if (m.getUrlTesis() == null || m.getUrlTesis().isBlank()) ps.setNull(9, Types.VARCHAR);
            else ps.setString(9, m.getUrlTesis());

            if (m.getContactoTesis() == null || m.getContactoTesis().isBlank()) ps.setNull(10, Types.VARCHAR);
            else ps.setString(10, m.getContactoTesis());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se obtuvo id generado para Material");
    }

    private void updateMaterial(Connection conn, Material m, Integer editorialId) throws SQLException {
        String sql = "UPDATE Material SET " + "tipo=?, titulo=?, ano_publicacion=?, num_edicion=?, id_editorial=?, " + "cantidad=?, precio=?, descripcion=?, url_tesis=?, contacto_tesis=? " + "WHERE id_material=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getTipo());
            ps.setString(2, m.getTitulo());


            if (m.getAnoPublicacion() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, m.getAnoPublicacion());

            if (m.getCantidad() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, m.getCantidad());

// precio
            if (m.getPrecio() == null) ps.setNull(7, Types.DECIMAL); // o Types.DOUBLE
            else ps.setDouble(7, m.getPrecio());


            if (m.getNumEdicion() == null || m.getNumEdicion().isBlank()) ps.setNull(4, Types.VARCHAR);
            else ps.setString(4, m.getNumEdicion());

            if (editorialId == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, editorialId);






            if (m.getDescripcion() == null || m.getDescripcion().isBlank()) ps.setNull(8, Types.LONGVARCHAR);
            else ps.setString(8, m.getDescripcion());

            if (m.getUrlTesis() == null || m.getUrlTesis().isBlank()) ps.setNull(9, Types.VARCHAR);
            else ps.setString(9, m.getUrlTesis());

            if (m.getContactoTesis() == null || m.getContactoTesis().isBlank()) ps.setNull(10, Types.VARCHAR);
            else ps.setString(10, m.getContactoTesis());

            ps.setInt(11, m.getId());
            ps.executeUpdate();
        }
    }

    private int ensureEditorial(Connection conn, Editorial ed) throws SQLException {
        String q = "SELECT id_editorial FROM Editorial WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, ed.getNombre().trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String ins = "INSERT INTO Editorial (nombre, pais, ciudad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ed.getNombre().trim());
            ps.setString(2, ed.getPais());
            ps.setString(3, ed.getCiudad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear Editorial");
    }

    private int ensureAutor(Connection conn, Autor a) throws SQLException {
        String q = "SELECT id_autor FROM Autor WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, a.getNombre().trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String ins = "INSERT INTO Autor (nombre, email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombre().trim());
            if (a.getEmail() == null || a.getEmail().isBlank()) ps.setNull(2, Types.VARCHAR);
            else ps.setString(2, a.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear Autor");
    }

    private int ensureTag(Connection conn, Tag t) throws SQLException {
        String q = "SELECT id_tag FROM Tag WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, t.getNombre().trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String ins = "INSERT INTO Tag (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNombre().trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear Tag");
    }

    private void insertMaterialAutor(Connection conn, int materialId, int autorId, boolean principal) throws SQLException {
        String sql = "INSERT INTO Material_Autor (id_material, id_autor, es_principal) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setInt(2, autorId);
            ps.setBoolean(3, principal);
            ps.executeUpdate();
        }
    }

    private void insertMaterialTag(Connection conn, int materialId, int tagId) throws SQLException {
        String sql = "INSERT INTO Material_Tag (id_material, id_tag) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setInt(2, tagId);
            ps.executeUpdate();
        }
    }

    private void deleteMaterialAutores(Connection conn, int materialId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Material_Autor WHERE id_material=?")) {
            ps.setInt(1, materialId);
            ps.executeUpdate();
        }
    }

    private void deleteMaterialTags(Connection conn, int materialId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Material_Tag WHERE id_material=?")) {
            ps.setInt(1, materialId);
            ps.executeUpdate();
        }
    }

    /* ===========================
       MAPEO BÁSICO (desde SELECT)
       =========================== */
    private Material mapMaterialBasico(ResultSet rs) throws SQLException {
        Material m = new Material();
        m.setId(rs.getInt("id_material"));
        m.setTipo(rs.getString("tipo"));
        m.setTitulo(rs.getString("titulo"));
        m.setAnoPublicacion(rs.getInt("anoPublicacion"));
        m.setNumEdicion(rs.getString("numEdicion"));
        m.setCantidad((Integer) rs.getObject("cantidad"));
        m.setPrecio((rs.getObject("precio") == null) ? null : rs.getDouble("precio"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setUrlTesis(rs.getString("urlTesis"));
        m.setContactoTesis(rs.getString("contactoTesis"));
        m.setPais(rs.getString("pais"));
        m.setCiudad(rs.getString("ciudad"));
        return m;
    }
}
