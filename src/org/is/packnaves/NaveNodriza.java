package org.is.packnaves;

import java.util.Random;

import org.is.packdisparos.Disparo;
import org.is.packdisparos.DisparoAlien;
import org.is.packlistas.ListaDisparos;

public class NaveNodriza extends NaveAlien {
	
	//probablemente esto sea una mae
	//private static NaveNodriza miNaveNodriza;
	private int toquesRecibidos;
	private static int MAX_TOQUES;
	private static final int POSICION_X_DEFECTO = 390;
	private static final int POSICION_Y_DEFECTO = -200;
	
	public NaveNodriza() {
		super("Final", POSICION_X_DEFECTO, POSICION_Y_DEFECTO, 7, 5000);
		toquesRecibidos = 0;
		Random rnd = new Random();
		MAX_TOQUES = rnd.nextInt(10);
	}

//	public static NaveNodriza getMiNaveNodriza() {
//		if(miNaveNodriza == null) {
//			miNaveNodriza = new NaveNodriza(POSICION_X_DEFECTO, POSICION_Y_DEFECTO, 7, 500000000);
//		}
//		
//		return miNaveNodriza;
//	}
	
	
	@Override
	public void disparar() {
		
		//podemos crear un id de disparo unico concatenando un numero que va aumentando a la id de la clase que dispara
		DisparoAlien d1 = new DisparoAlien("" + id + creadorDisparoUnico, x + 48, y + 96);
		creadorDisparoUnico++;
		ListaDisparos.getMiListaDisparos().anadirDisparo(d1);
		
	}
	
	public void mover(int pDireccion) {
		
		if(y <= 50) {
			aparecer();
		} else {
			super.mover(pDireccion);
		}
		
	}
	
	private void aparecer() {
		y += 2;
	}
	

	public boolean impacto(Disparo pDisparo) {
		
		boolean impactado = false;
		
		if((pDisparo.getX() >= x && pDisparo.getX() <= x + 90.0) && (pDisparo.getY() >= y && pDisparo.getY() <= y+90.0)) {
			
			impactado = true;
		
		}
		
		if(impactado) {
			toquesRecibidos++;
		}
	
		return impactado;
	}
	
	public boolean equals(Object o) {
		  if ( this == o ) return true;
		  if ( !(o instanceof NaveNodriza) ) return false;
		  NaveNodriza n1 = (NaveNodriza)o;
		  return id.equals(n1.getId());
	}
	
	/**
	 * Devuelve si la nave Nodriza ha recibido todos los impactos necesarios para morir
	 * @return
	 */
	public boolean naveMuerta() {
		return toquesRecibidos == MAX_TOQUES;
	}
	
	public boolean tocaBordeDerecho() {
		return x + 96 > 750;
	}
	
	public boolean tocaBordeIzquierda() {
		return x < 40;
	}
	
	public void desplazarEnY() {
		y += 20;
	}
	
	
	/**
	 * Metodo que devuelve los datos principales de la naveNodriza en el formato: "id;x;y;puntuacionAlien;vidasRestantes"
	 */
	public String toString() {
		return String.format("%1$s;%2$s;%3$s;%4$s;%5$s", id, x, y, puntuacionAlien, MAX_TOQUES-toquesRecibidos);
	}
}
