/**
 * EHUSpaceInvaders Creado por:
 * 
 * 	-Ruben Agudo Santos
 * 	-Jon Ander Fontán Valdelvira
 * 	-Jonatan Galean Sanchez
 *  
 */

package org.is.ehuspaceinvaders;

import jgame.*;
import jgame.platform.*;

import java.awt.Canvas;
import java.io.File;
import java.util.Random;
import java.util.StringTokenizer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.is.packlistas.ListaDisparos;
import org.is.packlistas.ListaNaves;
import org.is.packnaves.NaveHumana;
import org.is.simplelinkedlist.SimpleLinkedList;

public class ehuSpaceInvaders extends JGEngine {

	private static final long serialVersionUID = -5552193475679427072L;
	//int movimiento = -1;
	//int frecuencia=5;
	Clip sonidoDisparo;
	Clip sonidoExplosion;
	Clip sonidoGanado;
	boolean enemigoDerrotado = false;
	boolean naveHumanaTocada = false;
	boolean navesAlienEnTierra = false;
	boolean navealientocada = false;
	int xNaveAlienTocada;
	int yNaveAlienTocada;
	int puntuacionNaveAlienTocada;
	int vidasNaveFinal;
	int fase=0;
	int puntuacion=0;
	int framesPasados=0;
	SimpleLinkedList<String> listaImpactos=null;
	SimpleLinkedList<String> listaNavesAlienAux;
	SimpleLinkedList<String> listaDisparosAux;
	int finjuego=0;
	boolean juegoEmpezado=false;
	boolean juegoEnPausa=false;
	String naveQueDispara=null;
	int posix,posiy;
	
	/**
	 * Main de la aplicacion 
	 */
	public static void main(String [] args) {
		new ehuSpaceInvaders(new JGPoint(800,600));

	}

	
	/**
	 * Constructor de la aplicacion 
	 */
	public ehuSpaceInvaders(JGPoint size) { initEngine(size.x,size.y); }
	
	
	/** 
	 * Constructor del applet
	 */
	public ehuSpaceInvaders() { initEngineApplet(); }

	public void initCanvas() { setCanvasSettings(20,15,40,40,null,null,null); }

	
	/**
	 * Inicializacion del Juego 
	 */
	public void initGame() {
		defineImage("bgimage","-",0,"fondo800x600.jpg","-",0,0,800,600); //Definicion del fondo
		setBGImage("bgimage");//Insercion del fondo


		try {	         
	         // Obtencion de los distintos Clip de sonido
	         sonidoDisparo = AudioSystem.getClip();
	         sonidoExplosion = AudioSystem.getClip();
	         sonidoGanado = AudioSystem.getClip();
	         // Se carga con un ficheros wav
	         sonidoDisparo.open(AudioSystem.getAudioInputStream(new File("src/shot.wav")));
	         sonidoExplosion.open(AudioSystem.getAudioInputStream(new File("src/hit.wav")));
	         sonidoGanado.open(AudioSystem.getAudioInputStream(new File("src/start.wav")));
	         Thread.sleep(500);
	     	} catch (Exception e) {
	     		System.out.println("" + e);
	     	}
		
		defineMedia("mygame.tbl");//carga del fichero que lee las imagenes del juego.
		setFrameRate(60,0);//Ponemos los frames del juego a 60 fps.
		//Creamos las naves
		crearNavesInterface();
	}
	
	private void crearNavesInterface(){
		int i,j,x,y;
		i=0;
		j=0;
		x=390;
		y=500;
		
		new Player(x,y,0);
		NaveHumana.getMiNaveHumana().restablecerPosicionInicial();
		x=75;
		y=50;
		int tipo=0;
		String TipoMunieco = null;
		
		for(i=0;i<ListaNaves.NUM_FILAS;i++){
		
			for(j=0;j<ListaNaves.NUM_COLUMNAS;j++){
					
				if (tipo==0){
						TipoMunieco="invader31";
				}
				else{
					if(tipo==1){
						TipoMunieco="invader21";
					}
					else{
						if(tipo==2){
							TipoMunieco="invader11";
						}
					}
				}
				new Enemigo(-100,-100,i,j,TipoMunieco,tipo);//Creamos un enemigo con un tipo de imagen cada 2 lineas.

				x=x+48;
			
			}//Fin del for que crea las distintas columnas de cada fila
			
			if (i!=0 && i%2!=0){//if para cambiar el tipo de imagen del enemigo cada 2 lineas de enemigos
				if (tipo<2){
					tipo++;
				}	
				else{
					tipo=0;					
				}

			}
			x=75;
			y=y+48;
		}//Fin del for que crea las distintas filas
	}
	
