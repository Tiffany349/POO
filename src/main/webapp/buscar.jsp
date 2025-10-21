<%--
  Created by IntelliJ IDEA.
  User: yynah
  Date: 19/10/2025
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="sv.edu.udb.casoestudio.casoestudiofinal.models.Material" %>
<!DOCTYPE html>
<html>
<head>
    <title>Buscar Materiales - Biblioteca Metrópolis</title>
    <link rel="stylesheet" type="text/css" href="css/estilos.css">
</head>
<body>
<h1>Buscar Materiales</h1>

<form action="buscar" method="get">
    <label for="titulo">Título:</label>
    <input type="text" id="titulo" name="titulo"><br>

    <label for="autor">Autor:</label>
    <input type="text" id="autor" name="autor"><br>

    <label for="editorial">Editorial:</label>
    <input type="text" id="editorial" name="editorial"><br>

    <label for="tag">Etiqueta / Palabra clave:</label>
    <input type="text" id="tag" name="tag"><br>

    <button type="submit">Buscar</button>
</form>

<a href="index.jsp">Volver al inicio</a>
</body>
</html>
