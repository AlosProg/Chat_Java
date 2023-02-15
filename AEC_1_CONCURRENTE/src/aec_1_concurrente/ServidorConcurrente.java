/*
AEC_1_CONCURRENTE DE SISTEMAS DISTRIBUIDOS

Aquí haremos que cliente y servidor se comuniquen de forma concurrente con operaciones asíncronas.

Alumno: Antonio Luis Ojeda Soto*/

package aec_1_concurrente;

import java.io.*;
import java.net.*;

public class ServidorConcurrente {

    public static void main(String[] args) {
        
        InputStreamReader teclado = new InputStreamReader(System.in);
        BufferedReader mibuffer = new BufferedReader(teclado);
        
        Integer puerto_B;
        try {
            System.out.println("Bienvenido al Servidor \n" +
            "Introduzca el puerto en el que aceptar conexiones");//pedimos por teclado puerto del ServidorBasico
            String puertoB = mibuffer.readLine();// metemos el puerto en un String
            if (puertoB.length() == 0)// si no se ha introducido nada cogerá...
                puertoB = "3001";// ...puerto ServidorConcurrente por defecto
            System.out.println("Aceptando peticiones"); 
            
            puerto_B = Integer.parseInt (puertoB);//cambio de tipo
            
            ServerSocket socketAceptador = new ServerSocket (puerto_B); //creamos el socket, será proceso bloqueante, síncrono.
            Socket socketDatos = socketAceptador.accept(); //aceptando peticiones, se queda a la escucha
            System.out.println("Peticiones aceptadas");
            Thread hilo = new Thread(new HiloServidor(socketDatos));
            hilo.start(); // el hilo escucha las respuestas del cliente y las muestra
            
            while(true){ // mientras se atienden peticiones del cliente, aquí se envían los mensajes de respuesta
                String mensaje_salida = mibuffer.readLine();
                PrintWriter flujo_salida = new PrintWriter(new OutputStreamWriter(socketDatos.getOutputStream()));
                Thread.sleep (3000); // comprobamos que el hilo del cliente evita el bloqueo en la espera
                flujo_salida.println(mensaje_salida);
                flujo_salida.flush();
            //fin del bucle y volvemos a aceptar dar servicio a otros clientes o al mismo de forma concurrente
            } //end while
        } // end try
	
        catch (Exception ex) {
            ex.printStackTrace( );
	} //end catch
    }
}

class HiloServidor implements Runnable {// creación de hilos para atender mensajes del Cliente

    Socket socketDatos;
    
    HiloServidor(Socket socketConectado){
        socketDatos = socketConectado;
        
    }
          
    public void run (){// método donde se atienden los mensajes y se imprimen
        try {
              
            String mensaje_entrada = "";
            BufferedReader flujo_entrada = new BufferedReader(new InputStreamReader(socketDatos.getInputStream()));
            
            while (true){// mientras haya mensajes...
                mensaje_entrada = flujo_entrada.readLine();// ...los leemos del buffer y los metemos en un String
                System.out.println("CLIENTE: " + mensaje_entrada);// imprimimos el mensaje
            }
        }
        
        catch (Exception ex) {
            ex.printStackTrace( );
	} //end catch
    }
}
