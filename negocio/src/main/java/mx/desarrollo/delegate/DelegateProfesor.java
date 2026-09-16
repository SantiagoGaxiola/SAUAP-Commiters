package mx.desarrollo.delegate;

import mx.desarrollo.entity.Profesor;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;

public class DelegateProfesor {

    public void guardarProfesor(Profesor profesor){
        validarProfesor(profesor);
        profesor.setRfc(profesor.getRfc().trim().toUpperCase());
        ServiceLocator.getInstanceProfesorDAO().save(profesor);
    }

    public void actualizarProfesor(Profesor profesor){
        validarProfesor(profesor);
        profesor.setRfc(profesor.getRfc().trim().toUpperCase());
        ServiceLocator.getInstanceProfesorDAO().update(profesor);
    }

    public void eliminarProfesor(Profesor profesor){
        if(profesor == null){
            throw new IllegalArgumentException(
                    "El profesor no puede ser nulo."
            );
        }
        ServiceLocator.getInstanceProfesorDAO().delete(profesor);
    }

    public Optional<Profesor> buscarProfesor(Integer id){
        if(id == null){
            throw new IllegalArgumentException(
                    "El id de profesor no puede ser nulo."
            );
        }
        return ServiceLocator.getInstanceProfesorDAO().find(id);
    }

    public List<Profesor> obtenerTodos() {
        return ServiceLocator.getInstanceProfesorDAO().obtenerTodos();
    }

    private void validarProfesor(Profesor profesor){
        if(profesor == null){
            throw new IllegalArgumentException(
                    "El profesor no puede ser nulo."
            );
        }

        if(profesor.getNombre() == null || profesor.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if(profesor.getNombre().length() > 50){
            throw new IllegalArgumentException(
                    "El nombre no puede ser mayor a 50 caracteres."
            );
        }

        if(profesor.getApellidoPaterno() == null || profesor.getApellidoPaterno().trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El apellido paterno es obligatorio."
            );
        }

        if(profesor.getApellidoPaterno().length() > 50){
            throw new IllegalArgumentException(
                    "El apellido paterno no puede ser mayor a 50 caracteres."
            );
        }

        if(profesor.getApellidoMaterno() == null || profesor.getApellidoMaterno().trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El apellido materno es obligatorio."
            );
        }

        if(profesor.getApellidoMaterno().length() > 50){
            throw new IllegalArgumentException(
                    "El apellido materno no puede ser mayor a 50 caracteres."
            );
        }

        if(profesor.getRfc() == null || profesor.getRfc().trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El RFC es obligatorio."
            );
        }

        if(profesor.getRfc().trim().length() > 13){
            throw new IllegalArgumentException(
                    "El RFC no puede ser mayor a 13 caracteres."
            );
        }

        if(!profesor.getRfc().trim().toUpperCase().matches("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{2,3}$")) {
            throw new IllegalArgumentException(
                    "El RFC no tiene el formato valido."
            );
        }
    }
}
