package beans.usuario;

import java.io.Serializable;

import beans.base.AbstractBean;

public class Usuario extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private String apellido;
	
	public Usuario(String nombre, String apellido) {
		this.nombre = nombre;
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	
}
