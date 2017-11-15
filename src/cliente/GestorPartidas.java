package cliente;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class GestorPartidas {

	// URI del recurso que permite acceder al juego
	final private String baseURI = "http://localhost:8080/com.flota.ws/servicios/partidas/";
	Client cliente = null;
	// Para guardar el target que obtendrá con la operación nuevaPartida y que le permitirá jugar la partida creada
	private WebTarget targetPartida = null;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase
	 * Crea el cliente
	 */
	public GestorPartidas()  {
        // POR IMPLEMENTAR
	}

	/**
	 * Crea una nueva partida
	 * @param	numFilas	numero de filas del tablero
	 * @param	numColumnas	numero de columnas del tablero
	 * @param	numBarcos	numero de barcos
	 */
	public void nuevaPartida(int numFilas, int numColumnas, int numBarcos)   {

		Response response = cliente.target(baseURI).path("/8/8/6")
				.request().post(Entity.xml(""));

		if (response.getStatus() != 201) throw new RuntimeException("Fallo al crear partida");
		// Obtiene la informació sobre el URI del nuevo recurso partida de la cabecera 'Location' en la respuesta
		String recursoPartida = response.getLocation().toString();
		this.targetPartida = cliente.target(recursoPartida);
		response.close();

		System.out.println("Instancio una nueva partida con id: " + recursoPartida);
	}

	/**
	 * Crea la partida en juego
	 */
	public void borraPartida()   {		
        // POR IMPLEMENTAR
	}



	/**
	 * Prueba una casilla y devuelve el resultado
	 * @param	fila	fila de la casilla
	 * @param	columna	columna de la casilla
	 * @return			resultado de la prueba: AGUA, TOCADO, ya HUNDIDO, recien HUNDIDO
	 */
	public int pruebaCasilla( int fila, int columna)   {
        // POR IMPLEMENTAR
		return 0; // A MODIFICAR
	}

	/**
	 * Obtiene los datos de un barco.
	 * @param	idBarco	identificador del barco
	 * @return			cadena con informacion sobre el barco "fila#columna#orientacion#tamanyo"
	 */
	public String getBarco( int idBarco)   {
        // POR IMPLEMENTAR
		return null; // A MODIFICAR
	}


	/**
	 * Devuelve la informacion sobre todos los barcos
	 * @return			vector de cadenas con la informacion de cada barco
	 */
	protected String[] getSolucion() {
		String cadena = targetPartida.path("/solucion")
				.request().get(String.class);
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(cadena)));
			return XMLASolucion(doc);
		}
		catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
	
	/**
	 * Procesa un Document XML y lo convierte en la solucion de la partida
	 * @return			vector de cadenas con la informacion de cada barco
	 */
	protected String[] XMLASolucion(Document doc) {
		int numBarcos=0;
		Element root = doc.getDocumentElement();
		if (root.getAttribute("tam") != null && !root.getAttribute("tam").trim().equals(""))
			numBarcos = Integer.valueOf(root.getAttribute("tam"));
		NodeList nodes = root.getChildNodes();
		String[] solucion = new String[numBarcos];
		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
			if (element.getTagName().equals("barco")) {
				solucion[i] = element.getTextContent();
			}
			else System.out.println("[getSolucion: ] Error en el nombre de la etiqueta");
		}
		return solucion;
	}


} // fin clase
