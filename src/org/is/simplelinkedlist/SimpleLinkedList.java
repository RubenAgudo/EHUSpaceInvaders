package org.is.simplelinkedlist;

public class SimpleLinkedList<T> implements ILinkedList<T> {

	// Atributos
	private Node<T> first;
	private Node<T> actual;

	// Constructor
	public SimpleLinkedList() {
		first = null;
		actual = null;
	}

	// MÃ©todos publicos
	public void goFirst() {
		actual = first;
	}

	public void goNext() {
		if (actual != null)
			actual = actual.next;
	}

	public void goPrevious() {
		if (first != null) {
			if (first == actual)
				actual = null;
			else {
				Node<T> aux = first;
				while (aux.next != actual) {
					aux = aux.next;
				}
				actual = aux;
			}
		}
	}

	public void goLast() {
		if (first != null) {
			actual = first;
			while (actual.next != null) {
				actual = actual.next;
			}
		}
	}

	public boolean hasNext() {
		return (actual != null);
	}

	public boolean find(T elem) {
		
		boolean enc = false;
		goFirst();
		while(!enc && actual != null) {
			if(actual.data.equals(elem)) {
				enc = true;
			} else {
				goNext();
			}
		}
		
		return enc;

	}

	public void insert(T elem) {
		
		Node<T> nuevoNodo = new Node<T>(elem);
		
		if(first == null || first.next == null || actual == null) { //si solo hay un elemento o actual esta posicionado en el primer elemento de la lista o la lista está vacia
			insertFirst(elem);
		} else { //si actual esta en cualquier otra posicion 
			nuevoNodo.next = actual; //enlazamos el nuevo nodo con la posicion actual por la izquierda
			goPrevious(); //retrocedemos una posicion
			actual.next = nuevoNodo; //enlazamos la posicion anterior con el nuevo Nodo
			goNext(); //posicionamos actual en el nuevo elemento insertado
		}
		
	}

	public void insertFirst(T elem) {
		Node<T> newNode = new Node<T>(elem);
		newNode.next = first;
		first = newNode;
		actual = first.next;
	}

	public void insertLast(T elem) {
		Node<T> pNode = new Node<T>(elem);
		this.goLast();
		actual = pNode;
		actual = actual.next;
	}

	public void remove(T elem) {
		if (first != null) {
			if (first.data.equals(elem))
				first = first.next;
			else {
				actual = first;
				Node<T> ant = null;
				while ((actual.next != null) && !(actual.data.equals(elem))) {
					ant = actual;
					actual = actual.next;
				}
				if (actual.data.equals(elem))
					ant.next = actual.next;
			}
		}
		actual = first;
	};

	public void remove() {

		if(!this.isEmpty()){
			//Primera Posicion
			if (actual == first){
				first = first.next;
				actual = first;
			} else if (actual.next != null){ //Posicion Intermedia y posicion final
				Node<T> posterior = actual;
				this.goPrevious();
				actual.next = posterior.next;
			}
		}

	}

	public T remove(int index) {
		// Pre: Hay al menos "index" nodos
		
		Node<T> anterior = null;
		Node<T> aDevolver = null;
		//Primera Posicion
		if(index == 1){
			aDevolver = first; //guardamos el nodo a devolver
			first = first.next;
			actual = first;
		} else { //Resto de posiciones
			goFirst();
			for(int i = 1; i < index-1; i++) { //vamos hasta la posición anterior que queremos borrar
				this.goNext();
			}
			anterior = actual;
			this.goNext();
			anterior.next = actual.next;
			aDevolver = actual; //guardamos el nodo que borramos para devolverlo
			actual = actual.next; //apuntamos al siguiente
		}
		return (T) aDevolver;
	}

	public T get() {

		return this.actual.data;

	}

	public T get(int index) {
		// Pre: actual apunta a algÃºn nodo

		int x;
		actual = first;
		for(x = 1; x < index; x++){
			actual = actual.next;
		}
		return actual.data;
		
	}

	public boolean isEmpty() {
		return first == null;
	}

}
