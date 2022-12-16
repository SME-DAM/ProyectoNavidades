import java.util.InputMismatchException;
import java.util.Scanner;

// 17 horas de trabajo

public class Proyecto {
	// variables globales
	private static Scanner sc;

	// inicializa la maquina
	static void inicializarProductos(String[] productos, int[] ventas, double[] precios, int[][] tienda,
			int[][] existencias) {
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
						tienda[fila][columna] = posicion;
						existencias[fila][columna] = 3;
						colocado = true;
					}
					posicion++;
				}
			}
		}
	}

	// ordena alfabeticamente los productos segun el nombre
	// usa el algoritmo de la burbuja, dejando los vacios al final
	static public void ordenaAlfabetico(int[] ventas, String[] productos, double[] precios, int[][] tienda) {
		boolean desordenado = true;
		while (desordenado) {
			desordenado = false;
			for (int index = 0; index < productos.length - 1; index++) {
				if (productos[index].compareToIgnoreCase(productos[index + 1]) > 0 && !productos[index + 1].isEmpty()) {
					int vent = ventas[index];
					ventas[index] = ventas[index + 1];
					ventas[index + 1] = vent;
					String proc = productos[index];
					productos[index] = productos[index + 1];
					productos[index + 1] = proc;
					double prec = precios[index];
					precios[index] = precios[index + 1];
					precios[index + 1] = prec;
					for (int fila = 0; fila < 4; fila++) {
						for (int columna = 0; columna < 4; columna++) {
							if (tienda[fila][columna] == index) {
								tienda[fila][columna]++;
							} else if (tienda[fila][columna] == index + 1) {
								tienda[fila][columna]--;
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
	static double venderProducto(int[] posicion, int[] ventas, int[][] tienda, int[][] existencias, double[] precios) {
		int producto = eligeProducto(posicion, tienda);
		int stock = compruebaExistencias(posicion, existencias);
		if (stock > 0) {
			establecerExistencias(posicion, existencias, stock - 1);
			ventas[producto]++;
			return precios[producto];
		}
		return -1;
	}

	// muestra los productos de cada casilla con su precio
	static void mostarProductos(int[][] tienda, String[] productos, double[] precios) {
		int filas = tienda[0].length;
		int columnas = tienda.length;
		for (int fila = 0; fila < filas; fila++) {
			for (int columna = 0; columna < columnas; columna++) {
				int producto = tienda[fila][columna];
				imprimir(String.format("| %-20s | Posicion: %c%c | Precio: %2.2f |\n", productos[producto], 'A' + fila,
						'A' + columna, precios[producto]));
			}
		}
	}

	// muestra los precios de los productos registrados,
	// sus existencias y sus ventas
	private static void infoProductos(int[][] tienda, int[][] existencias, String[] productos, double[] precios,
			int[] ventas) {
		for (int prod = 0; prod < 16; prod++) {
			if (productos[prod].isEmpty())
				return;
			int unidades = 0;
			for (int fila = 0; fila < tienda[0].length; fila++) {
				for (int columna = 0; columna < tienda.length; columna++) {
					if (tienda[fila][columna] == prod) {
						unidades += existencias[fila][columna];
					}
				}
			}
			imprimir(String.format("| %-20s | Precio: %2.2f | U. disponibles: %3d | Ventas: %3d |\n", productos[prod],
					precios[prod], unidades, ventas[prod]));
		}
	}

	// cambia la password de administrador
	private static void actualizarPassword(String[] password) {
		imprimir("\nIntroduzca nueva contraseña");
		String newPass = leerCadena();
		imprimir("\nRepita nueva contraseña");
		String confPass = leerCadena();
		if (newPass.equals(confPass)) {
			imprimir("\nContraseña actualizada");
			password[0] = newPass;
		} else {
			imprimir("\nLas contraseñas no coinciden");
		}
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
	private static void reponerProducto(int[] posicion, int[][] existencias) {
		imprimir("\nIntroduzca la cantidad a reponer:");
		int cantidad = leerCantidad();
		while (cantidad <= 0) {
			imprimir("\nIntroduzca un entero positivo\n");
		}
		int stock = compruebaExistencias(posicion, existencias);
		if (cantidad + stock <= 5) {
			establecerExistencias(posicion, existencias, stock + cantidad);
			imprimir("\nEl producto se ha repuesto correctamente\n\n");
		} else {
			imprimir("\nSe ha producido un error\n\n");
		}
	}

	// devuelve las existencias de un slot
	private static int compruebaExistencias(int[] posicion, int[][] existencias) {
		return existencias[posicion[0]][posicion[1]];
	}

	// establece las existencias en un slot
	private static void establecerExistencias(int[] posicion, int[][] existencias, int stock) {
		existencias[posicion[0]][posicion[1]] = stock;
	}

	// pide el precio para un producto, devuelve el valor introducido o -1 en caso
	// de error
	private static double leerPrecio() {
		double precio = -1;
		try {
			precio = leerReal();
		} catch (InputMismatchException e) {
			leerCadena();
		}
		return precio;
	}

	// pide el precio en bucle hasta que es >0, despues lo establece en la posicion
	// indicada
	private static void actualizarPrecio(int index, double[] precios) {
		double nuevoPrecio = -1;
		while (nuevoPrecio < 0) {
			imprimir("Introduzca el nuevo precio de venta");
			nuevoPrecio = leerPrecio();
		}
		precios[index] = nuevoPrecio;

	}

	// devuelve el indice que contiene ese nombre de producto, o 1 en caso de no
	// existir
	private static int buscaProducto(String[] productos, String nombre) {
		int index = -1;
		for (int i = 0; i < productos.length; i++) {
			if (productos[i].equals(nombre)) {
				index = i;
			}
		}
		return index;
	}
	//devuelve el numero de slots ocupados por el producto de una posicion
	private static int cuentaOcurrenciasProducto(int[][] tienda, int[] posicion) {
		int producto = eligeProducto(posicion, tienda);
		int ocurrencias = 0;
		for (int fila = 0; fila < tienda[0].length; fila++) {
			for (int columna = 0; columna < tienda.length; columna++) {
				if (tienda[fila][columna] == producto) {
					ocurrencias++;
				}
			}
		}
		return ocurrencias;
	}

//añade un producto en la máquina en la posicion que se pasa como parametro,
//pide la cantidad del nuevo producto y su precio, si existe en otra posicion
//actualiza el precio al nuevo valor.
	private static boolean cambiarProducto(int[] posicion, String[] productos, int[][] tienda, int[] ventas) {
		int index = -1;
		imprimir("Introduce el nombre del producto:");
		String producto = leerCadena();
		if (producto.length() == 0)
			return false;
		int productoAntiguo = cuentaOcurrenciasProducto(tienda, posicion);
		if (productoAntiguo < 2) {
			index = eligeProducto(posicion, tienda);
			ventas[index]=0;
			productos[index] = "";
		}
		index = buscaProducto(productos, producto);
		if (index < 0) {
			index = añadeProductoLista(producto, productos);
		}
		añadeTienda(index, tienda, posicion);
		return true;
	}
//añade un producto a un slot de la tienda
	private static void añadeTienda(int index, int[][] tienda, int[] posicion) {
		tienda[posicion[0]][posicion[1]] = index;
	}
//devuelve el producto que hay en un slot de la tienda
	private static int eligeProducto(int[] posicion, int[][] tienda) {
		return tienda[posicion[0]][posicion[1]];
	}
//devuelve un entero leido del scanner, o -1 en caso de error
	private static int leerCantidad() {
		try {
			return leerEntero();
		} catch (InputMismatchException e) {
			leerCadena();
		}
		return -1;
	}
//añade un producto nuevo en una posicion libre, devuelve el index en el que se añadio, o -1 si no hay sitio
	private static int añadeProductoLista(String producto, String[] productos) {
		for (int i = 0; i < productos.length; i++) {
			if (productos[i].isEmpty()) {
				productos[i] = producto;
				return i;
			}
		}
		return -1;
	}
	// muestra los tres productos más vendidos
	private static void muestraTopVentas(int[] ventas, String[] productos, double[] precios) {
		String[] productosOrd = duplicaArrayString(productos);
		int[] ventasOrd = duplicaArrayInteger(ventas);
		double[] preciosOrd = duplicaArrayDouble(precios);
		ordenaVentas(productosOrd, ventasOrd, preciosOrd);
		int index = ventas.length;
		int cuenta = 0;
		while (index > 0) {
			index--;
			if (cuenta < 3 || ventasOrd[index + 1] == ventasOrd[index]) {
				if (!productosOrd[index].isEmpty() && ventasOrd[index] > 0) {
					cuenta++;
					imprimir(String.format("| %-20s | Precio: %2.2f |  Ventas: %3d |\n", productosOrd[index],
							preciosOrd[index], ventasOrd[index]));
				}
			} else {
				return;
			}
		}
		if (cuenta == 0)
			imprimir("\nNo se ha vendido nada aún!\n");
	}
	//clona un array de Double
	private static double[] duplicaArrayDouble(double[] precios) {
		double[] temp = new double[precios.length];
		for (int i = 0; i < precios.length; i++) {
			temp[i] = precios[i];

		}
		return temp;
	}
	//clona un array de Integer
	private static int[] duplicaArrayInteger(int[] ventas) {
		int[] temp = new int[ventas.length];
		for (int i = 0; i < ventas.length; i++) {
			temp[i] = ventas[i];
		}
		return temp;
	}
	//clona un array de String
	private static String[] duplicaArrayString(String[] productos) {
		String[] temp = new String[productos.length];
		for (int i = 0; i < productos.length; i++) {
			temp[i] = productos[i];
		}
		return temp;
	}

	// muestra el producto menos vendido
	private static void muestraMenosVentas(int[] ventas, String[] productos, double[] precios) {
		String[] productosOrd = productos;
		int[] ventasOrd = ventas;
		double[] preciosOrd = precios;
		ordenaVentas(productosOrd, ventasOrd, preciosOrd);
		int index = 0;
		while (index < ventasOrd.length) {
			if (ventasOrd[index] == ventasOrd[0]) {
				if (!productos[index].isEmpty()) {
					imprimir(String.format("| %-20s | Precio: %2.2f |  Ventas: %3d |\n", productosOrd[index],
							preciosOrd[index], ventasOrd[index]));
				}
			} else {
				return;
			}
			index++;
		}
	}

//ordena la matriz de vendidos de menor a mayor
	private static void ordenaVentas(String[] productos, int[] ventas, double[] precios) {
		boolean working = true;
		while (working) {
			working = false;
			for (int index = 0; index < productos.length - 1; index++) {
				if (ventas[index] > ventas[index + 1] && !productos[index + 1].isEmpty()) {
					int temp = ventas[index];
					ventas[index] = ventas[index + 1];
					ventas[index + 1] = temp;
					String tmp = productos[index];
					productos[index] = productos[index + 1];
					productos[index + 1] = tmp;
					working = true;
				}
			}
		}
	}

	// gestiona el menu de administracion, llama al resto de funciones gestion
	static boolean menuAdministrador(String[] password, double cajaTotal, int[][] tienda, int[] ventas,
			double[] precios, int[][] existencias, String[] productos) {
		imprimir("\nIntroduzca la contraseña de administracion\n");
		String pass = leerCadena();
		if (!password[0].equals(pass)) {
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
				leerCadena();
			}
			switch (opcion) {
			case 1:
				actualizarPassword(password);
				break;
			case 2:
				posicion = recogePosicion();
				if (posicion[0] >= 0)
					reponerProducto(posicion, existencias);
				break;
			case 3:
				posicion = recogePosicion();
				if (posicion[0] >= 0) {
					int index = eligeProducto(posicion, tienda);
					actualizarPrecio(index, precios);
				}
				break;
			case 4:
				posicion = recogePosicion();
				if (posicion[0] >= 0) {
					if (cambiarProducto(posicion, productos, tienda, ventas)) {
						int index = eligeProducto(posicion, tienda);
						actualizarPrecio(index, precios);
						establecerExistencias(posicion, existencias, 0);
						reponerProducto(posicion, existencias);
						imprimir("\nEl producto se ha actualizado correctamente\n");
					} else {
						imprimir("\nError, revise los datos\n");
					}
				}
				break;
			case 5:
				muestraTopVentas(ventas, productos, precios);
				break;
			case 6:
				muestraMenosVentas(ventas, productos, precios);
				break;
			case 7:
				ordenaAlfabetico(ventas, productos, precios, tienda);
				infoProductos(tienda, existencias, productos, precios, ventas);
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

	// wrapper de System.out.print
	static void imprimir(String salida) {
		System.out.print(salida);
	}

	// wrapper de Scanner.next()
	static String leerCadena() {
		return sc.next();
	}
	// wrapper de Scanner.nextInt()
	static int leerEntero() throws InputMismatchException {
		return sc.nextInt();
	}
	// wrapper de Scanner.nextDouble()
	static double leerReal() throws InputMismatchException {
		return sc.nextDouble();
	}

	public static void main(String[] args) {
		String[] productos = new String[16];
		double[] precios = new double[16];
		int[][] tienda = new int[4][4];
		int[][] existencias = new int[4][4];
		int[] ventas = new int[16];
		double cajaTotal = 0;
		String[] password = { "DAM" };
		boolean apagar = false;

		inicializarProductos(productos, ventas, precios, tienda, existencias);
		sc = new Scanner(System.in);
		sc.useDelimiter("\n");
		while (!apagar) {
			imprimir("\n*** Opciones ***\n\n1. Pedir golosina\n2. Mostrar golosinas\n3. Submenu administrador\n");
			int opcion = 0;
			try {
				opcion = leerEntero();
			} catch (InputMismatchException e) {
				leerCadena();
			}
			switch (opcion) {
			case 1:
				int[] posicion = recogePosicion();
				if (posicion[0] >= 0) {
					double venta = venderProducto(posicion, ventas, tienda, existencias, precios);
					if (venta > 0) {
						cajaTotal += venta;
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
				apagar = menuAdministrador(password, cajaTotal, tienda, ventas, precios, existencias, productos);
				break;
			default:
				imprimir("\nHa indicado una opcion incorrecta\n");
				break;
			}
		}
	}
}
