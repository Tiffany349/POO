package sv.edu.udb.casoestudio.casoestudiofinal.models;

public class Material {
    private int idMaterial;
    private String tipo;
    private String titulo;
    private String numEdicion;
    private int anoPublicacion;
    private int cantidad;
    private double precio;
    private String descripcion;
    private String urlTesis;
    private String contactoTesis;
    private int idEditorial;
    private int idAutorPrincipal;

    // Constructor vacío
    public Material() {}

    // Getters y Setters
    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNumEdicion() {
        return numEdicion;
    }

    public void setNumEdicion(String numEdicion) {
        this.numEdicion = numEdicion;
    }

    public int getAnoPublicacion() {
        return anoPublicacion;
    }

    public void setAnoPublicacion(int anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlTesis() {
        return urlTesis;
    }

    public void setUrlTesis(String urlTesis) {
        this.urlTesis = urlTesis;
    }

    public String getContactoTesis() {
        return contactoTesis;
    }

    public void setContactoTesis(String contactoTesis) {
        this.contactoTesis = contactoTesis;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public int getIdAutorPrincipal() {
        return idAutorPrincipal;
    }

    public void setIdAutorPrincipal(int idAutorPrincipal) {
        this.idAutorPrincipal = idAutorPrincipal;
    }
}
