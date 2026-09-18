package mx.desarrollo.delegate;

import mx.desarrollo.entity.Asigna;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;
import java.time.Duration;

public class DelegateAsigna {

    public void guardarAsignacion(Asigna asigna){
        validarAsignacion(asigna);
        validarLimiteHoras(asigna);
        validarTraslape(asigna);
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

    public void validarAsignacion(Asigna asigna){

        if(asigna.getProfesor() == null){
            throw new IllegalArgumentException(
                    "Seleccione un profesor."
            );
        }

        if(asigna.getUnidadAprendizaje() == null){
            throw new IllegalArgumentException(
                    "Seleccione una unidad de aprendizaje."
            );
        }

        if(asigna.getDia() == null){
            throw new IllegalArgumentException(
                    "Seleccione un dia de la semana."
            );
        }

        if(asigna.getGrupo() == null){
            throw new IllegalArgumentException(
                    "Seleccione un grupo."
            );
        }

        if(asigna.getPeriodo() == null){
            throw new IllegalArgumentException(
                    "Seleccione un periodo academico."
            );
        }

        if(asigna.getTipoHora() == null){
            throw new IllegalArgumentException(
                    "Seleccione el tipo de clase a asignar.."
            );
        }

        if(asigna.getHoraInicio() == null){
            throw new IllegalArgumentException(
                    "Seleccione una hora de inicio."
            );
        }

        if(asigna.getHoraFin() == null){
            throw new IllegalArgumentException(
                    "Seleccione una hora de fin."
            );
        }

        if (!asigna.getHoraInicio().isBefore(asigna.getHoraFin())) {
            throw new IllegalArgumentException(
                    "La hora de inicio debe ser anterior a la hora de fin."
            );
        }

    }

    private void validarLimiteHoras(Asigna asigna){

        List<Asigna> asignacionesExistentes = ServiceLocator.getInstanceAsignaDAO().obtenerPorUnidadPeriodoYTipo(
                asigna.getUnidadAprendizaje().getIdUnidadAP(),
                asigna.getPeriodo(),
                asigna.getTipoHora()
        );

        long horasYaAsignadas = 0;
        for(Asigna asignaExistente : asignacionesExistentes){
            horasYaAsignadas += Duration.between(asignaExistente.getHoraInicio(), asignaExistente.getHoraFin()).toHours();
        }

        long horasAsignacion = Duration.between(asigna.getHoraInicio(), asigna.getHoraFin()).toHours();

        long sumaHoras = horasYaAsignadas + horasAsignacion;

        if(sumaHoras > obtenerHorasPermitidas(asigna)){
            throw new IllegalArgumentException(
                    "No se pueden asignar mas horas de este tipo de clase."
            );
        }

    }

    private int obtenerHorasPermitidas(Asigna asigna){
        String tipoClase = asigna.getTipoHora();

        switch (tipoClase){
            case "Clase":
                return asigna.getUnidadAprendizaje().getHorasClase();
            case "Laboratorio":
                return asigna.getUnidadAprendizaje().getHorasLab();
            case "Taller":
                return asigna.getUnidadAprendizaje().getHorasTaller();
            default:
                throw new IllegalArgumentException(
                        "Tipo de clase no valido."
                );
        }
    }

    public void validarTraslape(Asigna asigna) {

        List<Asigna> traslapes =
                ServiceLocator.getInstanceAsignaDAO().obtenerTraslapes(
                        asigna.getProfesor().getIdProfesor(),
                        asigna.getDia(),
                        asigna.getPeriodo(),
                        asigna.getHoraInicio(),
                        asigna.getHoraFin()
                );

        if (!traslapes.isEmpty()) {
            throw new IllegalArgumentException(
                    "El profesor ya tiene una asignación en ese horario."
            );
        }
    }

}
