<%--
  Created by IntelliJ IDEA.
  User: yynah
  Date: 19/10/2025
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Resultados - Biblioteca Metrópolis</title>
    <link rel="stylesheet" type="text/css" href="css/estilos.css">
    <meta charset="UTF-8">
</head>
<body>
<h1>Resultados de la búsqueda</h1>

<c:choose>
    <c:when test="${not empty materiales}">
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
            <c:forEach var="m" items="${materiales}">
                <tr>
                    <td><c:out value="${m.titulo}"/></td>
                    <td><c:out value="${m.tipo}"/></td>
                    <td><c:out value="${m.anoPublicacion}"/></td>
                    <td>
                        <c:forEach var="a" items="${m.autores}">
                            <c:out value="${a.nombre}"/><br/>
                        </c:forEach>
                        <c:if test="${empty m.autores}">-</c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${m.autorPrincipal != null}">
                                <c:out value="${m.autorPrincipal.nombre}"/>
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${m.editorial != null}">
                                <c:out value="${m.editorial.nombre}"/>
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${m.numEdicion}"/></td>
                    <td>
                        <c:out value="${empty m.pais ? '-' : m.pais}"/> /
                        <c:out value="${empty m.ciudad ? '-' : m.ciudad}"/>
                    </td>
                    <td><c:out value="${m.cantidad}"/></td>
                    <td><c:out value="${m.precio}"/></td>
                    <td>
                        <c:forEach var="t" items="${m.tags}">
                            <c:out value="${t.nombre}"/><br/>
                        </c:forEach>
                        <c:if test="${empty m.tags}">-</c:if>
                    </td>
                    <td>
                        <c:out value="${empty m.urlTesis ? '-' : m.urlTesis}"/><br/>
                        <c:out value="${empty m.contactoTesis ? '-' : m.contactoTesis}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p>No se encontraron resultados.</p>
    </c:otherwise>
</c:choose>

<a href="buscar.jsp">Nueva búsqueda</a>
</body>
</html>
