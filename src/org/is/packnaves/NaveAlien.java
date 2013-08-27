package org.is.packnaves;

import org.is.packdisparos.DisparoAlien;
import org.is.packlistas.ListaDisparos;

public class NaveAlien extends Nave {

	protected int puntuacionAlien; //como es estatico y no se puede modificar puede ser publico
	private double multiplicadorVelocidad;
	
	/**
	 * Constructora de una Nave Alien
	 * @param pX Posicion x en la que va a estar en la pantalla
	 * @param pY Posicion y en la que va a estar en la pantalla
	 * @param pVelocidad Velocidad de movimiento de la Nave
	 * @param pPuntuacion Puntuacion de la nave que va a ganar el jugador al destruir la nave
	 */
	public NaveAlien(String pID, double pX, double pY, double pVelocidad, int pPuntuacion) {
		super(pID,pX, pY, pVelocidad);
		//direccion = 1;
		multiplicadorVelocidad = 1.0;
		puntuacionAlien = pPuntuacion;
		creadorDisparoUnico = 0;
	}

	@Override
	public void mover(int pDireccion) {
		x += velocidad * pDireccion * multiplicadorVelocidad;
	}
	
	/* public double moverDevolviendoPosicion(int pDireccion) {
		mover(pDireccion);
		return x;
	} */

	@Override
	public void disparar() {
		//podemos crear un id de disparo unico concatenando un numero que va aumentando a la id de la clase que dispara
		DisparoAlien d1 = new DisparoAlien("" + id + creadorDisparoUnico, x, y + 10);
		creadorDisparoUnico++;
		ListaDisparos.getMiListaDisparos().anadirDisparo(d1);
	}

	public void desplazarEnY() {
		y += 3.0;
		multiplicadorVelocidad += 0.015;
	}

	public boolean tocaBordeIzquierdo() {
		return x < 10;
	}

	public boolean tocaBordeDerecho() {
		return x > 750;
	}

	public int getPuntuacion() {
		return puntuacionAlien;
	}

	public String toString(){
		return String.format("%1$s;%2$s;%3$s;%4$s", id, x, y, puntuacionAlien);
	}
	
	public boolean equals(Object o) {
	  if ( this == o ) return true;
	  if ( !(o instanceof NaveAlien) ) return false;
	  NaveAlien n1 = (NaveAlien)o;
	  return id.equals(n1.getId());
	}
}
