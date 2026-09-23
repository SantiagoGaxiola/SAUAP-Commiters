package mx.desarrollo.delegate;

import mx.desarrollo.entity.Profesor;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;

public class DelegateProfesor {

    // Valida y guarda un nuevo profesor en la capa de persistencia
    public void guardarProfesor(Profesor profesor){
        validarProfesor(profesor);
        profesor.setRfc(profesor.getRfc().trim().toUpperCase());
        ServiceLocator.getInstanceProfesorDAO().save(profesor);
    }

    // Valida y actualiza los datos de un profesor existente
    public void actualizarProfesor(Profesor profesor){
        validarProfesor(profesor);
        profesor.setRfc(profesor.getRfc().trim().toUpperCase());
        ServiceLocator.getInstanceProfesorDAO().update(profesor);
    }

    // Elimina un profesor despues de comprobar que no sea nulo
    public void eliminarProfesor(Profesor profesor){
        if(profesor == null){
            throw new IllegalArgumentException(
                    "El profesor no puede ser nulo."
            );
        }
        ServiceLocator.getInstanceProfesorDAO().delete(profesor);
    }

    // Busca un profesor por su identificador
    public Optional<Profesor> buscarProfesor(Integer id){
        if(id == null){
            throw new IllegalArgumentException(
                    "El id de profesor no puede ser nulo."
            );
        }
        return ServiceLocator.getInstanceProfesorDAO().find(id);
    }

    // Obtiene todos los profesores registrados
    public List<Profesor> obtenerTodos() {
        return ServiceLocator.getInstanceProfesorDAO().obtenerTodos();
    }

    // Contiene las validaciones de negocio para los datos del profesor
    private void validarProfesor(Profesor profesor){

        // Verifica que exista un objeto Profesor
        if(profesor == null){
            throw new IllegalArgumentException(
                    "El profesor no puede ser nulo."
            );
        }

        // El nombre es obligatorio y no debe superar los 50 caracteres
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

        // El apellido paterno es obligatorio y no debe superar los 50 caracteres
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

        // El apellido materno es obligatorio y no debe superar los 50 caracteres
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

        // Verifica que el RFC sea obligatorio y tenga como maximo 13 caracteres
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

        // Verifica que el RFC cumpla con el formato establecido
        if(!profesor.getRfc().trim().toUpperCase().matches("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{2,3}$")) {
            throw new IllegalArgumentException(
                    "El RFC no tiene el formato valido."
            );
        }
    }
}
