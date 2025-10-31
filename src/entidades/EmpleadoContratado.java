package entidades;

public class EmpleadoContratado extends Empleado {
	private double valorHora;
	
	public EmpleadoContratado(String nombre, double valorHora) {
		super(nombre);
		this.valorHora = valorHora;
	}

	@Override
	public double calcularCostoTarea(Tarea tarea) {
		double totalHoras = tarea.getDuracion() * 8; //Convierto la duracion en dias a horas totales de trabajo
		return totalHoras * this.valorHora;
	}
}
