<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Agregar Material - Biblioteca Metrópolis</title>
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body>
<h1>Agregar Nuevo Material</h1>

<form action="AgregarMaterialServlet" method="post">

    Tipo:
    <select name="tipo" required>
        <option value="">--Selecciona el tipo--</option>
        <option value="Libro">Libro</option>
        <option value="Enciclopedia">Enciclopedia</option>
        <option value="Revista">Revista</option>
        <option value="Tesis">Tesis</option>
        <option value="DVD">DVD</option>
    </select>
    <br>

    Título: <input type="text" name="titulo" required><br>
    Año de Publicación: <input type="number" name="anoPublicacion" required><br>
    Número de Edición: <input type="text" name="numEdicion"><br>
    Cantidad: <input type="number" name="cantidad" required><br>
    Precio: <input type="number" step="0.01" name="precio" required><br>
    Descripción: <textarea name="descripcion"></textarea><br>

    Autor Principal:
    <select name="autorPrincipalId" required>
        <option value="">--Selecciona un autor--</option>
        <%
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca","root","");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id_autor, nombre FROM Autor");
                while(rs.next()) {
        %>
        <option value="<%= rs.getInt("id_autor") %>"><%= rs.getString("nombre") %></option>
        <%
                }
                rs.close(); stmt.close(); conn.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        %>
    </select>
    <br>

    Editorial:
    <select name="editorialId" required>
        <option value="">--Selecciona una editorial--</option>
        <%
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca","root","");
                Statement stmt2 = conn2.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT id_editorial, nombre FROM Editorial");
                while(rs2.next()) {
        %>
        <option value="<%= rs2.getInt("id_editorial") %>"><%= rs2.getString("nombre") %></option>
        <%
                }
                rs2.close(); stmt2.close(); conn2.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        %>
    </select>
    <br>

    URL Tesis: <input type="text" name="urlTesis"><br>
    Contacto Tesis: <input type="text" name="contactoTesis"><br>

    <input type="submit" value="Guardar Material">
</form>

<a href="index.jsp">Volver al Inicio</a>
</body>
</html>
