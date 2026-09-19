package mx.desarrollo.delegate;

import mx.desarrollo.entity.Asigna;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class DelegateAsigna {

    public static final LocalTime HORA_MIN = LocalTime.of(7, 0);
    public static final LocalTime HORA_MAX = LocalTime.of(22, 0);

    public void guardarAsignacion(Asigna asigna){
        validarAsignacion(asigna);
        ServiceLocator.getInstanceAsignaDAO().save(asigna);
    }

    public void actualizarAsignacion(Asigna asigna){
        validarAsignacion(asigna);
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

    public double horasAsignadas(Integer idUnidadAP, String periodo, String tipoHora, String grupo) {
        List<Asigna> existentes = ServiceLocator.getInstanceAsignaDAO().obtenerPorUnidadPeriodoTipoYGrupo(idUnidadAP, periodo, tipoHora, grupo);

        double total = 0;

        for (Asigna a : existentes) {
            total += Duration.between(
                    a.getHoraInicio(),
                    a.getHoraFin()
            ).toMinutes() / 60.0;
        }

        return total;
    }

    public double horasRestantes(
            UnidadAprendizaje unidad, String periodo, String tipoHora, String grupo) {

        int horasDefinidas = horasDefinidasPorTipo(unidad, tipoHora);

        return horasDefinidas - horasAsignadas(unidad.getIdUnidadAP(), periodo, tipoHora, grupo);
    }

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

    private void validarAsignacion(Asigna nueva) {
        if (nueva == null || nueva.getProfesor() == null || nueva.getUnidadAprendizaje() == null
                || nueva.getDia() == null || nueva.getGrupo() == null || nueva.getHoraInicio() == null || nueva.getHoraFin() == null
                || nueva.getTipoHora() == null || nueva.getPeriodo() == null) {
            throw new IllegalArgumentException("Faltan datos para guardar la asignacion.");
        }

        if (!nueva.getHoraInicio().isBefore(nueva.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser antes que la hora de fin.");
        }

        if (nueva.getHoraInicio().isBefore(HORA_MIN) || nueva.getHoraFin().isAfter(HORA_MAX)) {
            throw new IllegalArgumentException(
                    "El horario debe estar entre las 07:00 y las 22:00.");
        }

        List<Asigna> traslapes = ServiceLocator.getInstanceAsignaDAO().obtenerTraslapes(
                nueva.getProfesor().getIdProfesor(),
                nueva.getDia(),
                nueva.getPeriodo(),
                nueva.getHoraInicio(),
                nueva.getHoraFin());

        for (Asigna existente : traslapes) {
            if (nueva.getIdAsignacion() != null
                    && nueva.getIdAsignacion().equals(existente.getIdAsignacion())) {
                continue;
            }
            throw new IllegalArgumentException(
                    "El profesor ya tiene asignada " + existente.getUnidadAprendizaje().getNombre()
                            + " ese dia de " + existente.getHoraInicio() + " a " + existente.getHoraFin() + ".");
        }

        double restantes = horasRestantes(nueva.getUnidadAprendizaje(), nueva.getPeriodo(), nueva.getTipoHora(), nueva.getGrupo());
        double duracionSolicitada = Duration.between(nueva.getHoraInicio(), nueva.getHoraFin()).toMinutes() / 60.0;
        double margenIdAsignacion = (nueva.getIdAsignacion() != null) ? duracionSolicitada : 0;

        if (duracionSolicitada > restantes + margenIdAsignacion + 0.001) {
            throw new IllegalArgumentException(
                    "Esta unidad ya no tiene horas de " + nueva.getTipoHora()
                            + " disponibles en el periodo " + nueva.getPeriodo() + ".");
        }
    }



}
