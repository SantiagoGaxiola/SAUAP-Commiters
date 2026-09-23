package mx.desarrollo.persistence.dao;



import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;
// DAO encargado de trabajar con los datos de los usuarios.
// Hereda las operaciones generales de AbstractDAO.
public class UsuarioDAO extends AbstractDAO<Usuario> {
    private final EntityManager entityManager;
    
// Recibe el EntityManager que permite comunicarse con la base de datos.
    public UsuarioDAO(EntityManager em) {
        super(Usuario.class);
        this.entityManager = em;
    }

    public List<Usuario> obtenerTodos(){
        return entityManager
                .createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
