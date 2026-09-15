package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "unidad_aprendizaje")
public class UnidadAprendizaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidadAP", nullable = false)
    private Integer idUnidadAP;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "horas_clase", nullable = false)
    private Integer horasClase;

    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "horas_taller", nullable = false)
    private Integer horasTaller;

    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "horas_lab", nullable = false)
    private Integer horasLab;

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