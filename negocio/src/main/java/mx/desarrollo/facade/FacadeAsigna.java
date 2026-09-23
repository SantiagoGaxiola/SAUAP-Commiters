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

    // Solicita al Delegate buscar una asignacion por su identificador
    public Optional<Asigna> buscarAsignacion(Integer id){
        return delegateAsigna.buscarAsignacion(id);
    }

    // Solicita al Delegate obtener todas las asignaciones registradas
    public List<Asigna> obtenerTodos(){
        return delegateAsigna.obtenerTodos();
    }

    // Solicita al Delegate obtener las asignaciones de un profesor
    public List<Asigna> obtenerPorProfesor(Integer idProfesor){
        return delegateAsigna.obtenerPorProfesor(idProfesor);
    }

    // Solicita al Delegate obtener las asignaciones de un profesor dentro de un periodo
    public List<Asigna> obtenerPorProfesorYPeriodo(Integer idProfesor, String periodo){
        return delegateAsigna.obtenerPorProfesorYPeriodo(idProfesor, periodo);
    }

    // Solicita al Delegate obtener las asignaciones de un periodo
    public List<Asigna> obtenerPorPeriodo(String periodo){
        return delegateAsigna.obtenerPorPeriodo(periodo);
    }

    // Consulta las horas que aun estan disponibles para una unidad
    public double horasRestantes(mx.desarrollo.entity.UnidadAprendizaje unidad, String periodo, String tipoHora, String grupo){
        return delegateAsigna.horasRestantes(unidad, periodo, tipoHora, grupo);
    }

    // Consulta las horas definidas para un tipo de hora
    public int horasDefinidasPorTipo(mx.desarrollo.entity.UnidadAprendizaje unidad, String tipoHora){
        return delegateAsigna.horasDefinidasPorTipo(unidad, tipoHora);
    }
}
