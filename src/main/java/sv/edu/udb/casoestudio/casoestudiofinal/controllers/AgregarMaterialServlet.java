package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sv.edu.udb.casoestudio.casoestudiofinal.dao.MaterialDAO;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Autor;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Editorial;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AgregarMaterialServlet")
public class AgregarMaterialServlet extends HttpServlet {

    private final MaterialDAO dao = new MaterialDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // === Leer parámetros (null/blank-safe)
        String tipo            = trimOrNull(request.getParameter("tipo"));
        String titulo          = trimOrNull(request.getParameter("titulo"));
        String anoPublicacionS = trimOrNull(request.getParameter("anoPublicacion"));
        String numEdicion      = trimOrNull(request.getParameter("numEdicion"));
        String cantidadS       = trimOrNull(request.getParameter("cantidad"));
        String precioS         = trimOrNull(request.getParameter("precio"));
        String descripcion     = trimOrNull(request.getParameter("descripcion"));
        String autorPrincipal  = trimOrNull(request.getParameter("autorPrincipal"));
        String editorialNombre = trimOrNull(request.getParameter("editorial"));
        String urlTesis        = trimOrNull(request.getParameter("urlTesis"));
        String contactoTesis   = trimOrNull(request.getParameter("contactoTesis"));

        // Parseo seguro
        Integer anoPublicacion = parseIntOrNull(anoPublicacionS);
        Integer cantidad       = parseIntOrNull(cantidadS);
        Double  precio         = parseDoubleOrNull(precioS);

        // === Construir Material
        Material material = new Material();
        material.setTipo(tipo);
        material.setTitulo(titulo);
        // OJO: si tu modelo usa int primitivo, ajusta según corresponda
        if (anoPublicacion != null) material.setAnoPublicacion(anoPublicacion);
        material.setNumEdicion(numEdicion);
        if (cantidad != null) material.setCantidad(cantidad);
        if (precio != null) material.setPrecio(precio);
        material.setDescripcion(descripcion);
        material.setUrlTesis(urlTesis);
        material.setContactoTesis(contactoTesis);

        // === Autores (primero es principal)
        List<Autor> autores = new ArrayList<>();
        if (autorPrincipal != null) {
            Autor a = new Autor();
            a.setNombre(autorPrincipal);
            autores.add(a); // será principal en el DAO (por ser primero)
            material.setAutorPrincipal(a); // opcional, para la vista
        }
        material.setAutores(autores);

        // === Editorial (solo si hay nombre)
        if (editorialNombre != null) {
            Editorial ed = new Editorial();
            ed.setNombre(editorialNombre);
            material.setEditorial(ed);
        }

        // === Guardar
        dao.agregarMaterial(material);

        // Redirigir
        response.sendRedirect("index.jsp");
    }

    // Helpers simples
    private static String trimOrNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private static Integer parseIntOrNull(String s) {
        try { return (s == null) ? null : Integer.valueOf(s); }
        catch (NumberFormatException e) { return null; }
    }

    private static Double parseDoubleOrNull(String s) {
        try { return (s == null) ? null : Double.valueOf(s); }
        catch (NumberFormatException e) { return null; }
    }

    // Si igual querés permitir GET temporalmente:
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
