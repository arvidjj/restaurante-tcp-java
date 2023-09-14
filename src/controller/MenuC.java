package controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import beans.comida.Comida;
import beans.menu.MenuApp;
import service.cheff.CheffiServiceImpl;
import service.menu.MenuiServiceImpl;

public class MenuC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuApp menuPrincipal;
	
	public MenuC() {
		 Comida entrada1 = new Comida("Papas dulces", 100000);
	        Comida platoFondo1 = new Comida("Fideos", 100000);
	        Comida postre1 = new Comida("Macaruya", 100000);
	        Comida bebida1 = new Comida("Jugo", 100000);
	        
	        Comida entrada2 = new Comida("Rollos de queso", 100000);
	        Comida platoFondo2 = new Comida("Salmon", 100000);
	        Comida postre2 = new Comida("Helado", 100000);
	        Comida bebida2= new Comida("Soda", 100000);
	        Map<Integer, Comida> entradas = new HashMap<>();
	        Map<Integer, Comida> platosFondo = new HashMap<>();
	        Map<Integer, Comida> postres = new HashMap<>();
	        Map<Integer, Comida> bebidas = new HashMap<>();
	        entradas.put(1, entrada1);
	        platosFondo.put(1, platoFondo1);
	        postres.put(1, postre1);
	        bebidas.put(1, bebida1);
	        
	        entradas.put(2, entrada2);
	        platosFondo.put(2, platoFondo2);
	        postres.put(2, postre2);
	        bebidas.put(2, bebida2);
	        
	        menuPrincipal = new MenuApp();
	        menuPrincipal.setEntradas(entradas);
	        menuPrincipal.setPlatoFondo(platosFondo);
	        menuPrincipal.setPostres(postres);
	        menuPrincipal.setBebidas(bebidas);
	}

	public MenuApp getMenuPrincipal() {
		return menuPrincipal;
	}
	
}
