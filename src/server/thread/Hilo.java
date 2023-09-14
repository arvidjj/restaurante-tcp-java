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
import beans.usuario.Usuario;
import controller.MenuC;
import service.cheff.CheffiServiceImpl;
import service.cheff.ICheffService;
import service.pedido.PedidoiServiceImpl;
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
	private final PedidoiServiceImpl pedidoService;
	private final Loggeador log;
	
	public Hilo(Socket socket, Integer numeroHilo, Cheff chefAsignado, CheffiServiceImpl chefService, PedidoiServiceImpl pedidoServ) throws IOException {
		//Servicios
		cheffService =chefService;
		pedidoService =pedidoServ;
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
			final Pedido pedido = (Pedido) _dataInputStream.readObject();
			log.loggear("(hilo) Recibido pedido: " + pedido.getNumPedido() + " En la mesa: " + pedido.getNumMesa() + ", asiento: " + pedido.getNumAsiento());
			pedidoService.save(pedido); //guardar pedido en el servicio
			log.loggear("(hilo) Pedido #" + pedido.getNumPedido() + " asignado al Chef " + chefAsignado.getNombre());
			
			System.out.println("(hilo) Cheff asignado: " + chefAsignado.getNombre());
			System.out.println("(hilo) Recibido pedido: " + pedido.getNumPedido() + " En la mesa: " + pedido.getNumMesa() + ", asiento: " + pedido.getNumAsiento());
			System.out.println("(hilo) Orden: " + pedido.getPedMenu().getEntrada() + " " + pedido.getPedMenu().getPlatoFondo() + " " + pedido.getPedMenu().getBebidas());
			
			System.out.println("(hilo) Escribe el numero del pedido para marcar como completado, Pedido#: " + pedido.getNumPedido());
			String opcion = reader.readLine();
			
			if (opcion.equals(Integer.toString(pedido.getNumPedido()))) {
				pedido.setServido(true);
			}
			
			sleep(1000);
			_dataOutputStream.writeObject(pedido);
		
		_socketCliente.close();
		} catch (IOException e) {
			System.out.println("IOEXCEPTION en hilo");
		} catch (ClassNotFoundException e) {
			System.out.println("CLassNotFOundException en hilo");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("(hilo) Cheff " + chefAsignado.getNombre() + " ya esta disponible de nuevo.");
		log.loggear("(hilo) Cheff " + chefAsignado.getNombre() + " ya esta disponible de nuevo.");
		chefAsignado.setDisponible(true);
	}
	
}
