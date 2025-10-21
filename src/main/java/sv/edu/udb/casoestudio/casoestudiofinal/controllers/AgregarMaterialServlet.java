package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sv.edu.udb.casoestudio.casoestudiofinal.dao.MaterialDAO;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Autor;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;

import java.io.IOException;

@WebServlet("/AgregarMaterialServlet")
public class AgregarMaterialServlet extends HttpServlet {

    private MaterialDAO dao = new MaterialDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        String titulo = request.getParameter("titulo");
        int anoPublicacion = Integer.parseInt(request.getParameter("anoPublicacion"));
        String numEdicion = request.getParameter("numEdicion");
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        double precio = Double.parseDouble(request.getParameter("precio"));
        String descripcion = request.getParameter("descripcion");
        String autorPrincipal = request.getParameter("autorPrincipal");
        String editorial = request.getParameter("editorial");
        String urlTesis = request.getParameter("urlTesis");
        String contactoTesis = request.getParameter("contactoTesis");

        Material material = new Material();
        material.setTipo(tipo);
        material.setTitulo(titulo);
        material.setAnoPublicacion(anoPublicacion);
        material.setNumEdicion(numEdicion);
        material.setCantidad(cantidad);
        material.setPrecio(precio);
        material.setDescripcion(descripcion);
        material.setUrlTesis(urlTesis);
        material.setContactoTesis(contactoTesis);

        // Crear Autor principal
        Autor autor = new Autor();
        autor.setNombre(autorPrincipal);
        material.setAutorPrincipal(autor); // método que debes tener en Material.java

        // Si quieres, puedes añadirlo a la lista de autores
        material.getAutores().add(autor);

        // Casa editorial
        material.getEditorial().setNombre(editorial);

        // Guardar en DB
        dao.agregarMaterial(material);

        response.sendRedirect("index.jsp");
    }
}
