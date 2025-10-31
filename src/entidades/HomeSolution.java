package entidades;

import java.util.ArrayList;
import java.util.List;
import entidades.Tupla;

public class HomeSolution implements IHomeSolution {
	
	private List<Empleado> empleados;
	private List<Proyecto> proyectos;
	
	public HomeSolution() {
		this.empleados = new ArrayList<>();
		this.proyectos = new ArrayList<>();
	}
	
	@Override
	public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
		if (nombre == null || nombre.isEmpty() || valor <= 0) {
			throw new IllegalArgumentException("Los datos ingresados son invalidos.");
		}
		this.empleados.add(new EmpleadoContratado(nombre, valor));
	}

	@Override
	public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
		if (nombre == null || nombre.isEmpty() || valor <= 0 || categoria == null) {
			throw new IllegalArgumentException("Los datos ingresados son invalidos");
		}
		this.empleados.add(new EmpleadoDePlanta(nombre, valor, categoria));
	}

	@Override
	public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio, String[] cliente, String inicio, String fin) throws IllegalArgumentException {
		Proyecto nuevoProyecto = new Proyecto(titulos, descripcion, dias, domicilio, cliente, inicio, fin);
		this.proyectos.add(nuevoProyecto);
	}

	@Override
	public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) throws IllegalArgumentException {
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				if(p.isFinalizado()) {
					throw new IllegalArgumentException("El proyecto esta finalizado");
				}
				p.agregarTarea(titulo, descripcion, dias);
			}
			throw new IllegalArgumentException("El proyecto no existe.");
		}
	}

	@Override
	public void finalizarTarea(Integer numero, String titulo) throws Exception {
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				for (Tarea t: p.getTareas()) {
					if (t.getTitulo().equals(titulo)) {
						if (t.estaTerminada()) {
							throw new Exception("La tarea ya esta finalizada");
						}
						if (t.tieneEmpleadoAsignado()) {
							for (Empleado e: this.empleados) {
								if (e.getLegajo() == t.getLegajoEmpleadoAsignado()) {
									e.setDisponible(true);
								}
							}
						}
					t.finalizarTarea();
					}
				}
			}
		}
	}

	@Override
	public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException {
		Fecha fechaDeFinalizacion = new Fecha(fin);
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				if (fechaDeFinalizacion.esAnteriorA(p.getFechaInicio())) {
					throw new IllegalArgumentException("La fecha de finalizacion no puede ser anterior a la fecha de inicio");
				}
				p.finalizarProyecto(fin);
			}
		}
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
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				return p.calcularCostoTotal(this.empleados);
			}
		}
		return 0;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosFinalizados() {
		List<Tupla<Integer, String>> proyectosFinalizados = new ArrayList<>();
		for (Proyecto p: this.proyectos) {
			if (p.isFinalizado()) {
				proyectosFinalizados.add(new Tupla<>(p.getNroProyecto(), p.getDireccionVivienda()));
			}
		}
		return proyectosFinalizados;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosPendientes() {
		List<Tupla<Integer, String>> proyectosPendientes = new ArrayList<>();
		for (Proyecto p: this.proyectos) {
			if (!p.isFinalizado()) {
				boolean algunaTareaAsignada = false;
				for (Tarea t: p.getTareas()) {
					if (t.tieneEmpleadoAsignado()) {
						algunaTareaAsignada = true;
					}
				}
				if (!algunaTareaAsignada) {
				proyectosPendientes.add(new Tupla<>(p.getNroProyecto(), p.getDireccionVivienda()));
				}
			}
		}
		return proyectosPendientes;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {
		List<Tupla<Integer, String>> proyectosActivos = new ArrayList<>();
		for (Proyecto p: this.proyectos) {
			if (!p.isFinalizado()) {
				boolean algunaTareaAsignada = false;
				for (Tarea t: p.getTareas()) {
					if (t.tieneEmpleadoAsignado()) {
						algunaTareaAsignada = true;
					}
				}
				if (algunaTareaAsignada) {
				proyectosActivos.add(new Tupla<>(p.getNroProyecto(), p.getDireccionVivienda()));
				}
			}
		}
		return proyectosActivos;
	}

	@Override
	public Object[] empleadosNoAsignados() {
		List<Empleado> noAsignados = new ArrayList<>();
		for(Empleado e: this.empleados) {
			if(e.isDisponible()) {
				noAsignados.add(e);
			}
		}
		return noAsignados.toArray();
	}

	@Override
	public boolean estaFinalizado(Integer numero) {
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				return p.isFinalizado();
			}
		}
		return false;
	}

	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {
		for (Empleado e: this.empleados) {
			if (e.getLegajo() == legajo) {
				return e.getCantidadRetrasos();
			}
		}
		return 0;
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
		List<Tupla<Integer,String>> empleadosAsignados = new ArrayList<>();
			for (Proyecto p: this.proyectos) {
				if (p.getNroProyecto() == numero) {
					for (Tarea t: p.getTareas()) {
						if (t.tieneEmpleadoAsignado()) {
								for (Empleado e: this.empleados) {
									if (e.getLegajo() == t.getLegajoEmpleadoAsignado()) {
										empleadosAsignados.add(new Tupla<>(e.getLegajo(), e.getNombre()));
									}
								}
							}
						}
					}
				}
		return empleadosAsignados;
	}

	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {
		List<Tarea> tareasNoAsignadas = new ArrayList<>();
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				for (Tarea t: p.getTareas()) {
					if (!t.tieneEmpleadoAsignado()) {
						tareasNoAsignadas.add(t);
					}
				}
			}
		}
		return null;
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				p.getTareas().toArray();
			}
		}
		return null;
	}

	@Override
	public String consultarDomicilioProyecto(Integer numero) {
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				return p.getDireccionVivienda();
			}
		}
		return null;
	}

	@Override
	public boolean tieneRestrasos(Integer legajo) {
		for (Empleado e: this.empleados) {
			if (e.getLegajo() == legajo) {
				return e.tuvoRetrasos();
			}
		}
		return false;
	}

	@Override
	public List<Tupla<Integer, String>> empleados() {
		List<Tupla<Integer, String>> listaEmpleados = new ArrayList<>();
		for (Empleado e: this.empleados) {
			Tupla<Integer, String> nuevaTupla = new Tupla<>(e.getLegajo(), e.getNombre());
			listaEmpleados.add(nuevaTupla);
		}
		return listaEmpleados;
	}

	@Override
	public String consultarProyecto(Integer numero) {
		for (Proyecto p: this.proyectos) {
			if (p.getNroProyecto() == numero) {
				return Integer.toString(p.getNroProyecto());
			}
		}
		return null;
	}

}
