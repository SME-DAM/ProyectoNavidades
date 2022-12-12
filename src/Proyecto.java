import java.util.InputMismatchException;
import java.util.Scanner;

import javax.print.attribute.AttributeSetUtilities;

// 11 horas de trabajo

public class Proyecto {
	//variables globales
	private static Scanner sc;

	// inicializa la maquina
	static void inicializarProductos(String[]productos, int[] ventas, double[] precios, int[][][]tienda) {
		// datos del enunciado
		String[][] prods = { { "Lacasitos", "Chicles de fresa", "KitKat", "Palotes" },
				{ "Oreo", "Bolsa Haribo", "Chetoos", "Twix" },
				{ " M&M'S ", " Kinder Bueno ", "Papa Delta", "Chicles de menta" },
				{ "Lacasitos", "Crunch", "Milkybar", "KitKat" } };
		double[][] precs = { { 1.5, 0.8, 1.1, 0.9 }, { 1.8, 1, 1.2, 1 }, { 1.8, 1.3, 1.2, 0.8 },
				{ 1.5, 1.1, 1.1, 1.1 } };
		// reseteo las variables
		for (int i = 0; i < 16; i++) {
			productos[i] = "";
			ventas[i] = 0;
			precios[i] = 0;
		}
		// copio los datos del enunciado en las estructuras de control
		for (int fila = 0; fila < 4; fila++) {
			for (int columna = 0; columna < 4; columna++) {
				boolean colocado = false;
				int posicion = 0;
				String producto = prods[fila][columna].strip();
				while (!colocado) {
					if (productos[posicion].isEmpty()) {
						productos[posicion] = producto;
						precios[posicion] = precs[fila][columna];
					}
					if (productos[posicion].equalsIgnoreCase(producto)) {
						tienda[fila][columna][0] = posicion;
						tienda[fila][columna][1] = 3;
						colocado = true;
					}
					posicion++;
				}
			}
		}
	}
	// ordena alfabeticamente los productos segun el nombre
	// usa el algoritmo de la burbuja, dejando los vacios al final
	static public void ordenaAlfabetico(int[]ventas,String[]productos,double[]precios,int[][][] tienda) {
		boolean desordenado = true;
		while (desordenado) {
			desordenado = false;
			for (int index = 0; index < productos.length - 1; index++) {
				if (productos[index].compareToIgnoreCase(productos[index + 1]) > 0 && !productos[index + 1].isEmpty()) {
					String proc = productos[index];
					productos[index] = productos[index + 1];
					productos[index + 1] = proc;
					double prec = precios[index];
					precios[index] = precios[index + 1];
					precios[index + 1] = prec;
					for (int fila = 0; fila < 4; fila++) {
						for (int columna = 0; columna < 4; columna++) {
							if (tienda[fila][columna][0] == index) {
								tienda[fila][columna][0]++;
							} else if (tienda[fila][columna][0] == index + 1) {
								tienda[fila][columna][0]--;
							}
						}
					}
					desordenado = true;
				}
			}
		}
	}
	// vende el producto y devuelve el valor de la venta
	// si no hay existencias no hace nada y devuelve 0
	static double venderProducto(int[] posicion, int[] ventas, int[][][] tienda, double[] precios) {
		int[] celda = tienda[posicion[0]][posicion[1]];
		if (celda[1] > 0) {
			celda[1]--;
			ventas[celda[0]]++;
			return precios[celda[0]];
		}
		return -1;
	}
	// muestra los productos de cada casilla con su precio
	static void mostarProductos(int[][][] tienda, String[] productos, double[] precios) {
		int filas = tienda[0].length;
		int columnas = tienda.length;
		for (int fila = 0; fila < filas; fila++) {
			for (int columna = 0; columna < columnas; columna++) {
				int[] celda = tienda[fila][columna];
				imprimir(String.format("| %-20s | Posicion: %c%c | Precio: %2.2f |\n", productos[celda[0]], 'A' + fila,
						'A' + columna, precios[celda[0]]));
			}
		}
	}
	// muestra los precios de los productos registrados,
	// sus existencias y sus ventas
	private static void infoProductos(int[][][] tienda, String[] productos, double[] precios, int[] ventas) {
		for (int prod = 0; prod < 16; prod++) {
			if (productos[prod].isEmpty())
				return;
			int unidades = 0;
			for (int fila = 0; fila < tienda[0].length; fila++) {
				for (int columna = 0; columna < tienda.length; columna++) {
					unidades += tienda[fila][columna][0] == prod ? tienda[fila][columna][1] : 0;
				}
			}
			int vendido = ventas[prod];
			imprimir(String.format("| %-20s | Precio: %2.2f | U. disponibles: %3d | Ventas: %3d |\n", productos[prod],
					precios[prod], unidades, vendido));
		}
	}
	// cambia la password de administrador
	private static String actualizarPassword(String password) {
		imprimir("\nIntroduzca nueva contraseña");
		String newPass = leerCadena();
		imprimir("\nRepita nueva contraseña");
		String confPass = leerCadena();
		if (newPass.equals(confPass)) {
			imprimir("\nContraseña actualizada");
			return newPass;
		}
		imprimir("\nLas contraseñas no coinciden");
		return password;
	}
	// pide una posicion al usuario y si es válida la devuelve en un vector
	// en caso contrario devuelve un vector con valores negativos
	private static int[] recogePosicion() {
		imprimir("Introduce la posicion:");
		int[] out = { -1, -1 };
		String entrada = sc.next();
		if (entrada.length() >= 2) {
			int fila = entrada.charAt(0) - 'A';
			int columna = entrada.charAt(1) - 'A';
			if (!(fila < 0 || fila > 3 || columna < 0 || columna > 3)) {
				out[0] = fila;
				out[1] = columna;
				return out;
			}
		}
		imprimir("\nHa indicado una posicion incorrecta");
		return out;
	}
	// recibe la posicion a rellenar y pide la cantidad a reponer,
	// si es posible añadirlo devuelve true, en caso contrario false.
	private static boolean reponerProducto(int[] posicion, int[][][] tienda) {
		boolean actualizado = false;
		imprimir("Introduce la cantidad a reponer:");
		int cantidad = 0;
		try {
			cantidad = leerEntero();
		} catch (InputMismatchException e) {
			leerCadena();
		}
		if (cantidad <= 0)
			return actualizado;
		int existencias = tienda[posicion[0]][posicion[1]][1];
		if ((5 - existencias) >= cantidad) {
			tienda[posicion[0]][posicion[1]][1] += cantidad;
			actualizado = true;
		}
		return actualizado;
	}
//pide el precio para el producto de la posicion pasada como parametro. Si el valor es menor o 
//igual que cero devuelve false, en caso contrario devuelve true y lo actualiza .
	private static double leerPrecio() {
		double precio = -1;
		try {
			precio = leerReal();
		} catch (InputMismatchException e) {
			leerCadena();
		}
		return precio;
	}

