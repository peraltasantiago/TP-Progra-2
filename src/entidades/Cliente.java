package entidades;

public class Cliente {

	//ATRIBUTOS
	private int nroCliente;
	private String nombre;
	private String mail;
	private String telefono;
	
	
																		
	//CONSTRUCTOR
	public Cliente(String nombre, String mail, String telefono) { 
		
		this.nombre= nombre;
		this.mail= mail;
		this.telefono = telefono;
		
		
		
	 }
		//getters 
		
		public int getNroCliente() {
			return nroCliente;
			}
		
		public String getNombre() {
			return nombre;
		}
		
		public String getMail() {
			return mail;
		}
		
		public String getTelefono() {
			return telefono;
		}
		
		
	
	
	
	
}
