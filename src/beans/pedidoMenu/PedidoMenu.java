package beans.pedidoMenu;

import java.io.Serializable;

import beans.base.AbstractBean;

public class PedidoMenu extends AbstractBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String entrada;
	private String platoFondo;
	private String postre;
	private String bebidas;
	
	public PedidoMenu() {
	}
	
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	public String getPlatoFondo() {
		return platoFondo;
	}
	public void setPlatoFondo(String platoFondo) {
		this.platoFondo = platoFondo;
	}
	public String getPostre() {
		return postre;
	}
	public void setPostre(String postre) {
		this.postre = postre;
	}
	public String getBebidas() {
		return bebidas;
	}
	public void setBebidas(String bebidas) {
		this.bebidas = bebidas;
	}
	
	
	
}
