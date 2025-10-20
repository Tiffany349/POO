package sv.edu.udb.casoestudio.casoestudiofinal.models;

public class Autor {
    private int id;
    private String nombre;
    private String email;

    public Autor() {}
    public Autor(String nombre) { this.nombre = nombre; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
