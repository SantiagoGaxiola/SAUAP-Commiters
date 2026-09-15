package mx.desarrollo.delegate;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;

public class DelegateUnidadAprendizaje {

    public void guardarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        ServiceLocator.getInstanceUnidadAprendizajeDAO().save(unidadAprendizaje);
    }

    public void actualizarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        ServiceLocator.getInstanceUnidadAprendizajeDAO().update(unidadAprendizaje);
    }

    public void eliminarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        ServiceLocator.getInstanceUnidadAprendizajeDAO().delete(unidadAprendizaje);
    }

    public Optional<UnidadAprendizaje> buscarUnidadAprendizaje(Integer id){
        return ServiceLocator.getInstanceUnidadAprendizajeDAO().find(id);
    }

    public List<UnidadAprendizaje> obtenerTodos(){
        return ServiceLocator.getInstanceUnidadAprendizajeDAO().obtenerTodos();
    }
}
