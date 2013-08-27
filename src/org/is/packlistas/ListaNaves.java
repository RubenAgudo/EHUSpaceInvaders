package org.is.packlistas;

import java.util.Random;
import org.is.packdisparos.*;
import org.is.packnaves.*;
import org.is.simplelinkedlist.SimpleLinkedList;

public class ListaNaves {

	private static ListaNaves miListaNaves;
	private NaveAlien[][] listaNavesAliens;
	private NaveNodriza naveNodriza;
	//private double[] listaPosicionesX; 
	
	private int movimiento; //indica el sentido del movimiento del ejercito de naves aliens
	private int numElementos; //indica el numero de navesAlien restantes, sin contar la naveNodriza
	
	public static final int NUM_FILAS = 6;//determina el numero de filas que va a tener el ejercito de naves regulares
	public static final int NUM_COLUMNAS = 11; //determina el numero de columnas que va a tener el ejercito de naves regulares
	
	private ListaNaves() {
		listaNavesAliens = new NaveAlien[NUM_FILAS][NUM_COLUMNAS]; //las naves enemigas normales
		//listaPosicionesX = new double[NUM_COLUMNAS]; //lista virtual de posiciones X de la nave, se actualiza a cada ciclo para mantener una referencia
		
		movimiento = 1;
		
		numElementos = NUM_FILAS * NUM_COLUMNAS;
		//rellenamos la lista de naves regulares
		crearEjercitoNavesRegulares();
	}

	
	/**
	 * Metodo que crea el ejercito de naves regulares.
	 */
	private void crearEjercitoNavesRegulares() {
		int x = 75, y = 50;
		int ind1,ind2;
		int puntuacion = 125;
		
		for(ind1 = 0; ind1 < NUM_FILAS; ind1++) {
			
			
			for(ind2 = 0; ind2 < NUM_COLUMNAS; ind2++) {
				
				NaveAlien nuevaNave = new NaveAlien("" + ind1 + ind2, x, y, 0.5, puntuacion); //creamos la nave
				
				listaNavesAliens[ind1][ind2] = nuevaNave; //la insertamos en el contenedor
				
				
				// if(ind1 == 0) {
				
					// listaPosicionesX[ind2] = x;
				
				// }
				
				x += 48; //desplazamos la posicion 16 pixels a la derecha
				
			}
			
			//si el indice 1 es multiplo de dos, es decir, cada 2 filas
			if(ind1 %2 ==0 ) {
				
				puntuacion -= 25; //reducimos la puntuacion en 25 puntos
			}
			y += 48; //aumentamos la y, es decir bajamos una fila en la posicion
			x = 75; //volvemos a la posicion inicial
			
		}
		
	}
	
	
	
	/**
	 * M�todo que hace que una nave alien dispare.
	 * Se selecciona una nave al azar, si en esa posicion hay una nave, y la nave puede disparar, se dispara, si no, no dispara
	 * 
	 * @return El ID de la nave que ha disparado, si no puede disparar devuelve un string vacio
	 */
	public String disparar() {
		
		String resultado = "";
		
		if(numElementos > 0 ) {
			
			Random rnd = new Random();
			int fila = rnd.nextInt(NUM_FILAS);
			int columna = rnd.nextInt(NUM_COLUMNAS);
			int porcentaje = rnd.nextInt(100);
			NaveAlien n1 = listaNavesAliens[fila][columna];
			
			if(n1 != null ) {
				
				if(noHayNavesDebajo(fila, columna) && porcentaje < 50) {
					
					n1.disparar();
					resultado = String.format("%1$s;%2$s", n1.getId(),n1.getCreadorDisparoUnico()-1);
				} 
				
			}
			
		} else {
			Random rnd = new Random();
			int num = rnd.nextInt(20);
			if(num <= 1 && naveNodriza.getY() >= 50) {
				naveNodriza.disparar();
				resultado = String.format("%1$s;%2$s", naveNodriza.getId(), naveNodriza.getCreadorDisparoUnico()-1);
			}
			
		}
		
		
		return resultado;
		
	}
	
