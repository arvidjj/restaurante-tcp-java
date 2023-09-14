package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import beans.pedido.Pedido;
import beans.pedidoConjunto.PedidoConjunto;
import beans.pedidoMenu.PedidoMenu;
import controller.MenuC;
import utils.Configurator;

public class ClienteGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private JComboBox<Integer> personasComboBox;
    private JComboBox<Integer> mesaComboBox;
    
	private JComboBox<String> entradaComboBox1;
    private JComboBox<String> platoFondoComboBox1;
    private JComboBox<String> bebidasComboBox1;
    private JComboBox<String> postreComboBox1;
    
	private JComboBox<String> entradaComboBox2;
    private JComboBox<String> platoFondoComboBox2;
    private JComboBox<String> bebidasComboBox2;
    private JComboBox<String> postreComboBox2;
    
	private JComboBox<String> entradaComboBox3;
    private JComboBox<String> platoFondoComboBox3;
    private JComboBox<String> bebidasComboBox3;
    private JComboBox<String> postreComboBox3;
    
	private JComboBox<String> entradaComboBox4;
    private JComboBox<String> platoFondoComboBox4;
    private JComboBox<String> bebidasComboBox4;
    private JComboBox<String> postreComboBox4;
    
    
    private JButton enviarPedidoButton;
    private Socket clienteSocket;
    private ObjectOutputStream dataOutputStream;
    private ObjectInputStream dataInputStream;
    private Configurator configuracion = new Configurator("src/config.properties");
    
    private MenuC menuRecibido;
    
    
    public ClienteGUI() throws ClassNotFoundException {
        setTitle("Realizar un pedido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new GridLayout(4, 2));
        
        JLabel personasLabel = new JLabel("Cantidad de personas:");
        personasComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        //seleccionar comidas
        JPanel opcionesPanel = new JPanel(new GridLayout(2, 2));
        JPanel comidasPanel = new JPanel(new GridLayout(4, 3));
        
        bebidasComboBox1 = new JComboBox<>();
        postreComboBox1 = new JComboBox<>();
        platoFondoComboBox1 = new JComboBox<>();
        entradaComboBox1 = new JComboBox<>();
        
        bebidasComboBox2 = new JComboBox<>();
        postreComboBox2 = new JComboBox<>();
        platoFondoComboBox2 = new JComboBox<>();
        entradaComboBox2 = new JComboBox<>();
        
        bebidasComboBox3 = new JComboBox<>();
        postreComboBox3 = new JComboBox<>();
        platoFondoComboBox3 = new JComboBox<>();
        entradaComboBox3 = new JComboBox<>();
        
        bebidasComboBox4 = new JComboBox<>();
        postreComboBox4 = new JComboBox<>();
        platoFondoComboBox4 = new JComboBox<>();
        entradaComboBox4 = new JComboBox<>();        

        //seleccionar la mesa y la silla
        JLabel mesaLabel = new JLabel("Mesa:");
        mesaComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        //boton para enviar el pedido
        enviarPedidoButton = new JButton("Enviar Pedido");
        
        opcionesPanel.add(mesaLabel);
        opcionesPanel.add(mesaComboBox);
        opcionesPanel.add(personasLabel);
        opcionesPanel.add(personasComboBox);
        
        
        comidasPanel.add(new JLabel("Silla 1"));
        comidasPanel.add(entradaComboBox1);
        comidasPanel.add(platoFondoComboBox1);
        comidasPanel.add(bebidasComboBox1);
        comidasPanel.add(postreComboBox1);
        
        comidasPanel.add(new JLabel("Silla 2"));
        comidasPanel.add(entradaComboBox2);
        comidasPanel.add(platoFondoComboBox2);
        comidasPanel.add(bebidasComboBox2);
        comidasPanel.add(postreComboBox2);
        
        comidasPanel.add(new JLabel("Silla 3"));
        comidasPanel.add(entradaComboBox3);
        comidasPanel.add(platoFondoComboBox3);
        comidasPanel.add(bebidasComboBox3);
        comidasPanel.add(postreComboBox3);
        
        comidasPanel.add(new JLabel("Silla 4"));
        comidasPanel.add(entradaComboBox4);
        comidasPanel.add(platoFondoComboBox4);
        comidasPanel.add(bebidasComboBox4);
        comidasPanel.add(postreComboBox4);
    
        
        add(opcionesPanel);
        add(comidasPanel);
        
        add(new JLabel());
        add(enviarPedidoButton);
        
        // Mostrar la ventana
        setLocationRelativeTo(null);
        setVisible(true);
        //conexión con el servidor
        try {
            clienteSocket = new Socket(configuracion.getProperty("direccion"), configuracion.getIntProperty("puerto"));
            dataOutputStream = new ObjectOutputStream(clienteSocket.getOutputStream());
            dataInputStream = new ObjectInputStream(clienteSocket.getInputStream());
            
            //1- OBTENER MENU DEL STREAM
            menuRecibido = new MenuC();
            System.out.println("Obteniendo menu...");
            menuRecibido = (MenuC) dataInputStream.readObject();
            
            popularComboBoxes(menuRecibido); //popular los combo boxes con el menu recibido del server
            
            //2- AL PRESIONAR EL BOTON "ENVIAR PEDIDO" IR A LA FUNCION enviarPedido()
            enviarPedidoButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
    					enviarPedido();
    				} catch (ClassNotFoundException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
                }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar al servidor.");
        }
    }
    
    private void popularComboBoxes(MenuC menu) {
    	//obteners strings de las comidas
    	String[] entradaOptions = menu.getMenuPrincipal().getEntradas().values()
                .stream()
                .map(comida -> comida.getNombre())
                .toArray(String[]::new);
    	String[] platoFondoOptions = menu.getMenuPrincipal().getPlatoFondo().values()
                .stream()
                .map(comida -> comida.getNombre())
                .toArray(String[]::new);
    	String[] bebidasOptions = menu.getMenuPrincipal().getBebidas().values()
                .stream()
                .map(comida -> comida.getNombre())
                .toArray(String[]::new);
    	String[] postreOptions = menu.getMenuPrincipal().getPostres().values()
                .stream()
                .map(comida -> comida.getNombre())
                .toArray(String[]::new);
    	
    	//popular los combo boxes
    	entradaComboBox1.setModel(new DefaultComboBoxModel<>(entradaOptions));
        platoFondoComboBox1.setModel(new DefaultComboBoxModel<>(platoFondoOptions));
        bebidasComboBox1.setModel(new DefaultComboBoxModel<>(bebidasOptions));
        postreComboBox1.setModel(new DefaultComboBoxModel<>(postreOptions));
        
        entradaComboBox2.setModel(new DefaultComboBoxModel<>(entradaOptions));
        platoFondoComboBox2.setModel(new DefaultComboBoxModel<>(platoFondoOptions));
        bebidasComboBox2.setModel(new DefaultComboBoxModel<>(bebidasOptions));
        postreComboBox2.setModel(new DefaultComboBoxModel<>(postreOptions));
        
        entradaComboBox3.setModel(new DefaultComboBoxModel<>(entradaOptions));
        platoFondoComboBox3.setModel(new DefaultComboBoxModel<>(platoFondoOptions));
        bebidasComboBox3.setModel(new DefaultComboBoxModel<>(bebidasOptions));
        postreComboBox3.setModel(new DefaultComboBoxModel<>(postreOptions));
        
        entradaComboBox4.setModel(new DefaultComboBoxModel<>(entradaOptions));
        platoFondoComboBox4.setModel(new DefaultComboBoxModel<>(platoFondoOptions));
        bebidasComboBox4.setModel(new DefaultComboBoxModel<>(bebidasOptions));
        postreComboBox4.setModel(new DefaultComboBoxModel<>(postreOptions));
    }

    private void enviarPedido() throws ClassNotFoundException {
        try {
            
            int mesa = (int) mesaComboBox.getSelectedItem();        
            PedidoConjunto pedidosTotalesEnMesa = new PedidoConjunto(mesa);          

            //añadir los pedidos al pedido en conjunto
           
            for (int i = 0; i < personasComboBox.getSelectedIndex() + 1; i++) {
            	int silla = i + 1;
            	//crear un objeto Pedido
            	Pedido pedido = new Pedido(silla);
            	PedidoMenu pedidoMenu = new PedidoMenu();
            	switch (i) {
                case 0:
                	pedidoMenu.setEntrada(entradaComboBox1.getSelectedItem().toString());
                    pedidoMenu.setPlatoFondo(platoFondoComboBox1.getSelectedItem().toString());
                    pedidoMenu.setBebidas(bebidasComboBox1.getSelectedItem().toString());
                    pedidoMenu.setPostre(postreComboBox1.getSelectedItem().toString());
                    break;
                case 1:
                	pedidoMenu.setEntrada(entradaComboBox2.getSelectedItem().toString());
                    pedidoMenu.setPlatoFondo(platoFondoComboBox2.getSelectedItem().toString());
                    pedidoMenu.setBebidas(bebidasComboBox2.getSelectedItem().toString());
                    pedidoMenu.setPostre(postreComboBox2.getSelectedItem().toString());
                    break;
                case 2:
                	pedidoMenu.setEntrada(entradaComboBox3.getSelectedItem().toString());
                    pedidoMenu.setPlatoFondo(platoFondoComboBox3.getSelectedItem().toString());
                    pedidoMenu.setBebidas(bebidasComboBox3.getSelectedItem().toString());
                    pedidoMenu.setPostre(postreComboBox3.getSelectedItem().toString());
                    break;
                case 3:
                	pedidoMenu.setEntrada(entradaComboBox4.getSelectedItem().toString());
                    pedidoMenu.setPlatoFondo(platoFondoComboBox4.getSelectedItem().toString());
                    pedidoMenu.setBebidas(bebidasComboBox4.getSelectedItem().toString());
                    pedidoMenu.setPostre(postreComboBox4.getSelectedItem().toString());
                    break;
            	}
                pedido.setPedMenu(pedidoMenu);
                pedidosTotalesEnMesa.addPedido(pedido);
            }
            
            //enviar el pedido al servidor
            dataOutputStream.writeObject(pedidosTotalesEnMesa);
            JOptionPane.showMessageDialog(this, "Pedido enviado con éxito.");

            //limpiar las selecciones
            limpiarSelecciones();
            
            final PedidoConjunto pedidoEnConjuntoRecibido = (PedidoConjunto) dataInputStream.readObject();
            //leer respuesta
            System.out.println("Respuesta del servidor: " + pedidoEnConjuntoRecibido.isServidoEnTotalidad());
            JOptionPane.showMessageDialog(this, "Tu pedido ya esta listo.");
            
            dataInputStream.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al enviar el pedido.");
        }
    }
    
    public void limpiarSelecciones() {
    	entradaComboBox1.setSelectedIndex(0);
        platoFondoComboBox1.setSelectedIndex(0);
        bebidasComboBox1.setSelectedIndex(0);
        postreComboBox1.setSelectedIndex(0);
        
        entradaComboBox2.setSelectedIndex(0);
        platoFondoComboBox2.setSelectedIndex(0);
        bebidasComboBox2.setSelectedIndex(0);
        postreComboBox2.setSelectedIndex(0);
        
        entradaComboBox3.setSelectedIndex(0);
        platoFondoComboBox3.setSelectedIndex(0);
        bebidasComboBox3.setSelectedIndex(0);
        postreComboBox3.setSelectedIndex(0);
        
        entradaComboBox4.setSelectedIndex(0);
        platoFondoComboBox4.setSelectedIndex(0);
        bebidasComboBox4.setSelectedIndex(0);
        postreComboBox4.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
				new ClienteGUI();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
}
