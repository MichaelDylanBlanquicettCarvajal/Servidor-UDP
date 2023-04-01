import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class Cliente extends Thread {

    private int id;

    private int solicitudes;


    public Cliente(int pId, int pS) {
        id = pId;
        solicitudes = pS;
    }

    @Override
    public void run() {

        String nombreSolicitud = "Cliente"+Integer.toString(id)+"-Prueba-"+Integer.toString(solicitudes)+".txt";
        Date fecha = new Date();
        File archivo = new File("logs/" + fecha.toString() + "-log.txt");
        
        try(FileWriter escribir = new FileWriter(archivo)) {
            
            //Se elige el archivo que se desea recibir
            //escribir.write("100");
            //escribir.write("250");
            escribir.write("Arepas");

            while (solicitudes > 0) {
                final int PUERTO_SERV = 5000;
                byte[] buffer = new byte[65536];

                // Encontrar el servidor
                InetAddress direccionServ = InetAddress.getByName("192.168.0.159");

                DatagramSocket socket = new DatagramSocket();

                // Enviar el nombre del archvio
                buffer = nombreSolicitud.getBytes();
                DatagramPacket salidaNombre = new DatagramPacket(buffer, buffer.length, direccionServ, PUERTO_SERV);
                socket.send(salidaNombre);

                // Enviar el mensaje
                buffer = archivo.toString().getBytes();

                DatagramPacket salida = new DatagramPacket(buffer, buffer.length, direccionServ, PUERTO_SERV);
                socket.send(salida);

                // Recibir respuesta del servidor
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
                socket.receive(respuesta);

                String res = new String(respuesta.getData());

                escribir.write("Respuesta del servidor: \n");
                for (int i = 0; i < res.length(); i++) {
                    escribir.write(res.charAt(i));
                }
                escribir.write("\n");
                socket.close();

                solicitudes = solicitudes - 1;
            }

            escribir.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}