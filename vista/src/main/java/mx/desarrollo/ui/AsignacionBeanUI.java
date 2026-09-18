package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Asigna;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.entity.UnidadAprendizaje;
import mx.desarrollo.facade.FacadeAsigna;
import mx.desarrollo.facade.FacadeProfesor;
import mx.desarrollo.facade.FacadeUnidadAprendizaje;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("asignacionUI")
@ViewScoped
public class AsignacionBeanUI implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AsignacionBeanUI.class.getName());

    private static final List<String> DIAS = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes");
    private static final List<String> TIPOS_HORA = List.of("Clase", "Taller", "Laboratorio");
    private static final List<String> PERIODOS = List.of("2026-1", "2026-2");

    private static final int HORA_INICIO_DIA = 7;
    private static final int HORA_FIN_DIA = 22;

    @Inject
    private LoginBeanUI loginUI;

    private final FacadeProfesor facadeProfesor;
    private final FacadeUnidadAprendizaje facadeUnidadAprendizaje;
    private final FacadeAsigna facadeAsigna;

    private List<Profesor> profesores;
    private List<UnidadAprendizaje> unidades;
    private final List<String> dias = DIAS;
    private List<FilaHorario> horario;
    private List<OpcionTipo> tiposDisponibles;

    private Integer idProfesorSeleccionado;
    private Integer idUnidadSeleccionada;
    private String diaSeleccionado;
    private String tipoSeleccionado;
    private Date horaInicio;
    private Date horaFin;
    private String grupo;
    private String periodo;

    private boolean hayConflicto;
    private String mensajeConflicto;

    public AsignacionBeanUI() {
        facadeProfesor = new FacadeProfesor();
        facadeUnidadAprendizaje = new FacadeUnidadAprendizaje();
        facadeAsigna = new FacadeAsigna();
    }

    @PostConstruct
    public void init() {
        cargarCatalogos();
        tiposDisponibles = new ArrayList<>();
        recalcularHorario();
    }

    private void cargarCatalogos() {
        try {
            profesores = facadeProfesor.obtenerTodos();
            unidades = facadeUnidadAprendizaje.obtenerTodos();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar catalogos de asignacion", e);
            profesores = new ArrayList<>();
            unidades = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al cargar profesores/unidades:", "Intente mas tarde"));
        }
    }
    public void recalcularTipos() {
        tiposDisponibles = new ArrayList<>();
        tipoSeleccionado = null;

        UnidadAprendizaje unidad = getUnidadSeleccionada();
        if (unidad == null || periodo == null || periodo.isBlank()) {
            return;
        }

        try {
            for (String tipo : TIPOS_HORA) {
                int definidas = facadeAsigna.horasDefinidasPorTipo(unidad, tipo);
                if (definidas <= 0) {
                    continue;
                }
                double restantes = facadeAsigna.horasRestantes(unidad, periodo, tipo);
                if (restantes <= 0) {
                    continue;
                }
                String etiqueta = tipo + " - quedan " + formatoHoras(restantes) + "h por agenda";
                tiposDisponibles.add(new OpcionTipo(tipo, etiqueta));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al calcular tipos de sesion disponibles", e);
        }
    }

    private String formatoHoras(double horas) {
        if (horas == Math.floor(horas)) {
            return String.valueOf((int) horas);
        }
        return String.valueOf(horas);
    }

    public void recalcularHorario() {
        horario = new ArrayList<>();
        hayConflicto = false;
        mensajeConflicto = null;

        for (int h = HORA_INICIO_DIA; h <= HORA_FIN_DIA; h++) {
            horario.add(new FilaHorario(String.format("%02d:00", h), dias.size()));
        }

        LocalTime reqInicio = aLocalTime(horaInicio);
        LocalTime reqFin = aLocalTime(horaFin);

        if (reqInicio != null && reqFin != null
                && (reqInicio.isBefore(LocalTime.of(HORA_INICIO_DIA, 0))
                || reqFin.isAfter(LocalTime.of(HORA_FIN_DIA, 0))
                || !reqInicio.isBefore(reqFin))) {
            hayConflicto = true;
            mensajeConflicto = "Fuera de horario (7:00 a 22:00)";
        }


        if (idProfesorSeleccionado == null) {
            return;
        }

        List<Asigna> asignacionesProfesor;
        try {
            asignacionesProfesor = facadeAsigna.obtenerPorProfesor(idProfesorSeleccionado);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al consultar horario del profesor", e);
            return;
        }

        for (Asigna existente : asignacionesProfesor) {
            int columna = dias.indexOf(normalizarDia(existente.getDia()));
            if (columna < 0) {
                continue;
            }

            boolean seTraslapaconLoSolicitado = diaSeleccionado != null
                    && normalizarDia(diaSeleccionado).equalsIgnoreCase(normalizarDia(existente.getDia()))
                    && reqInicio != null && reqFin != null
                    && existente.getHoraInicio().isBefore(reqFin)
                    && reqInicio.isBefore(existente.getHoraFin());

            if (seTraslapaconLoSolicitado) {
                hayConflicto = true;
                if(mensajeConflicto == null) {
                    mensajeConflicto = "No se puede guardar: traslape";
                }
            }

            for (FilaHorario fila : horario) {
                int hora = Integer.parseInt(fila.getEtiqueta().substring(0, 2));
                LocalTime horaSlot = LocalTime.of(hora, 0);
                if (!existente.getHoraInicio().isAfter(horaSlot) && existente.getHoraFin().isAfter(horaSlot)) {
                    Celda celda = fila.getCeldas().get(columna);
                    celda.setTexto(existente.getUnidadAprendizaje().getNombre());
                    celda.setEstado(seTraslapaconLoSolicitado ? "conflicto" : "existente");
                }
            }
        }

    }

    public void guardar() {
        recalcularHorario();

        if (hayConflicto) {
            return;
        }

        if (idProfesorSeleccionado == null || idUnidadSeleccionada == null || diaSeleccionado == null
                || horaInicio == null || horaFin == null || tipoSeleccionado == null
                || grupo == null || grupo.isBlank() || periodo == null || periodo.isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Faltan datos:", "Completa profesor, unidad, dia, horas, grupo y periodo"));
            return;
        }

        try {
            Profesor profesor = profesores.stream()
                    .filter(p -> p.getIdProfesor().equals(idProfesorSeleccionado))
                    .findFirst().orElse(null);
            UnidadAprendizaje unidad = unidades.stream()
                    .filter(u -> u.getIdUnidadAP().equals(idUnidadSeleccionada))
                    .findFirst().orElse(null);

            if (profesor == null || unidad == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error al guardar:", "Selecciona de nuevo el profesor y la unidad"));
                return;
            }

            Asigna asigna = new Asigna();
            asigna.setUsuario(loginUI.getUsuario());
            asigna.setProfesor(profesor);
            asigna.setUnidadAprendizaje(unidad);
            asigna.setDia(diaSeleccionado);
            asigna.setHoraInicio(aLocalTime(horaInicio));
            asigna.setHoraFin(aLocalTime(horaFin));
            asigna.setGrupo(grupo);
            asigna.setPeriodo(periodo);
            asigna.setTipoHora(tipoSeleccionado);

            facadeAsigna.guardarAsignacion(asigna);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Asignacion guardada:", "Se agrego correctamente al horario"));

            diaSeleccionado = null;
            horaInicio = null;
            horaFin = null;
            recalcularTipos();
            recalcularHorario();

        } catch (IllegalArgumentException e) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede guardar:", e.getMessage()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar la asignacion", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al guardar la asignacion:", "Intente mas tarde"));
        }
    }

    private LocalTime aLocalTime(Date fecha) {
        if (fecha == null) {
            return null;
        }
        return fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime()
                .withSecond(0).withNano(0);
    }

    private String normalizarDia(String dia) {
        if (dia == null) {
            return "";
        }
        return dia.trim();
    }



    public static class FilaHorario implements Serializable {
        private final String etiqueta;
        private final List<Celda> celdas;

        public FilaHorario(String etiqueta, int numDias) {
            this.etiqueta = etiqueta;
            this.celdas = new ArrayList<>();
            for (int i = 0; i < numDias; i++) {
                celdas.add(new Celda());
            }
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public List<Celda> getCeldas() {
            return celdas;
        }
    }

    public static class Celda implements Serializable {
        private String texto = "";
        private String estado = "vacio";

        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    public static class OpcionTipo implements Serializable {
        private final String valor;
        private final String etiqueta;

        public OpcionTipo(String valor, String etiqueta) {
            this.valor = valor;
            this.etiqueta = etiqueta;
        }

        public String getValor() {
            return valor;
        }

        public String getEtiqueta() {
            return etiqueta;
        }
    }



    public List<Profesor> getProfesores() {
        return profesores;
    }

    public List<UnidadAprendizaje> getUnidades() {
        return unidades;
    }

    public List<String> getDias() {
        return dias;
    }

    public List<String> getPeriodos() {
        return PERIODOS;
    }

    public List<FilaHorario> getHorario() {
        return horario;
    }

    public List<OpcionTipo> getTiposDisponibles() {
        return tiposDisponibles;
    }

    public Integer getIdProfesorSeleccionado() {
        return idProfesorSeleccionado;
    }

    public void setIdProfesorSeleccionado(Integer idProfesorSeleccionado) {
        this.idProfesorSeleccionado = idProfesorSeleccionado;
    }

    public Integer getIdUnidadSeleccionada() {
        return idUnidadSeleccionada;
    }

    public void setIdUnidadSeleccionada(Integer idUnidadSeleccionada) {
        this.idUnidadSeleccionada = idUnidadSeleccionada;
    }

    public UnidadAprendizaje getUnidadSeleccionada() {
        if (idUnidadSeleccionada == null || unidades == null) {
            return null;
        }
        return unidades.stream()
                .filter(u -> u.getIdUnidadAP().equals(idUnidadSeleccionada))
                .findFirst().orElse(null);
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public String getDiaSeleccionado() {
        return diaSeleccionado;
    }

    public void setDiaSeleccionado(String diaSeleccionado) {
        this.diaSeleccionado = diaSeleccionado;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public boolean isHayConflicto() {
        return hayConflicto;
    }

    public String getMensajeConflicto() {
        return mensajeConflicto;
    }
}