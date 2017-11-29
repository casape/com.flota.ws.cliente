package cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

public class ClienteFlotaWS {

	// Sustituye esta clase por tu versión de la clase Juego de la práctica 1

	// Modifícala para que instancie un objeto de la clase AuxiliarClienteFlota
	// en el método 'ejecuta'

	// Modifica todas las llamadas al objeto de la clase Partida
	// por llamadas al objeto de la clase AuxiliarClienteFlota.
	// Los métodos a llamar tendrán la misma signatura.

	/**
	 * Implementa el juego 'Hundir la flota' mediante una interfaz gráfica (GUI)
	 */

	/**
	 * Parametros por defecto de una partida
	 */
	public static final int NUMFILAS = 8, NUMCOLUMNAS = 8, NUMBARCOS = 6;

	private GuiTablero guiTablero = null;
	private GestorPartidas partida = null;

	/**
	 * Atributos de la partida guardados en el juego para simplificar su
	 * implementación
	 */
	private int quedan = NUMBARCOS, disparos = 0;

	/**
	 * Programa principal. Crea y lanza un nuevo juego
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		ClienteFlotaWS juego = new ClienteFlotaWS();
		juego.ejecuta();
	} // end main

	/**
	 * Lanza una nueva hebra que crea la primera partida y dibuja la interfaz
	 * grafica: tablero
	 */
	private void ejecuta() {
		partida= new GestorPartidas();
		partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
				guiTablero.dibujaTablero();
			}
		});
	} // end ejecuta

	private void salir() {
		try {
//			partida.fin();
			guiTablero.liberaRecursos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/******************************************************************************************/
	/*********************
	 * * CLASE INTERNA GuiTablero *
	 ***************************************/
	/******************************************************************************************/
	private class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null; // Tablero de juego
		private JLabel estado = null; // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas
											// de la partida

		/**
		 * Constructor de una tablero dadas sus dimensiones
		 */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas = numFilas;
			this.numColumnas = numColumnas;
			frame = new JFrame("Flotar la hundida");
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					salir();
				}
			});
		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		public void dibujaTablero() {
			anyadeMenu();
			anyadeGrid(numFilas, numColumnas);
			anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
			frame.setSize(300, 300);
			frame.setVisible(true);
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {
			// Listener del menu
			MenuListener mlist = new MenuListener();

			// barra del menu
			JMenuBar menuBar = new JMenuBar();
			// añadir barra del menu al frame
			frame.getContentPane().add(menuBar, BorderLayout.NORTH);

			// menu
			JMenu opcionesPartidaMenu = new JMenu("Opciones");
			// añadir menu a la barra
			menuBar.add(opcionesPartidaMenu);

			// elementos del menuOpciones
			JMenuItem mostrarSolucion = new JMenuItem("Mostrar Solucion");
			JMenuItem nuevaPartida = new JMenuItem("Nueva Partida");
			JMenuItem salirJuego = new JMenuItem("Salir");
			// añadir elementos al menu Opciones
			opcionesPartidaMenu.add(mostrarSolucion);
			opcionesPartidaMenu.add(nuevaPartida);
			opcionesPartidaMenu.add(salirJuego);
			// añadir listeners a los elementos del menu opciones
			mostrarSolucion.addActionListener(mlist);
			nuevaPartida.addActionListener(mlist);
			salirJuego.addActionListener(mlist);
		} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas. Cada
		 * casilla sera un boton con su correspondiente escuchador
		 *
		 * @param nf
		 *            numero de filas
		 * @param nc
		 *            numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) { // COMENTAR
			// JPANEL
			JPanel centro = new JPanel();
			// crear layout
			GridLayout centroLay = new GridLayout(nf + 1, nc + 2);
			// asignar layout al jpanelCentro
			centro.setLayout(centroLay);

			// asignar tamaño
			buttons = new JButton[nf][nc];

			// crear button listener
			ButtonListener blist = new ButtonListener();

			// añadir botones, letras, numeros al jpanel
			centro.add(new JLabel(" "));

			for (int i = 1; i <= nc; i++) {
				centro.add(new JLabel(Integer.toString(i), SwingConstants.CENTER));
			}

			centro.add(new JLabel(" "));

			char letra = 'A';
			for (int i = 0; i < nf; i++) {
				centro.add(new JLabel(String.valueOf((letra)), SwingConstants.CENTER));

				for (int n = 0; n < nc; n++) {
					buttons[i][n] = new JButton();
					buttons[i][n].addActionListener(blist);
					buttons[i][n].putClientProperty("fila", i);
					buttons[i][n].putClientProperty("columna", n);
					centro.add(buttons[i][n]);
				}
				centro.add(new JLabel(String.valueOf((letra)), SwingConstants.CENTER));
				letra++;
			}

			frame.getContentPane().add(centro, BorderLayout.CENTER);

		}

		/**
		 * Anyade el panel de estado al tablero
		 *
		 * @param cadena
		 *            cadena inicial del panel de estado
		 */
		private void anyadePanelEstado(String cadena) {
			JPanel panelEstado = new JPanel();
			estado = new JLabel(cadena);
			panelEstado.add(estado);
			// El panel de estado queda en la posición SOUTH del frame
			frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 *
		 * @param cadenaEstado
		 *            nuevo estado
		 */
		public void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 */
		public void muestraSolucion() {
			// Colorea casillas a mar y despues comprueba el id de cada casilla
			// para pintarlo
			String[] solucion = null;
			solucion = partida.getSolucion();

			for (int i = 0; i < numFilas; i++)
				for (int j = 0; j < numColumnas; j++)
					guiTablero.pintaBoton(buttons[i][j], Color.CYAN);

			for (String barco : solucion)
				pintaBarcoHundido(barco, Color.MAGENTA);

		} // end muestraSolucion

		/**
		 * Pinta un barco como hundido en el tablero
		 *
		 * @param cadenaBarco
		 *            cadena con los datos del barco codifificados como
		 *            "filaInicial#columnaInicial#orientacion#tamanyo"
		 * @param color
		 *            color a usar
		 */
		public void pintaBarcoHundido(String cadenaBarco, Color color) {
			// Una vez hundido se encarga de pintarlo segun orientacion
			String[] partes = cadenaBarco.split("#");

			int fInicial = Integer.parseInt(partes[0]);
			int cInicial = Integer.parseInt(partes[1]);
			String orientacion = partes[2];
			int tamayo = Integer.parseInt(partes[3]);

			if (orientacion.equals("V")) {
				for (int i = 0; i < tamayo; i++) {
					pintaBoton(buttons[fInicial + i][cInicial], color);
				}
			} else {
				for (int i = 0; i < tamayo; i++) {
					pintaBoton(buttons[fInicial][cInicial + i], color);
				}
			}
		}

		// end pintaBarcoHundido

		/**
		 * Pinta un botón de un color dado
		 *
		 * @param b
		 *            boton a pintar
		 * @param color
		 *            color a usar
		 */
		public void pintaBoton(JButton b, Color color) {
			b.setBackground(color);
			// El siguiente código solo es necesario en Mac OS X
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pintándolas del gris por defecto
		 */
		public void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					buttons[i][j].setBackground(null);
					buttons[i][j].setOpaque(true);
					buttons[i][j].setBorderPainted(true);
				}
			}
		} // end limpiaTablero

		/**
		 * Destruye y libera la memoria de todos los componentes del frame
		 */
		public void liberaRecursos() {
			frame.dispose();
		} // end liberaRecursos

	} // end class GuiTablero

	/******************************************************************************************/
	/*********************
	 * * CLASE INTERNA MenuListener *
	 ***************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Segun elemento menu realiza accion
			JMenuItem elem = (JMenuItem) e.getSource();
			String texto = elem.getText();
			if (texto.equals("Mostrar Solucion")) {
				guiTablero.muestraSolucion();
			} else if (texto.equals("Nueva Partida")) {
				partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				// Resetea el juego a partir de aqui
				guiTablero.limpiaTablero();
				quedan = NUMBARCOS;
				disparos = 0;
				guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
			} else if (texto.equals("Salir")) {
				salir();
			}

		} // end actionPerformed

	} // end class MenuListener

	/******************************************************************************************/
	/*********************
	 * * CLASE INTERNA ButtonListener *
	 *************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero Para poder
	 * identificar el boton que ha generado el evento se pueden usar las
	 * propiedades de los componentes, apoyandose en los metodos
	 * putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {
		// Segun casilla la prueba si no ha sido ya tocada

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton boton = new JButton();
			Color colorDefault = boton.getBackground();
			boton = (JButton) e.getSource();

			if (quedan > 0 && boton.getBackground().equals(colorDefault)) {
				int fila = (int) boton.getClientProperty("fila");
				int columna = (int) boton.getClientProperty("columna");
				int casilla = -5;
				casilla = partida.pruebaCasilla(fila, columna);
				if (casilla == -1)
					guiTablero.pintaBoton(boton, Color.CYAN);
				else if (casilla == -2)
					guiTablero.pintaBoton(boton, Color.ORANGE);
				else {
					guiTablero.pintaBarcoHundido(partida.getBarco(casilla), Color.RED);
					quedan--;
				}
				disparos++;

				if (quedan == 0) {
					guiTablero.cambiaEstado("Game Over con disparos: " + disparos);

				} else {
					guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
				}
			}
			// end actionPerformed

		} // end class ButtonListener
	}
}// end class Juego
