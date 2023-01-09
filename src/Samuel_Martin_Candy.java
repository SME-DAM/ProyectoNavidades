import java.util.InputMismatchException;
import java.util.Scanner;

// 22 horas de trabajo

public class Samuel_Martin_Candy {

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

	// descuenta el producto y devuelve el valor de la venta
	// si no hay existencias no hace nada y devuelve -1
	static double venderProducto(int[] posicion, int[] ventas, int[][] tienda, int[][] existencias, double[] precios) {
		int producto = eligeProducto(posicion, tienda);
		if(!actualizarExistencias(posicion, existencias, -1)) {
			return -1;
		}
		ventas[producto]++;
		return precios[producto];
	}

	// muestra los productos de cada casilla con su precio
	static String mostarProductos(int[][] tienda, String[] productos, double[] precios) {
		String out = "";
		int filas = tienda[0].length;
		int columnas = tienda.length;
		for (int fila = 0; fila < filas; fila++) {
			for (int columna = 0; columna < columnas; columna++) {
				int producto = tienda[fila][columna];
				out += String.format("| %-20s | Posicion: %c%c | Precio: %.2f |\n", productos[producto], 'A' + fila,
						'A' + columna, precios[producto]);
			}
		}
		return out;
	}

	// muestra los precios de los productos registrados,
	// sus existencias y sus ventas
	private static String infoProductos(int[][] tienda, int[][] existencias, String[] productos, double[] precios,
			int[] ventas) {
		String out = "";
		for (int prod = 0; prod < 16; prod++) {
			if (productos[prod].isEmpty())
				return out;
			int unidades = 0;
			for (int fila = 0; fila < tienda[0].length; fila++) {
				for (int columna = 0; columna < tienda.length; columna++) {
					if (tienda[fila][columna] == prod) {
						unidades += existencias[fila][columna];
					}
				}
			}
			out += String.format("| %-20s | Precio: %.2f | U. disponibles: %3d | Ventas: %3d |\n", productos[prod],
					precios[prod], unidades, ventas[prod]);
		}
		return out;
	}

	// pide una posicion al usuario y si es válida la devuelve en un vector
	// en caso contrario devuelve un vector con valores negativos
	private static boolean recogePosicion(String entrada, int[] out) {
		if (entrada.length() != 2) {
			return false;
		}
		int fila = entrada.charAt(0) - 'A';
		int columna = entrada.charAt(1) - 'A';
		if ((fila < 0 || fila > 3 || columna < 0 || columna > 3)) {
			return false;
		}
		out[0] = fila;
		out[1] = columna;
		return true;
	}

	//verifica los datos de entrada y trata de actualizar la cantidad
	//devuelve el texto a mostrar al usuario. 
	private static String reponerProducto(int[] posicion, int[][] existencias, int cantidad) {
		if (cantidad < 0) {
			return "\nNo puedes reponer una cantidad negativa\n";
		}
		if (cantidad == 0) {
			return "\n¿Qué sentido tiene reponer 0 unidades?\n";
		}
		if (!actualizarExistencias(posicion, existencias, cantidad)) {
			return "\nNo caben tantas unidades\n";
		}
		return "\nEl producto se ha repuesto correctamente\n\n";
	}

	// devuelve las existencias de un slot
	private static int compruebaExistencias(int[] posicion, int[][] existencias) {
		return existencias[posicion[0]][posicion[1]];
	}

	private static void reseteaExistencias(int[] posicion, int[][] existencias) {
		existencias[posicion[0]][posicion[1]]=0;
	}
	
	// establece las existencias en un slot
	private static boolean actualizarExistencias(int[] posicion, int[][] existencias, int cantidad) {
		int stock = compruebaExistencias(posicion, existencias);
		if (cantidad + stock > 5 || cantidad + stock <= 0) {
			return false;
		}
		existencias[posicion[0]][posicion[1]] += cantidad;
		return true;
	}

