package sv.edu.udb.casoestudio.casoestudiofinal.models;

public class Editorial {
    private int id;
    private String nombre;

    public Editorial() {}
    public Editorial(String nombre) { this.nombre = nombre; }
    public Editorial(int id, String nombre) { this.id = id; this.nombre = nombre; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
