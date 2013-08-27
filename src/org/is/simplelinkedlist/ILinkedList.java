package org.is.simplelinkedlist;

public interface ILinkedList<T> {

	public void goFirst();

	public void goNext();

	public void goPrevious();

	public void goLast();
	
	public boolean hasNext();
	
	public boolean find(T elem);
	
	public void insert(T elem);

	public void insertFirst(T elem);

	public void insertLast(T elem);
	
	public T remove(int index);
		// Pre: Hay al menos "index" nodos
	
	public void remove();

	public void remove(T elem);

	public T get();

	public T get(int index);
		// Pre: Hay al menos "index" nodos

	public boolean isEmpty();	

}