	/**
	 * M�todo que indica si debajo de la nave actual hay naves
	 * @param pFila
	 * @param pColumna
	 * @return
	 */
	private boolean noHayNavesDebajo(int pFila, int pColumna) {
		
		boolean resultado = true;
		boolean hayNavesDebajo = false;
		int x = pFila + 1;
		
		//comprobamos todas las naves debajo de mi, si alguna es distinta de null no puede disparar
		while(x < NUM_FILAS && !hayNavesDebajo) {
			
			NaveAlien n1 = listaNavesAliens[x][pColumna];
			
			if(n1 != null) {
				
				hayNavesDebajo = true;
				
			}
			
			x++;
			
		}
		
		if(hayNavesDebajo) {
			
			resultado = false;
			
		}
		
		return resultado;
		
	}

	/**
	 * Metodo que devuelve una lista ligada de Strings que contiene lo siguiente "ID;posX;posY"
	 * @return SimpleLinkedList<String>
	 */
	public SimpleLinkedList<String> obtenerDatosNaves(){
		int x,y;
		SimpleLinkedList<String> listaDatos = new SimpleLinkedList<String>();
		
		if(numElementos > 0) {
			
			for(x = 0; x < NUM_FILAS; x++){
				
				for(y = 0; y < NUM_COLUMNAS; y++){
					
					NaveAlien n1 = listaNavesAliens[x][y];
					
					if(n1!=null){
						
						listaDatos.insert(n1.toString());
					
					}
					
				}
				
			}
		} else {
			listaDatos.insert(naveNodriza.toString());
		}
		
		
		return listaDatos;
	}
	
	
	/**
	 * Devuelve la instancia �nica de ListaNaves
	 * @return miListaNaves
	 */
	public static ListaNaves getMiListaNaves() {
		if(miListaNaves == null) {
			miListaNaves = new ListaNaves();
		}
		return miListaNaves;
	}
	
	/**
	 * M�todo que desplaza todas las naves alien, lo mueve un pixel a derecha o izquierda.
	 */
	public void desplazarEjercitoAmenazador() {
		
		int ind1 = 0, ind2 = 0;
		boolean bordeTocado = false;
		
		if(numElementos > 0) {
			for(ind1 = 0; ind1 < NUM_FILAS; ind1++) {
				
				for(ind2 = 0; ind2 < NUM_COLUMNAS; ind2++) {
				
					NaveAlien n1 = listaNavesAliens[ind1][ind2];
					
					if(n1 != null) {
							
						if(n1.tocaBordeIzquierdo() && movimiento == -1) {
							
							movimiento = 1;
							bordeTocado = true;
							
						} else if (n1.tocaBordeDerecho() && movimiento == 1) {
							
							movimiento = -1;
							bordeTocado = true;
							
						}
						
						
						if(bordeTocado) {
							
							bajarEjercitoDeNaves();
							bordeTocado = false;
						}
					
						//si es la primera fila
						/* if(ind1 == 0) { //actualizamos el chivato de posiciones, hemos tenido que reescribir el metodo mover por el tema del multiplicador de velocidad
							listaPosicionesX[ind2] = n1.moverDevolviendoPosicion(movimiento);
						} else { */
							n1.mover(movimiento);
						//}
						
						
					}//fin del if(n1!=null)
				
				} //fin del segundo for
				
			}//fin del primer for
			
		} else {
			if(naveNodriza == null) {
				naveNodriza = new NaveNodriza();
			}
			
			if(naveNodriza.tocaBordeIzquierdo() && movimiento == -1) {
				
				movimiento = 1;
				bordeTocado = true;
				
			} else if(naveNodriza.tocaBordeDerecho() && movimiento == 1) {
				
				movimiento = -1;
				bordeTocado = true;
				
			}
			if(bordeTocado) {
				
				naveNodriza.desplazarEnY();
				bordeTocado = false;
			}
			
			naveNodriza.mover(movimiento);
		}
			
		
	}

	/**
	 * Metodo que baja en el eje Y el numero de naves
	 */
	private void bajarEjercitoDeNaves() {
		int ind1, ind2;
		
		for(ind1 = 0; ind1 < NUM_FILAS; ind1++) {
			
			for(ind2 = 0; ind2 < NUM_COLUMNAS; ind2++) {
				
				NaveAlien n1 = listaNavesAliens[ind1][ind2];
				if(n1!= null) {
					n1.desplazarEnY();
				}
				
			}
		}
		
	}