	/**
	 * Acciones a realizar por cada frame
	 */
	public void doFrame() {
		
		if(juegoEmpezado){
			if(!juegoEnPausa){
				if (getKey(KeyEsc)){//si se a pulsado la tecla Esc pongo el juego en pausa
					juegoEnPausa=true;
					clearKey(KeyEsc);
				}
				if(finjuego==0){//si no es fin de juego[0 -> juego normal; 1 -> Game Ovcer; 2 -> Invasion Rechazada]
					framesPasados++;
					if(!naveHumanaTocada){//si la nave humana no ha perdido una vida
						ListaNaves.getMiListaNaves().desplazarEjercitoAmenazador();//Desplazamos el ejercito de naves Alien/Nave Nodriza.
						listaNavesAlienAux = ListaNaves.getMiListaNaves().obtenerDatosNaves();//Obtenemos la lista de todos los datos de las naves para actualizar posiciones
						naveQueDispara=ListaNaves.getMiListaNaves().disparar();//Obtenemos la nave que dispara en cada frame.
						ListaDisparos.getMiListaDisparos().desplazarDisparos();//Desplazamos los posibles disparos.
					}
					listaDisparosAux=ListaDisparos.getMiListaDisparos().obtenerDatosDisparos();//Obtenemos datos de disparos para moverlos por la pantalla
					listaImpactos = ListaDisparos.getMiListaDisparos().comprobarImpactos();//COmprobar todos los impactos con los distintos elementos existentes.
					
					if(ListaNaves.getMiListaNaves().tierraInvadida()) {
						finjuego = 3;
					}
					
					moveObjects(//mover todos los objetos del JGame (JGObject)
						null,//nombre de los elementos que se moveran (null todos)
						0
					);
					if(ListaNaves.getMiListaNaves().estaVacia() && !enemigoDerrotado){//Si todas las naves han sido derrotadas...
						fase = 1;
						new EnemigoFinal(390,-200);//Creamos el enemigo final
					}
				}
				else{//si finjuego!=0
					if (getKey(KeyCtrl)){//si se a pulsado la tecla Control se reinicia el juego
						removeObjects(null, 1);
						fase=0;
						crearNavesInterface();
						ListaNaves.getMiListaNaves().inicializar();
						ListaDisparos.getMiListaDisparos().inicializar();
						NaveHumana.getMiNaveHumana().inicializar();

						finjuego=0;
					}
				}
			}//fin if juego en pausa
			else{
				if (getKey(KeyEsc)){//si se a pulsado la tecla Escape
					juegoEnPausa=false;
					clearKey(KeyEsc);
				}
			}
		}//fin if juego empezado
		else{//si juegoEmpezado=false;
			if (getKey(KeyCtrl)){//si se a pulsado la tecla Control
				juegoEmpezado=true;
			}
		}
	}

	
	/**
	 *  Este metodo dibujara los distintos textos en cada frame.
	 *
	 *  	-Puntuacion, vidas, escudos,....
	 */
	public void paintFrame() {
		setColor(JGColor.white);
		drawString("Puntuacion: "+ NaveHumana.getMiNaveHumana().getPuntuacionAcumulada(),5, 5, -1);//Mostramos la puntuacion del juego (Texto, PosX, PosY, align)
		drawString("Numero de Vidas: " + NaveHumana.getMiNaveHumana().getVidas(),100, 580, 0); //Mostramos las vidas restantes a la nave.
		drawString("Energia de los escudos: " + NaveHumana.getMiNaveHumana().getToques(),680, 580, 0); //Mostramos los escudos restantes a la nave
		
		setColor(JGColor.red);//Definir color rojo para el texto
		
		
		
		if(ListaNaves.getMiListaNaves().estaVacia()){
			drawString("Energia Enemiga: ", 300, 5, 0);
			for (int i = 0; i < vidasNaveFinal; i++) {
				drawRect(400+19*i, 5, 20, 20, true, false);
			}
		}
		
		if(juegoEnPausa){
			drawString("PULSE ESCAPE PARA REANUDAR LA PARTIDA",400, 400, 0);//Mostramos El texto game over en mitad de la pantalla
		}
		
		if (!juegoEmpezado){
			drawString("Utilice las teclas de direccion Izquierda/Derecha para desplazar la nave",400, 100, 0);//Mostramos El texto game over en mitad de la pantalla
			drawString("Utilice la tecla Control para disparar",400, 150, 0);//Mostramos El texto game over en mitad de la pantalla
			drawString("Utilice la tecla Escape para Pausar/Reanudar la partida",400, 200, 0);//Mostramos El texto game over en mitad de la pantalla
			drawString("Desarrollado por Rubén Agudo, Jon Ander Fontan y Jonatan Galean",400, 250, 0);//Mostramos a los creadores del juego
			drawString("PULSE CONTROL PARA INICIAR LA PARTIDA",400, 400, 0);//Mostramos El texto game over en mitad de la pantalla
		}
		
		
		if (finjuego==1){//Si es Game Over
			drawString("GAME OVER",400, 300, 0);//Mostramos El texto game over en mitad de la pantalla
			drawString("PULSE CONTROL PARA REINICIAR",400, 400, 0);//Mostramos El texto game over en mitad de la pantalla
		}
		
		else if(finjuego==2){//si se ha rechazado la invasion
			drawString("Invasion Rechazada!!",400, 300, 0);	//mostramos el texto Invasion Rechazada en mitad de la pantalla
			//reproducirSonidoGanar();
		}
		
		else if(finjuego==3){//si se ha rechazado la invasion
			drawString("La tierra ha sido Invadida!!!",400, 300, 0);	//mostramos el texto Invasion Rechazada en mitad de la pantalla
		}		
		
		if(navealientocada){//Navealientocada es una variable que sera true siempre que una nave alien sea destruida con esto mostraremos su puntuacion.
			Random colorAleatorio = new Random();
			int numColor=colorAleatorio.nextInt(10);
			numColor++;
			//utilizamos un random para que en cada frame el texto tenga un color distinto.
			switch (numColor) {
			case 1:
				setColor(JGColor.blue);
				break;
			case 2:
				setColor(JGColor.cyan);
				break;
			case 3:
				setColor(JGColor.green);
				break;
			case 4:
				setColor(JGColor.magenta);
				break;
			case 5:
				setColor(JGColor.orange);
				break;
			case 6:
				setColor(JGColor.pink);
				break;
			case 7:
				setColor(JGColor.red);
				break;
			case 8:
				setColor(JGColor.white);
				break;
			case 9:
				setColor(JGColor.yellow);
				break;
				
			case 10:
				setColor(JGColor.white);
				break;
			default:
				break;
			}
			drawString("" + puntuacionNaveAlienTocada ,xNaveAlienTocada+40, yNaveAlienTocada, 0);//mostramos la puntuacion de la nave destruida en varios colores aleatorios.
		}
	}
	
	
	/**
	 * Clase que representa en el tablero grafico la bala disparada por la nave Humana. 
	 */
	public class BalaAliada extends JGObject{
		String ID ="";//ID unico de cada bala.
		double yAntigua;//variable usada para eliminar la bala de la interface en caso de que no se mueva (Halla salido de la pantalla)
		boolean enc=false;//Variable usada para ver si se encuentra la bala en toda la lista;
		int framesParaDestruir=0;
		
