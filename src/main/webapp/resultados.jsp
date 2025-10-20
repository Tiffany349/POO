<%--
  Created by IntelliJ IDEA.
  User: yynah
  Date: 19/10/2025
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%-- Resultados.jsp corregido --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="sv.edu.udb.casoestudio.casoestudiofinal.models.Material" %>
<%@ page import="sv.edu.udb.casoestudio.casoestudiofinal.models.Autor" %>
<%@ page import="sv.edu.udb.casoestudio.casoestudiofinal.models.Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title>Resultados - Biblioteca Metrópolis</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>Resultados de la búsqueda</h1>

<%
    List<Material> lista = (List<Material>) request.getAttribute("materiales");
    if(lista != null && !lista.isEmpty()){
%>
<table border="1">
    <thead>
    <tr>
        <th>Título</th>
        <th>Tipo</th>
        <th>Año</th>
        <th>Autor(es)</th>
        <th>Autor Principal</th>
        <th>Editorial</th>
        <th>Edición</th>
        <th>País / Ciudad</th>
        <th>Cantidad</th>
        <th>Precio</th>
        <th>Tags</th>
        <th>URL / Contacto Tesis</th>
    </tr>
    </thead>
    <tbody>
    <% for(Material m : lista){ %>
    <tr>
        <td><%= m.getTitulo() %></td>
        <td><%= m.getTipo() %></td>
        <td><%= m.getAnoPublicacion() %></td>
        <td>
            <% for(Autor a : m.getAutores()){ %>
            <%= a.getNombre() %><br>
            <% } %>
        </td>
        <td><%= m.getAutorPrincipal() != null ? m.getAutorPrincipal().getNombre() : "-" %></td>
        <td><%= m.getEditorial() != null ? m.getEditorial().getNombre() : "-" %></td>
        <td><%= m.getNumEdicion() != null ? m.getNumEdicion() : "-" %></td>
        <td><%= m.getPais() != null ? m.getPais() : "-" %> / <%= m.getCiudad() != null ? m.getCiudad() : "-" %></td>
        <td><%= m.getCantidad() %></td>
        <td><%= m.getPrecio() %></td>
        <td>
            <% for(Tag t : m.getTags()){ %>
            <%= t.getNombre() %><br>
            <% } %>
        </td>
        <td>
            <%= m.getUrlTesis() != null ? m.getUrlTesis() : "-" %><br>
            <%= m.getContactoTesis() != null ? m.getContactoTesis() : "-" %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<%  } else { %>
<p>No se encontraron resultados.</p>
<% } %>

<a href="buscar.jsp">Nueva búsqueda</a>
</body>
</html>
