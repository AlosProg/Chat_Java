/* AEC_1_BASICO DE SISTEMAS DISTRIBUIDOS

Alumno: Antonio Luis Ojeda Soto*/

package aec_1_basico;

import java.io.*;
import java.net.*;

public class ServidorBasico {

    public static void main(String[] args) {
        
        InputStreamReader teclado = new InputStreamReader(System.in);
        BufferedReader mibuffer = new BufferedReader(teclado);
        
        Integer puerto_B;
        
        try {
            
            System.out.println("Bienvenido al Servidor \n" +
            "Introduzca el puerto en el que aceptar conexiones");//pedimos por teclado puerto del ServidorBasico
            String puertoB = mibuffer.readLine();// metemos el puerto en un String
            if (puertoB.length() == 0)// si no se ha introducido nada cogerá...
                puertoB = "3001";// ...puerto ServidorBasico por defecto
            System.out.println("Aceptando peticiones"); 
            
            puerto_B = Integer.parseInt (puertoB);//cambio de tipo
            
            ServerSocket socketAceptador = new ServerSocket (puerto_B); //creamos el socket, será proceso bloqueante, síncrono.
            Socket socketDatosB = socketAceptador.accept(); //aceptando peticiones, se queda a la escuha
            
            // Creamos flujo de escritura que va a circular por el socket
            PrintWriter flujo_salida = new PrintWriter(new OutputStreamWriter(socketDatosB.getOutputStream()));
            // Creamos flujo de lectura que va a circular por el socket
            BufferedReader flujo_entrada = new BufferedReader(new InputStreamReader(socketDatosB.getInputStream()));
            
            String mensaje_entrada = ""; // incializamos variable de mensaje entrante
            
            System.out.println("Peticiones aceptadas");
            
            while(!mensaje_entrada.equals("finalizar")){ // mientras el mensaje recibido no sea "finalizar"...
                mensaje_entrada = flujo_entrada.readLine(); // ...leemos lo que nos manda el Cliente
                System.out.println("CLIENTE: " + mensaje_entrada); // y lo imprimimos
                String mensaje_salida = mibuffer.readLine( ); // lo que hemos metido por consola en el buffer lo metemos en un String
                flujo_salida.println(mensaje_salida);// preparamos el mensaje
                flujo_salida.flush();// y mandamos el mensaje
                
            } //end while
            System.out.println("Finalizando la aceptación de peticiones");
            socketDatosB.close(); // cerramos el socket
            
        } // end try
        
	catch (Exception ex) {
            ex.printStackTrace( );
	} //end catch
    }
}