		public BalaAliada(double x, double y,String IDNave){//Constructora de bala aliada
			super("balaAliada",true,x,y,1,"balaaliada",0,0,-5,-5,-2);//Creamos la bala y le damos sus imagenes y posiciones correspondientes
			int creadorUnico = NaveHumana.getMiNaveHumana().getCreadorDisparoUnico()-1;
			ID = IDNave+creadorUnico;//Asignamos el ID Unico
			//NaveHumana.getMiNaveHumana().disparar();//disparamos una vala en la logica de diseño	
			yAntigua=y;//asignamos valor a yAntigua
		}
		
		public void move(){//Metodo que movera la bala en cada doframe
			enc=false;//La bala no ha sido aun encontrada
			listaDisparosAux.goFirst();//me posiciono en el inicio de la lista Disparos
			
			while(listaDisparosAux.hasNext() && !enc) {//mientras halla disparos en la lista y la bala no haya sido encontrada
				StringTokenizer st = new StringTokenizer(listaDisparosAux.get(),";");//lista Disparos tiene elementos del tipo "ID;PosX;Posy"
				String id = st.nextToken();//cojo el elemento ID
				
				if(id.equals(ID)) {//Si el id de la lista es igual al ID de la Bala
					enc=true;//se ha encontrado la bala
					this.x = Double.parseDouble(st.nextToken());//actualizo PosX
					this.y = Double.parseDouble(st.nextToken());//Actualizo PosY
				}
				
				listaDisparosAux.goNext();//Ir al siguiente elemento de la lista.
			}//Fin del While que recorre la lista
			
			if (yAntigua==y){//si la y es igual que la antigua y (No se ha movido, ya por que se haya salido o haya impactado)
				framesParaDestruir++;
				if (framesParaDestruir>4){
					this.remove();//Eliminamos la vala del tablero grafico
				}
			}
			else{//si la bala aun se mueve
				yAntigua=y;//actualizamos el valor de yAntigua
				framesParaDestruir=0;
			}
		}//Fin del metodo move()
	}//Fin de la clase BalaAliada
	
	
	/**
	 * Clase que representa en el tablero grafico las balas disparadas por las Naves Alien. 
	 */
	public class BalaEnemiga extends JGObject{
		String ID ="";//ID unico de cada bala.
		double yAntigua;//variable usada para eliminar la bala de la interface en caso de que no se mueva (Halla salido de la pantalla)
		boolean enc=false;//Variable usada para ver si se encuentra la bala en toda la lista;
		int framesParaDestruir=0;
		
