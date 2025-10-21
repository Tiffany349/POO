<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="sv.edu.udb.casoestudio.casoestudiofinal.models.Autor" %>
<!DOCTYPE html>
<html>
<head>
    <title>Agregar Material</title>
</head>
<body>
<h1>Agregar Material</h1>

<form action="AgregarMaterialServlet" method="get">
    Tipo:
    <select name="tipo" required>
        <option value="Libro">Libro</option>
        <option value="Enciclopedia">Enciclopedia</option>
        <option value="Revista">Revista</option>
        <option value="Tesis">Tesis</option>
        <option value="DVD">DVD</option>
    </select><br>

    Título: <input type="text" name="titulo" required><br>

    Año de publicación: <input type="number" name="anoPublicacion" required><br>

    Número de edición: <input type="text" name="numEdicion"><br>

    Cantidad: <input type="number" name="cantidad" required><br>

    Precio: <input type="number" step="0.01" name="precio" required><br>

    Descripción: <input type="text" name="descripcion"><br>

    <!-- Autor principal editable -->
    Autor principal:
    <input type="text" name="autorPrincipal" list="autoresList" required>
    <datalist id="autoresList">
        <%
            List<Autor> autores = (List<Autor>) request.getAttribute("autores");
            if(autores != null) {
                for(Autor a : autores){
        %>
        <option value="<%= a.getNombre() %>">
                <%
                }
            }
        %>
    </datalist>
    <br>

    Casa Editorial: <input type="text" name="editorial"><br>

    URL de tesis: <input type="text" name="urlTesis"><br>
    Contacto de tesis: <input type="text" name="contactoTesis"><br>

    <input type="submit" value="Agregar Material">
</form>

<a href="index.jsp">Volver</a>
</body>
</html>
