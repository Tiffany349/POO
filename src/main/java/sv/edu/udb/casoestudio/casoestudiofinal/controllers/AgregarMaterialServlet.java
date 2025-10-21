package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import sv.edu.udb.casoestudio.casoestudiofinal.dao.MaterialDAO;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;

import java.io.IOException;

@WebServlet("/AgregarMaterialServlet")
public class AgregarMaterialServlet extends HttpServlet {

    private MaterialDAO dao = new MaterialDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Recoger datos del formulario
            String tipo = request.getParameter("tipo");
            String titulo = request.getParameter("titulo");
            String numEdicion = request.getParameter("numEdicion");
            int anoPublicacion = Integer.parseInt(request.getParameter("anoPublicacion"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            String descripcion = request.getParameter("descripcion");
            int idAutorPrincipal = Integer.parseInt(request.getParameter("autorPrincipalId"));
            int idEditorial = Integer.parseInt(request.getParameter("editorialId"));
            String urlTesis = request.getParameter("urlTesis");
            String contactoTesis = request.getParameter("contactoTesis");

            // Crear objeto Material
            Material material = new Material();
            material.setTipo(tipo);
            material.setTitulo(titulo);
            material.setNumEdicion(numEdicion);
            material.setAnoPublicacion(anoPublicacion);
            material.setCantidad(cantidad);
            material.setPrecio(precio);
            material.setDescripcion(descripcion);
            material.setIdAutorPrincipal(idAutorPrincipal);
            material.setIdEditorial(idEditorial);
            material.setUrlTesis(urlTesis);
            material.setContactoTesis(contactoTesis);

            // Guardar en la base de datos
            dao.agregarMaterial(material);

            // Redirigir a index
            response.sendRedirect("index.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error al guardar el material: " + e.getMessage());
        }
    }
}
