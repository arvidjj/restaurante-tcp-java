package beans.pedido;

import java.io.Serializable;
import java.util.ArrayList;

import beans.base.AbstractBean;
import beans.pedidoMenu.PedidoMenu;

public class Pedido extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int numPedido;
	private int numAsiento;
	private PedidoMenu pedMenu;
	private boolean Servido;
	
	public Pedido(int numAsiento) {
		this.numAsiento = numAsiento;
		this.pedMenu = new PedidoMenu();
	}
	public int getNumPedido() {
		return numPedido;
	}
	public void setNumPedido(int numPedido) {
		this.numPedido = numPedido;
	}
	public int getNumAsiento() {
		return numAsiento;
	}
	public void setNumAsiento(int numAsiento) {
		this.numAsiento = numAsiento;
	}
	public PedidoMenu getPedMenu() {
		return pedMenu;
	}
	public void setPedMenu(PedidoMenu pedMenu) {
		this.pedMenu = pedMenu;
	}
	public boolean isServido() {
		return Servido;
	}
	public void setServido(boolean servido) {
		Servido = servido;
	}
	
	
	
}
