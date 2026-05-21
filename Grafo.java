public class Grafo {

    public static final int INF = 999999;
    public static final int MAX = 100;

    private String[] ciudades;
    private int[][]  matriz;
    private int      numVertices;

    public Grafo() {
        ciudades    = new String[MAX];
        matriz      = new int[MAX][MAX];
        numVertices = 0;
    }

    public boolean agregarNodo(String ciudad) {
        if (buscarIndice(ciudad) >= 0) return false;
        for (int i = 0; i <= numVertices; i++) {
            matriz[numVertices][i] = INF;
            matriz[i][numVertices] = INF;
        }
        matriz[numVertices][numVertices] = 0;
        ciudades[numVertices] = ciudad;
        numVertices++;
        return true;
    }

    public boolean agregarArco(String c1, String c2, int km) {
        agregarNodo(c1);
        agregarNodo(c2);
        matriz[buscarIndice(c1)][buscarIndice(c2)] = km;
        return true;
    }

    public boolean eliminarArco(String c1, String c2) {
        int i = buscarIndice(c1);
        int j = buscarIndice(c2);
        if (i < 0 || j < 0 || matriz[i][j] == INF) return false;
        matriz[i][j] = INF;
        return true;
    }

    public boolean existeArco(String c1, String c2) {
        int i = buscarIndice(c1);
        int j = buscarIndice(c2);
        return i >= 0 && j >= 0 && matriz[i][j] != INF;
    }

    public int getNumVertices()       { return numVertices; }
    public String getCiudad(int i)    { return ciudades[i]; }
    public boolean existeCiudad(String c) { return buscarIndice(c) >= 0; }

    public int getIndice(String ciudad) { return buscarIndice(ciudad); }

    public int[][] getMatriz() {
        int[][] copia = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                copia[i][j] = matriz[i][j];
        return copia;
    }

    public String[] listarCiudades() {
        String[] lista = new String[numVertices];
        for (int i = 0; i < numVertices; i++) lista[i] = ciudades[i];
        return lista;
    }

    public void imprimirMatriz() {
        System.out.println("\n===== MATRIZ DE ADYACENCIA =====");
        System.out.printf("%-20s", "");
        for (int j = 0; j < numVertices; j++)
            System.out.printf("%-14s", cortar(ciudades[j], 12));
        System.out.println();
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-20s", cortar(ciudades[i], 18));
            for (int j = 0; j < numVertices; j++)
                System.out.printf("%-14s", matriz[i][j] == INF ? "INF" : matriz[i][j]);
            System.out.println();
        }
        System.out.println("================================\n");
    }

    private int buscarIndice(String ciudad) {
        for (int i = 0; i < numVertices; i++)
            if (ciudades[i].equals(ciudad)) return i;
        return -1;
    }

    private String cortar(String s, int max) {
        return s.length() > max ? s.substring(0, max - 2) + ".." : s;
    }
}
