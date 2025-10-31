package entidades;

public abstract class Empleado {
	private String nombre;
	private int legajo;
	private static int contadorLegajo = 0;
	private int cantidadRetrasos;
	private boolean tuvoRetrasos;
	private boolean disponible;
	
	public Empleado(String nombre) {
		this.legajo = ++contadorLegajo;
		this.nombre = nombre;
		this.cantidadRetrasos = 0; //Empieza sin retrasos
		this.tuvoRetrasos = false;
		this.disponible = true; //Empieza estando disponible
	}
	
	public void acumularRetrasos() {
		this.tuvoRetrasos = true;
		this.cantidadRetrasos++;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public String getNombre() {
		return nombre;
	}

	public int getLegajo() {
		return legajo;
	}

	public int getCantidadRetrasos() {
		return cantidadRetrasos;
	}
	
	public boolean tuvoRetrasos() {
		return tuvoRetrasos;
	}
	
	public abstract double calcularCostoTarea(Tarea tarea);
	
}
