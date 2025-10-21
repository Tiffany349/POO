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
    private final MaterialService service = new MaterialService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String titulo = trimOrNull(request.getParameter("titulo"));
        String autor = trimOrNull(request.getParameter("autor"));
        String editorial = trimOrNull(request.getParameter("editorial"));
        String tag = trimOrNull(request.getParameter("tag"));

        List<Material> lista = service.buscarMateriales(titulo, autor, editorial, tag);

        request.setAttribute("materiales", lista);
        request.getRequestDispatcher("/resultados.jsp").forward(request, response);
    }
    private String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

}

