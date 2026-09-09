package mx.desarrollo.persistence.dao;



import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Alumno;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;


public class AlumnoDAO extends AbstractDAO<Alumno> {
    private final EntityManager entityManager;

    public AlumnoDAO(EntityManager em) {
        super(Alumno.class);
        this.entityManager = em;
    }

    public List<Alumno> obtenerTodos(){
        return entityManager
                .createQuery("SELECT a FROM Alumno a", Alumno.class)
                .getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
