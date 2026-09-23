package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateUsuario;
import mx.desarrollo.entity.Usuario;

public class FacadeUsuario {

    private final DelegateUsuario delegateUsuario;

    public FacadeUsuario() {
        this.delegateUsuario = new DelegateUsuario();
    }

    // Solicita al Delegate verificar las credenciales del usuario
    public Usuario login(String password, String correo){
        return delegateUsuario.login(password, correo);
    }

    // Solicita al Delegate guardar un nuevo usuario
    public void saveUsario(Usuario usuario){
        delegateUsuario.saveUsario(usuario);
    }

}