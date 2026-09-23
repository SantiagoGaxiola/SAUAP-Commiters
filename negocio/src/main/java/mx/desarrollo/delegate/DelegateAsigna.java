package mx.desarrollo.delegate;

import mx.desarrollo.entity.Asigna;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class DelegateAsigna {

    // Define el horario minimo permitido para las asignaciones
    public static final LocalTime HORA_MIN = LocalTime.of(7, 0);

    // Define el horario maximo permitido para las asignaciones
    public static final LocalTime HORA_MAX = LocalTime.of(22, 0);

    // Valida y guarda una nueva asignacion
    public void guardarAsignacion(Asigna asigna){
        validarAsignacion(asigna);
        ServiceLocator.getInstanceAsignaDAO().save(asigna);
    }

    // Valida y actualiza una asignacion existente
    public void actualizarAsignacion(Asigna asigna){
        validarAsignacion(asigna);
        ServiceLocator.getInstanceAsignaDAO().update(asigna);
    }

    // Elimina una asignacion
    public void eliminarAsignacion(Asigna asigna){
        ServiceLocator.getInstanceAsignaDAO().delete(asigna);
    }

    // Busca una asignacion por su identificador
    public Optional<Asigna> buscarAsignacion(Integer id){
        return ServiceLocator.getInstanceAsignaDAO().find(id);
    }

    // Obtiene todas las asignaciones registradas
    public List<Asigna> obtenerTodos() {
        return ServiceLocator.getInstanceAsignaDAO().obtenerTodos();
    }

    // Obtiene las asignaciones de un profesor
    public List<Asigna> obtenerPorProfesor(Integer idProfesor){
        return ServiceLocator.getInstanceAsignaDAO().obtenerPorProfesor(idProfesor);
    }

    // Obtiene las asignaciones de un profesor dentro de un periodo
    public List<Asigna> obtenerPorProfesorYPeriodo(Integer idProfesor, String periodo){
        return ServiceLocator.getInstanceAsignaDAO()
                .obtenerPorProfesorYPeriodo(idProfesor, periodo);
    }

    // Obtiene las asignaciones registradas en un periodo
    public List<Asigna> obtenerPorPeriodo(String periodo){
        return ServiceLocator.getInstanceAsignaDAO().obtenerPorPeriodo(periodo);
    }

    // Calcula las horas ya asignadas de una unidad por periodo tipo de hora y grupo
    public double horasAsignadas(Integer idUnidadAP, String periodo, String tipoHora, String grupo) {
        List<Asigna> existentes = ServiceLocator.getInstanceAsignaDAO().obtenerPorUnidadPeriodoTipoYGrupo(idUnidadAP, periodo, tipoHora, grupo);

        double total = 0;

        // Suma la duracion de las asignaciones existentes
        for (Asigna a : existentes) {
            total += Duration.between(
                    a.getHoraInicio(),
                    a.getHoraFin()
            ).toMinutes() / 60.0;
        }

        return total;
    }

    // Calcula las horas que aun puede recibir una unidad de aprendizaje
    public double horasRestantes(
            UnidadAprendizaje unidad, String periodo, String tipoHora, String grupo) {

        int horasDefinidas = horasDefinidasPorTipo(unidad, tipoHora);

        return horasDefinidas - horasAsignadas(unidad.getIdUnidadAP(), periodo, tipoHora, grupo);
    }

    // Obtiene las horas definidas para el tipo de hora seleccionado
    public int horasDefinidasPorTipo(UnidadAprendizaje unidad, String tipoHora) {
        if (unidad == null || tipoHora == null) {
            return 0;
        }
        switch (tipoHora) {
            case "Clase":
                return unidad.getHorasClase() != null ? unidad.getHorasClase() : 0;
            case "Taller":
                return unidad.getHorasTaller() != null ? unidad.getHorasTaller() : 0;
            case "Laboratorio":
                return unidad.getHorasLab() != null ? unidad.getHorasLab() : 0;
            default:
                return 0;
        }
    }

    // Contiene las validaciones de negocio para la asignacion
    private void validarAsignacion(Asigna nueva) {

        // Verifica que todos los datos necesarios hayan sido proporcionados
        if (nueva == null || nueva.getProfesor() == null || nueva.getUnidadAprendizaje() == null
                || nueva.getDia() == null || nueva.getGrupo() == null || nueva.getHoraInicio() == null || nueva.getHoraFin() == null
                || nueva.getTipoHora() == null || nueva.getPeriodo() == null) {
            throw new IllegalArgumentException("Faltan datos para guardar la asignacion.");
        }

        // Verifica que la hora de inicio sea anterior a la hora de fin
        if (!nueva.getHoraInicio().isBefore(nueva.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser antes que la hora de fin.");
        }

        // Verifica que el horario este dentro del rango permitido
        if (nueva.getHoraInicio().isBefore(HORA_MIN) || nueva.getHoraFin().isAfter(HORA_MAX)) {
            throw new IllegalArgumentException(
                    "El horario debe estar entre las 07:00 y las 22:00.");
        }

        // Busca asignaciones que se traslapen con el nuevo horario del profesor
        List<Asigna> traslapes = ServiceLocator.getInstanceAsignaDAO().obtenerTraslapes(
                nueva.getProfesor().getIdProfesor(),
                nueva.getDia(),
                nueva.getPeriodo(),
                nueva.getHoraInicio(),
                nueva.getHoraFin());

        // Evita que un profesor tenga dos asignaciones en el mismo horario
        for (Asigna existente : traslapes) {

            // Permite conservar la misma asignacion durante una actualizacion
            if (nueva.getIdAsignacion() != null
                    && nueva.getIdAsignacion().equals(existente.getIdAsignacion())) {
                continue;
            }
            throw new IllegalArgumentException(
                    "El profesor ya tiene asignada " + existente.getUnidadAprendizaje().getNombre()
                            + " ese dia de " + existente.getHoraInicio() + " a " + existente.getHoraFin() + ".");
        }

        // Calcula las horas disponibles para la unidad en el periodo seleccionado
        double restantes = horasRestantes(nueva.getUnidadAprendizaje(), nueva.getPeriodo(), nueva.getTipoHora(), nueva.getGrupo());

        // Calcula la duracion de la nueva asignacion
        double duracionSolicitada = Duration.between(nueva.getHoraInicio(), nueva.getHoraFin()).toMinutes() / 60.0;

        // Considera la duracion actual al modificar una asignacion existente
        double margenIdAsignacion = (nueva.getIdAsignacion() != null) ? duracionSolicitada : 0;

        // Verifica que la nueva asignacion no exceda las horas disponibles
        if (duracionSolicitada > restantes + margenIdAsignacion + 0.001) {
            throw new IllegalArgumentException(
                    "Esta unidad ya no tiene horas de " + nueva.getTipoHora()
                            + " disponibles en el periodo " + nueva.getPeriodo() + ".");
        }
    }



}
