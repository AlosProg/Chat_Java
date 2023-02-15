/*
AEC_1_BASICO DE SISTEMAS DISTRIBUIDOS

Alumno: Antonio Luis Ojeda Soto*/

package aec_1_basico;

import java.io.*;
import java.net.*;

public class ClienteBasico {

    public static void main(String[] args) {
        
        InputStreamReader teclado = new InputStreamReader(System.in);
        BufferedReader mibuffer = new BufferedReader(teclado);
        
        InetAddress dirIP_B;
        Integer puerto_B;
        
        try {
            System.out.println("Bienvenido al Cliente \n" +  
            "Introduzca el nombre o IP del Servidor"); // pedimos por teclado dirección IP del servidor
            String direccionB = mibuffer.readLine(); // metemos la IP en un String
            if (direccionB.length() == 0) // si no se ha introducido nada cogerá...
                direccionB = "localhost";  // ...direccion IP Servidor por defecto
            
            System.out.println("Introduzca el puerto de escucha del Servidor");// pedimos por teclado puerto del Servidor
            String puertoB = mibuffer.readLine(); // metemos el puerto en un String
            if (puertoB.length() == 0)// si no introducimos nada cogerá...
                puertoB = "3001";// ...puerto Servidor por defecto
            System.out.println("Comience a chatear.");
            dirIP_B = InetAddress.getByName(direccionB); //cambio de tipo 
            puerto_B = Integer.parseInt (puertoB); // cambio de tipo
         
            Socket misocket = new Socket (dirIP_B, puerto_B); //creamos el socket, será proceso bloqueante, síncrono.
         
            // Creamos flujo de escritura que va a circular por el socket
            PrintWriter flujo_salida = new PrintWriter(new OutputStreamWriter(misocket.getOutputStream()));
            // Creamos flujo de lectura que va a circular por el socket
            BufferedReader flujo_entrada = new BufferedReader(new InputStreamReader(misocket.getInputStream()));
            
            String mensaje_entrada = "";// incializamos variable de mensaje entrante    
            
            while(!mensaje_entrada.equals("finalizar")){ // mientras el mensaje recibido no sea "finalizar"...
                String mensaje_salida = mibuffer.readLine( ); // lo que hemos metido por consola en el buffer lo metemos en un String
                flujo_salida.println(mensaje_salida); // preparamos el mensaje
                flujo_salida.flush();// y lo mandamos
                mensaje_entrada = flujo_entrada.readLine(); // leemos lo que nos manda el Servidor
                System.out.println("SERVIDOR: " + mensaje_entrada);// y lo imprimimos
            }
            misocket.close();
        } // end try
        
	catch (Exception ex) {
            ex.printStackTrace( );
	} //end catch
    }
}