	// pide el precio en bucle hasta que es >0, despues lo establece en la posicion
	// indicada
	private static String actualizarPrecio(int index, double[] precios, Double precio) {
		if (precio <= 0)
			return "\nNo soy una ONG, tengo que ganar dinero!\n";
		precios[index] = precio;
		return String.format("\nPrecio actualizado a %.2f€\n", precio);
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

	// devuelve el numero de slots ocupados por el producto de una posicion
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
//si existe en otra posicion actualiza el precio al nuevo valor.
	private static void cambiarProducto(int[] posicion, String[] productos, int[][] tienda, int[] ventas,
			String producto) {
		int productoAntiguo = cuentaOcurrenciasProducto(tienda, posicion);
		int index = -1;
		if (productoAntiguo < 2) {
			index = eligeProducto(posicion, tienda);
			ventas[index] = 0;
			productos[index] = "";
		}
		index = buscaProducto(productos, producto);
		if (index < 0) {
			index = añadeProductoLista(producto, productos);
		}
		añadeTienda(index, tienda, posicion);
	}

//añade un producto a un slot de la tienda
	private static void añadeTienda(int index, int[][] tienda, int[] posicion) {
		tienda[posicion[0]][posicion[1]] = index;
	}

//devuelve el producto que hay en un slot de la tienda
	private static int eligeProducto(int[] posicion, int[][] tienda) {
		return tienda[posicion[0]][posicion[1]];
	}

//añade un producto nuevo en una posicion libre, devuelve el index en el que se añadió, o -1 si no hay sitio
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
	private static String muestraTopVentas(int[] ventas, String[] productos, double[] precios) {
		String[] productosOrd = duplicaArrayString(productos);
		int[] ventasOrd = duplicaArrayInteger(ventas);
		double[] preciosOrd = duplicaArrayDouble(precios);
		ordenaVentas(productosOrd, ventasOrd, preciosOrd);
		int index = ventas.length;
		int cuenta = 0;
		String salida = "";
		while (index > 0) {
			index--;
			if (cuenta < 3 || ventasOrd[index + 1] == ventasOrd[index]) {
				if (!productosOrd[index].isEmpty() && ventasOrd[index] > 0) {
					cuenta++;
					salida += (String.format("| %-20s | Precio: %2.2f |  Ventas: %3d |\n", productosOrd[index],
							preciosOrd[index], ventasOrd[index]));
				}
			} else {
				return salida;
			}
		}
		if (cuenta == 0)
			salida = "\nNo se ha vendido nada aún!\n";
		return salida;
	}

	// clona un array de Double
	private static double[] duplicaArrayDouble(double[] precios) {
		double[] temp = new double[precios.length];
		for (int i = 0; i < precios.length; i++) {
			temp[i] = precios[i];

		}
		return temp;
	}

	// clona un array de Integer
	private static int[] duplicaArrayInteger(int[] ventas) {
		int[] temp = new int[ventas.length];
		for (int i = 0; i < ventas.length; i++) {
			temp[i] = ventas[i];
		}
		return temp;
	}

	// clona un array de String
	private static String[] duplicaArrayString(String[] productos) {
		String[] temp = new String[productos.length];
		for (int i = 0; i < productos.length; i++) {
			temp[i] = productos[i];
		}
		return temp;
	}

	// muestra el producto menos vendido
	private static String muestraMenosVentas(int[] ventas, String[] productos, double[] precios) {
		String[] productosOrd = productos;
		int[] ventasOrd = ventas;
		double[] preciosOrd = precios;
		ordenaVentas(productosOrd, ventasOrd, preciosOrd);
		int index = 0;
		String salida = "";
		while (index < ventasOrd.length) {
			if (ventasOrd[index] == ventasOrd[0]) {
				if (!productos[index].isEmpty()) {
					salida += (String.format("| %-20s | Precio: %2.2f |  Ventas: %3d |\n", productosOrd[index],
							preciosOrd[index], ventasOrd[index]));
				}
			} else {
				return salida;
			}
			index++;
		}
		return salida;
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

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String[] productos = new String[16];
		double[] precios = new double[16];
		int[][] tienda = new int[4][4];
		int[][] existencias = new int[4][4];
		int[] ventas = new int[16];
		double cajaTotal = 0;
		String password = "DAM";
		boolean apagar = false;
		int[] posicion = new int[2];

		inicializarProductos(productos, ventas, precios, tienda, existencias);
		sc.useDelimiter("\n");
		while (!apagar) {
			System.out
					.print("\n*** Opciones ***\n\n1. Pedir golosina\n2. Mostrar golosinas\n3. Submenu administrador\n");
			int opcion = 0;
			try {
				opcion = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.next();
			}
			switch (opcion) {
			case 1: // pedir producto
				System.out.print("Introduce la posicion:");
				if (!recogePosicion(sc.next(), posicion)) {
					System.out.print("\nPosición invalida\n");
					continue;
				}
				if (compruebaExistencias(posicion, existencias) <= 0) {
					System.out.print("\nNo hay existencias de ese producto\n");
					continue;
				}
				double valorVenta = venderProducto(posicion, ventas, tienda, existencias, precios);
				cajaTotal += valorVenta;
				System.out.print("\nPuede retirar su producto\n");
				break;
			case 2: // mostrar producto
				System.out.print(mostarProductos(tienda, productos, precios));
				break;
			case 3: // menu administracion
				System.out.print("\nIntroduzca la contraseña de administracion\n");
				String pass = sc.next();
				if (!password.equals(pass)) {
					System.out.print("\nLa contraseña es incorrecta\n");
				} else {
				System.out.print("\nBienvenid@ al panel de administracion:\n");
				boolean admin = true;
				while (admin) {
					System.out.print("\n*** Opciones administrativas***\n\n1. Cambiar contraseña\n");
					System.out.print("2. Reponer producto\n3. Cambiar precio\n4. Cambiar producto\n");
					System.out.print("5. Más vendidos\n6. Menos vendidos\n7. Informacion productos\n");
					System.out.print("8. Ventas totales\n9. Cerrar sesion\n10. Apagar la máquina\n\n");
					opcion = 0;
					try {
						opcion = sc.nextInt();
					} catch (InputMismatchException e) {
						sc.next();
					}
					switch (opcion) {
					case 1: // cambiar contraseña
						System.out.print("\nIntroduzca nueva contraseña\n");
						String newPass = sc.next();
						System.out.print("\nRepita nueva contraseña\n");
						String confPass = sc.next();
						if (newPass.equals(confPass)) {
							System.out.print("\nContraseña actualizada\n");
							password = newPass;
						} else {
							System.out.print("\nLas contraseñas no coinciden\n");
						}
						break;
					case 2: // reponer producto
						System.out.print("Introduce la posicion:");
						if (recogePosicion(sc.next(), posicion)) {
							System.out.print("\nIntroduzca la cantidad a reponer\n");
							System.out.printf("Actualmente queda(n) %d\n", compruebaExistencias(posicion, existencias));
							try {
								System.out.print(reponerProducto(posicion, existencias, sc.nextInt()));
							} catch (InputMismatchException e) {
								System.out.print("\nRevise los datos\n");
								sc.next();
							}
						} else {
							System.out.print("\nPosición invalida\n");
						}
						break;
					case 3: // cambiar precio
						System.out.print("Introduce la posicion:");
						if (recogePosicion(sc.next(), posicion)) {
							int index = eligeProducto(posicion, tienda);
							try {
								System.out.print("\nIntroduzca el precio de venta.");
								System.out.printf("Actualmente está a  %.02f€\n", precios[index]);
								System.out.print(actualizarPrecio(index, precios, sc.nextDouble()));
							} catch (InputMismatchException e) {
								System.out.print("\nRevise los datos\n");
								sc.next();
							}
						} else {
							System.out.print("\nPosición invalida\n");
						}
						break;
					case 4: // cambiar producto
						System.out.print("Introduce la posicion:");
						if (recogePosicion(sc.next(), posicion)) {
							System.out.print("Introduce el nombre del nuevo producto:");
							cambiarProducto(posicion, productos, tienda, ventas, sc.next());
							int index = eligeProducto(posicion, tienda);
							System.out.print("\nIntroduzca el precio de venta.");
							try {
								System.out.print(actualizarPrecio(index, precios, sc.nextDouble()));
								reseteaExistencias(posicion, existencias);
								System.out.print("\nIntroduzca la cantidad a reponer\n");
								System.out.print(reponerProducto(posicion, existencias, sc.nextInt()));
								System.out.print("\nEl producto se ha actualizado correctamente\n");
							} catch (InputMismatchException e) {
								System.out.print("Revise los datos");
								sc.next();
							}
						} else {
							System.out.print("\nPosición invalida\n");
						}
						break;
					case 5: // mostrar más vendidos
						System.out.print(muestraTopVentas(ventas, productos, precios));
						break;
					case 6: // mostrar menos vendidos
						System.out.print(muestraMenosVentas(ventas, productos, precios));
						break;
					case 7: // mostrar información de los productos
						ordenaAlfabetico(ventas, productos, precios, tienda);
						System.out.print(infoProductos(tienda, existencias, productos, precios, ventas));
						break;
					case 8: // Recaudación total
						System.out.print(String.format("\nVentas totales hasta este momento: %.02f€\n\n", cajaTotal));
						break;
					case 9: // Salir menu de administración
						admin = false;
						break;
					case 10: // apagar la máquina
						admin = false;
						apagar = true;
						break;
					default: // opcion erronea
						System.out.print("\nHa indicado una opcion incorrecta\n");
						break;
					}
				}
				}
				break;
			default: // opcion erronea
				System.out.print("\nHa indicado una opcion incorrecta\n");
				break;
			}
		}
		sc.close();
	}
}
