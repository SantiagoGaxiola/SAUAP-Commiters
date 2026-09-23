package mx.desarrollo.delegate;



import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateUsuario {

    // Verifica las credenciales del usuario y devuelve el usuario encontrado
    public Usuario login(String password, String correo){
        Usuario usuario = new Usuario();

        // Obtiene los usuarios registrados mediante el DAO
        List<Usuario> usuarios = ServiceLocator.getInstanceUsuarioDAO().findAll();

        // Compara el correo y la contrasena con los usuarios registrados
        for(Usuario us:usuarios){
            if(us.getContrasena().equalsIgnoreCase(password) && us.getCorreo().equalsIgnoreCase(correo)){
                usuario = us;
            }
        }
        return usuario;
    }

    // Guarda un nuevo usuario mediante el DAO
    public void saveUsario(Usuario usuario){
        ServiceLocator.getInstanceUsuarioDAO().save(usuario);
    }

}