	private static void actualizarPrecio(int index, double[] precios) {
		double nuevoPrecio = -1;
		while(nuevoPrecio < 0) {
			imprimir("Introduzca el nuevo precio de venta");
			nuevoPrecio = leerPrecio();
		}
		
	}
//añade un producto en la máquina en la posicion que se pasa como parametro,
//pide la cantidad del nuevo producto y su precio, si existe en otra posicion
//actualiza el precio al nuevo valor.
	private static boolean actualizarProducto(int[] posicion,String[] productos, int[][][] tienda, int[] ventas) {
		imprimir("Introduce el nombre del producto:");
		String producto = leerCadena();
		if (producto.isEmpty())
			return false;
		int index = 0;
		boolean existe = false;
		while (!existe && index < productos.length) {
			if (productos[index].equals(producto)) {
				existe = true;
			} else {
				index++;
			}
		}
		if (existe) {
			tienda[posicion[0]][posicion[1]][0] = index;
		} else {
			index = tienda[posicion[0]][posicion[1]][0];
			int ocurrencias = 0;
			for (int fila = 0; fila < 4; fila++) {
				for (int columna = 0; columna < 4; columna++) {
					ocurrencias += tienda[fila][columna][0] == index ? 1 : 0;
				}
			}
			if (ocurrencias <= 1) {
				productos[index] = producto;
				for (int ind = 0; ind < ventas.length; ind++)
					if (ventas[ind]== index)
						ventas[ind] = 0;
			} else {
				for (int ind = 0; ind < productos.length; ind++)
					if (productos[ind].isEmpty()) {
						productos[ind] = producto;
						for (int venta = 0; venta < ventas.length; venta++)
							if (ventas[venta] == index)
								ventas[venta] = 0;
						tienda[posicion[0]][posicion[1]][0] = ind;
						tienda[posicion[0]][posicion[1]][1] = 0;
						ind = productos.length;
					}
			}
		}
		while (!reponerProducto(posicion,tienda));
		return true;
	}

//muestra los tres productos más vendidos
	private static void muestraTopVentas(int[]ventas,String[]productos,double[]precios) {
		String[] productosOrd = productos.clone();
		int[] ventasOrd = ventas.clone();
		double[] preciosOrd = precios.clone();
		ordenaVentas(productosOrd, ventasOrd, preciosOrd);
		int index = ventas.length;
		int cuenta = 0;
		while (index > 0) {
			index--;
			if (cuenta < 3 || ventasOrd[index + 1] == ventasOrd[index]) {
				if (!productosOrd[index].isEmpty() && ventasOrd[index] > 0) {
					cuenta++;
					imprimir(String.format("| %-20s | Precio: %2.2f |  Ventas: %3d |\n", productosOrd[index], preciosOrd[index], ventasOrd[index]));
				}
			} else {
				return;
			}
		}
		if (cuenta == 0)
			imprimir("\nNo se ha vendido nada aún!\n");
	}

//muestra el producto menos vedido
	private static void muestraMenosVentas(int[]ventas,String[]productos,double[]precios) {
		String[] productosOrd = productos;
		int[] ventasOrd = ventas;
		double[] preciosOrd = precios;
		ordenaVentas(productosOrd, ventasOrd, preciosOrd);
		int index = 0;
		while (index < ventasOrd.length) {
			if (ventasOrd[index] == ventasOrd[0]) {
				if (!productos[index].isEmpty()) {
					imprimir(String.format("| %-20s | Precio: %2.2f |  Ventas: %3d |\n", productosOrd[index], preciosOrd[index], ventasOrd[index]));
				}
			} else {
				return;
			}
			index++;
		}
	}

//ordena la matriz de vendidos de menor a mayor
	private static void ordenaVentas(String[] productos, int[]ventas, double[] precios) {
		boolean working = true;
		while (working) {
			working = false;
			for (int index = 0; index < productos.length - 1; index++) {
				if (ventas[index] > ventas[index + 1] && !productos[index + 1].isEmpty()) {
					int temp = ventas[index];
					ventas[index] = ventas[index + 1];
					ventas[index + 1] = temp;
					String tmp = productos[index];
					productos[index]=productos[index+1];
					productos[index + 1]= tmp;
					working = true;
				}
			}
		}
	}

