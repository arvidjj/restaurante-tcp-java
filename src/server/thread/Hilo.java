package server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import beans.cheff.Cheff;
import beans.pedido.Pedido;
import beans.pedidoConjunto.PedidoConjunto;
import beans.usuario.Usuario;
import controller.MenuC;
import service.cheff.CheffiServiceImpl;
import service.cheff.ICheffService;
import service.pedido.PedidoiServiceImpl;
import service.pedidoConjunto.PedidoConjuntoiServiceImpl;
import service.usuario.IUsuarioService;	
import service.usuario.UsuarioiServiceImpl;
import utils.Loggeador;

public class Hilo extends Thread {
	
	private final Socket _socketCliente;
	private final ObjectInputStream _dataInputStream;
	private final ObjectOutputStream _dataOutputStream;
	private final Integer _numeroHilo;
	private final Cheff chefAsignado; 
	private final CheffiServiceImpl cheffService;
	
	private final PedidoConjuntoiServiceImpl pedidoConjuntoService;
	private final PedidoiServiceImpl pedidoService;
	
	private final Loggeador log;
	
	public Hilo(Socket socket, Integer numeroHilo, Cheff chefAsignado, CheffiServiceImpl chefService, PedidoConjuntoiServiceImpl pedidoConjServ, PedidoiServiceImpl pedidoServ) throws IOException {
		//Servicios
		cheffService =chefService;
		pedidoService =pedidoServ;
		pedidoConjuntoService =pedidoConjServ;
		log = new Loggeador();
		
		_socketCliente = socket;
		_numeroHilo = numeroHilo;
		_dataInputStream = new ObjectInputStream(_socketCliente.getInputStream());
		_dataOutputStream = new ObjectOutputStream(_socketCliente.getOutputStream());	
		this.chefAsignado = chefAsignado;
		
		this.start();
	}
	
	@Override
	public void run() {
		BufferedReader reader = 
				  new BufferedReader(new InputStreamReader(System.in));
		try {
			chefAsignado.setDisponible(false);
			//1- DEVOLVER MENU
			MenuC menuPrincipal = new MenuC();
			_dataOutputStream.writeObject(menuPrincipal);
			
			//2- OBTENER PEDIDO (CUANDO EL CLIETNE PRESIONA ENVIAR)
			final PedidoConjunto pedidoEnConjunto = (PedidoConjunto) _dataInputStream.readObject();
			log.loggear("(hilo " + _numeroHilo +") Recibido pedidos de la mesa: " + pedidoEnConjunto.getNumMesa());
			System.out.println("(hilo " + _numeroHilo +") Recibido pedidos de la mesa: " + pedidoEnConjunto.getNumMesa());
			
			pedidoConjuntoService.save(pedidoEnConjunto);
			
			log.loggear("(hilo " + _numeroHilo +") Pedido #" + pedidoEnConjunto.getNumMesa() + " asignado al Chef " + chefAsignado.getNombre());
			System.out.println("(hilo " + _numeroHilo +") Cheff asignado: " + chefAsignado.getNombre());
			
			for (Pedido p : pedidoEnConjunto.getPedidos()) {		
				pedidoService.save(p);//guardar pedidos en el servicio
				System.out.println("(hilo " + _numeroHilo +") Asiento: " + p.getNumAsiento());
				System.out.println("(hilo " + _numeroHilo +") Orden: " + p.getPedMenu().getEntrada() + " " + p.getPedMenu().getPlatoFondo() + " " + p.getPedMenu().getBebidas());
			}
			
			System.out.println("(hilo " + _numeroHilo +") Escribe el numero del pedido para marcar como completado, Pedido#: " + pedidoEnConjunto.getNumMesa());
			String opcion = reader.readLine();
			
			if (opcion.equals(Integer.toString(pedidoEnConjunto.getNumMesa()))) {
				pedidoEnConjunto.setServidoEnTotalidad(true);
			}
			
			sleep(1000);
			_dataOutputStream.writeObject(pedidoEnConjunto);
		
		_socketCliente.close();
		} catch (IOException e) {
			System.out.println("IOEXCEPTION en hilo");
		} catch (ClassNotFoundException e) {
			System.out.println("CLassNotFOundException en hilo");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("(hilo " + _numeroHilo +") Cheff " + chefAsignado.getNombre() + " ya esta disponible de nuevo.");
		log.loggear("(hilo " + _numeroHilo +") Cheff " + chefAsignado.getNombre() + " ya esta disponible de nuevo.");
		chefAsignado.setDisponible(true);
	}
	
}
