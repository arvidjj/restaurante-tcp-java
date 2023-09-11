package beans.cheff;

import java.io.Serializable;

import beans.base.AbstractBean;
import beans.pedido.Pedido;

public class Cheff extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private String apellido;
	private Pedido pedido;
	private boolean disponible = true;
	
	public Cheff(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
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

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	
	
}
