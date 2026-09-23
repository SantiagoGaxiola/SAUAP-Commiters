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
    /* Indica si el formulario esta editando una unidad existente (true) o dando de alta una nueva (false). */
    private boolean editando;

    /* Crea el facade de negocio usado por este bean para leer/guardar/eliminar unidades. */
    public UnidadAprendizajeBeanUI(){
        facadeUnidadAprendizaje = new FacadeUnidadAprendizaje();
    }

    /* Prepara el formulario en modo alta y carga el catalogo al entrar a la vista. */
    @PostConstruct
    public void init(){
        nuevaUnidad();
        cargarUnidades();
    }

    /* Obtiene todas las unidades del facade y vuelve a aplicar el filtro vigente sobre la lista nueva. */
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

    /*Filtra las unidades cargadas por coincidencia de nombre . Se ejecuta en memoria sobre la lista ya cargada, no vuelve a consultar
     negocio, para que la busqueda en la vista se sienta instantanea.*/
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

    /* Limpia el formulario y lo pone en modo alta (por ejemplo, tras cancelar una edicion). */
    public void nuevaUnidad(){
        unidadForm = new UnidadAprendizaje();
        editando = false;
    }

    /*Copia los datos de la unidad seleccionada al formulario y activa el modo edicion. Se copian los campos en vez de reusar la referencia para que
     los cambios sin guardar en el formulario no alteren la fila de la tabla.*/
    public void cargarParaEditar(UnidadAprendizaje unidad) {
        unidadForm = new UnidadAprendizaje();
        unidadForm.setIdUnidadAP(unidad.getIdUnidadAP());
        unidadForm.setNombre(unidad.getNombre());
        unidadForm.setHorasClase(unidad.getHorasClase());
        unidadForm.setHorasTaller(unidad.getHorasTaller());
        unidadForm.setHorasLab(unidad.getHorasLab());
        editando = true;
    }

    /*Accion del boton "guardar" del formulario: segun el modo actual(editando) decide si debe actualizar o insertar la unidad, y en
     cualquier caso reinicia el formulario y recarga la lista al terminar.*/
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

    /*Accion del boton "eliminar" en la tabla del catalogo. Si la unidad esta en uso en alguna asignacion, el facade lanzara una excepcion
     que aqui se atrapa para mostrar un mensaje claro en la vista.*/
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

    /* getters y setters: exponen la lista filtrada para la tabla, el formulario para el dialogo de alta/edicion y el filtro/estado para el binding de la vista */


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
