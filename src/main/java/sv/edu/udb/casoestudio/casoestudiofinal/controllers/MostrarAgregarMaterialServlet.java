package sv.edu.udb.casoestudio.casoestudiofinal.controllers;

import sv.edu.udb.casoestudio.casoestudiofinal.dao.AutorDAO;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Autor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/mostrarAgregarMaterial")
public class MostrarAgregarMaterialServlet extends HttpServlet {

    private AutorDAO autorDAO = new AutorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Autor> autores = autorDAO.listarTodos(); // método que devuelve todos los autores
        request.setAttribute("autores", autores);
        request.getRequestDispatcher("agregarMaterial.jsp").forward(request, response);
    }
}

