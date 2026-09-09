package mx.desarrollo.delegate;


import mx.desarrollo.entity.Alumno;
import mx.desarrollo.persistence.integration.ServiceLocator;

public class DelegateAlumno {
    public void saveAlumno(Alumno alumno){
        ServiceLocator.getInstanceAlumnoDAO().save(alumno);
    }

}