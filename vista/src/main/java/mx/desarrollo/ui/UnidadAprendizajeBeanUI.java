package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.facade.FacadeUnidadAprendizaje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("catalogoUI")
@ViewScoped
public class UnidadAprendizajeBeanUI implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UnidadAprendizajeBeanUI.class.getName());

    private final  FacadeUnidadAprendizaje facadeUnidadAprendizaje;

    private List<UnidadAprendizaje> unidades;
    private List<UnidadAprendizaje> unidadesFiltradas;
    private UnidadAprendizaje unidadForm;
    private String filtro;
    private boolean editando;

    public UnidadAprendizajeBeanUI(){
        facadeUnidadAprendizaje = new FacadeUnidadAprendizaje();
    }

    @PostConstruct
    public void init(){
        nuevaUnidad();
        cargarUnidades();
    }

    private void cargarUnidades(){
        try {
            unidades = facadeUnidadAprendizaje.obtenerTodos();
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los profesores", e);
            unidades = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al cargar las unidades", "intente ms tarde"));
        }
        filtrar();
    }

    public void filtrar(){
        if(filtro == null || filtro.isBlank()){
            unidadesFiltradas = unidades;
        }  else {
            String texto = filtro.toLowerCase();
            unidadesFiltradas = unidades.stream()
                    .filter(u -> u.getNombre() != null && u.getNombre().toLowerCase().contains(texto))
                    .collect(Collectors.toList());
        }
    }

    public void nuevaUnidad(){
        unidadForm = new UnidadAprendizaje();
        editando = false;
    }

    public void cargarParaEditar(UnidadAprendizaje unidad) {
        unidadForm = new UnidadAprendizaje();
        unidadForm.setIdUnidadAP(unidad.getIdUnidadAP());
        unidadForm.setNombre(unidad.getNombre());
        unidadForm.setHorasClase(unidad.getHorasClase());
        unidadForm.setHorasTaller(unidad.getHorasTaller());
        unidadForm.setHorasLab(unidad.getHorasLab());
        editando = true;
    }

    public void guardarUnidad(){
        try{
            if(editando){
                facadeUnidadAprendizaje.actualizarUnidadAprendizaje(unidadForm);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Unidad actualizada", unidadForm.getNombre()));
            } else {
                facadeUnidadAprendizaje.guardarUnidadAprendizaje(unidadForm);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Unidad guardado", unidadForm.getNombre()));
            }
            nuevaUnidad();
            cargarUnidades();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar unidad de aprendizaje", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar la unidad", "No se pudo guardar la unidad de aprendizaje"));
        }
    }

    public void eliminarUnidad(UnidadAprendizaje unidad) {
        try {
            facadeUnidadAprendizaje.eliminarUnidadAprendizaje(unidad);
            cargarUnidades();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar unidad de aprendizaje", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al eliminar la unidad:", "Puede estar en uso en una asignacion"));
        }
    }



    public List<UnidadAprendizaje> getUnidadesFiltradas() {
        return unidadesFiltradas;
    }

    public UnidadAprendizaje getUnidadForm() {
        return unidadForm;
    }

    public void setUnidadForm(UnidadAprendizaje unidadForm) {
        this.unidadForm = unidadForm;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public boolean isEditando() {
        return editando;
    }

}
