package org.is.packnaves;

import org.is.packdisparos.Disparo;

public abstract class Nave {

	
	
	//Los he puesto en comentario porque no estoy seguro de si tiene que ser la propia clase la que debe guardar su posicion
	//Preguntar eso a bego el martes
	protected double x; 
	protected double y;
	protected double velocidad;
	protected String id;
	protected int creadorDisparoUnico;
	
	public Nave(String pId,double pX, double pY, double pVelocidad) {
		x = pX;
		y = pY;
		velocidad = pVelocidad;
		id = pId;
		creadorDisparoUnico = 0;
	}
	
	//getters y setters
	public double getX() {
		return x;
	}

//	public void setX(int x) {
//		this.x = x;
//	}

	public double getY() {
		return y;
	}

//	public void setY(int pY) {
//		this.y = pY;
//	}
//
//	public int getId() {
//		return id;
//	}
	
	//otros metodos
	//Susceptible de ser modificado, tal vez deberian pasarme el disparo completo, teniendo el disparo su posicion actual en pantalla
	/**
	 * Metodo que comprueba si una nave y un disparo colisionan
	 * @param pDisparo
	 * @return
	 */
	public boolean impacto(Disparo pDisparo) {
		
		boolean impactado = false;
		
		//posible optimizacion, comprobar primero el eje Y, si no esta en un eje Y valido antes de cualquier nave o por encima de todas las naves
		//no se comprueba el eje X siempre daria false
		if((pDisparo.getX() >= x && pDisparo.getX() <= x+36.0) && (pDisparo.getY() >= y && pDisparo.getY() <= y+24.0)) {
		
			impactado = true;
		
		}
		//}
		return impactado;
	}
	
	
	/**
	 * Metodo que devuelve el id de una nave
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Devuelve el entero que se utiliza para crear un identificador de disparo unico
	 * @return int
	 */
	public int getCreadorDisparoUnico() {
		return creadorDisparoUnico;
	}

	/**
	 * Mueve la nave
	 * @param pDireccion es un int que indica la direccion del movimiento 
	 */
	public abstract void mover(int pDireccion);
	
	public abstract void disparar();
	
}
