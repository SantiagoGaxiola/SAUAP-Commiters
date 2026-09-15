package mx.desarrollo.integration;

import mx.desarrollo.facade.FacadeAlumno;
import mx.desarrollo.facade.FacadeUsuario;
import mx.desarrollo.facade.FacadeProfesor;
import mx.desarrollo.facade.FacadeUnidadAprendizaje;
import mx.desarrollo.facade.FacadeAsigna;

public class ServiceFacadeLocator {

    private static FacadeAlumno facadeAlumno;
    private static FacadeUsuario facadeUsuario;
    private static FacadeProfesor facadeProfesor;
    private static FacadeAsigna facadeAsigna;
    private static FacadeUnidadAprendizaje facadeUnidadAprendizaje;

    public static FacadeAlumno getInstanceFacadeAlumno() {
        if (facadeAlumno == null) {
            facadeAlumno = new FacadeAlumno();
            return facadeAlumno;
        } else {
            return facadeAlumno;
        }
    }

    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
            return facadeUsuario;
        } else {
            return facadeUsuario;
        }
    }

    public static FacadeProfesor getInstanceFacadeProfesor() {
        if (facadeProfesor == null) {
            facadeProfesor = new FacadeProfesor();
        }
        return facadeProfesor;
    }

    public static FacadeUnidadAprendizaje getInstanceFacadeUnidadAprendizaje() {
        if (facadeUnidadAprendizaje == null) {
            facadeUnidadAprendizaje = new FacadeUnidadAprendizaje();
        }
        return facadeUnidadAprendizaje;
    }

    public static FacadeAsigna getInstanceFacadeAsigna() {
        if (facadeAsigna == null) {
            facadeAsigna = new FacadeAsigna();
        }
        return facadeAsigna;
    }
}
