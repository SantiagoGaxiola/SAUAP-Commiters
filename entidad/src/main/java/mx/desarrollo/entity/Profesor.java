package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Esta clase representa a un profesor dentro del sistema.
// Sus datos se almacenan en la tabla "profesor".
@Entity
@Table(name = "profesor")
public class Profesor {

    // Identificador único del profesor.
    // Se genera automáticamente al registrar un nuevo profesor.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor", nullable = false)
    private Integer idProfesor;

    //Nombre del profesor
    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
//Apellido paterno
    @NotNull
    @Size(max = 50)
    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;
//Apellido materno
    @NotNull
    @Size(max = 50)
    @Column(name = "apellido_materno", nullable = false, length = 50)
    private String apellidoMaterno;

    //RFC validado a 13 caractares
    @NotNull
    @Size(max = 13)
    @Column(name = "rfc", nullable = false, length = 13, unique = true)
    private String rfc;

    //Métodos para acceder, modificar y consultar los datos del profesor.
    public Profesor() {
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
}