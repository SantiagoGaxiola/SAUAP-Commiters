package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Asigna;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.time.LocalTime;
import java.util.List;

public class AsignarDAO extends AbstractDAO<Asigna> {

    private final EntityManager entityManager;

    public AsignarDAO(EntityManager entityManager) {
        super(Asigna.class);
        this.entityManager = entityManager;
    }

    public List<Asigna> obtenerTodos() {
        return entityManager.createQuery(
                "SELECT a FROM Asigna a",
                Asigna.class
        ).getResultList();
    }

    public List<Asigna> obtenerPorProfesor(Integer idProfesor) {
        return entityManager.createQuery(
                        "SELECT a FROM Asigna a WHERE a.profesor.idProfesor = :idProfesor",
                        Asigna.class
                ).setParameter("idProfesor", idProfesor)
                .getResultList();
    }

    public List<Asigna> obtenerPorPeriodo(String periodo) {
        return entityManager.createQuery(
                        "SELECT a FROM Asigna a WHERE a.periodo = :periodo",
                        Asigna.class
                ).setParameter("periodo", periodo)
                .getResultList();
    }

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