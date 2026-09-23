package mx.desarrollo.delegate;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.util.Optional;

public class DelegateUnidadAprendizaje {

    // Valida y guarda una nueva unidad de aprendizaje
    public void guardarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        validarUnidadAprendizaje(unidadAprendizaje);

        if(!ServiceLocator.getInstanceUnidadAprendizajeDAO().findByOneParameter(unidadAprendizaje.getNombre().trim(), "nombre").isEmpty()){
            throw new IllegalArgumentException(
                    "Ya existe una unidad de aprendizaje con ese nombre."
            );
        }

        // Envia la unidad validada a persistencia
        ServiceLocator.getInstanceUnidadAprendizajeDAO().save(unidadAprendizaje);
    }

    // Valida y actualiza una unidad de aprendizaje existente
    public void actualizarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        validarUnidadAprendizaje(unidadAprendizaje);
        ServiceLocator.getInstanceUnidadAprendizajeDAO().update(unidadAprendizaje);
    }

    // Elimina una unidad de aprendizaje
    public void eliminarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        if(unidadAprendizaje == null){
            throw new IllegalArgumentException(
                    "La unidad de aprendizaje no puede ser nula."
            );
        }
        ServiceLocator.getInstanceUnidadAprendizajeDAO().delete(unidadAprendizaje);
    }

    // Busca una unidad de aprendizaje por su nombre
    public Optional<UnidadAprendizaje> buscarUnidadAprendizaje(String nombre){
        validarBusqueda(nombre);
        return ServiceLocator.getInstanceUnidadAprendizajeDAO().find(nombre);
    }

    // Obtiene todas las unidades de aprendizaje registradas
    public List<UnidadAprendizaje> obtenerTodos(){
        return ServiceLocator.getInstanceUnidadAprendizajeDAO().obtenerTodos();
    }

    // Contiene las validaciones de negocio para la unidad de aprendizaje
    public void validarUnidadAprendizaje(UnidadAprendizaje unidadAprendizaje){
        if(unidadAprendizaje == null){
            throw new IllegalArgumentException(
                    "La unidad de aprendizaje no puede ser nula."
            );
        }

        // Verifica que el nombre sea obligatorio
        if(unidadAprendizaje.getNombre() == null || unidadAprendizaje.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El nombre de la unidad de aprendizaje es obligatorio."
            );
        }

        // Verifica que el nombre solo contenga caracteres permitidos
        if(!unidadAprendizaje.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")){
            throw new IllegalArgumentException(
                    "El nombre de la unidad de aprendizaje no puede contener caracteres especiales."
            );
        }

        // Verifica que el nombre no supere los 50 caracteres
        if(unidadAprendizaje.getNombre().trim().length() > 50){
            throw new IllegalArgumentException(
                    "El nombre de la unidad de aprendizaje no puede ser mayor a 50 caracteres."
            );
        }

        // Valida las horas de clase y establece 0 si no fueron proporcionadas
        if(unidadAprendizaje.getHorasClase() == null){
            unidadAprendizaje.setHorasClase(0);
        }else{

            if(unidadAprendizaje.getHorasClase()<0){
                throw new IllegalArgumentException(
                        "Ingresa una cantidad valida de horas de clase."
                );

            }

            if(unidadAprendizaje.getHorasClase()>4){
                throw new IllegalArgumentException(
                        "La cantidad de horas de clase no puede ser mayor a 4."
                );

            }

        }

        // Valida las horas de laboratorio y establece 0 si no fueron proporcionadas
        if(unidadAprendizaje.getHorasLab() == null){
            unidadAprendizaje.setHorasLab(0);
        }else{

            if(unidadAprendizaje.getHorasLab()<0){
                throw new IllegalArgumentException(
                        "Ingresa una cantidad valida de horas de laboratorio."
                );

            }

            if(unidadAprendizaje.getHorasLab()>4){
                throw new IllegalArgumentException(
                        "La cantidad de horas de laboratorio no puede ser mayor a 4."
                );

            }

        }

        // Valida las horas de taller y establece 0 si no fueron proporcionadas
        if(unidadAprendizaje.getHorasTaller() == null){
            unidadAprendizaje.setHorasTaller(0);
        }else{

            if(unidadAprendizaje.getHorasTaller()<0){
                throw new IllegalArgumentException(
                        "Ingresa una cantidad valida de horas de taller."
                );

            }

            if(unidadAprendizaje.getHorasTaller()>4){
                throw new IllegalArgumentException(
                        "La cantidad de horas de taller no puede ser mayor a 4."
                );

            }

        }

    }

    // Contiene las validaciones para realizar una busqueda por nombre
    public void validarBusqueda(String nombre){
        if(nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El nombre de la unidad de aprendizaje es obligatorio."
            );
        }

        if(nombre.trim().length() > 50){
            throw new IllegalArgumentException(
                    "El nombre de la unidad de aprendizaje no puede ser mayor a 50 caracteres."
            );
        }

        // Evita realizar busquedas utilizando solo numeros
        if(nombre.trim().matches("\\d+")){
            throw new IllegalArgumentException(
                    "El nombre no puede contener solo numeros."
            );
        }

    }
}
