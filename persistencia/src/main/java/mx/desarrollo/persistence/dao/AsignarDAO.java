package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Asigna;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.time.LocalTime;
import java.util.List;

// DAO encargado de manejar las asignaciones de profesores
// y unidades de aprendizaje.

public class AsignarDAO extends AbstractDAO<Asigna> {

    private final EntityManager entityManager;

    public AsignarDAO(EntityManager entityManager) {
        super(Asigna.class);
        this.entityManager = entityManager;
    }
    //Obtiene todas las asignaciones registradas
    public List<Asigna> obtenerTodos() {
        entityManager.clear();
        return entityManager.createQuery(
                "SELECT a FROM Asigna a",
                Asigna.class
        ).getResultList();
    }

    //Como su nombre lo indica obtiene por Profesor asignado
    public List<Asigna> obtenerPorProfesor(Integer idProfesor) {
        return entityManager.createQuery(
                        "SELECT a FROM Asigna a WHERE a.profesor.idProfesor = :idProfesor",
                        Asigna.class
                ).setParameter("idProfesor", idProfesor)
                .getResultList();
    }
    //Obtiene por Profesor y periodo asignados.
    public List<Asigna> obtenerPorProfesorYPeriodo(Integer idProfesor, String periodo) {
        return entityManager.createQuery(
                        "SELECT a FROM Asigna a " +
                                "WHERE a.profesor.idProfesor = :idProfesor " +
                                "AND a.periodo = :periodo",
                        Asigna.class
                )
                .setParameter("idProfesor", idProfesor)
                .setParameter("periodo", periodo)
                .getResultList();
    }

    //Obtiene unicamente por periodo asignado
    public List<Asigna> obtenerPorPeriodo(String periodo) {
        return entityManager.createQuery(
                        "SELECT a FROM Asigna a WHERE a.periodo = :periodo",
                        Asigna.class
                ).setParameter("periodo", periodo)
                .getResultList();
    }

    //Obtiene por unidad de aprendizaje, periodo, el tipo(clase,taller,laboratorio) y el grupo.
    public List<Asigna> obtenerPorUnidadPeriodoTipoYGrupo(
            Integer idUnidadAP,
            String periodo,
            String tipoHora,
            String grupo) {

        return entityManager.createQuery(
                        "SELECT a FROM Asigna a " +
                                "WHERE a.unidadAprendizaje.idUnidadAP = :idUnidadAP " +
                                "AND a.periodo = :periodo " +
                                "AND a.tipoHora = :tipoHora " +
                                "AND a.grupo = :grupo",
                        Asigna.class
                )
                .setParameter("idUnidadAP", idUnidadAP)
                .setParameter("periodo", periodo)
                .setParameter("tipoHora", tipoHora)
                .setParameter("grupo", grupo)
                .getResultList();
    }

        //Es el que extrae los traslapes del horario
    public List<Asigna> obtenerTraslapes(
            Integer idProfesor,
            String dia,
            String periodo,
            LocalTime horaInicio,
            LocalTime horaFin) {

        return entityManager.createQuery(
                        "SELECT a FROM Asigna a " +
                                "WHERE a.profesor.idProfesor = :idProfesor " +
                                "AND a.dia = :dia " +
                                "AND a.periodo = :periodo " +
                                "AND a.horaInicio < :horaFin " +
                                "AND a.horaFin > :horaInicio",
                        Asigna.class
                )
                .setParameter("idProfesor", idProfesor)
                .setParameter("dia", dia)
                .setParameter("periodo", periodo)
                .setParameter("horaInicio", horaInicio)
                .setParameter("horaFin", horaFin)
                .getResultList();
    }


    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}