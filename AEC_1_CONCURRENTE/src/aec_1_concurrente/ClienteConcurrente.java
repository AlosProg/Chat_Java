/*
AEC_1_CONCURRENTE DE SISTEMAS DISTRIBUIDOS

Aquí haremos que cliente y servidor se comuniquen de forma concurrente con operaciones asíncronas.

Alumno: Antonio Luis Ojeda Soto*/

package aec_1_concurrente;

import java.io.*;
import java.net.*;

public class ClienteConcurrente {

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
         
            Socket misocket = new Socket (dirIP_B, puerto_B);
            Thread hilo = new Thread(new HiloCliente(misocket));
            hilo.start();// el hilo escucha las respuestas del servidor y las muestra
         
            while(true){ // mientras el hilo principal atiende al Servidor aquí se envían las peticiones
                String mensaje = mibuffer.readLine();
                // preparamos el flujo de datos sobre el que leer
                PrintWriter flujo_salida = new PrintWriter(new OutputStreamWriter(misocket.getOutputStream()));
                // escribimos una linea, con operacion no bloqueante, asíncrono
                flujo_salida.println(mensaje);// preparamos el mensaje
                flujo_salida.flush();// y lo mandamos
                
            } // end while
        } // end try
            
        catch (Exception ex) {
            ex.printStackTrace( );
	} //end catch
    }
}

class HiloCliente implements Runnable { // creación de hilos para atender mensajes del Servidor

    Socket socketDatos;
    
    HiloCliente(Socket socketConectado){
        socketDatos = socketConectado;
    }

    public void run () {// método donde se atienden los mensajes y se imprimen
        try{
            BufferedReader flujo_entrada = new BufferedReader(new InputStreamReader(socketDatos.getInputStream()));
            while (true){ // mientras haya mensajes...
                String mensaje_entrada = flujo_entrada.readLine();// ...los leemos del buffer y los metemos en un String
                System.out.println("SERVIDOR: " + mensaje_entrada);// imprimimos el mensaje
            }
        }
           
        catch (Exception ex) {
            ex.printStackTrace( );
	} //end catch
    }
}
