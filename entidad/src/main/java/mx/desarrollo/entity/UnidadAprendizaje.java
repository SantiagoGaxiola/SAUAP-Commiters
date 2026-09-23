package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Esta clase representa una unidad de aprendizaje.
// La información se almacena en la tabla "unidad_aprendizaje".
@Entity
@Table(name = "unidad_aprendizaje")
public class UnidadAprendizaje {

    // Identificador único de la unidad de aprendizaje.
    // Se genera automáticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidadAP", nullable = false)
    private Integer idUnidadAP;

    // Nombre de la unidad de aprendizaje.
    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    // Número de horas de clase de la unidad.
    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "horas_clase", nullable = false)
    private Integer horasClase;

    // Número de horas de taller.
    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "horas_taller", nullable = false)
    private Integer horasTaller;

    // Número de horas de laboratorio.
    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "horas_lab", nullable = false)
    private Integer horasLab;

    //Métodos para acceder, consultar y modificar los datos de la unidad de aprendizaje.
    public UnidadAprendizaje() {
    }

    public Integer getIdUnidadAP() {
        return idUnidadAP;
    }

    public void setIdUnidadAP(Integer idUnidadAP) {
        this.idUnidadAP = idUnidadAP;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getHorasClase() {
        return horasClase;
    }

    public void setHorasClase(Integer horasClase) {
        this.horasClase = horasClase;
    }

    public Integer getHorasTaller() {
        return horasTaller;
    }

    public void setHorasTaller(Integer horasTaller) {
        this.horasTaller = horasTaller;
    }

    public Integer getHorasLab() {
        return horasLab;
    }

    public void setHorasLab(Integer horasLab) {
        this.horasLab = horasLab;
    }
}