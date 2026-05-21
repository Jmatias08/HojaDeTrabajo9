# main.py
# CC2003 - Algoritmos y Estructura de Datos - UVG 2020
# Hoja de Trabajo 10 - Version Python con NetworkX
#
# INSTRUCCIONES:
#   1. Instalar dependencia:  pip install networkx
#   2. Poner guategrafo.txt en la misma carpeta
#   3. Correr:                python main.py

import sys
from grafo        import Grafo
from floyd        import Floyd
from lector_grafo import leer_archivo


def mostrar_ciudades(grafo):
    print("  Ciudades: " + ", ".join(grafo.listar_ciudades()))


def mostrar_centro(floyd):
    centro, ecc = floyd.get_centro()
    print("\n--- Centro del Grafo ---")
    if centro is None:
        print("  No se puede determinar (grafo desconectado o vacio).")
    else:
        print(f"  Ciudad centro: {centro}")
        print(f"  Excentricidad: {ecc} KM")
        print("  (Recomendada para oficinas centrales de logistica Covid-19)")
    print()


def opcion_ruta(grafo, floyd):
    print("\n--- Ruta mas corta ---")
    mostrar_ciudades(grafo)
    origen  = input("Ciudad origen  : ").strip()
    destino = input("Ciudad destino : ").strip()

    if not grafo.existe_ciudad(origen):
        print(f"  [!] '{origen}' no existe en el grafo.\n")
        return
    if not grafo.existe_ciudad(destino):
        print(f"  [!] '{destino}' no existe en el grafo.\n")
        return

    dist = floyd.get_distancia(origen, destino)
    ruta = floyd.get_ruta(origen, destino)
    print(f"\n  Ruta     : {ruta}")
    print(f"  Distancia: {'Sin ruta' if dist is None else str(dist) + ' KM'}")
    print()


def opcion_modificar(grafo, floyd_ref):
    print("\n--- Modificar Grafo ---")
    print("  a) Eliminar arco  (cordon sanitario / derrumbe)")
    print("  b) Agregar arco   (nueva conexion)")
    sub = input("Seleccione [a/b]: ").strip().lower()
    mostrar_ciudades(grafo)

    if sub == "a":
        c1 = input("Ciudad origen  : ").strip()
        c2 = input("Ciudad destino : ").strip()
        if grafo.eliminar_arco(c1, c2):
            print(f"  -> Arco eliminado: {c1} -> {c2}")
        else:
            print("  [!] Arco no encontrado.\n")
            return

    elif sub == "b":
        c1 = input("Ciudad origen  : ").strip()
        c2 = input("Ciudad destino : ").strip()
        try:
            km = int(input("Distancia (KM) : ").strip())
            if km <= 0:
                raise ValueError
            grafo.agregar_arco(c1, c2, km)
            print(f"  -> Arco agregado: {c1} -> {c2} ({km} KM)")
        except ValueError:
            print("  [!] KM invalido.\n")
            return
    else:
        print("  [!] Opcion invalida.\n")
        return

    print("\nRecalculando Floyd...")
    floyd_ref[0] = Floyd(grafo)
    grafo.imprimir_matriz()
    floyd_ref[0].imprimir_apsp()
    mostrar_centro(floyd_ref[0])


def main():
    print("=======================================================")
    print("  Centro de Respuesta al Covid-19 - Rutas Guatemala")
    print("  CC2003 Algoritmos y Estructura de Datos - UVG 2020")
    print("  [Python + NetworkX]")
    print("=======================================================\n")

    archivo = sys.argv[1] if len(sys.argv) > 1 else "guategrafo.txt"
    grafo   = Grafo()

    print(f"Cargando grafo desde: {archivo}")
    arcos = leer_archivo(archivo, grafo)
    if arcos < 0:
        print("No se pudo leer el archivo. Iniciando con grafo vacio.\n")
    else:
        print(f"  -> {arcos} arcos, {grafo.num_ciudades()} ciudades.\n")

    grafo.imprimir_matriz()
    floyd = Floyd(grafo)
    floyd.imprimir_apsp()
    mostrar_centro(floyd)

    # Usamos lista para poder reasignar floyd dentro de opcion_modificar
    floyd_ref = [floyd]

    while True:
        print("=== MENU ===")
        print("  1. Ruta mas corta entre dos ciudades")
        print("  2. Centro del grafo")
        print("  3. Modificar grafo (agregar/eliminar arco)")
        print("  4. Salir")
        op = input("Opcion: ").strip()

        if   op == "1": opcion_ruta(grafo, floyd_ref[0])
        elif op == "2": mostrar_centro(floyd_ref[0])
        elif op == "3": opcion_modificar(grafo, floyd_ref)
        elif op == "4":
            print("\nPrograma finalizado.")
            break
        else:
            print("  [!] Opcion invalida.\n")


if __name__ == "__main__":
    main()
