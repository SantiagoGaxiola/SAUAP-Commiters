package mx.desarrollo.delegate;

import mx.desarrollo.entity.Profesor;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;

public class DelegateProfesor {

    public void guardarProfesor(Profesor profesor){
        ServiceLocator.getInstanceProfesorDAO().save(profesor);
    }

    public void actualizarProfesor(Profesor profesor){
        ServiceLocator.getInstanceProfesorDAO().update(profesor);
    }

    public void eliminarProfesor(Profesor profesor){
        ServiceLocator.getInstanceProfesorDAO().delete(profesor);
    }

    public Optional<Profesor> buscarProfesor(Integer id){
        return ServiceLocator.getInstanceProfesorDAO().find(id);
    }

    public List<Profesor> obtenerTodos() {
        return ServiceLocator.getInstanceProfesorDAO().obtenerTodos();
    }
}
