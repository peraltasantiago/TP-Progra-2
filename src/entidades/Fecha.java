package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Fecha {

	private LocalDate fechaInterna;
	private static DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public Fecha(String fechaStr) {
		this.fechaInterna = LocalDate.parse(fechaStr, FORMATO);
	}
	
	public boolean esPosteriorA(Fecha fecha) {
		return this.fechaInterna.isAfter(fecha.fechaInterna);
	}
	
	public boolean esAnteriorA(Fecha fecha) {
		return this.fechaInterna.isBefore(fecha.fechaInterna);
	}
	
	public Fecha agregarDias(double dias) {
		double diasASumar = Math.ceil(dias);
		LocalDate nuevaFechaInterna = this.fechaInterna.plusDays((long) diasASumar);
		return new Fecha(nuevaFechaInterna.toString());
	}
	
	public String toString() {
		return this.fechaInterna.format(FORMATO);
	}
}