	// gestiona el menu de administracion
	static boolean menuAdministrador(String password, double cajaTotal,int[][][] tienda, int[]ventas, double[]precios, String[] productos) {
		imprimir("\nIntroduzca la contraseña de administracion\n");
		String pass = leerCadena();
		if (!password.equals(pass)) {
			imprimir("\nLa contraseña es incorrecta\n");
			return false;
		}
		imprimir("\nBienvenid@ al panel de administracion:\n");
		while (true) {
			imprimir("\n*** Opciones administrativas***\n\n1. Cambiar contraseña\n");
			imprimir("2. Reponer productos\n3. Cambiar precio\n4. Cambiar producto\n");
			imprimir("5. Más vendidos\n6. Menos vendidos\n7. Informacion productos\n");
			imprimir("8. Ventas totales\n9. Cerrar sesion\n10. Apagar la máquina\n\n");
			int opcion = 0;
			int[] posicion;
			try {
				opcion = sc.nextInt();
			} catch (InputMismatchException e) {

			}
			switch (opcion) {
			case 1:
				password = actualizarPassword(password);
				break;
			case 2:
				posicion = recogePosicion();
				if (posicion[0] >= 0) {
					if (reponerProducto(posicion, tienda)) {
						imprimir("\nEl producto se ha repuesto correctamente\n");
					} else {
						imprimir("\nError, revise los datos\n");
					}
				}
				break;
			case 3:
				posicion = recogePosicion();
				if (posicion[0] >= 0) {
					int index = tienda[posicion[0]][posicion[1]][0];
					actualizarPrecio(index, precios);
				}
				break;
			case 4:
				posicion = recogePosicion();
				if (posicion[0] >= 0)
					if (actualizarProducto(posicion, productos, tienda, ventas)) {
						imprimir("\nEl producto se a actualizado correctamente\n");
					} else {
						imprimir("\nError, revise los datos\n");
					}
				break;
			case 5:
				muestraTopVentas(ventas,productos,precios);
				break;
			case 6:
				muestraMenosVentas(ventas,productos,precios);
				break;
			case 7:
				infoProductos(tienda, productos, precios, ventas);
				break;
			case 8:
				imprimir(String.format("\nVentas totales hasta este momento: %2.2f€\n\n", cajaTotal));
				break;
			case 9:
				return false;
			case 10:
				return true;
			default:
				imprimir("\nHa indicado una opcion incorrecta\n");
				break;
			}
		}
	}
	// salida
	static void imprimir(String salida){
		System.out.print(salida);
	}
	//entradas
	static String leerCadena() {
		return sc.next();
	}

	static int leerEntero() throws InputMismatchException {
		return sc.nextInt();
	}

	static double leerReal() throws InputMismatchException {
		return sc.nextDouble();
	}

	public static void main(String[] args) {

		String[] productos = new String[16];
		double[] precios = new double[16];
		int[][][] tienda = new int[4][4][2];
		int[] ventas = new int[16];
		double cajaTotal = 0;
		String password = "DAM";
		boolean apagar = false;
		
		inicializarProductos(productos,ventas,precios,tienda);
		ordenaAlfabetico(ventas, productos, precios, tienda);
		sc = new Scanner(System.in);
		while (!apagar) {
			imprimir("\n*** Opciones ***\n\n1. Pedir golosina\n2. Mostrar golosinas\n3. Submenu administrador\n");
			int opcion=0;
			try {
				opcion = leerEntero();
			} catch (InputMismatchException e) {
				leerCadena();
			}
			switch (opcion) {
			case 1:
				int[] posicion = recogePosicion();
				if (posicion[0] >= 0) {
					double venta = venderProducto(posicion, ventas, tienda, precios);
					cajaTotal += venta;
					if (venta > 0) {
						imprimir("\nPuede retirar su producto\n");
					} else {
						imprimir("\nNo hay existencias de ese producto\n");
					}
				}
				break;
			case 2:
				mostarProductos(tienda, productos, precios);
				break;
			case 3:
				menuAdministrador(password, cajaTotal, tienda, ventas, precios,productos);
				break;
			default:
				imprimir("\nHa indicado una opcion incorrecta\n");
				break;
			}
		}
	}
}