	/**
	 * Metodo que comprueba si un disparo impacta en alguna nave, en caso de impactar, se devuelve la nave, en caso contrario
	 * se devuelve null. Si la nave es impactada, la posicion que ocupaba en el array pasa a ser null
	 * @param pDisparo
	 * @return Devuelve la nave que ha sido impactada
	 */
	public Nave hayImpactos(Disparo pDisparo) {
		
		int x = NUM_FILAS-1, y = NUM_COLUMNAS-1;
		boolean impactado = false;
		
		Nave n1 = null;
		
		if(pDisparo instanceof DisparoHumano) { //si el disparo es un disparo realizado por la nave humana
			
//			boolean salir = false;
//			int fila = NUM_FILAS - 1;
//			
//			
//			int columna = obtenerNumColumnaEnRangoYDeImpacto(pDisparo);
//			
//			//usamos un poco de JGA
//			SimpleLinkedList<NaveAlien> listaPosiblesImpactos = obtenerNavesDeUnaColumna(columna);
//			
//			listaPosiblesImpactos.goFirst();
//			
//			while(listaPosiblesImpactos.hasNext() && !salir) {
//				
//				
//				n1 = listaPosiblesImpactos.get();
//				
//				
//				//si la nave no ha sido impactada aun
//				if(n1!=null) {
//				
//					
//					//si la nave de mas abajo (la primera de la lista), esta mas arriba que el disparo, entonces es imposible que impacte
//					if(n1.getY() - 16 < pDisparo.getY()) {
//				
//						salir = true;
//					
//					} else { //pueden impactar
//					
//						if(n1.impacto(pDisparo)) {
//						
//							listaNavesAliens[fila][columna] = null; //me falta como averiguar la columna en la que se encuentran
//							numElementos--;
//							salir = true;
//							
//						} else {
//							
//							listaPosiblesImpactos.goNext();
//							
//						}
//					}
//				}
//					
//				fila--; //para poder comprobar la siguiente fila (la superior)
//				
//			}

			if(numElementos > 0) {
				
				while(x  >= 0 && ! impactado) {
					
					
					while(y >= 0 && ! impactado) {
						
						n1 = listaNavesAliens[x][y]; //obtenemos la nave que ocupa la posicion x,y
						
						if(n1 != null) { //si la nave no ha sido previamente eliminada
						
							//si la nave ha sido impactada por el disparo la a�adimos a la lista de naves a 
							//devolver y la eliminamos (ponemos a null esa posicion del array de naves)
							if(n1.impacto(pDisparo)) { 
								
								listaNavesAliens[x][y] = null;
								impactado = true;
								numElementos--;
								
							}//final de la comprobacion de impacto
							
						}//final de si la nave es null
						
						y--;
						
					}//final del while que recorre las columnas
					
					y=NUM_COLUMNAS-1;
					x--;
					
				}//final del while que recorre las filas
				
			} else {
				
				if(naveNodriza == null) {
					naveNodriza = new NaveNodriza();
				}
				
				if(naveNodriza.impacto(pDisparo)) {
					
					n1 = naveNodriza;
					impactado = true;
					
					
					if(naveNodriza.naveMuerta()) {
						naveNodriza = null;
						numElementos--;
					}
					
				}
				
			}
			
		} else { //si el disparo es un disparo realizado por una nave alien
			
			if(NaveHumana.getMiNaveHumana().impacto(pDisparo)) {//si la nave humana ha sido impactada
				
				n1 = NaveHumana.getMiNaveHumana(); //devolvemos la nave humana para que el controlador sepa que debe destruir la nave y situarla en el centro
				impactado=true;
				
			}
			
		} //final del else
		
		if (impactado){
			return n1;	
		} else {
			return null;
		}
		
		
	}
	
//	private SimpleLinkedList<NaveAlien> obtenerNavesDeUnaColumna(int pColumna) {
//		
//		SimpleLinkedList<NaveAlien> lista = new SimpleLinkedList<NaveAlien>();
//		int ind = 0;
//		NaveAlien n1;
//		
//		//si no hay columna no se cojen naves
//		if(pColumna!= -1) {
//				
//			for(ind = 0; ind < NUM_FILAS; ind++) {
//			
//				//Metemos todas las naves de una columna en la lista ligada de naves
//				
//				n1 = listaNavesAliens[ind][pColumna];
//				
//				lista.insert(n1);
//				
//			
//			}
//			
//		}
//		return lista;
//	}
//
//
//	/**
//	 * Metodo que dado un disparo devuelve las naves que estan en el rango Y de impactos
//	 * @param pDisparo
//	 * @return el numero de columna que puede ser impactado
//	 */
//	private int obtenerNumColumnaEnRangoYDeImpacto(Disparo pDisparo) {
//		
//		int columna = encontrarPosibleColumnaImpactos(pDisparo.getX());
//		
//		return columna;
//	}
//
//
//	/**
//	 * Dada una posicion X de un disparo, busca a ver si alguna columna de Naves Aliens esta en su rango
//	 * @param pX posicion X del disparo.
//	 * @return el indice, -1 si no hay posibles columnas
//	 */
//	private int encontrarPosibleColumnaImpactos(double pX) {
//		
//		return subBuscarPosibleColumna(pX, 0, NUM_COLUMNAS-1);
//		
//	}
//
//	/**
//	 * Sub busqueda dicotomica de una posible columna
//	 * @param pX
//	 * @param pInicio
//	 * @param pFin
//	 * @return obtiene la columna que esta en el rango de un disparo, devuelve -1 si ninguna columna esta en rango
//	 */
//	private int subBuscarPosibleColumna(double pX, int pInicio, int pFin) {
//		int centro = (pInicio + pFin)/2;
//		int resultado = -1;
//		
//		if(pFin-pInicio >= 0) {
//			
//			if(compararEnRango(listaPosicionesX[centro], pX) == 1) {
//				
//				subBuscarPosibleColumna(pX, pInicio, centro-1);
//			
//			} else {
//				
//				if(compararEnRango(listaPosicionesX[centro], pX) == -1) {
//					
//					subBuscarPosibleColumna(pX, centro + 1, pFin);
//					
//				} else {
//					
//					resultado = centro;
//					
//				}
//					
//			}
//			
//		}
//		return resultado;
//	}
//	
//	/**
//	 * Compara dos numeros en un rango de [base - 4, base + 4]
//	 * @param pBase
//	 * @param pComparado
//	 * @return Si el numero comparado esta en el rango [base - 4, base + 4] se devuelve un 0
//	 * Si, el comparado es mayor que base + 4 se devuelve un -1
//	 * Si, el comparado es menor que base - 4 se devuelve un 1
//	 */
//	private int compararEnRango(double pBase, double pComparado) {
//		
//		int resultado = 0; //de primeras suponemos que el pComparado esta dentro del rango
//		double limInferior = pBase - 4; //creamos el limite inferior
//		double limSuperior = pBase + 4; //creamos el limite superior
//		
//		if( limSuperior < pComparado ) {
//
//			resultado = -1;
//			
//		} else {
//			
//			if( limInferior > pComparado ) {
//				
//				resultado = 1;
//				
//			}
//			
//		}
//		
//		return resultado;
//	}

