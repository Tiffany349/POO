package sv.edu.udb.casoestudio.casoestudiofinal.models;


public class Editorial {
    private int id;
    private String nombre;
    private String pais;    // <--- añadir
    private String ciudad;  // <--- añadir

    public Editorial() {}

    public Editorial(int id, String nombre, String pais, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.ciudad = ciudad;
    }

    public Editorial(String nombre) {
        this.nombre = nombre;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPais() { return pais; }        // <---
    public String getCiudad() { return ciudad; }    // <---

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPais(String pais) { this.pais = pais; }        // <---
    public void setCiudad(String ciudad) { this.ciudad = ciudad; } // <---

}

