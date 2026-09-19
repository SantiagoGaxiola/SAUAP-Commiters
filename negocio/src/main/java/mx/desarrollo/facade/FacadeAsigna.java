package mx.desarrollo.facade;


import mx.desarrollo.delegate.DelegateAsigna;
import mx.desarrollo.entity.Asigna;

import java.util.List;
import java.util.Optional;

public class FacadeAsigna {

    private final DelegateAsigna delegateAsigna;

    public FacadeAsigna() {
        this.delegateAsigna = new DelegateAsigna();
    }

    public void guardarAsignacion(Asigna asigna){
        delegateAsigna.guardarAsignacion(asigna);
    }

    public void actualizarAsignacion(Asigna asigna){
        delegateAsigna.actualizarAsignacion(asigna);
    }

    public void eliminarAsignacion(Asigna asigna){
        delegateAsigna.eliminarAsignacion(asigna);
    }

    public Optional<Asigna> buscarAsignacion(Integer id){
        return delegateAsigna.buscarAsignacion(id);
    }

    public List<Asigna> obtenerTodos(){
        return delegateAsigna.obtenerTodos();
    }

    public List<Asigna> obtenerPorProfesor(Integer idProfesor){
        return delegateAsigna.obtenerPorProfesor(idProfesor);
    }

    public List<Asigna> obtenerPorProfesorYPeriodo(Integer idProfesor, String periodo){
        return delegateAsigna.obtenerPorProfesorYPeriodo(idProfesor, periodo);
    }

    public List<Asigna> obtenerPorPeriodo(String periodo){
        return delegateAsigna.obtenerPorPeriodo(periodo);
    }

    public double horasRestantes(mx.desarrollo.entity.UnidadAprendizaje unidad, String periodo, String tipoHora, String grupo){
        return delegateAsigna.horasRestantes(unidad, periodo, tipoHora, grupo);
    }

    public int horasDefinidasPorTipo(mx.desarrollo.entity.UnidadAprendizaje unidad, String tipoHora){
        return delegateAsigna.horasDefinidasPorTipo(unidad, tipoHora);
    }
}
