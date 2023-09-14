package server;

import javax.swing.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import beans.cheff.Cheff;
import beans.cocina.Cocina;
import beans.comida.Comida;
import server.thread.Hilo;
import service.cheff.CheffiServiceImpl;
import service.pedido.PedidoiServiceImpl;
import service.pedidoConjunto.PedidoConjuntoiServiceImpl;
import utils.Configurator;
import utils.Loggeador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorGUI extends JFrame {
    /**
	 * 
	 */
	private final Loggeador log; //logger
	private static final long serialVersionUID = 1L;
	private JTextArea logTextArea;
    private JButton asignarPedidoButton;
    Configurator configuracion = new Configurator("src/config.properties");
    private JComboBox<String> boxCheffs;
    
    private int contadorHilo; //contador de pedidos
    private int siguienteEnCola; //siguiente pedido en cola
    
    private Cocina cocina;
    //Servicios
    CheffiServiceImpl cheffService;
    PedidoiServiceImpl pedidoService;
    PedidoConjuntoiServiceImpl pedidoConjuntoService;
    
    private Socket socketCliente;
    private Map<Integer, Socket> colaSockets;
    
    public ServidorGUI() {
        super("Servidor TCP");
        log = new Loggeador();
        colaSockets = new ConcurrentHashMap<>();
        siguienteEnCola = 1;
        
        logTextArea = new JTextArea(20, 20);
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        asignarPedidoButton = new JButton("Asignar Pedido"); 
        
        pedidoConjuntoService = new PedidoConjuntoiServiceImpl();
        pedidoService = new PedidoiServiceImpl();
        cheffService = new CheffiServiceImpl();
        Cheff chef1 = new Cheff("Ramon");
        chef1.setId(0);
        Cheff chef2 = new Cheff("Felipe");
        chef2.setId(1);
        Cheff chef3 = new Cheff("Juan");
        chef3.setId(2);
        
        Cheff chef4 = new Cheff("Alberto");
        chef4.setId(3);
        Cheff chef5 = new Cheff("Pedro");
        chef5.setId(4);
        Cheff chef6 = new Cheff("Jose");
        chef6.setId(5);
        
        cheffService.save(chef1);
        cheffService.save(chef2);
        cheffService.save(chef3);
        cheffService.save(chef4);
        cheffService.save(chef5);
        cheffService.save(chef6);
        
        boxCheffs = new JComboBox<>();
        String[] chefNames = cheffService.getAll().values()
             .stream()
             .map(chef -> chef.getNombre())
             .toArray(String[]::new);
        
     	boxCheffs.setModel(new DefaultComboBoxModel<>(chefNames));
     
        boxCheffs.setSelectedIndex(0); 
        
        JPanel panel = new JPanel();
        panel.add(asignarPedidoButton);
        panel.add(new JLabel("Seleccionar Chef: "));
        panel.add(boxCheffs);
        
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(logScrollPane, BorderLayout.CENTER);
        container.add(panel, BorderLayout.SOUTH);
        
        contadorHilo = 1;

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        //asignarPedidoButton.setEnabled(false); //inicialmente deshabilitar
        
        cocina = new Cocina(cheffService, pedidoConjuntoService, pedidoService); //inicializar la cocina
        asignarPedidoButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                try {
                	String selectedChefName = (String) boxCheffs.getSelectedItem();
                	Cheff chefAsignado = cheffService.findByName(selectedChefName); //obtener el cheff elegido
                	
                    if ( (!(colaSockets.isEmpty())) && chefAsignado.isDisponible()) {                        
                        //System.out.println(siguienteEnCola); //debugging
                        //System.out.println(colaSockets.get(siguienteEnCola));
                        cocina.asignarPedido(colaSockets.get(siguienteEnCola), selectedChefName);//usar la clase cocina
                        colaSockets.remove(siguienteEnCola); //quitar de la cola el que ya se uso
                        siguienteEnCola++; //aumentar la cola
                    } else {
                        log("No se ha aceptado ningún socket, no hay pedidos en espera, o el cheff no esta disponible.");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        iniciarServidor();
        
    }

    private void iniciarServidor() {
        try {
            ServerSocket serverSocket = new ServerSocket(configuracion.getIntProperty("puerto"));
            log("Servidor TCP esperando conexiones en el puerto " + serverSocket.getLocalPort());
            while (true) {
                log("Esperando nuevo pedido...");
                socketCliente = serverSocket.accept(); 
                log("Pedido recibido desde puerto: " + socketCliente.getPort()+ ", seleccione el chef que desea asignar este pedido");
                
                colaSockets.put(contadorHilo, socketCliente);
                contadorHilo++;
                //asignarPedidoButton.setEnabled(true);
            }
        } catch (IOException e) {
            log("Error: " + e.getMessage());
        }
    }
    
    private void log(String mensaje) {
        Loggeador logi = new Loggeador();
    	
        logTextArea.append("("+ logi.obtenerFechaYHora() + ") " + mensaje + "\n");
        log.loggear(mensaje);
        
    }

    public static void main(String[] args) {

            new ServidorGUI();
    }
}

