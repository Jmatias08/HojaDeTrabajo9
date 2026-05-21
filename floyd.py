# floyd.py
# Algoritmo de Floyd-Warshall usando NetworkX

import networkx as nx

class Floyd:
    def __init__(self, grafo):
        self.grafo = grafo
        self.pred  = {}
        self.dist  = {}
        self.ejecutar()

    def ejecutar(self):
        self.pred, self.dist = nx.floyd_warshall_predecessor_and_distance(
            self.grafo.G, weight="weight"
        )

    def get_distancia(self, origen, destino):
        try:
            d = self.dist[origen][destino]
            return None if d == float("inf") else int(d)
        except KeyError:
            return None

    def get_ruta(self, origen, destino):
        try:
            nodos = nx.reconstruct_path(origen, destino, self.pred)
            return " -> ".join(nodos)
        except (KeyError, nx.NetworkXError):
            return f"No existe ruta de {origen} a {destino}."

    def get_excentricidad(self, ciudad):
        nodos = self.grafo.listar_ciudades()
        max_dist = 0
        for origen in nodos:
            if origen == ciudad:
                continue
            d = self.dist.get(origen, {}).get(ciudad, float("inf"))
            if d == float("inf"):
                return float("inf")
            if d > max_dist:
                max_dist = d
        return int(max_dist)

    def get_centro(self):
        nodos = self.grafo.listar_ciudades()
        min_ecc = float("inf")
        centro  = None
        for ciudad in nodos:
            ecc = self.get_excentricidad(ciudad)
            if ecc < min_ecc:
                min_ecc = ecc
                centro  = ciudad
        return centro, min_ecc

    def imprimir_apsp(self):
        nodos = self.grafo.listar_ciudades()
        ancho = max((len(n) for n in nodos), default=5) + 2
        col   = 10
        print("\n===== MATRIZ APSP (Caminos mas cortos) =====")
        print(f"{'':>{ancho}}", end="")
        for n in nodos:
            print(f"{n:>{col}}", end="")
        print()
        for origen in nodos:
            print(f"{origen:>{ancho}}", end="")
            for destino in nodos:
                d = self.dist.get(origen, {}).get(destino, float("inf"))
                print(f"{'INF' if d == float('inf') else int(d):>{col}}", end="")
            print()
        # Fila de excentricidades
        print(f"{'Excentricidad':>{ancho}}", end="")
        for ciudad in nodos:
            ecc = self.get_excentricidad(ciudad)
            print(f"{'INF' if ecc == float('inf') else ecc:>{col}}", end="")
        print()
        print("============================================\n")
