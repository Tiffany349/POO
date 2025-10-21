package sv.edu.udb.casoestudio.casoestudiofinal.models;

import java.util.ArrayList;
import java.util.List;

public class Material {

    private int id;
    private String tipo;              // Libro, Enciclopedia, Revista, Tesis, DVD
    private String titulo;
    private Integer anoPublicacion;   // wrapper para permitir null
    private String numEdicion;

    // País / ciudad vienen de Editorial (por el JOIN). Los dejamos aquí para mostrar en JSP.
    private String pais;
    private String ciudad;

    private Editorial editorial;

    // IMPORTANTES: wrappers para poder mandar NULL a la DB
    private Integer cantidad;
    private Double  precio;

    private String descripcion;
    private String urlTesis;
    private String contactoTesis;

    private List<Autor> autores = new ArrayList<>();
    private Autor autorPrincipal;
    private List<Tag>   tags    = new ArrayList<>();

    public Material() {}

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getAnoPublicacion() { return anoPublicacion; }
    public void setAnoPublicacion(Integer anoPublicacion) { this.anoPublicacion = anoPublicacion; }

    public String getNumEdicion() { return numEdicion; }
    public void setNumEdicion(String numEdicion) { this.numEdicion = numEdicion; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public Editorial getEditorial() { return editorial; }
    public void setEditorial(Editorial editorial) { this.editorial = editorial; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrlTesis() { return urlTesis; }
    public void setUrlTesis(String urlTesis) { this.urlTesis = urlTesis; }

    public String getContactoTesis() { return contactoTesis; }
    public void setContactoTesis(String contactoTesis) { this.contactoTesis = contactoTesis; }

    // Listas null-safe
    public List<Autor> getAutores() {
        if (autores == null) autores = new ArrayList<>();
        return autores;
    }
    public void setAutores(List<Autor> autores) {
        this.autores = (autores == null) ? new ArrayList<>() : autores;
    }

    public Autor getAutorPrincipal() { return autorPrincipal; }
    public void setAutorPrincipal(Autor autorPrincipal) { this.autorPrincipal = autorPrincipal; }

    public List<Tag> getTags() {
        if (tags == null) tags = new ArrayList<>();
        return tags;
    }
    public void setTags(List<Tag> tags) {
        this.tags = (tags == null) ? new ArrayList<>() : tags;
    }
}