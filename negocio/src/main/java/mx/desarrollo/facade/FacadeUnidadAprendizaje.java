package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateUnidadAprendizaje;
import mx.desarrollo.entity.UnidadAprendizaje;

import java.util.List;
import java.util.Optional;

public class FacadeUnidadAprendizaje {

    private final DelegateUnidadAprendizaje delegateUnidadAprendizaje;

    public FacadeUnidadAprendizaje(){
        this.delegateUnidadAprendizaje = new DelegateUnidadAprendizaje();
    }


    public void guardarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        delegateUnidadAprendizaje.guardarUnidadAprendizaje(unidadAprendizaje);
    }

    public void actualizarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        delegateUnidadAprendizaje.actualizarUnidadAprendizaje(unidadAprendizaje);
    }

    public void eliminarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        delegateUnidadAprendizaje.eliminarUnidadAprendizaje(unidadAprendizaje);
    }

    // Solicita al Delegate buscar una unidad de aprendizaje por su nombre
    public Optional<UnidadAprendizaje> buscarUnidadAprendizaje(String nombre){
        return delegateUnidadAprendizaje.buscarUnidadAprendizaje(nombre);
    }

    // Solicita al Delegate obtener todas las unidades de aprendizaje registradas
    public List<UnidadAprendizaje> obtenerTodos(){
        return delegateUnidadAprendizaje.obtenerTodos();
    }

}
