/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.helper.LoginHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable{

    private static final Logger LOGGER = Logger.getLogger(LoginBeanUI.class.getName());

    private LoginHelper loginHelper;
    private Usuario usuario;

    /* Crea el helper de login*/
    public LoginBeanUI() {
        loginHelper = new LoginHelper();
    }

    /* Inicializa un Usuario vacio para enlazar el formulario de login antes de que el usuario escriba algo. */
    @PostConstruct
    public void init(){
        usuario = new Usuario();
    }

    /*Accion del boton de login: valida credenciales con LoginHelper y, si son correctas, redirige a la pantalla de profesores; si no,
     muestra un mensaje de error en la propia vista.*/
    public void login() throws IOException {
        String appURL = "/profesores.xhtml";
        Usuario us;

        try {
            us = loginHelper.Login(usuario.getCorreo(), usuario.getContrasena());
        } catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Error al intentar hacer login", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al iniciar sesion:", "Intente mas tarde"));
            return;
        }

        if (us != null && us.getIdUsuario() != null) {

            usuario = us;
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(FacesContext.getCurrentInstance().getExternalContext()
                            .getRequestContextPath() + appURL);
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Usuario o contraseña incorrecta:", "Intente de nuevo"));
        }
    }

    /*Cierra la sesion invalidando el HttpSession (borra al usuario autenticado y cualquier dato de sesion) y regresa a la vista de login.
     * @return outcome de navegacion JSF hacia login.xhtml
     */
    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }


    /* getters y setters*/

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
