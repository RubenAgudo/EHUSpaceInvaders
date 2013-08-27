package org.is.packlistas;


import org.is.packdisparos.*;
import org.is.packnaves.*;
import org.is.simplelinkedlist.SimpleLinkedList;

public class ListaDisparos {

	private static ListaDisparos miListaDisparos = new ListaDisparos();
	private SimpleLinkedList<Disparo> lista; //lista enlazada creada por nosotros en la asignatura de EDA
	private int cantidadDisparosHumanos;
	public static final int MAX_DISPAROS_HUMANOS = 3;
	
	private ListaDisparos() {
		lista = new SimpleLinkedList<Disparo>();
		cantidadDisparosHumanos = 0;
	}
	
	/**
	 * Devuelve la instancia unica de ListaDisparos
	 * @return
	 */
	public static ListaDisparos getMiListaDisparos() {
		return miListaDisparos;
	}

	
	/**
	 * Metodo que añade un disparo realizar a la lista de disparos.
	 * - Si el disparo es un DisparoHumano y la nave humana aun no ha realizado el maximo de disparos permitidos, se inserta el disparo,
	 * si no se omite y no se inserta.
	 * - Si el disparo es un DisparoAlien, se inserta siempre.
	 * @param pDisparo
	 */
	public void anadirDisparo(Disparo pDisparo) {
		
		//Los 3 primeros disparos son humanos, el resto de disparos son disparos alien
		if(pDisparo instanceof DisparoHumano && cantidadDisparosHumanos < MAX_DISPAROS_HUMANOS) {
			
			lista.insertFirst(pDisparo);
			cantidadDisparosHumanos++;
			
		} else {
			
			lista.insertFirst(pDisparo);
		
		}
		
	}
	
	/**
	 * Metodo que dado un disparo lo elimina de la lista
	 * @param pDisparo
	 */
	private void eliminarDisparo(Disparo pDisparo) {
		lista.remove(pDisparo);
	}
	
	
	/**
	 * Metodo que comprueba si alguno de los disparos impacta alguna nave, en caso de que haya impactado elimina el disparo
	 * e inserta la nave en una lista, porque puede suceder que dos disparos impacten al mismo tiempo en distintas naves.
	 * @return una lista ligada de los id de las naves
	 */
	public SimpleLinkedList<String> comprobarImpactos() {
		
		SimpleLinkedList<String> listaNavesImpactadas = new SimpleLinkedList<String>();
		Nave naveImpactada;
		
		lista.goFirst();
		
		while(lista.hasNext()) {
			
			Disparo d1 = lista.get();
			
			
			naveImpactada = ListaNaves.getMiListaNaves().hayImpactos(d1);
			
			if(naveImpactada != null) { //si la nave ha sido impactada
				
				//insertamos el identificador de la nave para que el controlador grafico sepa cual hay que borrar
				listaNavesImpactadas.insertFirst(naveImpactada.getId()); 
				
				if(d1 instanceof DisparoHumano) { //si el disparo es humano, restamos en 1 los disparos humanos realizados
					
					cantidadDisparosHumanos--;
					//Si el disparo es humano sabemos que la nave que hemos devuelto es alien, asique podemos hacer cast y aumentar
					//la puntuacion obtenida por la nave humana
					NaveHumana.getMiNaveHumana().aumentarPuntuacion(((NaveAlien) naveImpactada).getPuntuacion());
				
				}
				
				eliminarDisparo(d1);
				
			}
			
			lista.goNext();
			
		}
		
		return listaNavesImpactadas;
	}
	
	/**
	 * Metodo que desplaza los disparos a lo largo de la pantalla
	 */
	public void desplazarDisparos() {
		
		Disparo d1 = null;
		lista.goFirst(); //nos situamos al inicio de la lista de disparos
		
		while(lista.hasNext()) { //mientras haya mas disparos
			
			d1 = lista.get();
			
			d1.mover();
			
			if(d1.getY() < 0 || d1.getY() > 650) { //si se ha salido de la pantalla
				eliminarDisparo(d1);
				if(d1 instanceof DisparoHumano) {
					cantidadDisparosHumanos--;
				}
			}
			
			lista.goNext();
		}
	}	
	
	/**
	 * Obtiene la cantidad de disparos humanos
	 * @return
	 */
	public int obtenerNumeroDisparosHumanos(){
		return cantidadDisparosHumanos;
	}
	
	/**
	 * Metodo que devuelve una lista ligada de Strings que contiene lo siguiente "ID;posX;posY"
	 * @return SimpleLinkedList<String>
	 */
	public SimpleLinkedList<String> obtenerDatosDisparos(){
		
		SimpleLinkedList<String> listaDatos = new SimpleLinkedList<String>();
		
		lista.goFirst();
		
		while(lista.hasNext()) {
			listaDatos.insert(lista.get().toString());
			lista.goNext();
		}
		return listaDatos;
	}
	
	
	public void inicializar() {
		miListaDisparos = new ListaDisparos();
	}
	
}
