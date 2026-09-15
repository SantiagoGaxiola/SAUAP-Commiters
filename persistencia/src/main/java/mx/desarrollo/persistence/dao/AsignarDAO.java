package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Asigna;
import mx.desarrollo.persistence.persistence.AbstractDAO;

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

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}