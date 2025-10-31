package entidades;

import java.util.ArrayList;
import java.util.List;

public class Tarea {

	//ATRBIBUTOS
	private String titulo;
	private String descripcion;
	private double duracion;
	private double diasDeRetraso;
	private boolean estaTerminada; //(se refiere a si esta o no finalizado)
	private Integer legajoEmpleadoAsignado;
	private List<Integer> historialResponsables;//Esta lista es nueva. ES UN HISTORIAL QUE GUARDA QUE EMPLEADO HIZO CADA TAREA.
	private boolean huboRetraso;
	
	//CONSTRUCTOR
	public Tarea(String titulo, String descripcion, double duracion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        
        this.diasDeRetraso = 0;//Empieza sin retrasos
        this.estaTerminada = false;//No puede crearse ya terminada
        this.legajoEmpleadoAsignado = null;//La tarea empieza sin empleado asignado
        
        this.historialResponsables = new ArrayList<>();//Y el historial empieza vacio
	}
	
	//METODOS
	
	public void asignarEmpleado(Integer legajo) {
		this.legajoEmpleadoAsignado = legajo;
		if(!this.historialResponsables.contains(legajo)) {
			this.historialResponsables.add(legajo);
		}
	}
	
	//FALTA METODO PARA LIBERAR EMPLEADO? Y LIBERARLO AL TERMINAR UNA TAREA
	
	public void registrarRetraso(double dia) {
		this.diasDeRetraso += dia;
		this.huboRetraso = true;
	}
	
	public String getTitulo() {
		return this.titulo;
	}
	
	public boolean huboRetraso() {
		return this.huboRetraso;
	}
	
	public void finalizarTarea() {
		this.estaTerminada = true;
		this.legajoEmpleadoAsignado = null;
	}
	
	public void cambiarDuracion(double nuevaDuracion) {
        this.duracion = nuevaDuracion;
    }
	
	public double totalDiasDeRetraso() {
        return this.diasDeRetraso;
    }
	
	//GETTERS
	public boolean tieneEmpleadoAsignado() {
	    if (this.legajoEmpleadoAsignado != null) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public Integer getLegajoEmpleadoAsignado() {
		return legajoEmpleadoAsignado;
	}
	
	public boolean estaTerminada(){
		return this.estaTerminada;
	}
	
	public double getDuracion() {
        return duracion;
    }
	
	public List<Integer> getHistorialResponsables() {
        return historialResponsables;
    }
	
	
	@Override
    public String toString() { //"El toString de Tarea sólo debe devolver el título"
        return this.titulo;
    }
	
}
