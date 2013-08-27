package org.is.packdisparos;

public class DisparoAlien extends Disparo {

	public DisparoAlien(String pId, double pX, double pY) {
		super(pId, pX+12, pY + 17);
	}

	@Override
	public void mover() {
		y += 8;
	}
	
	public boolean equals(Object o) {
		  if ( this == o ) return true;
		  if ( !(o instanceof DisparoAlien) ) return false;
		  DisparoAlien d1 = (DisparoAlien)o;
		  return id.equals(d1.id);
	}
}