		public BalaEnemiga(double x, double y,String IDNave){//Constructora de bala aliada
			super("balaEnemiga",true,x,y,1,"balaenemiga",0,0,5,5,-2);//Creamos la bala y le damos sus imagenes y posiciones correspondientes.
			StringTokenizer stNave = new StringTokenizer(naveQueDispara,";");
			ID = "" + stNave.nextToken();
			ID = "" + ID + stNave.nextToken();//Asignamos el ID Unico formado por "IDNave+IDDisparoUnico" para saber que bala corresponde a que disparo
			yAntigua=y;//asignamos valor a yAntigua

		}
		public void move(){//Metodo que movera la bala en cada doframe 
			enc=false;//La bala no ha sido encontrada
			listaDisparosAux.goFirst();//Nos posicionamos al inicio de la lista.
			
			while(listaDisparosAux.hasNext() && !enc) {//Mientras haya balas en la lista y la bala no haya sido encontrada.
				StringTokenizer st = new StringTokenizer(listaDisparosAux.get(),";");
				String id = st.nextToken();
				
				if(id.equals(ID)) {//Si el id de la lista es igual al ID de la Bala
					enc=true;//se ha encontrado la bala
					this.x = Double.parseDouble(st.nextToken());//actualizo PosX
					this.y = Double.parseDouble(st.nextToken());//Actualizo PosY
				}
				
				listaDisparosAux.goNext();//Ir al siguiente elemento de la lista.
			}//Fin del while que recorre la lista
			
			if (yAntigua==y){//si la y es igual que la antigua y (No se ha movido, ya por que se haya salido o haya impactado)
				framesParaDestruir++;
				if (framesParaDestruir>4){
					this.remove();//Eliminamos la vala del tablero grafico
				}			}
			else{//Si la bala aun se mueve
				yAntigua=y;//Actualizamos el valor de yAntigua
				framesParaDestruir=0;
			}
		}//Fin del metodo move()
	}//Fin de la clase BalaEnemiga
	
	
	/**
	 *Clase que representa a la Nave Aliada (Jugador) en la interface grafica
	 */
	public class Player extends JGObject {
		String ID = "";//ID unico del la nave
		boolean muerta=false;
		int framesPasadosDesdeexplotado=0;//Frames que han pasado desde que la nave ha sido impactada
		int framesPasadosDesdeDisparo=0;//Frames que han pasado desde que la nave ha disparado
		
