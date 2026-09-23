/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.facade.FacadeProfesor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("profesorUI")
@ViewScoped
public class ProfesorBeanUI implements Serializable{

    private static final Logger LOGGER = Logger.getLogger(ProfesorBeanUI.class.getName());

    /* Formato oficial de RFC 4 letras + AAMMDD + 3 alfanumericos, usado para validar antes de guardar. */
    private static final String RFC_REGEX = "^[A-Za-z]{4}\\d{6}[A-Za-z0-9]{3}$";

    private final FacadeProfesor facadeProfesor;

    private List<Profesor> profesores;
    private Profesor nuevoProfesor;

    /* Crea el facade de negocio usado por este bean para leer/guardar profesores. */
    public ProfesorBeanUI() {
        facadeProfesor = new FacadeProfesor();
    }


    /* Prepara un Profesor vacio para el formulario de alta y carga la lista inicial. */
    @PostConstruct
    public void init(){
        nuevoProfesor = new Profesor();
        cargarProfesores();
    }

    /* Obtiene todos los profesores del facade, si falla, deja la lista vacia y avisa al usuario en la vista. */
    private void cargarProfesores(){
        try {
            profesores = facadeProfesor.obtenerTodos();
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los profesores", e);
            profesores = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener los profesores", "intente ms tarde"));
        }
    }

    /*Accion del boton registrar, valida el formato del RFC en la propia vista antes de llamar al facade, para dar feedback inmediato sin
     round-trip innecesario a negocio. Si el guardado es exitoso, limpia el formulario y recarga la lista.*/
    public void registrarProfesor(){
        if (nuevoProfesor.getRfc() == null || !nuevoProfesor.getRfc().matches(RFC_REGEX)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "RFC invalido", "Formato: 4 letras + 6 digitos (AAMMDD) + 3 alfanumericos"));
            return;
        }
        try{
            facadeProfesor.guardarProfesor(nuevoProfesor);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Registro guardado", nuevoProfesor.getNombre()));
            nuevoProfesor = new Profesor();
            cargarProfesores();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar profesor", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar profesor", "Verifica que el RFC no este repetido"));
        }
    }

    /* getters y setters, exponen la lista para la tabla y el objeto para el formulario de alta */

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public Profesor getNuevoProfesor() {
        return nuevoProfesor;
    }

    public void setNuevoProfesor(Profesor nuevoProfesor) {
        this.nuevoProfesor = nuevoProfesor;
    }



}
