package server;

import javax.swing.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import beans.cheff.Cheff;
import server.thread.Hilo;
import service.cheff.CheffiServiceImpl;
import utils.Configurator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorGUI extends JFrame {
    /**
	 * 
	 */
	private static final Logger loggeador = LogManager.getLogger(ServidorGUI.class.getName());  //logger
	private static final long serialVersionUID = 1L;
	private JTextArea logTextArea;
    private JButton asignarPedidoButton;
    Configurator configuracion = new Configurator("src/config.properties");
    private JComboBox<String> boxCheffs;
    
    private int contadorHilo;
    CheffiServiceImpl cheffService;

    public ServidorGUI() {
        super("Servidor TCP");

        logTextArea = new JTextArea(20, 20);
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        asignarPedidoButton = new JButton("Asignar Pedido"); 
        
        cheffService = new CheffiServiceImpl();
        Cheff chef1 = new Cheff("Cheff ramon");
        Cheff chef2 = new Cheff("Cheff felipe");
        Cheff chef3 = new Cheff("Cheff juan");
        cheffService.save(chef1);
        cheffService.save(chef2);
        cheffService.save(chef3);
        
        for (String chef : cheffService.getAll().values().stream()
                .map(comida -> comida.getNombre())
                .toArray(String[]::new)) {
            System.out.println(chef);
        }
        
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
        
        asignarPedidoButton.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
                try {
                	System.out.println("hola");
					asignarPedido(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
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
                Socket socketCliente = serverSocket.accept();
                log("Pedido recibido");

                asignarPedido(socketCliente);
            }
        } catch (IOException e) {
            log("Error en el servidor: " + e.getMessage());
        }
    }

    private void asignarPedido(Socket socketcliente) throws IOException {
        String selectedChefName = (String) boxCheffs.getSelectedItem();
        Cheff chefAsignado = cheffService.findByName(selectedChefName);

        if (chefAsignado != null) {
            new Hilo(socketcliente, contadorHilo, chefAsignado);
            log("Pedido asignado al Chef " + chefAsignado.getNombre());
            contadorHilo++;
        } else {
            log("No hay chefs disponibles en este momento.");
        }
    }

    
    private void log(String mensaje) {
    	////OBTENER FECHA Y HORA
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);
        
        logTextArea.append("("+ formattedDateTime + ") " + mensaje + "\n");
        loggeador.info("("+ formattedDateTime + ") " +mensaje);
    }

    public static void main(String[] args) {

            new ServidorGUI();
    }
}

