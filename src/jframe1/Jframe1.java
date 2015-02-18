/**
 * Juego JFrame
 * 
 * Juego donde Diddy el changuito pelea contra los fantasmas
 * 
 * @author Patricio Sanchez and David Benitez
 * @version 1.0
 * @date 2/18/2015
 */

package jframe1;
import java.applet.AudioClip;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;


public class Jframe1 extends JFrame implements Runnable, KeyListener{
    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maximo numero de personajes por alto
    private Base basMalo;        // Objeto malo
    private LinkedList <Base> llsFantasmas;   //Linkedlist de fantasmas
    private LinkedList <Base> llsJuanito;     //LinkedList Juanito
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    /* private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private AudioClip adcSonidoJuanito;  // Objeto sonido Juanito */
    
    /* Variables para las vidas y puntos */
    private int iVidas;
    private int iPuntos;
    private boolean bVivo;
    
    //Variable de Direcciones
    private int iDireccion;
    
    //Cantidad de fantasmas y juanitos
    private int iCantFantasmas;
    private int iCantJuanitos;
    
    //Cantidad de juanitos que han chocado con Nena
    private int iJuanitosChocados;
    
    private boolean bPause;    //Pausa
    
    //Objeto game over
    private Base basGameOver;
    
    public Jframe1 (){
        //Inicializando Vidas y Puntos
        iVidas = (int) ((Math.random()*3) + 3);
        iPuntos = 0;
        bVivo = true;
        
        //Inicializando Pausa
        bPause = false;
        
        //Direcciones (0-inmovil, 1-Arriba, 2-Abajo, 3-Izq, 4-Derecha)
        iDireccion = 0;
        
        URL urlImagenJuanito = this.getClass().getResource("juanito.gif");
        llsJuanito = new LinkedList <Base> ();
        
        iCantJuanitos = (int) (Math.random() * 6) + 10;
        
        for (int iI = 0; iI < iCantJuanitos; iI++){
            int iPosX = ((int)(Math.random()*(getWidth())));    
            int iPosY = -(int) (Math.random() *(getHeight()));
            Base basJuanito = new Base(iPosX,iPosY,getWidth()/iMAXANCHO,
            getHeight()/iMAXALTO,
            Toolkit.getDefaultToolkit().getImage(urlImagenJuanito));
            llsJuanito.add(basJuanito);
        }
        
        // defino la imagen del malo
	URL urlImagenMalo = this.getClass().getResource("diddy.gif");
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	basMalo = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
        
        //Creando los fantasmas
        URL urlImagenFantasma = this.getClass().getResource("fantasmita.gif");
        llsFantasmas = new LinkedList <Base> ();
        
        iCantFantasmas = (int) (Math.random() * 3) + 8;
        
        for (int iI = 0; iI < iCantFantasmas; iI++){
            iPosX = -((int)(Math.random()*(getWidth())));    
            iPosY = (int) (Math.random() *(getHeight()));
            Base basFantasma = new Base(iPosX,iPosY,getWidth()/iMAXANCHO,
            getHeight()/iMAXALTO,
            Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
            llsFantasmas.add(basFantasma);
        }
        
        /*Inicializacion de los Sonidos
        URL urlSonidoChimpy = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpy = getAudioClip (urlSonidoChimpy);
        
        URL urlSonidoJuanito = this.getClass().getResource("monkey1.wav");
        adcSonidoJuanito = getAudioClip (urlSonidoJuanito);
        */
        
        //Creando game over
        URL urlImagenGameOver = this.getClass().getResource("game-over.gif");
        
        basGameOver = new Base (200, 200, 750/2, 450/2, 
            Toolkit.getDefaultToolkit().getImage(urlImagenGameOver));
        
        //Inicializando el keylistener
        addKeyListener(this);
    }

    
    /**
     * Clase main del programa
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
    }

    @Override
    public void run() {
        
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        
    }
    
}
