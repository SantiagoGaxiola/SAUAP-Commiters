package mx.desarrollo.delegate;

import mx.desarrollo.entity.Asigna;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;

public class DelegateAsigna {

    public void guardarAsignacion(Asigna asigna){
        ServiceLocator.getInstanceAsignaDAO().save(asigna);
    }

    public void actualizarAsignacion(Asigna asigna){
        ServiceLocator.getInstanceAsignaDAO().update(asigna);
    }

    public void eliminarAsignacion(Asigna asigna){
        ServiceLocator.getInstanceAsignaDAO().delete(asigna);
    }

    public Optional<Asigna> buscarAsignacion(Integer id){
        return ServiceLocator.getInstanceAsignaDAO().find(id);
    }

    public List<Asigna> obtenerTodos() {
        return ServiceLocator.getInstanceAsignaDAO().obtenerTodos();
    }

    public List<Asigna> obtenerPorProfesor(Integer idProfesor){
        return ServiceLocator.getInstanceAsignaDAO().obtenerPorProfesor(idProfesor);
    }

    public List<Asigna> obtenerPorPeriodo(String periodo){
        return ServiceLocator.getInstanceAsignaDAO().obtenerPorPeriodo(periodo);
    }
}
