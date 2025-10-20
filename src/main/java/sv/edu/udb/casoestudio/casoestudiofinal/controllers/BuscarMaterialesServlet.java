package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import sv.edu.udb.casoestudio.casoestudiofinal.service.MaterialService;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/buscar")
public class BuscarMaterialesServlet extends HttpServlet {
    private MaterialService service = new MaterialService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String editorial = request.getParameter("editorial");
        String tag = request.getParameter("tag");
        List<Material> lista = service.buscarMateriales(titulo, autor, editorial, tag);
        request.setAttribute("materiales", lista);
        request.getRequestDispatcher("resultados.jsp").forward(request, response);
    }
}

