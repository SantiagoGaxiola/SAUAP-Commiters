package mx.desarrollo.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.dao.*;
import mx.desarrollo.persistence.dao.AsignarDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

// Clase que centraliza la creación y acceso a los diferentes DAO.
// De esta manera, las demás capas pueden obtener el DAO que necesitan.
public class ServiceLocator {

    private static AlumnoDAO alumnoDAO;
    private static UsuarioDAO usuarioDAO;
    private static ProfesorDAO profesorDAO;
    private static UnidadAprendizajeDAO unidadAprendizajeDAO;
    private static AsignarDAO asignaDAO;

    // Obtiene un EntityManager desde HibernateUtil
    // para que los DAO puedan comunicarse con la base de datos.
    private static EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }


        //Esta parte no se utiliza pero se encuentra debido a que reciclamos el codigo jeje
    /**
     * Se crea la instancia de AlumnoDAO si esta no existe.
     * Se mantiene temporalmente para compatibilidad con el código existente.
     */
    public static AlumnoDAO getInstanceAlumnoDAO() {
        if (alumnoDAO == null) {
            alumnoDAO = new AlumnoDAO(getEntityManager());
        }
        return alumnoDAO;
    }

    /**
     * Se crea la instancia de UsuarioDAO si esta no existe.
     */
    public static UsuarioDAO getInstanceUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO(getEntityManager());
        }
        return usuarioDAO;
    }

    /**
     * Se crea la instancia de ProfesorDAO si esta no existe.
     */
    public static ProfesorDAO getInstanceProfesorDAO() {
        if (profesorDAO == null) {
            profesorDAO = new ProfesorDAO(getEntityManager());
        }
        return profesorDAO;
    }

    /**
     * Se crea la instancia de UnidadAprendizajeDAO si esta no existe.
     */
    public static UnidadAprendizajeDAO getInstanceUnidadAprendizajeDAO() {
        if (unidadAprendizajeDAO == null) {
            unidadAprendizajeDAO =
                    new UnidadAprendizajeDAO(getEntityManager());
        }
        return unidadAprendizajeDAO;
    }

    /**
     * Se crea la instancia de AsignaDAO si esta no existe.
     */
    public static AsignarDAO getInstanceAsignaDAO() {
        if (asignaDAO == null) {
            asignaDAO = new AsignarDAO(getEntityManager());
        }
        return asignaDAO;
    }
}