		public Player(double x,double y,double speed) {//Constructora de NaveHumana
			super("Player",true,x,y,1,"navehumana", 0,0,speed,speed,-1);//Asignamos a la nave Humana si imagen y sus posicion en pantalla correspondiente
			ID="Player";//Le asignamos el ID Unico de la nave Humana
		}//Fin de Constructora
		
		public void hit(JGObject obj) {//Metodo que se le llamara cuando la nave humana haya sido golpeada
				if (NaveHumana.getMiNaveHumana().getToques()==NaveHumana.TOQUES){//si los escudos se han gastado
					this.setGraphic("explosion");//Muestro Sprite de explosion
					reproducirSonidoExplosion();//Reproduzco Sonido de explosion.
					naveHumanaTocada=true;//Indico que la nave ha sido tocada
					ListaDisparos.getMiListaDisparos().inicializar();//Elimino todas las balas del tablero
				}
				if (NaveHumana.getMiNaveHumana().gameOver()){//Si el jugador no tien mas vidas
					this.setGraphic("explosion");//Muestro el sprite de explosion
					reproducirSonidoExplosion();//Reproduzco sonido de explosion
					reproducirSonidoExplosion();//x2
					naveHumanaTocada=true;//Indico que la nave humana ha sido tocada
					ListaDisparos.getMiListaDisparos().inicializar();//Elimino todas las balas del tablero
					
				}
		}//Fin del metodo hit()
		
