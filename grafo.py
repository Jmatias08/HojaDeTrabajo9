# grafo.py
# Wrapper sobre NetworkX para representar el grafo dirigido de Guatemala

import networkx as nx

class Grafo:
    def __init__(self):
        self.G = nx.DiGraph()

    def agregar_nodo(self, ciudad):
        if ciudad not in self.G:
            self.G.add_node(ciudad)
            return True
        return False

    def agregar_arco(self, c1, c2, km):
        self.G.add_edge(c1, c2, weight=km)

    def eliminar_arco(self, c1, c2):
        if self.G.has_edge(c1, c2):
            self.G.remove_edge(c1, c2)
            return True
        return False

    def existe_arco(self, c1, c2):
        return self.G.has_edge(c1, c2)

    def existe_ciudad(self, ciudad):
        return ciudad in self.G

    def listar_ciudades(self):
        return list(self.G.nodes())

    def num_ciudades(self):
        return self.G.number_of_nodes()

    def imprimir_matriz(self):
        nodos = list(self.G.nodes())
        ancho = max((len(n) for n in nodos), default=5) + 2
        col   = 10
        print("\n===== MATRIZ DE ADYACENCIA =====")
        print(f"{'':>{ancho}}", end="")
        for n in nodos:
            print(f"{n:>{col}}", end="")
        print()
        for origen in nodos:
            print(f"{origen:>{ancho}}", end="")
            for destino in nodos:
                if origen == destino:
                    print(f"{'0':>{col}}", end="")
                elif self.G.has_edge(origen, destino):
                    print(f"{self.G[origen][destino]['weight']:>{col}}", end="")
                else:
                    print(f"{'INF':>{col}}", end="")
            print()
        print("================================\n")
