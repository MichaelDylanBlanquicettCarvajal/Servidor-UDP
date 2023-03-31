import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Cliente extends Thread {

    private int id;

    private int solicitudes;

    private String request;

    public Cliente(int pId, int pSo, String pR) {
        id = pId;
        solicitudes = pSo;
        request = pR;
    }

    @Override
    public void run() {

        String logname = "Cliente"+Integer.toString(id)+"-Prueba-"+Integer.toString(solicitudes);
        File archivo = new File("ArchivosRecibidos/" + logname);
        
        try(FileWriter escribir = new FileWriter(archivo)) {
            

            while (solicitudes > 0) {
                final int PUERTO_SERV = 5000;
                byte[] buffer = new byte[65536];

                // Encontrar el servidor
                InetAddress direccionServ = InetAddress.getByName("192.168.1.116");

                DatagramSocket socket = new DatagramSocket();

                // Enviar el mensaje
                buffer = request.getBytes();

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