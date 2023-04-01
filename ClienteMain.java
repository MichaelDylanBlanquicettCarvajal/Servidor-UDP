import java.util.ArrayList;

public class ClienteMain {
    public static void main(String[] args) {
        

        int id = 0;

        int solicitudes = 1;

        int numUsuarios = 1;
        ArrayList<Cliente> clientes = new ArrayList<>();

        for (int i = 0; i < clientes.size(); i++) {

            Cliente nuevo = new Cliente(id, solicitudes);
            clientes.add(nuevo);
        }

        for (int i = 0; i < clientes.size(); i++) {
            clientes.get(i).start();
        }
        
    }
}
