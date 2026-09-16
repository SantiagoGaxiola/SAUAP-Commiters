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

    private static final String RFC_REGEX = "^[A-Za-z]{4}\\d{6}[A-Za-z0-9]{3}$";

    private final FacadeProfesor facadeProfesor;

    private List<Profesor> profesores;
    private Profesor nuevoProfesor;

    public ProfesorBeanUI() {
        facadeProfesor = new FacadeProfesor();
    }


    @PostConstruct
    public void init(){
        nuevoProfesor = new Profesor();
        cargarProfesores();
    }

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

    public void eliminarProfesor(Profesor profesor){
        try {
            facadeProfesor.eliminarProfesor(profesor);
            cargarProfesores();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar profesor", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al eliminar profesor: ", "Intente mas tarde"));
        }
    }

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
