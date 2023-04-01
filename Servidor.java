import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Servidor extends Thread{

    private boolean indicador;

    public Servidor(){
        this.indicador = true;
    }

    @Override
    public void run() {

        while (indicador) {
            final int PUERTO = 5000;
            byte[] buffer = new byte[65536];

            System.out.println("Servidor Disponible");

            try {

                DatagramSocket socket = new DatagramSocket(PUERTO);

                // Recibir el nombre del paquete
                DatagramPacket entradaNombre = new DatagramPacket(buffer, buffer.length);

                socket.receive(entradaNombre);

                // Se crea un archivo de la solicitud en el log
                String nombreArchivo = new String(entradaNombre.getData());
                Date fecha = new Date();
                String logname = fecha.toString() + ".txt";
                File archivo = new File("logs/" + logname);

                // Recibir la informacion del archivo
                DatagramPacket entrada = new DatagramPacket(buffer, buffer.length);
                socket.receive(entrada);
                String mensaje = new String(entrada.getData());

                // Meter nombre de la peticion en el log
                FileWriter escribir = new FileWriter(archivo);
                escribir.write(nombreArchivo+"\n");

                // Se recopila informacion del solicitante
                int puertoPeticion = entrada.getPort();
                InetAddress direccion = entrada.getAddress();
                String p = Integer.toString(puertoPeticion);
                String d = direccion.toString();

                // Se añade información relevante en el Log
                fecha = new Date();

                escribir.write(fecha.toString() + ":" + "Puerto del Solicitante: \n");
                for (int i = 0; i < p.length(); i++) {
                    escribir.write(p.charAt(i));
                }
                escribir.write("\n");

                fecha = new Date();
                escribir.write(fecha.toString() + ":" + "Direccion IP del Solicitante: \n");
                for (int i = 0; i < d.length(); i++) {
                    escribir.write(d.charAt(i));
                }
                escribir.write("\n");

                fecha = new Date();
                escribir.write(fecha.toString() + ":" + "Mensaje del solicitante: \n");
                for (int i = 0; i < mensaje.length(); i++) {
                    escribir.write(mensaje.charAt(i));
                }
                escribir.write("\n");

                // Se planea la respuesta
                String path;

                if (mensaje.equals("100")) {
                    path = "Archivo 100MB.txt";
                } else if (mensaje.equals("250")) {
                    path = "Archivo 250MB.txt";
                } else {
                    path = "Alternativa.txt";
                }

                File r = new File(path);

                String respuesta = r.toString();

                buffer = respuesta.getBytes();

                DatagramPacket salida = new DatagramPacket(buffer, buffer.length, direccion, puertoPeticion);

                // Enviar la respuesta
                fecha = new Date();
                socket.send(salida);

                escribir.write(fecha.toString() + ":" + "Respuesta enviada: \n");
                escribir.write(path);
                escribir.write("\n");

                escribir.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void cerrar(){
        indicador = false;
    }
}