		public void move() {//Metodo que se le llamara por cada doFrame y que movera la nave por el tablero
			framesPasadosDesdeDisparo=framesPasadosDesdeDisparo+1;//aumento los frames pasados desde el ultimo disparo
			if(listaImpactos.find(ID)){//Si la nave se encuentra dentro de la lista de naves impactadas este frame
				this.hit(null);//llamo al metodo hit
			}
			else{//sino ha sido impactada.
				if(!naveHumanaTocada){//si la nave NO ha sido tocada con anterioridad 
					if(!NaveHumana.getMiNaveHumana().gameOver()){//si la nave NO esta muerta
						if(fase!=1){//fase=1 la nave final esta saliendo
							x=NaveHumana.getMiNaveHumana().getX();//cojo la posicion de X de la nave y la actualizo
	
							if (getKey(KeyRight)){//si se a pulsado la tecla Derecha
								if (x<763){//si la nave esta dentro del borde derecho de la pantalla
									NaveHumana.getMiNaveHumana().mover(1);//le indico a la nave de la logica que se mueva
									x=NaveHumana.getMiNaveHumana().getX();//le pido la nueva posicion de la x 
									posix=(int) x;//actualizo la PosX de la nave del tablero
								}
							}//fin del if de pulsado tecla derecha
								
							if (getKey(KeyLeft)){//si se ha pulsado la tecla Izquierda
								if (x>0){//si la nave esta dentro del borde izquierdo de la pantalla
									NaveHumana.getMiNaveHumana().mover(-1);//le indico a la nave de la logica que se mueva
									x=NaveHumana.getMiNaveHumana().getX();//le pido la nueva posicion de la x 
									posix=(int) x;//actualizo la PosX de la nave del tablero
								}
							}//fin del if de pulsado tecla izquierda
				
							if (getKey(KeyCtrl)) {//si se ha pulsado la tecla Control
								
								
								if(ListaDisparos.getMiListaDisparos().obtenerNumeroDisparosHumanos() < ListaDisparos.MAX_DISPAROS_HUMANOS) {
								
									if (framesPasadosDesdeDisparo >= 25){//si han pasado mas de 50 frames desde el ultimo disparo
										//if (countObjects("BalaAliada",0)<3){//si no hay mas de 3 balas en el Terreno
										framesPasadosDesdeDisparo=0;//Reseteo los frames desde el ultimo disparo
										NaveHumana.getMiNaveHumana().disparar();
										//new JGObject("BalaAliada",true,x+12,y-8,4,"balaaliada", 0,-5, -2);
										new BalaAliada(this.x+12, this.y-3, this.ID);//creo una nueva bala aliada en la posicion correspondiente
										
										reproducirSonidoDisparo(); //Reproduzco el sonido de disparo
										//}
									}
								
								}
									
								clearKey(KeyCtrl);//Libero del Bufer la recla que tenga pulsado (Para evitar 2 pulsaciones inmediatas)
							}//fin del if de pulsado tecla Control
						}//Fin del if !Fase=2
					}//Fin del if !muerta
						
					else{//si el jugador ha muerto
						this.remove();//elimino la nave 
						finjuego=1;
					}
				}
				else{// si la nave ha sido tocada
					if(framesPasadosDesdeexplotado<=100){//si NO han pasado 100 frames desde que ha explotado
						framesPasadosDesdeexplotado++;//aumento ese contador de frames
					}
					else{//si han pasado 100 frames desde que ha explotado
						framesPasadosDesdeexplotado=0;//reinicio el contador
						naveHumanaTocada=false;//la nave ya no estaria todaca
						if(!NaveHumana.getMiNaveHumana().gameOver()){//si la nave no ha muerto (es decir tocada pero no muerta aun le quedan vidas)
							NaveHumana.getMiNaveHumana().restablecerPosicionInicial();//restauro la posicion inicial de las naves
							this.setGraphic("navehumana");//cambio la imagen de la explosion por la de la nave humana nuevamente.
						}
					}
				}
			}
		}//Fin del metodo move();
	}//Fin de la Clase Player
		
	
	/**
	 *Clase que representa las distintas naves alien en el interface Grafica
	 */
	public class Enemigo extends JGObject {
		int frecuencia=10;//marcara la frecuencia con la que los aliens cambian de imagen para dar sensacion de movimiento
		int movimiento=-1;//Direccion de la nave enemiga (1 Der; -1 Izq)
		boolean muerto=false;//Variable que nos dira si la nave Enemiga ha muerto
		int framesmuerto=0;//Frames pasados desde que la nave Enemiga muere
		int puntuacion=0;//Puntuacion de la nave Enemiga
		boolean grafico=false;//booleana que indicara que grafico tiene de los 2 posibles de cada tipo
		int tipo;//Tipo de imagen que tendra la nave
		String ID;//ID unico de la nave
		
		public Enemigo(double x,double y,int i,int j,String pTipoMunieco,int pTipo) {//constructora de la nave Enemiga
			super(""+i+j,true,x,y,1,pTipoMunieco,0, 0, -2 );//Creamos la nave Enemiga con su Imagen y su posicion en la interface grafica
			ID=""+i+j;//asignamos el ID unico
			tipo=pTipo;//Declaramos de que tipo es el alien de los 3 posibles
		}//Fin de la constructora
		
