package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Esta clase representa la información de un usuario
// y se relaciona con la tabla "usuario" de la base de datos.
@Entity
@Table(name = "usuario")
public class Usuario {

// Identificador único del usuario.
// Su valor se genera automáticamente en la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    //Correo utilizado por el usuario para ingresar a la aplicación
    @Size(max = 45)
    @NotNull
    @Column(name = "correo", nullable = false, length = 45, unique = true)
    private String correo;

    //Contraseña utilizada para ingresar a la aplicación
    @Size(max = 45)
    @NotNull
    @Column(name = "contrasena", nullable = false, length = 45)
    private String contrasena;

    //Métodos para consultar, acceder y modificar los datos del usuario.
    public Usuario() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setId(int i) {
    }

    public Object setId() {
        return null;
    }
}