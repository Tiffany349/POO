package sv.edu.udb.casoestudio.casoestudiofinal.service;

import sv.edu.udb.casoestudio.casoestudiofinal.dao.MaterialDAO;
import sv.edu.udb.casoestudio.casoestudiofinal.models.Material;

import java.util.List;

public class MaterialService {
    private MaterialDAO dao = new MaterialDAO();

    public List<Material> getMaterialesPorTipoYAntiguedad(String tipo, int maxAnios){
        return dao.listarPorTipoYAntiguedad(tipo, maxAnios);
    }

    public List<Material> buscarMateriales(String titulo, String autor, String editorial, String tag){
        return dao.buscarPorParametros(titulo, autor, editorial, tag);
    }
}
