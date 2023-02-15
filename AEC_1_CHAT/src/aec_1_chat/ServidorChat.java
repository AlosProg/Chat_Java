/*
AEC_1_CHAT DE SISTEMAS DISTRIBUIDOS

Aquí conectaremos a varios clientes con un hilo por cliente simulando un chat. Para ello crearemos una clase
nueva, la clase OtroCliente, en el lado servidor y luego instanciarla para que aparezcan varios clientes.
También se añade un nuevo método "enviarATodos" para enviar el mensaje a todos.

Alumno: Antonio Luis Ojeda Soto*/

package aec_1_chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorChat {
    
    // Estructura de datos elegida para ir metiendo los sockets de los clientes que se incorporen
    public static ArrayList <Socket> socketsconectados=new ArrayList<>();
    
    public static void main(String[] args) {
        
        InputStreamReader teclado = new InputStreamReader(System.in);
        BufferedReader mibuffer = new BufferedReader(teclado);
        
        Integer puerto_B;
        
        try {// preparamos el servidor para aceptar conexiones de clientes
            System.out.println("Bienvenido al chat.");
            System.out.println("Introduzca el puerto en el que aceptar conexiones");//pedimos por teclado puerto del ServidorChat
            String puertoB = mibuffer.readLine();// metemos el puerto en un String
            if (puertoB.length() == 0)// si no se ha introducido nada cogerá...
                puertoB = "3001";// ...puerto ServidorConcurrente por defecto
            System.out.println("Aceptando conexiones"); 
            
            puerto_B = Integer.parseInt (puertoB);//cambio de tipo
            
            ServerSocket socketAceptador = new ServerSocket (puerto_B); //creamos el socket, será proceso bloqueante, síncrono.
                    
            while (true){ // vamos aceptando los clientes que se vayan conectando
                    Socket socketDatos = socketAceptador.accept(); //aceptando peticiones, se queda a la escucha
                    System.out.println("Conexión aceptada");
                    
                    socketsconectados.add(socketDatos);// introducimos los sockets de los clientes en la ED.
                
                    OtroCliente nuevocliente=new OtroCliente(socketDatos);// creamos un nuevo cliente
                    Thread hilo = new Thread(new HiloServidor(nuevocliente));// creamos el hilo del nuevo cliente   
                    hilo.start(); // iniciamos el hilo
                //}
                }
        }
        catch (Exception ex) {
            ex.printStackTrace( );
        } //end catch
    }
    
    // método que recoge por parámetro el mensaje de un cliente y lo envía al resto a través del servidor
    static synchronized void enviarATodos(String mensaje){
        
        ArrayList <Socket> socketsactuales=socketsconectados;
        
        try {
            for(int i=0; i<socketsactuales.size(); i++) { // bucle del libro, en donde recorre los socket sconectaodos
            // y se les envía el mensaje a cada uno de ellos
                Socket otrosocket = (Socket)socketsactuales.get(i);
                PrintWriter flujo_salida = new PrintWriter(new OutputStreamWriter(otrosocket.getOutputStream()));
                flujo_salida.println(mensaje);
                flujo_salida.flush();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace( );
        } //end catch
    }
}    


class HiloServidor implements Runnable {// hilo del servidor para conectarse con cada cliente
    
    Socket socketDatos;
    OtroCliente otrocliente;
    
    HiloServidor(OtroCliente micliente){ // constructor para recepcionar el socket del cliente
        
        socketDatos=micliente.misocket;
    }
        
   
    public void run() {
        try {
              
            String mensaje_entrada = "";
            BufferedReader flujo_entrada = new BufferedReader(new InputStreamReader(socketDatos.getInputStream()));
            
            while (true){// mientras haya mensajes...
                mensaje_entrada = flujo_entrada.readLine();// ...los leemos del buffer y los metemos en un String
                System.out.println("Respuesta del cliente: " + mensaje_entrada);// imprimimos el mensaje
                Thread.sleep (3000);
                ServidorChat.enviarATodos(mensaje_entrada); // enviamos el mensaje a todos a través del servidor
            }
        }
        
        catch (Exception ex) {
            ex.printStackTrace( );
	}
    }
}

// creamos esta clase para luego crear instancias de otros clientes
class OtroCliente{
    Socket misocket;
    
    OtroCliente(Socket conectado){
        misocket=conectado;
    }
}
