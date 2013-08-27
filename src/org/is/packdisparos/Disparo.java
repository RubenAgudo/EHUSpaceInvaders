package org.is.packdisparos;

public abstract class Disparo {

	protected String id;
	protected double x;
	protected double y;
	
	/**
	 * Construye un nuevo disparo
	 * @param pX
	 * @param pY
	 */
	public Disparo(String pId, double pX, double pY) {
		id = pId;
		this.x = pX;
		this.y = pY;
	}
	/**
	 * Desplaza el disparo por la pantalla, es decir, actualiza su posición "Y" actual.
	 * - Si es un DisparoHumano la posicion "Y" decrecerá (notese que la posicion (0,0) se encuentra en la esquina superior izquierda de la pantalla)
	 * - Si es un DisparoAlien la posición "Y" aumentará (el disparo bajara en la pantalla)
	 */
	public abstract void mover();

	/**
	 * Devuelve la posicion "y" actual del disparo
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * Devuelve la posicion "x" actual del disparo
	 * @return
	 */
	public double getX() {
		return x;
	}
	
	public String toString(){
		return String.format("%1$s;%2$s;%3$s", id, x, y);
	}
	
	public abstract boolean equals(Object o);
	
}