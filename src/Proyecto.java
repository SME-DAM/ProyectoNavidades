import java.util.Scanner;

public class Proyecto {
	
	
	private static Scanner sc;

	//inicializa la maquina
	static void inicializarProductos(String[] prod, double[] prec, int[][][] tienda, int[] ventas) {
		String[][] productos = {
				{"Lacasitos", "Chicles de fresa", "KitKat", "Palotes"},
				{"Oreo", "Bolsa Haribo", "Chetoos", "Twix"},
				{" M&M'S ", " Kinder Bueno ", "Papa Delta", "Chicles de menta"},
				{"Lacasitos", "Crunch", "Milkybar", "KitKat"}
				};
		double[][] precios = {
				{1.5, 0.8, 1.1, 0.9},
				{1.8, 1, 1.2, 1},
				{1.8, 1.3, 1.2, 0.8},
				{1.5, 1.1, 1.1, 1.1}
				};
		//vacio las variables
		for (int i =0;i<16;i++) {
			prod[i]="";
			ventas[i]=0;
			prec[i]=0;
		}
		//copio los datos del enunciado
		for (int fila = 0; fila < 4;fila++) {
			for (int columna = 0; columna < 4; columna++) {
				boolean colocado = false;
				int posicion=0;
				String producto = productos[fila][columna].strip();
				while (!colocado) {
					if(prod[posicion].equals("")) {
						prod[posicion] = producto;
						prec[posicion] = precios[fila][columna];
					}
					if (prod[posicion].equalsIgnoreCase(producto)) {
						tienda[fila][columna][0] = posicion;
						tienda[fila][columna][1] = 3;
						colocado = true;
					}
					posicion++;
				}
			}
		}
	}
	//vende el producto y devuelve el valor de la venta
	static double venderProducto(int[][][] tienda, int[] pos, double[] precios, int[] ventas) {
		int[] celda = tienda[pos[0]][pos[1]];
		if(celda[1] > 0) {
			celda[1]--;
			int prod = celda[0];
			ventas[prod]++;
			return precios[prod];
		}
		return 0;
	}
	//muestra los productos de cada casilla
	static void mostarProductos(String[] productos, int[][][]tienda,double[] precios) {
		int filas = tienda[0].length;
		int columnas = tienda.length;
		for (int fila = 0; fila < filas;fila++) {
			for (int columna = 0; columna < columnas; columna++) {
				int[] celda = tienda[fila][columna];
				System.out.printf("| %-20s | Posicion: %c%c | Precio: %2.2f |\n",
						productos[celda[0]],'A'+fila,'A'+columna,precios[celda[0]]);
			}
		}
	}
	//devuelve el texto del menu
	static String mostrarOpcionesMenu() {
		String salida = "*** Opciones ***\n\n";
		salida += "1. Pedir golosina\n";
		salida += "2. Mostrar golosinas\n";
		salida += "3. Submenu administrador\n";
		return salida;
	}
	
	public static void main(String[] args) {
		String[] productos = new String[16];
		double[] precios = new double[16];
		int[][][] tienda = new int[4][4][2];
		int[] ventas = new int[16];
		int cajaTotal = 0;
		String password = "DAM";
		
		inicializarProductos(productos, precios, tienda,ventas);
		sc = new Scanner(System.in);
		
		boolean apagar = false;
		
		while(!apagar) {
			System.out.print(mostrarOpcionesMenu());
			char opcion = sc.next().charAt(0);
			switch (opcion) {
			case '1':
				System.out.println("Introduce la posicion de la golosina:");
				String entrada = sc.next();
				if (entrada.length()==2) {
					int fila = 'D'-entrada.charAt(0);
					int columna = 'D'-entrada.charAt(1);
					if(!(fila<0||fila>3||columna<0||columna>3)) {
						int[] posicion = {fila,columna};
						double venta = venderProducto(tienda, posicion, precios, ventas);
						cajaTotal+=venta;
						if(venta > 0) {
							System.out.println("\nPuede retirar su producto");
						} else {
							System.out.println("\nNo hay existencias de ese producto");
						}
						break;
					}
				}
				System.out.println("Ha indicado una posicion incorrecta");
				break;
			case '2':
				mostarProductos(productos, tienda, precios);
				break;
			case '3':
				
				break;

			default:
				
				break;
			}
		}
	}
}
