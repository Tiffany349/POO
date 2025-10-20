package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import sv.edu.udb.casoestudio.casoestudiofinal.service.MaterialService;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/listar")
public class ListarMaterialesServlet extends HttpServlet {
    private MaterialService service = new MaterialService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        int maxAnios = Integer.parseInt(request.getParameter("maxAnios"));
        List<Material> lista = service.getMaterialesPorTipoYAntiguedad(tipo, maxAnios);
        request.setAttribute("materiales", lista);
        request.getRequestDispatcher("resultados.jsp").forward(request, response);
    }
}

