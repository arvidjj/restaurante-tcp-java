package beans.pedidoConjunto;

	import java.io.Serializable;
import java.util.ArrayList;

import beans.base.AbstractBean;
import beans.pedido.Pedido;

	public class PedidoConjunto extends AbstractBean implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private ArrayList<Pedido> pedidos;
		private int numMesa;
		private boolean servidoEnTotalidad;
		
		public PedidoConjunto(int numMesa) {
			super();
			this.numMesa = numMesa;
			servidoEnTotalidad = false;
			pedidos = new ArrayList<>();
		}
		public ArrayList<Pedido> getPedidos() {
			return pedidos;
		}
		public void setPedidos(ArrayList<Pedido> pedidos) {
			this.pedidos = pedidos;
		}
		public int getNumMesa() {
			return numMesa;
		}
		public void setNumMesa(int numMesa) {
			this.numMesa = numMesa;
		}
		
		public void addPedido(Pedido pedido) {
			pedidos.add(pedido);
		}
		public boolean isServidoEnTotalidad() {
			return servidoEnTotalidad;
		}
		public void setServidoEnTotalidad(boolean servidoEnTotalidad) {
			this.servidoEnTotalidad = servidoEnTotalidad;
		}
		
		
		
	}
