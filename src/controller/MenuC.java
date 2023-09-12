package controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import beans.comida.Comida;
import beans.menu.MenuApp;

public class MenuC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuApp menuPrincipal;
	
	public MenuC() {
		 Comida entrada1 = new Comida("Entrada 1", 100000);
	        Comida platoFondo1 = new Comida("PlatoFondo 1", 100000);
	        Comida postre1 = new Comida("Postre 1", 100000);
	        Comida bebida1 = new Comida("Bebida 1", 100000);
	        Map<Integer, Comida> entradas = new HashMap<>();
	        Map<Integer, Comida> platosFondo = new HashMap<>();
	        Map<Integer, Comida> postres = new HashMap<>();
	        Map<Integer, Comida> bebidas = new HashMap<>();
	        entradas.put(1, entrada1);
	        platosFondo.put(1, platoFondo1);
	        postres.put(1, postre1);
	        bebidas.put(1, bebida1);
	        
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
