function validarBusqueda() {
    const titulo = document.getElementById("titulo").value;
    const autor = document.getElementById("autor").value;
    const editorial = document.getElementById("editorial").value;
    const tag = document.getElementById("tag").value;

    if(!titulo && !autor && !editorial && !tag){
        alert("Ingrese al menos un criterio de búsqueda");
        return false;
    }
    return true;
}
