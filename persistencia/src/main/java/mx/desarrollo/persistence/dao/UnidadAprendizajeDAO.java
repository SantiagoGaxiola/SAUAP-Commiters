package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class UnidadAprendizajeDAO extends AbstractDAO<UnidadAprendizaje> {

    private final EntityManager entityManager;

    public UnidadAprendizajeDAO(EntityManager entityManager) {
        super(UnidadAprendizaje.class);
        this.entityManager = entityManager;
    }

    public List<UnidadAprendizaje> obtenerTodos() {
        return entityManager.createQuery(
                "SELECT u FROM UnidadAprendizaje u",
                UnidadAprendizaje.class
        ).getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}