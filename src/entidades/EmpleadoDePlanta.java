package entidades;

public class EmpleadoDePlanta extends Empleado {

	private double valorDia;
	private String categoria;
	
	public EmpleadoDePlanta(String nombre, double valorDia, String categoria) {
		super(nombre);
		this.valorDia = valorDia;
		this.categoria = categoria;
	}

	@Override
	public double calcularCostoTarea(Tarea tarea) {
		double diasTotales = Math.ceil(tarea.getDuracion()); //Redondea el decimal al entero que le sigue en caso de que tenga medio dia de trabajo
		if (this.getCantidadRetrasos() == 0) 
			return (diasTotales * valorDia) * 1.02;
		else
			return diasTotales * valorDia;
		}
	
	public String getCategoria() {
		return categoria;
	}
	}