		public void move() {//metodo que movera las naves Alien en cada DoFrame
			if(listaImpactos.find(ID)){//Si la nave ha sido impactada este frame
				this.hit(null);//llamamos al metodo hit
			}
			else{//si la nave no ha sido impactada este frame
				if(!muerto){//si la nave no esta muerta
					if(framesPasados%frecuencia==0){//comprobacion para cambiar la imagen dependiendo del tipo y de la imagen activa
						if (tipo==0){
							if(grafico==true){
								this.setGraphic("invader32");
								grafico=false;
							}
							else{
								this.setGraphic("invader31");
								grafico=true;
							}
						}
						else{
							if(tipo==1){
								if(grafico==true){
									this.setGraphic("invader22");
									grafico=false;
								}
								else{
									this.setGraphic("invader21");
									grafico=true;
								}
							}
							else{
								if(tipo==2){
									if(grafico==true){
										this.setGraphic("invader12");
										grafico=false;
									}
									else{
										this.setGraphic("invader11");
										grafico=true;
									}
								}
							}
						}
					}	
					
					//actualizacion de posiciones de las naves Alien
					listaNavesAlienAux.goFirst();//me posiciono al comienzo de las
					Boolean enc=false;
					
					while(listaNavesAlienAux.hasNext() && !enc) {//mientras haya elementos en la lista de naves y no haya sido encontrada
						StringTokenizer st = new StringTokenizer(listaNavesAlienAux.get(),";");
						String id = st.nextToken();
							
						if(id.equals(ID)) {//si el id de la nave de la lisgta es el mismo que el de este nave
							enc=true;//marco la nave como encontrada
							this.x = Double.parseDouble(st.nextToken());//actualizo PosX
							this.y = Double.parseDouble(st.nextToken());//actualizo PosY
							this.puntuacion= Integer.parseInt(st.nextToken());//actualizo la puntuacion de la nave
						}
							
						listaNavesAlienAux.goNext();
					}//Fin del qhile que recorre la lista de naves
					
					if(!naveQueDispara.equals("")){//si hay alguna nave que ha disparado
						StringTokenizer stNave = new StringTokenizer(naveQueDispara,";");
						String idNave = stNave.nextToken();
						if (this.ID.equals(idNave)){//si la nave que dispara es esta
							new BalaEnemiga(this.x+48, this.y+96,this.ID);//creo una nueva bala
						}
					}
				}//fin del if !muerta
				else{//si esta muerta
					//actualizo las variables globales que mostraran la puntuacion de la variable muerta
					xNaveAlienTocada = (int) this.x;//Actualizo el valor de X
					yNaveAlienTocada = (int) this.y;//Actualizo el Valor de Y
					puntuacionNaveAlienTocada=this.puntuacion;//Actualizo la puntuacion
					navealientocada=true;//variable que indica si hay alguna nave alien tocada para mostrar su puntuacion queraria activada
					framesmuerto++;	//incremento los frames muerto;
					if (framesmuerto>=17){//si ha muerto hace mas de 16 frames
						remove();//elimino la nave de la interface grafica
						navealientocada=false;//dejara de mostrar la puntuacion de la nave en pantalla
					}
				}
			}
		}//fin del metodo Move
		
		public void hit(JGObject o) {//metodo llamado si la nave ha sido tocada
			this.setGraphic("explosion");//Cambio la imagen por la de la explosion
			reproducirSonidoExplosion();//se reproduce el sonido de la explosion
			muerto=true;//Nave Alien estaria Muerta
		}//Fin del metodo hit()
	}//Fin de la clase Enemigo
	

	/**
	 * Clase que representa a la nave Nodriza en la interface grafica
	 */
	public class EnemigoFinal extends JGObject {
		boolean muerto=false;//booleana que indicara si el Enemigo final esta muetro
		int framesmuerto=0;//frames desde que ha muerto el enemigo final
		int puntuacion=0;//Puntuacion correspondiente al enemigo final
		boolean grafico=false;
		String ID="Final";//ID que servira para identificar el Enemigo Final
	
		public EnemigoFinal(double x,double y) {//Constructora de la clase EnemigoFinal
			super("Final",true,x,y,1,"final2",0, 0, -2 );//creo el enemigo FInal en su posicion y con su imagen
		}//Fin de la Constructora;
		
