package server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import beans.pedido.Pedido;
import beans.usuario.Usuario;
import service.usuario.IUsuarioService;	
import service.usuario.UsuarioiServiceImpl;

public class Hilo extends Thread {
	
	private final Socket _socketCliente;
	private final ObjectInputStream _dataInputStream;
	private final ObjectOutputStream _dataOutputStream;
	private final Integer _numeroHilo;
	
	private final IUsuarioService userService;
	
	public Hilo(Socket socket, Integer numeroHilo) throws IOException {
		userService =new UsuarioiServiceImpl();
		_socketCliente = socket;
		_numeroHilo = numeroHilo;
		_dataInputStream = new ObjectInputStream(_socketCliente.getInputStream());
		_dataOutputStream = new ObjectOutputStream(_socketCliente.getOutputStream());	
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
			System.out.println("Recibido pedido: " + pedido.getNumPedido() + " En la mesa: " + pedido.getNumMesa() + ", asiento: " + pedido.getNumAsiento());
			System.out.println("Orden " + pedido.getPedMenu().getEntrada() + " " + pedido.getPedMenu().getPlatoFondo() + " " + pedido.getPedMenu().getBebidas());
			
			System.out.println("A que cheff deseas asignar este pedido?");
			String cheff = reader.readLine();
			
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