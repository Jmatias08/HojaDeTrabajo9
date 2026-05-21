public class Floyd {

    private int[][] dist;
    private int[][] next;
    private int     n;
    private Grafo   grafo;

    public Floyd(Grafo grafo) {
        this.grafo = grafo;
        ejecutar();
    }

    public void ejecutar() {
        n = grafo.getNumVertices();
        int[][] C = grafo.getMatriz();

        dist = new int[n][n];
        next = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                dist[i][j] = C[i][j];
                next[i][j] = (i != j && C[i][j] < Grafo.INF) ? j : -1;
            }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (dist[i][k] < Grafo.INF && dist[k][j] < Grafo.INF
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
    }

    public int getDistancia(String origen, String destino) {
        int i = grafo.getIndice(origen);
        int j = grafo.getIndice(destino);
        return (i < 0 || j < 0) ? Grafo.INF : dist[i][j];
    }

    public String getRuta(String origen, String destino) {
        int i = grafo.getIndice(origen);
        int j = grafo.getIndice(destino);
        if (i < 0 || j < 0)           return "Ciudad no encontrada.";
        if (i == j)                    return origen;
        if (dist[i][j] >= Grafo.INF)  return "No existe ruta de " + origen + " a " + destino + ".";

        StringBuilder sb = new StringBuilder(grafo.getCiudad(i));
        int actual = i;
        while (actual != j) {
            actual = next[actual][j];
            if (actual < 0) return "Error al reconstruir ruta.";
            sb.append(" -> ").append(grafo.getCiudad(actual));
        }
        return sb.toString();
    }

    public int getExcentricidad(String ciudad) {
        int col = grafo.getIndice(ciudad);
        if (col < 0) return Grafo.INF;
        int max = 0;
        for (int fila = 0; fila < n; fila++) {
            if (fila == col) continue;
            if (dist[fila][col] >= Grafo.INF) return Grafo.INF;
            if (dist[fila][col] > max) max = dist[fila][col];
        }
        return max;
    }

    public String getCentro() {
        int    minEcc = Grafo.INF;
        String centro = null;
        for (int i = 0; i < n; i++) {
            int ecc = getExcentricidad(grafo.getCiudad(i));
            if (ecc < minEcc) { minEcc = ecc; centro = grafo.getCiudad(i); }
        }
        return centro;
    }

    public void imprimirAPSP() {
        System.out.println("\n===== MATRIZ APSP (Caminos mas cortos) =====");
        System.out.printf("%-20s", "");
        for (int j = 0; j < n; j++)
            System.out.printf("%-14s", cortar(grafo.getCiudad(j), 12));
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%-20s", cortar(grafo.getCiudad(i), 18));
            for (int j = 0; j < n; j++)
                System.out.printf("%-14s", dist[i][j] >= Grafo.INF ? "INF" : dist[i][j]);
            System.out.println();
        }
        System.out.printf("%-20s", "Excentricidad");
        for (int j = 0; j < n; j++) {
            int ecc = getExcentricidad(grafo.getCiudad(j));
            System.out.printf("%-14s", ecc >= Grafo.INF ? "INF" : ecc);
        }
        System.out.println();
        System.out.println("============================================\n");
    }

    public int[][] getDist() { return dist; }
    public int[][] getNext() { return next; }

    private String cortar(String s, int max) {
        return s.length() > max ? s.substring(0, max - 2) + ".." : s;
    }
}
