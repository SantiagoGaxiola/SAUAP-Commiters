package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

// DAO encargado de realizar operaciones relacionadas con los profesores.
// También hereda las operaciones generales de AbstractDAO.
public class ProfesorDAO extends AbstractDAO<Profesor> {
    // EntityManager utilizado para comunicarse con la base de datos.
    private final EntityManager entityManager;

    public ProfesorDAO(EntityManager entityManager) {
        super(Profesor.class);
        this.entityManager = entityManager;
    }

// Obtiene todos los profesores registrados en la base de datos.
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