package entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Proyecto {
	private int nroProyecto;
	private static int contadorProyecto = 0;
	private Cliente cliente;
	private String direccionVivienda;
	private Fecha fechaInicio;
	private Fecha fechaFinEstimada;
	private Fecha fechaFinReal;
	private boolean finalizado;
	private boolean tieneRetraso;
	private Map<String,Tarea> tareas;
	private List<String> historialEmpleados;
	
	public Proyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio, String[] cliente, String inicio, String fin) {
		this.nroProyecto = ++contadorProyecto;
		this.direccionVivienda = domicilio;
		this.finalizado = false;
		this.tieneRetraso = false;
		
		this.fechaInicio = new Fecha(inicio);
		this.fechaFinEstimada = new Fecha(fin);
		if (this.fechaInicio.esPosteriorA(fechaFinEstimada)) {
			throw new IllegalArgumentException ("La fecha de inicio no puede ser posterior a la fecha de fin");
		}
		
		String nombreCliente = cliente[0];
		String mailCliente = cliente [1];
		String telCliente = cliente [2]; //Asumo que el cliente que se pasa por parametro es del estilo [nombre, email, telefono]
		this.cliente = new Cliente(nombreCliente, mailCliente, telCliente);
		
		this.tareas = new HashMap<>();
		for (int i = 0; i < titulos.length; i++) { //Itero por los diferentes arreglos con el mismo indice guardando en cada variable cada uno de los valores de las diferentes tareas. Con ellas creo una nueva tarea y la agrego a la lista.
			String tituloActual = titulos[i];
			String descripcionActual = descripcion[i];
			double duracionActual = dias[i];
			Tarea nuevaTarea = new Tarea(tituloActual, descripcionActual, duracionActual);
			this.tareas.put(tituloActual,nuevaTarea);
		}
		
		this.historialEmpleados = new ArrayList<>();
	}
		
	public void agregarTarea(String titulo, String descripcion, double duracion) {
		if (this.finalizado) {
			throw new IllegalArgumentException("No se puede agregar una tarea a un proyecto finalizado.");
		}
		Tarea nuevaTarea = new Tarea(titulo, descripcion, duracion);
		this.tareas.put(titulo,nuevaTarea);
		this.recalcularFechaFin();
	}
	
	public void recalcularFechaFin() {
		double duracion = 0;
		for (Tarea t: this.tareas.values()) {
			duracion += t.getDuracion();
		}
		this.fechaFinEstimada = this.fechaInicio.agregarDias(duracion);
	}
	
	public double calcularCostoTotal(List<Empleado> empleados) {
		double subTotal = 0;
		for (Tarea tarea: this.tareas.values()) {
			Integer legajoEmpleadoAsignado = tarea.getLegajoEmpleadoAsignado();
			if (legajoEmpleadoAsignado != null) { //Solo miro las tareas asignadas
				for (Empleado empleadoResponsable: empleados) {
					if (empleadoResponsable.getLegajo() == legajoEmpleadoAsignado)
						subTotal += empleadoResponsable.calcularCostoTarea(tarea);
				}
			}	
		}
		double total = subTotal * 1.35;
		
		if (this.tieneRetraso) {
			total *= 0.25;
		}
		return total;
	}
	
	public Tarea buscarTarea(String titulo) {
		//for (Tarea tarea: this.tareas.values()) {
			//if(tarea.toString().equals(titulo)) {
				return this.tareas.get(titulo);
			//}
		//}	
	}
	
	public void finalizarProyecto(String fechaFin) {
		Fecha fechaDeFinalizacion = new Fecha(fechaFin);
		if (fechaDeFinalizacion.esAnteriorA(this.fechaInicio)) {
			throw new IllegalArgumentException("La fecha de finalizacion no puede ser anterior a la fecha de inicio");
		}
		this.finalizado = true;
		this.fechaFinReal = fechaDeFinalizacion;
	}
	
	public int getNroProyecto() {
		return nroProyecto;
	}
	
	public List<Tarea> getTareas() {
		return new ArrayList<>(this.tareas.values());//devuelve una lista con los valores del map
	}
	
	public String getDireccionVivienda() {
		return direccionVivienda;
	}
	
	public boolean estaFinalizado() {
		return finalizado;
	}
	
	public Fecha getFechaInicio() {
		return fechaInicio;
	}
	
	public Fecha getFechaFinEstimada() {
		return fechaFinEstimada;
	}
	
	public Fecha getFechaFinReal() {
		return fechaFinReal;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public List<String> getHistorialEmpleados() {
		return historialEmpleados;
	}

	public boolean huboRetraso() {
		for(Tarea tarea: this.tareas.values()) {
			if(tarea.huboRetraso()) {
				this.tieneRetraso = true;
				return tieneRetraso;
			}
		}
		return tieneRetraso;
	}
}