		public void move() {//Metodo que mueve la nave Enemiga y que se le llamara en el el doFrame
			if(listaImpactos.find(ID)){//si la nave EnemigaFinal esta en la lista de naves impactadas
				this.hit(null);//llamo al metodo hit
			}
			else{
				if(!muerto){//si el EnemigoFinal NO esta muerto
					
					listaNavesAlienAux.goFirst();//me posiciono al inicio de la lista de naves existentes
					boolean enc=false;//La nave aun no ha sido encontrada en la lista
					
					while(listaNavesAlienAux.hasNext() && !enc) {//si hay mas naves en la lista y no se ha encontrado la nave EnemigoFinal
						StringTokenizer st = new StringTokenizer(listaNavesAlienAux.get(),";");
						String id = st.nextToken();
							
						if(id.equals(ID)) {//si la nave es la siguiente de la lista
							enc=true;//marco que ha sido encontrada
							this.x = Double.parseDouble(st.nextToken());//actualizo el valor de X
							this.y = Double.parseDouble(st.nextToken());//Actualizo el valor de y
							this.puntuacion= Integer.parseInt(st.nextToken());//Actualizo el valor de la puntuacion
							vidasNaveFinal=Integer.parseInt(st.nextToken());
						}
							
						if(y>=49){//si la nave Enemigo final ha dejado de salir
							fase=0;//habilito el movimiento de la navehumana
						}
						listaNavesAlienAux.goNext();//me muevo a la siguiente nave de la lista
					}//Fin del while que recorre la lista
								
					if(!naveQueDispara.equals("")){//si hay una nave que dispara
						StringTokenizer stNave = new StringTokenizer(naveQueDispara,";");
						String idNave = stNave.nextToken();
						if (this.ID.equals(idNave)){//si la n ave que dispara es EnemigoFinal
							new BalaEnemiga(this.x, this.y,this.ID);//creo una nueva bala en la posicion correspondiente
						}
					}
				}//fin del if !muerto
				else{//si esta muerta
					//actualizo las variables globales que mostraran la puntuacion de la variable muerta
					xNaveAlienTocada = (int) this.x;//Actualizo el valor de X
					yNaveAlienTocada = (int) this.y;//Actualizo el Valor de Y
					puntuacionNaveAlienTocada=this.puntuacion;//Actualizo la puntuacion
					navealientocada=true;//indico que la nave ha sido tocada para poner su puntuacion+
					framesmuerto++;	//incremento los frames muerto;
					
					if (framesmuerto==33){//si hace 33 frames que ha muerto
						remove();//elimino la nave Enemigo Final
						navealientocada=false;//elimino la variable gloval para que no muestre mas su puntuacion
						finjuego=2;//fin del juego con mensaje invasion rechazada
					}
				}
			}
		}//Fin del metodo move()
	
		public void hit(JGObject o) {
			if(ListaNaves.getMiListaNaves().juegoGanado()){//juegoGanado devuelve un true si la nave final ha sido derrotada
				this.setGraphic("explosiong");//cambio la imagen de la nave final por una explosion
				reproducirSonidoGanar();//reproduzco el sonido de la victoria
				muerto=true;//Indico que la nave EnemigoFinal ha muerto
			}
		}//Fin del metodo hit()
	}//Fin de la calse Enemigo Final


	private void reproducirSonidoDisparo(){//Metodo que reproduce el sonido del disparo
		// Comienza la reproducción
		sonidoDisparo.start();
		try {
	         
	         // Se obtiene el Clip de sonido para la proxima reproduccion
	         sonidoDisparo = AudioSystem.getClip();
	         
	         // Se carga con el fichero wav para la proxima reproduccion
	         sonidoDisparo.open(AudioSystem.getAudioInputStream(new File("src/shot.wav")));
	         
	         Thread.sleep(10);
	         
		} catch (Exception e) {
	         System.out.println("" + e);
		}
	}

	private void reproducirSonidoExplosion(){//Metodo que reproduce el sonido de los toques
		// Comienza la reproducción
		sonidoExplosion.start();
		try {
	         
			// Se obtiene el Clip de sonido para la proxima reproduccion
			sonidoExplosion = AudioSystem.getClip();
	         
			// Se carga con el fichero wav para la proxima reproduccion
			sonidoExplosion.open(AudioSystem.getAudioInputStream(new File("src/hit.wav")));
	         
			Thread.sleep(10);
	         
		} catch (Exception e) {
				System.out.println("" + e);
		}
	}
	
	private void reproducirSonidoGanar(){//Metodo que reproduce el sonido de la Victoria
		// Comienza la reproducción
		sonidoGanado.start();
	}
	
}//fin de EHUSpaceInvaders
