package mx.desarrollo.persistence.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para inicializar y obtener el {@link EntityManagerFactory}.
 */

// Como se indica en el comentario de arriba es la clase encargada de iniciar Hibernate y crear la conexión
// que permite trabajar con la base de datos desde Java.
public class HibernateUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = buildEntityManagerFactory();

    /**
     * Crea la instancia de EntityManagerFactory a partir del archivo persistence.xml.
     */
    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            // Carga la configuración de persistence.xml
            // y crea la conexión con la base de datos.
            return Persistence.createEntityManagerFactory("persistencePU");
        } catch (Throwable ex) {
            System.err.println("Error creando EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void close() {
        if (ENTITY_MANAGER_FACTORY != null && ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
