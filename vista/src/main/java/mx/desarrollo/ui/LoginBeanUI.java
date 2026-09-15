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

    public LoginBeanUI() {
        loginHelper = new LoginHelper();
    }


    @PostConstruct
    public void init(){
        usuario = new Usuario();
    }

    public void login() throws IOException {
        String appURL = "/index.xhtml";
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


    /* getters y setters*/

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
