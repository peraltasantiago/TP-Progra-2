package entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import entidades.Tupla;

public class HomeSolution implements IHomeSolution {
	
	private Map<Integer, Empleado> empleados; //La clave es el nro de legajo y el valor es el Empleado
	private Map<Integer, Proyecto> proyectos; //La clave es el nro de proyecto y el valor es el Proyecto
	
	public HomeSolution() {
		this.empleados = new HashMap<>();
		this.proyectos = new HashMap<>();
	}
	
	@Override
	public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
		if (nombre == null || nombre.isEmpty() || valor < 0) {
			throw new IllegalArgumentException("Los datos ingresados son invalidos.");
		}
		EmpleadoContratado nuevoEmpleado = new EmpleadoContratado(nombre, valor);
		this.empleados.put(nuevoEmpleado.getLegajo(), nuevoEmpleado);
	}

	@Override
	public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
		if (nombre == null || nombre.isEmpty() || valor <= 0 || categoria == null) {
			throw new IllegalArgumentException("Los datos ingresados son invalidos");
		}
		if (!categoria.equalsIgnoreCase("INICIAL") && !categoria.equalsIgnoreCase("TECNICO") && !categoria.equalsIgnoreCase("EXPERTO")) {
			throw new IllegalArgumentException("Los datos ingresados son invalidos");
		}
		EmpleadoDePlanta nuevoEmpleado = new EmpleadoDePlanta(nombre, valor, categoria);
		this.empleados.put(nuevoEmpleado.getLegajo(), nuevoEmpleado);
	}

	@Override
	public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio, String[] cliente, String inicio, String fin) throws IllegalArgumentException {
		Proyecto nuevoProyecto = new Proyecto(titulos, descripcion, dias, domicilio, cliente, inicio, fin);
		this.proyectos.put(nuevoProyecto.getNroProyecto(), nuevoProyecto);
	}

	@Override
	public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
		Proyecto proyecto = this.proyectos.get(numero);
		if (proyecto.estaFinalizado()) {
			throw new Exception ("El proyecto ya esta finalizado.");
		}
		Tarea tarea = proyecto.buscarTarea(titulo);
		if (tarea.tieneEmpleadoAsignado()) {
			throw new Exception ("La tarea ya esta asignada a otro empleado.");
		}
		for (Empleado empleado: this.empleados.values()) {
			if (empleado.estaDisponible()) {
				tarea.asignarEmpleado(empleado.getLegajo());
				empleado.setDisponible(false);
				return;
			}
		}
		throw new Exception ("No hay empleados disponibles para asignar a esta tarea.");
	}

	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {
		Proyecto proyecto = this.proyectos.get(numero);
		if (proyecto.estaFinalizado()) {
			throw new Exception ("El proyecto ya esta finalizado.");
		}
		Tarea tarea = proyecto.buscarTarea(titulo);
		if (tarea.tieneEmpleadoAsignado()) {
			throw new Exception ("La tarea ya esta asignada a otro empleado.");
		}
		Empleado posibleEmpleado = null;
		int menorRetraso = Integer.MAX_VALUE; //Inicializo al maximo Integer para poder hacer la comparacion
		
		for (Empleado empleado: this.empleados.values()) {
			if (empleado.estaDisponible()) {
				if (empleado.getCantidadRetrasos() < menorRetraso) {
					menorRetraso = empleado.getCantidadRetrasos();
					posibleEmpleado = empleado;
				}
			}
		}
//		if (posibleEmpleado == null) {
//			throw new Exception ("No hay empleados disponibles para esta tarea.");
//		}
		tarea.asignarEmpleado(posibleEmpleado.getLegajo());
		posibleEmpleado.setDisponible(false);
	}

	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) throws IllegalArgumentException {
		Proyecto proyecto = this.proyectos.get(numero);
		if (proyecto == null) {
			throw new IllegalArgumentException("El proyecto no existe.");
		}
		Tarea tarea = proyecto.buscarTarea(titulo);
		if (tarea == null) {
			throw new IllegalArgumentException("La tarea no existe en este proyecto.");
		}
		tarea.registrarRetraso(cantidadDias);
		if (tarea.tieneEmpleadoAsignado()) {
			Empleado empleado = this.empleados.get(tarea.getLegajoEmpleadoAsignado());
			empleado.acumularRetrasos();
		}
		proyecto.recalcularFechaFin();
	}

	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) throws IllegalArgumentException {
		Proyecto proyecto = this.proyectos.get(numero);
			if(proyecto.estaFinalizado()) {
				throw new IllegalArgumentException("El proyecto esta finalizado");
			}
			proyecto.agregarTarea(titulo, descripcion, dias);
			}

	@Override
	public void finalizarTarea(Integer numero, String titulo) throws Exception {
		Proyecto proyecto = this.proyectos.get(numero);
		Tarea tarea = proyecto.buscarTarea(titulo);
			if (tarea.estaTerminada()) {
				throw new Exception("La tarea ya esta finalizada");
			}
			if (tarea.tieneEmpleadoAsignado()) {
				Empleado empleado = this.empleados.get(tarea.getLegajoEmpleadoAsignado());
						empleado.setDisponible(true);
			}
			tarea.finalizarTarea();
		}

	@Override
	public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException {
		Fecha fechaDeFinalizacion = new Fecha(fin);
		Proyecto proyecto = this.proyectos.get(numero);
			if (fechaDeFinalizacion.esAnteriorA(proyecto.getFechaInicio())) {
				throw new IllegalArgumentException("La fecha de finalizacion no puede ser anterior a la fecha de inicio");
			}
			for (Tarea tarea: proyecto.getTareas()) {
				if (tarea.tieneEmpleadoAsignado()) {
					Empleado empleado = this.empleados.get(tarea.getLegajoEmpleadoAsignado());
					empleado.setDisponible(true);
				}
			}
			proyecto.finalizarProyecto(fin);
	}

	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double costoProyecto(Integer numero) {
		Proyecto proyecto = this.proyectos.get(numero);
		if (proyecto == null) {
			return 0;
		}
		return proyecto.calcularCostoTotal(new ArrayList<>(this.empleados.values()));
	}

	@Override
	public List<Tupla<Integer, String>> proyectosFinalizados() {
		List<Tupla<Integer, String>> proyectosFinalizados = new ArrayList<>();
		for (Proyecto proyecto: this.proyectos.values()) {
			if (proyecto.estaFinalizado()) {
				proyectosFinalizados.add(new Tupla<>(proyecto.getNroProyecto(), proyecto.getDireccionVivienda()));
			}
		}
		return proyectosFinalizados;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosPendientes() {
		List<Tupla<Integer, String>> proyectosPendientes = new ArrayList<>();
		for (Proyecto proyecto: this.proyectos.values()) {
			if (!proyecto.estaFinalizado()) {
				boolean algunaTareaAsignada = false;
				for (Tarea t: proyecto.getTareas()) {
					if (t.tieneEmpleadoAsignado()) {
						algunaTareaAsignada = true;
					}
				}
				if (!algunaTareaAsignada) {
				proyectosPendientes.add(new Tupla<>(proyecto.getNroProyecto(), proyecto.getDireccionVivienda()));
				}
			}
		}
		return proyectosPendientes;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {
		List<Tupla<Integer, String>> proyectosActivos = new ArrayList<>();
		for (Proyecto proyecto: this.proyectos.values()) {
			if (!proyecto.estaFinalizado()) {
				boolean algunaTareaAsignada = false;
				for (Tarea t: proyecto.getTareas()) {
					if (t.tieneEmpleadoAsignado()) {
						algunaTareaAsignada = true;
					}
				}
				if (algunaTareaAsignada) {
				proyectosActivos.add(new Tupla<>(proyecto.getNroProyecto(), proyecto.getDireccionVivienda()));
				}
			}
		}
		return proyectosActivos;
	}

	@Override
	public Object[] empleadosNoAsignados() {
		List<Empleado> noAsignados = new ArrayList<>();
		for(Empleado empleado: this.empleados.values()) {
			if(empleado.estaDisponible()) {
				noAsignados.add(empleado);
			}
		}
		return noAsignados.toArray();
	}

	@Override
	public boolean estaFinalizado(Integer numero) {
		Proyecto proyecto = this.proyectos.get(numero);
			return proyecto.estaFinalizado();
	}
	
	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {
		Empleado empleado = this.empleados.get(legajo);
			return empleado.getCantidadRetrasos();
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
		List<Tupla<Integer,String>> empleadosAsignados = new ArrayList<>();
		Proyecto proyecto = this.proyectos.get(numero);	
			for (Tarea tarea: proyecto.getTareas()) {
				if (tarea.tieneEmpleadoAsignado()) {
					Empleado empleado = this.empleados.get(tarea.getLegajoEmpleadoAsignado());
					empleadosAsignados.add(new Tupla<>(empleado.getLegajo(), empleado.getNombre()));
					}
				}
		return empleadosAsignados;
	}

	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {
		List<Tarea> tareasNoAsignadas = new ArrayList<>();
		Proyecto proyecto = this.proyectos.get(numero);
		if (proyecto != null && proyecto.estaFinalizado()) {
			throw new IllegalArgumentException("No se pueden consultar las tareas de un proyecto ya finalizado.");
		}
		for (Tarea t: proyecto.getTareas()) {
			if (!t.tieneEmpleadoAsignado()) {
				tareasNoAsignadas.add(t);
			}
		}
		return tareasNoAsignadas.toArray();
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		Proyecto proyecto = this.proyectos.get(numero);
			return proyecto.getTareas().toArray();
	}

	@Override
	public String consultarDomicilioProyecto(Integer numero) {
		Proyecto proyecto = this.proyectos.get(numero);
			return proyecto.getDireccionVivienda();
	}

	@Override
	public boolean tieneRestrasos(Integer legajo) {
		Empleado empleado = this.empleados.get(legajo);
			return empleado.tuvoRetrasos();
	}

	@Override
	public List<Tupla<Integer, String>> empleados() {
		List<Tupla<Integer, String>> listaEmpleados = new ArrayList<>();
		for (Empleado empleado: this.empleados.values()) {
			Tupla<Integer, String> nuevaTupla = new Tupla<>(empleado.getLegajo(), empleado.getNombre());
			listaEmpleados.add(nuevaTupla);
		}
		return listaEmpleados;
	}

	@Override
	public String consultarProyecto(Integer numero) {
		Proyecto proyecto = this.proyectos.get(numero);
			return Integer.toString(proyecto.getNroProyecto());
	}
}
