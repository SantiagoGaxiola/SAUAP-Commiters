package mx.desarrollo.facade;


import mx.desarrollo.delegate.DelegateProfesor;
import mx.desarrollo.entity.Profesor;

import java.util.List;
import java.util.Optional;

public class FacadeProfesor {

    private final DelegateProfesor delegateProfesor;

    public FacadeProfesor() {
        this.delegateProfesor = new DelegateProfesor();
    }


    public void guardarProfesor(Profesor profesor){
        delegateProfesor.guardarProfesor(profesor);
    }

    public void actualizarProfesor(Profesor profesor){
        delegateProfesor.actualizarProfesor(profesor);
    }

    public void eliminarProfesor(Profesor profesor){
        delegateProfesor.eliminarProfesor(profesor);
    }

    public Optional<Profesor> buscarProfesor(Integer id){
        return delegateProfesor.buscarProfesor(id);
    }

    public List<Profesor> obtenerTodos(){
        return delegateProfesor.obtenerTodos();
    }

}
