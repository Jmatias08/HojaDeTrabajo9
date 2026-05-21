# lector_grafo.py
# Lee el archivo guategrafo.txt y carga el grafo

def leer_archivo(ruta, grafo):
    arcos = 0
    try:
        with open(ruta, "r", encoding="utf-8") as f:
            for num, linea in enumerate(f, 1):
                linea = linea.strip()
                if not linea:
                    continue
                partes = linea.split()
                if len(partes) < 3:
                    print(f"  [!] Linea {num} ignorada (formato incorrecto): {linea}")
                    continue
                try:
                    km = int(partes[2])
                    if km <= 0:
                        raise ValueError
                    grafo.agregar_arco(partes[0], partes[1], km)
                    arcos += 1
                except ValueError:
                    print(f"  [!] Linea {num} KM invalido: {linea}")
    except FileNotFoundError:
        print(f"  Error: No se encontro el archivo '{ruta}'")
        return -1
    return arcos
