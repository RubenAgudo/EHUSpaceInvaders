package org.is.packnaves;

import org.is.packdisparos.Disparo;
import org.is.packdisparos.DisparoHumano;
import org.is.packlistas.ListaDisparos;

public class NaveHumana extends Nave {
	/*Singleton*/
	private static NaveHumana miNaveHumana;
	
	/*atributos "comunes"*/
	private int vidas;
	private int toquesRecibidos;
	private int puntuacionAcumulada;
	
	/*atributos estaticos y finales*/
	public static int TOQUES;
	private static final int POSICION_X_DEFECTO = 390;
	private static final int POSICION_Y_DEFECTO = 500;
	
	/**
	 * 
	 * @param pX Posicion X de la nave
	 * @param pY Posicion Y de la nave
	 * @param pVelocidad Valor que indica la velocidad de la nave, valores posibles: 1-3
	 */
	private NaveHumana(int pVelocidad) {
		super("Player",POSICION_X_DEFECTO, POSICION_Y_DEFECTO, pVelocidad);
		vidas = 5 - pVelocidad;
		TOQUES = 5 - pVelocidad;
		toquesRecibidos = 0;
		puntuacionAcumulada = 0;		
	}

	public static NaveHumana getMiNaveHumana() {
		if(miNaveHumana == null) {
			miNaveHumana = new NaveHumana(3); //la velocidad por defecto es 2
		}
		return miNaveHumana;
	}
	
	@Override
	public void mover(int pDireccion) {
		x += velocidad * pDireccion;
	}

	
	/**
	 * Metodo que hace que una nave dispare. Si ya se han realizado 3 disparos humanos no dispara
	 */
	public void disparar() {
		DisparoHumano disparo = new DisparoHumano("" + id + creadorDisparoUnico, x, y);
		creadorDisparoUnico++;
		ListaDisparos.getMiListaDisparos().anadirDisparo(disparo);
	}
	
	
	/**
	 * Metodo que aumenta en 1 los toques.
	 * Si el numero de toques recibidos, llega al limite de toques, resta una vida
	 */
	private void aumentarToques() {
		
		//boolean resultado = false;
		
		toquesRecibidos++;
		
		if(puntuacionAcumulada >= 50) {
			puntuacionAcumulada -= 50;
		}
		
		if(toquesRecibidos > TOQUES) {
			
			vidas--;
			toquesRecibidos = 0;
			puntuacionAcumulada /= 2;
			//resultado = true;
		}
		
		//return resultado;
	}
	
	public boolean impacto(Disparo pDisparo) {
		
		boolean impactado = super.impacto(pDisparo);
		if(impactado) {
			aumentarToques();
		}
		
		return impactado;
		
	}
	
	/**
	 * Metodo que devuelve si no le quedan vidas a la nave Humana
	 * @return
	 */
	public boolean gameOver() {
		return vidas == -1;
	}

	/**
	 * Metodo que dada una puntuacion de una nave, aumenta la puntuacion acumulada de la nave
	 * @param pPuntuacion
	 */
	public void aumentarPuntuacion(int pPuntuacion) {
		
		puntuacionAcumulada += pPuntuacion;
		
	}
	
	/**
	 * Metodo que devuelve la puntuacion acumulada por la NaveHumana
	 * @return
	 */
	public int getPuntuacionAcumulada() {
		
		return puntuacionAcumulada;
		
	}
	
	public int getVidas() {
		
		return vidas;
		
	}
	
	public int getToques() {
		
		return TOQUES-toquesRecibidos;
		
	}
	
	/**
	 * Metodo que restablece la posicion inicial de una nave
	 */
	public void restablecerPosicionInicial() {
		
		x = POSICION_X_DEFECTO;
		y = POSICION_Y_DEFECTO;
		
	}
	
	public boolean equals(Object o) {
		  if ( this == o ) return true;
		  if ( !(o instanceof NaveHumana) ) return false;
		  NaveHumana n1 = (NaveHumana)o;
		  return id.equals(n1.getId());
	}

	/**
	 * Metodo que indica si la nave ha perdido una vida
	 * @return
	 */
	public boolean naveMuerta() {
		return TOQUES < toquesRecibidos;
	}

	public void inicializar() {
		miNaveHumana = new NaveHumana(3);
	}
	
}
