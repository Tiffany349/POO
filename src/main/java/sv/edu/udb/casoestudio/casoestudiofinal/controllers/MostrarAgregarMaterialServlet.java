package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/mostrarAgregarMaterial")
public class MostrarAgregarMaterialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CORRECTO: JSP en la raíz de webapp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/agregarMaterial.jsp");
        dispatcher.forward(request, response);
    }
}
