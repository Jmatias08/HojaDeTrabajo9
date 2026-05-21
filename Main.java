import java.util.Scanner;

public class Main {

    private static Grafo   grafo;
    private static Floyd   floyd;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=======================================================");
        System.out.println("  Centro de Respuesta al Covid-19 - Rutas Guatemala");
        System.out.println("  CC2003 Algoritmos y Estructura de Datos - UVG 2020");
        System.out.println("=======================================================\n");

        grafo = new Grafo();
        String archivo = (args.length > 0) ? args[0] : "guategrafo.txt";

        System.out.println("Cargando grafo desde: " + archivo);
        int arcos = LectorGrafo.leerArchivo(archivo, grafo);
        if (arcos < 0)
            System.out.println("No se pudo leer el archivo. Iniciando con grafo vacio.\n");
        else
            System.out.println("  -> " + arcos + " arcos, " + grafo.getNumVertices() + " ciudades.\n");

        grafo.imprimirMatriz();
        floyd = new Floyd(grafo);
        floyd.imprimirAPSP();
        mostrarCentro();

        boolean activo = true;
        while (activo) {
            mostrarMenu();
            switch (sc.nextLine().trim()) {
                case "1": opcionRuta();      break;
                case "2": mostrarCentro();   break;
                case "3": opcionModificar(); break;
                case "4":
                    activo = false;
                    System.out.println("\nPrograma finalizado.");
                    break;
                default:
                    System.out.println("  [!] Opcion invalida.\n");
            }
        }
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("=== MENU ===");
        System.out.println("  1. Ruta mas corta entre dos ciudades");
        System.out.println("  2. Centro del grafo");
        System.out.println("  3. Modificar grafo (agregar/eliminar arco)");
        System.out.println("  4. Salir");
        System.out.print("Opcion: ");
    }

    private static void mostrarCentro() {
        String centro = floyd.getCentro();
        System.out.println("\n--- Centro del Grafo ---");
        if (centro == null)
            System.out.println("  No se puede determinar (grafo desconectado).");
        else {
            System.out.println("  Ciudad centro: " + centro);
            System.out.println("  Excentricidad: " + floyd.getExcentricidad(centro) + " KM");
        }
        System.out.println();
    }

    private static void opcionRuta() {
        System.out.println("\n--- Ruta mas corta ---");
        mostrarCiudades();
        System.out.print("Ciudad origen  : ");
        String origen  = sc.nextLine().trim();
        System.out.print("Ciudad destino : ");
        String destino = sc.nextLine().trim();

        if (!grafo.existeCiudad(origen))  { System.out.println("  [!] '" + origen  + "' no existe.\n"); return; }
        if (!grafo.existeCiudad(destino)) { System.out.println("  [!] '" + destino + "' no existe.\n"); return; }

        int dist = floyd.getDistancia(origen, destino);
        System.out.println("\n  Ruta     : " + floyd.getRuta(origen, destino));
        System.out.println("  Distancia: " + (dist >= Grafo.INF ? "Sin ruta" : dist + " KM"));
        System.out.println();
    }

    private static void opcionModificar() {
        System.out.println("\n--- Modificar Grafo ---");
        System.out.println("  a) Eliminar arco (cordon sanitario / derrumbe)");
        System.out.println("  b) Agregar arco  (nueva conexion)");
        System.out.print("Seleccione [a/b]: ");
        String sub = sc.nextLine().trim().toLowerCase();
        mostrarCiudades();

        if (sub.equals("a")) {
            System.out.print("Ciudad origen  : ");
            String c1 = sc.nextLine().trim();
            System.out.print("Ciudad destino : ");
            String c2 = sc.nextLine().trim();
            if (grafo.eliminarArco(c1, c2))
                System.out.println("  -> Arco eliminado: " + c1 + " -> " + c2);
            else {
                System.out.println("  [!] Arco no encontrado.\n");
                return;
            }

        } else if (sub.equals("b")) {
            System.out.print("Ciudad origen  : ");
            String c1 = sc.nextLine().trim();
            System.out.print("Ciudad destino : ");
            String c2 = sc.nextLine().trim();
            System.out.print("Distancia (KM) : ");
            try {
                int km = Integer.parseInt(sc.nextLine().trim());
                if (km <= 0) throw new NumberFormatException();
                grafo.agregarArco(c1, c2, km);
                System.out.println("  -> Arco agregado: " + c1 + " -> " + c2 + " (" + km + " KM)");
            } catch (NumberFormatException e) {
                System.out.println("  [!] KM invalido.\n");
                return;
            }

        } else {
            System.out.println("  [!] Opcion invalida.\n");
            return;
        }

        System.out.println("\nRecalculando Floyd...");
        floyd = new Floyd(grafo);
        grafo.imprimirMatriz();
        floyd.imprimirAPSP();
        mostrarCentro();
    }

    private static void mostrarCiudades() {
        System.out.print("  Ciudades: ");
        String[] lista = grafo.listarCiudades();
        for (int i = 0; i < lista.length; i++)
            System.out.print(lista[i] + (i < lista.length - 1 ? ", " : "\n"));
    }
}
