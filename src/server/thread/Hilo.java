package server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import beans.cheff.Cheff;
import beans.pedido.Pedido;
import beans.usuario.Usuario;
import service.cheff.CheffiServiceImpl;
import service.cheff.ICheffService;
import service.usuario.IUsuarioService;	
import service.usuario.UsuarioiServiceImpl;

public class Hilo extends Thread {
	
	private final Socket _socketCliente;
	private final ObjectInputStream _dataInputStream;
	private final ObjectOutputStream _dataOutputStream;
	private final Integer _numeroHilo;
	private final Cheff chefAsignado; 
	private final ICheffService cheffService;
	
	public Hilo(Socket socket, Integer numeroHilo, Cheff chefAsignado) throws IOException {
		cheffService =new CheffiServiceImpl();
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
			//final Usuario usuario = (Usuario) _dataInputStream.readObject();
			final Pedido pedido = (Pedido) _dataInputStream.readObject();
			//usuario.setApellido(usuario.getApellido().toUpperCase() + " ==>");
			//userService.save(usuario);
			System.out.println("Cheff asignado: " + chefAsignado.getNombre());
			System.out.println("Recibido pedido: " + pedido.getNumPedido() + " En la mesa: " + pedido.getNumMesa() + ", asiento: " + pedido.getNumAsiento());
			System.out.println("Orden: " + pedido.getPedMenu().getEntrada() + " " + pedido.getPedMenu().getPlatoFondo() + " " + pedido.getPedMenu().getBebidas());
			
			System.out.println("Presiona 1 para marcar como completado");
			String opcion = reader.readLine();
			
			if (opcion.equals("1")) {
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
	}
}