	public boolean estaVacia(){
		return numElementos == 0;
	}
	
	public boolean juegoGanado() {
		return numElementos == -1;
	}
	
	
	/**
	 * Metodo que indica si la tierra ha sido invadida, es decir, si la posicion y de la nave de mas abajo es <= que la de la nave humana
	 * @return Si la tierra ha sido invadida
	 */
	public boolean tierraInvadida() {
		
		NaveAlien n1;
		int x = NUM_FILAS-1, y = NUM_COLUMNAS-1;
		boolean tierraInvadida = false;
		boolean continuarComprobando = true; //con comprobar una nave nos llega
		
		while(x >= 0 && continuarComprobando) {
			
			
			while(y >= 0 && continuarComprobando) {
				
				n1 = listaNavesAliens[x][y]; //obtenemos la nave que ocupa la posicion x,y
				
				if(n1 != null) { //si la nave no ha sido previamente eliminada
					
					continuarComprobando = false;
					
					//si la posicion  en vertical de una nave es menor o igual que la de nuestra nave, nos han invadido
					if(n1.getY() + 24 >= NaveHumana.getMiNaveHumana().getY()) { 
						
						tierraInvadida = true;
						
					}//final de la comprobacion de invasion
					
				} //final de si la nave es null
				
				y--;
				
			}//final del while que recorre las columnas
			y=NUM_COLUMNAS-1;
			x--;
			
		}//final del while que recorre las filas
		
		return tierraInvadida;
	}
	
	public void inicializar() {
		miListaNaves = new ListaNaves();
	}
}
