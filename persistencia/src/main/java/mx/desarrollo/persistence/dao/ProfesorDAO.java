package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class ProfesorDAO extends AbstractDAO<Profesor> {

    private final EntityManager entityManager;

    public ProfesorDAO(EntityManager entityManager) {
        super(Profesor.class);
        this.entityManager = entityManager;
    }

    public List<Profesor> obtenerTodos() {
        return entityManager.createQuery(
                "SELECT p FROM Profesor p",
                Profesor.class
        ).getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}