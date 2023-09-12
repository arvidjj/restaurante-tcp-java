package beans.menu;

import java.io.Serializable;
import java.util.Map;

import beans.base.AbstractBean;
import beans.comida.Comida;

	public class MenuApp extends AbstractBean implements Serializable {

		private static final long serialVersionUID = 1L;
		private String nombre;
		private Map<Integer, Comida> entradas;
		private Map<Integer, Comida> platoFondo;
		private Map<Integer, Comida> postres;
		private Map<Integer, Comida> bebidas;
		public MenuApp() {
		}
		
		public Map<Integer, Comida> getEntradas() {
			return entradas;
		}
		public void setEntradas(Map<Integer, Comida> entradas) {
			this.entradas = entradas;
		}
		public Map<Integer, Comida> getPlatoFondo() {
			return platoFondo;
		}
		public void setPlatoFondo(Map<Integer, Comida> platoFondo) {
			this.platoFondo = platoFondo;
		}
		public Map<Integer, Comida> getPostres() {
			return postres;
		}
		public void setPostres(Map<Integer, Comida> postres) {
			this.postres = postres;
		}
		public Map<Integer, Comida> getBebidas() {
			return bebidas;
		}
		public void setBebidas(Map<Integer, Comida> bebidas) {
			this.bebidas = bebidas;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		
		
	}
