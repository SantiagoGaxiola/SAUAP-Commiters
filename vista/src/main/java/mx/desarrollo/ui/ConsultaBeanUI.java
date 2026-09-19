/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.desarrollo.entity.Asigna;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.facade.FacadeAsigna;
import mx.desarrollo.facade.FacadeProfesor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("consultaUI")
@ViewScoped
public class ConsultaBeanUI implements Serializable{

    private static final Logger LOGGER = Logger.getLogger(ConsultaBeanUI.class.getName());
    private final FacadeProfesor facadeProfesor = new FacadeProfesor();

    private static final Map<String, String> ABREVIATURA_DIA = Map.of(
            "Lunes", "Lun",
            "Martes", "Mar",
            "Miercoles", "Mier",
            "Jueves", "Jue",
            "Viernes", "Vie",
            "Sabado", "Sab",
            "Domingo", "Dom"
    );

    private final FacadeAsigna facadeAsigna;

    private List<TarjetaProfesor> tarjetas;

    public ConsultaBeanUI() {
        facadeAsigna = new FacadeAsigna();
    }


    @PostConstruct
    public void init(){
        cargar();
    }

    public void recargar(){
        recargar();
    }

    private void cargar() {
        try{
            List <Asigna> todas = facadeAsigna.obtenerTodos();

            Map<Integer, TarjetaProfesor> porProfesor = new LinkedHashMap<>();
            for (Asigna a : todas) {
                Profesor p = a.getProfesor();
                if (p == null){
                    continue;
                }
                TarjetaProfesor tarjeta = porProfesor.computeIfAbsent(
                        p.getIdProfesor(), id -> new TarjetaProfesor(p));
                tarjeta.agregar(a);
            }
            for (Profesor p : facadeProfesor.obtenerTodos()) {
                porProfesor.computeIfAbsent(p.getIdProfesor(), id -> new TarjetaProfesor(p));
            }
            tarjetas = new ArrayList<>(porProfesor.values());
            tarjetas.sort(Comparator.comparing(t -> t.getProfesor().getNombreCompleto()));
            for (TarjetaProfesor t : tarjetas) {
                t.ordenar();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la consulta general de asignaciones", e);
            tarjetas = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al cargar las asignaciones:", "Intente mas tarde"));
        }
    }

    public List<TarjetaProfesor> getTarjetas() {
        return tarjetas;
    }


    public static class TarjetaProfesor implements Serializable {
        private final Profesor profesor;
        private final Map<Integer, FilaUnidad> unidadesPorId = new LinkedHashMap<>();
        private List<FilaUnidad> unidades;

        public TarjetaProfesor(Profesor profesor) {
            this.profesor = profesor;
        }

        public void agregar(Asigna asignacion) {
            UnidadAprendizaje unidad = asignacion.getUnidadAprendizaje();
            if (unidad == null) {
                return;
            }
            FilaUnidad fila = unidadesPorId.computeIfAbsent(
                    unidad.getIdUnidadAP(), id -> new FilaUnidad(unidad));
            fila.agregar(asignacion);

        }

        public void ordenar() {
            unidades = new ArrayList<>(unidadesPorId.values());
            unidades.sort(Comparator.comparing(f -> f.getUnidad().getNombre()));
            for (FilaUnidad f : unidades) {
                f.ordenar();
            }
        }

        public Profesor getProfesor() {
            return profesor;
        }

        public List<FilaUnidad> getUnidades() {
            return unidades;
        }

        public int getTotalUnidades() {
            return unidadesPorId.size();
        }

    }

    public static class FilaUnidad implements Serializable {
        private final UnidadAprendizaje unidad;
        private final List<Sesion> sesiones = new ArrayList<>();

        public FilaUnidad(UnidadAprendizaje unidad) {
            this.unidad = unidad;
        }

        public void agregar(Asigna asignacion) {
            sesiones.add(new Sesion(asignacion));
        }

        public void ordenar() {
            sesiones.sort(Comparator
                    .comparing(Sesion::getDia, Comparator.comparingInt(ConsultaBeanUI::ordenDia))
                    .thenComparing(Sesion::getHoraInicio));
        }

        public UnidadAprendizaje getUnidad() {

            return unidad;
        }

        public List<Sesion> getSesiones() {
            return sesiones;
        }
    }

    public static class Sesion implements Serializable {
        private final String tipoHora;
        private final String dia;
        private final LocalTime horaInicio;
        private final LocalTime horaFin;
        private final String periodo;
        private final String grupo;

        public Sesion(Asigna a) {
            this.tipoHora = a.getTipoHora();
            this.dia = a.getDia();
            this.horaInicio = a.getHoraInicio();
            this.horaFin = a.getHoraFin();
            this.periodo = a.getPeriodo();
            this.grupo = a.getGrupo();
        }

        public String getTipoHora() {
            return tipoHora;
        }

        public String getDia() {
            return dia;
        }
        public String getPeriodo() {
            return periodo;
        }
        public String getGrupo() {
            return grupo;
        }

        public LocalTime getHoraInicio() {
            return horaInicio;
        }

        public LocalTime getHoraFin() {
            return horaFin;
        }

        public String getDiaAbreviado() {
            return ABREVIATURA_DIA.getOrDefault(dia,dia);
        }

        public String getEtiquetaHorario() {
            return getDiaAbreviado() + " " + formatoHora(horaInicio) + "-" + formatoHora(horaFin);
        }

        public String getEtiquetaTipo() {
            return tipoHora + " - " + getDuracionHoras() + "h" + " - " + getGrupo() + " - " + getPeriodo();
        }

        public int getDuracionHoras() {
            return (int) Duration.between(horaInicio, horaFin).toHours();
        }

        public String getClaseChip() {
            if(tipoHora == null){
                return "";
            }
            switch (tipoHora){
                case "Clase": return "clase";
                case "Taller": return "taller";
                case "Laboratorio": return "lab";
                default: return "";
            }
        }

        private String formatoHora(LocalTime hora) {
            return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
        }

    }

    private static int ordenDia(String dia){
        List<String> orden = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        int i = orden.indexOf(dia);
        return i < 0 ? orden.size() : i;
    }



}
