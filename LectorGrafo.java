import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorGrafo {

    public static int leerArchivo(String ruta, Grafo grafo) {
        int arcos = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int numLinea = 0;
            while ((linea = br.readLine()) != null) {
                numLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split("\\s+");
                if (partes.length < 3) {
                    System.out.println("  [!] Linea " + numLinea + " ignorada: " + linea);
                    continue;
                }
                try {
                    int km = Integer.parseInt(partes[2]);
                    if (km <= 0) throw new NumberFormatException();
                    grafo.agregarArco(partes[0], partes[1], km);
                    arcos++;
                } catch (NumberFormatException e) {
                    System.out.println("  [!] Linea " + numLinea + " KM invalido: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("  Error al leer archivo: " + e.getMessage());
            return -1;
        }
        return arcos;
    }
}
