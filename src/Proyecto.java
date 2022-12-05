import java.util.InputMismatchException;
import java.util.Scanner;

public class Proyecto {

	private static Scanner sc;
	private static String[] productos = new String[16];
	private static double[] precios = new double[16];
	private static int[][][] tienda = new int[4][4][2];
	private static int[] ventas = new int[16];
	private static double cajaTotal = 0;
	private static String password = "DAM";
	private static boolean apagar = false;

	// inicializa la maquina
	static void inicializarProductos() {
		//datos del enunciado
		String[][] prods = { { "Lacasitos", "Chicles de fresa", "KitKat", "Palotes" },
				{ "Oreo", "Bolsa Haribo", "Chetoos", "Twix" },
				{ " M&M'S ", " Kinder Bueno ", "Papa Delta", "Chicles de menta" },
				{ "Lacasitos", "Crunch", "Milkybar", "KitKat" } };
		double[][] precs = { { 1.5, 0.8, 1.1, 0.9 }, { 1.8, 1, 1.2, 1 }, { 1.8, 1.3, 1.2, 0.8 },
				{ 1.5, 1.1, 1.1, 1.1 } };
		//reseteo las variables
		for (int i = 0; i < 16; i++) {
			productos[i] = "";
			ventas[i] = 0;
			precios[i] = 0;
		}
		//copio los datos del enunciado en las estructuras de control
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
	//ordena alfabeticamente los productos por nombre
	//usa el algoritmo de la burbuja, dejando los vacios al final
	static public void ordenaAlfabetico() {
		boolean working = true;
		while (working) {
			working = false;
			for (int index = 0; index < productos.length - 1; index++) {
				if (productos[index].compareToIgnoreCase(productos[index + 1]) > 0 && !productos[index + 1].isEmpty()) {
					String proc = productos[index];
					productos[index] = productos[index + 1];
					productos[index + 1] = proc;
					double prec = precios[index];
					precios[index] = precios[index + 1];
					precios[index + 1] = prec;
					int vent = ventas[index];
					ventas[index] = ventas[index + 1];
					ventas[index + 1] = vent;
					for (int fila = 0; fila < 4; fila++) {
						for (int columna = 0; columna < 4; columna++) {
							if (tienda[fila][columna][0] == index) {
								tienda[fila][columna][0]++;
							} else if (tienda[fila][columna][0] == index + 1) {
								tienda[fila][columna][0]--;
							}
						}
					}
					working = true;
				}

			}
		}
	}
	// vende el producto y devuelve el valor de la venta
	// si no hay existencias no hace nada y devuelve 0
	static double venderProducto(int fila, int columna) {
		int[] celda = tienda[fila][columna];
		if (celda[1] > 0) {
			celda[1]--;
			int prod = celda[0];
			ventas[prod]++;
			System.out.println(productos[prod]);
			return precios[prod];
		}
		return 0;
	}

	// muestra los productos de cada casilla con su precio
	static void mostarProductos() {
		int filas = tienda[0].length;
		int columnas = tienda.length;
		for (int fila = 0; fila < filas; fila++) {
			for (int columna = 0; columna < columnas; columna++) {
				int[] celda = tienda[fila][columna];
				System.out.printf("| %-20s | Posicion: %c%c | Precio: %2.2f |\n", productos[celda[0]],
						'A' + fila, 'A' + columna, precios[celda[0]]);
			}
		}
	}

	//muestra los precios de los productos registrados,
	//sus existencias y sus ventas
	private static void infoProductos() {
		int filas = tienda[0].length;
		int columnas = tienda.length;
		for (int prod =0;prod<16;prod++) {
			if(productos[prod].isEmpty()) return;
			int unidades = 0;
			for (int fila = 0; fila < filas; fila++) {
				for (int columna = 0; columna < columnas; columna++) {
					unidades += tienda[fila][columna][0]==prod?tienda[fila][columna][1]:0;
				}
			}		
			System.out.printf("| %-20s | Precio: %2.2f | U. disponibles: %3d | Ventas: %3d |\n",
					productos[prod], precios[prod], unidades,ventas[prod]);
		}
	}
	private static void actualizarPassword() {
		System.out.println("\nIntroduzca nueva contraseña");
		String tempPass = sc.next();
		System.out.println("\nRepita nueva contraseña");
		String confPass = sc.next();
		if(tempPass.equals(confPass)&&!tempPass.isEmpty()) {
			password=tempPass;
			System.out.println("\nContraseña actualizada");
			return;
		}
		System.out.println("\nLas contraseñas no coinciden");
		
	}
	//comprueba que haya sitio y repone los productos especificados
	private static int[] recogePosicion() {
		System.out.println("Introduce la posicion del producto:");
		int[] out= {-1,-1};
		String entrada = sc.next();
		if (entrada.length() >= 2) {
			int fila = entrada.charAt(0) - 'A';
			int columna = entrada.charAt(1) - 'A';
			if (!(fila < 0 || fila > 3 || columna < 0 || columna > 3)) {
				out[0]=fila;
				out[1]=columna;
				return out;
			}
		}
		System.out.println("\nHa indicado una posicion incorrecta");
		return out;
	}
	//recibe la posicion a rellenar y pide la cantidad a reponer,
	//si es posible añadirlo devuelve true, en caso contrario false.
	private static boolean reponerProductos(int[] posicion) {
		boolean actualizado = false;
		System.out.println("Introduce la cantidad a reponer:");
		int cantidad = 0;
		try {
			cantidad = sc.nextInt();
		} catch (InputMismatchException e) {
			
		}
		if (cantidad <= 0) return actualizado;
		int existencias = tienda[posicion[0]][posicion[1]][1];
		if ((5 - existencias) >= cantidad) {
			tienda[posicion[0]][posicion[1]][1]+=cantidad;
			actualizado = true;
		}	
		return actualizado;
	}
	
private static boolean actualizarPrecio(int[] posicion) {
	System.out.println("Introduce el nuevo precio de venta:");
	double precio = 0;
	try {
		precio = sc.nextDouble();
	} catch (InputMismatchException e) {
		sc.nextLine();
	}
	if (precio <= 0) return false;
	precios[tienda[posicion[0]][posicion[1]][0]]=precio;
	return true;
}
	
	//gestiona el menu de administracion
	static void menuAdministrador() {
		System.out.println("\nIntroduzca la contraseña de administración");
		String pass = sc.next();
		if (!password.equals(pass)) {
			System.out.println("\nLa contraseña es incorrecta");
			return;
		}
		System.out.println("\nBienvenid@ al panel de administración:\n");
		boolean salir = false;
		while (!salir) {
			System.out.println("\n*** Opciones administracion***\n\n1. Cambiar contraseña");
			System.out.println("2. Reponer productos\n3. Cambiar precio\n4. Cambiar producto");
			System.out.println("5. Más vendidos\n6. Menos vendidos\n7. Informacion productos");
			System.out.println("8. Ventas totales\n9. Cerrar sesion\n10. Apagar la máquina\n");
			int opcion = 0;
			int[] posicion;
			try {
				opcion = sc.nextInt();
			} catch (InputMismatchException e) {

			}
			switch (opcion) {
			case 1:
				actualizarPassword();
				break;
			case 2:
				posicion = recogePosicion();
				if(posicion[0]>=0) {
					if(reponerProductos(posicion)) {
						System.out.println("\nEl producto se ha repuesto correctamente");
					} else {
						System.out.println("\nError, revise los datos");
					}
				}
				break;
			case 3:
				posicion = recogePosicion();
				if(posicion[0]>=0) {
					if(actualizarPrecio(posicion)) {
						System.out.println("\nEl precio se a actualizado correctamente");
					} else {
						System.out.println("\nError, revise los datos");
					}
				}
				break;
			case 7:
				infoProductos();
				break;
			case 8:
				System.out.printf("\nVentas totales hasta este momento: %2.2f€\n\n",cajaTotal);
				break;
			case 9:
				salir = true;
				break;
			case 10:
				apagar = true;
				salir = true;
				break;
			default:
				System.out.println("\nHa indicado una opcion incorrecta");
				break;
			}
		}
	}
	
	public static void main(String[] args) {

		inicializarProductos();
		ordenaAlfabetico();
		sc = new Scanner(System.in);

		while (!apagar) {
			System.out.print("\n*** Opciones ***\n\n1. Pedir golosina\n2. Mostrar golosinas\n3. Submenu administrador\n");
			String input = sc.next();
			char opcion = input.length()==1?input.charAt(0):'E';
			switch (opcion) {
			case '1':
				int[] posicion = recogePosicion();
				if (posicion[0]>=0) {
					double venta = venderProducto(posicion[0], posicion[1]);
					cajaTotal += venta;
					if (venta > 0) {
						System.out.println("\nPuede retirar su producto");
					} else {
						System.out.println("\nNo hay existencias de ese producto");
					}
				}
				break;
			case '2':
				mostarProductos();
				break;
			case '3':
				menuAdministrador();
				break;
			case '4':
				infoProductos();
				for (int index = 0; index < productos.length; index++) {
					System.out.printf("| Index: %2d | %-20s | Precio: %2.2f | Ventas: %3d |\n",
							index, productos[index], precios[index],ventas[index]);
				}
			default:
				System.out.println("\nHa indicado una opcion incorrecta");
				break;
			}
		}
	}
}
