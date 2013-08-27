package org.is.packdisparos;

public class DisparoHumano extends Disparo {

	public DisparoHumano(String pId, double pX, double pY) {
		//posicion x de la nave humana, y con una altura de 10, recordar, el 0,0 es la esquina superior izquierda de la pantalla
		super(pId, pX + 12, pY - 3); 
	}

	@Override
	public void mover() {
		y -= 5; //reducimos en 5 pixels la posicion Y del disparo humano
	}

	public boolean equals(Object o) {
		  if ( this == o ) return true;
		  if ( !(o instanceof DisparoHumano) ) return false;
		  DisparoHumano d1 = (DisparoHumano)o;
		  return id.equals(d1.id);
	}
}
