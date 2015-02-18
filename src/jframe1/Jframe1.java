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
    private static final int iWidth = 800; //Tamaño
    private static final int iHeight = 500; //Tamaño
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
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (bVivo) {
            if(!bPause){
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        switch(iDireccion) {
            case 1: {
                basMalo.setY(basMalo.getY() - 3); 
                break;    //se mueve hacia arriba
            }
            case 2: {
                basMalo.setY(basMalo.getY() + 3);
                break;    //se mueve hacia abajo
            }
            case 3: {
                basMalo.setX(basMalo.getX() - 3);
                break;    //se mueve hacia izquierda
            }
            case 4: {
                basMalo.setX(basMalo.getX() + 3);
                break;    //se mueve hacia derecha	
            }
        }
        
        //Velocidad de los fantasmas de 3-5 pixeles a la derecha
        for(Base basFantasmita : llsFantasmas){
                basFantasmita.setX(basFantasmita.getX() + 
                                    ((int) (Math.random()* 3) + 3));
        }
        
        //Velocidad de Juanitos dependen de las vidas
        for(Base basJuanito : llsJuanito){
            if(iVidas != 0){
                basJuanito.setY(basJuanito.getY() + 5/iVidas);
            }
        }
        
        if (iVidas <= 0){
            bVivo = false;
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        switch(iDireccion){
            case 1: { //Revisa colision cuando sube
                    if (basMalo.getY() < 0) {
                            iDireccion = 0;
                            basMalo.setY(basMalo.getY() + 1);
                    }
                    break;    	
            }     
            case 2: { //Revisa colision cuando baja
                    if (basMalo.getY() + basMalo.getAlto() > getHeight()) {
                            iDireccion = 0;
                            basMalo.setY(basMalo.getY() - 1);
                    }
                    break;    	
            } 
            case 3: { //Revisa colision cuando va izquierda.
                    if (basMalo.getX() < 0) {
                            iDireccion = 0;
                            basMalo.setX(basMalo.getX() + 1);
                    }
                    break;    	
            }    
            case 4: { //Revisa colision cuando va derecha.
                    if (basMalo.getX() + basMalo.getAncho() > getWidth()) {
                            iDireccion = 0;
                            basMalo.setX(basMalo.getX() - 1);
                    }
                    break;    	
            }			
        }
        
        //Colisiones del Fantasma
        for (Base basFantasmita : llsFantasmas){
            if (basMalo.intersecta(basFantasmita)){
                iPuntos ++;
                //adcSonidoChimpy.play();
                basFantasmita.setX(-(int)(getWidth() * (Math.random())));
                basFantasmita.setY((int) (Math.random() *(getHeight())));
            }
        }

        for (Base basFantasmita : llsFantasmas){
            if (basFantasmita.getY() + basFantasmita.getAlto() > getHeight()){
                basFantasmita.setY(getHeight() - basFantasmita.getAlto());
            }

            if (basFantasmita.getX() >= getWidth()){
                basFantasmita.setX(-(int)(getWidth() * (Math.random())));
                basFantasmita.setY((int) (Math.random() *(getHeight())));
            }
        }
        
        //Colisiones de Juanito
        for (Base basJuanito : llsJuanito){
            if (basMalo.intersecta(basJuanito)){
                iJuanitosChocados ++;
                //adcSonidoJuanito.play();
                basJuanito.setX((int)(getWidth() * (Math.random())));
                basJuanito.setY(-(int) (Math.random() *(getHeight())));
            }
        }

        for (Base basJuanito : llsJuanito){
            if (basJuanito.getX() + basJuanito.getAncho() > getWidth()){
                basJuanito.setX(getWidth() - basJuanito.getAncho());
            }
            
            if(basJuanito.getX() <= 0){
                basJuanito.setX(basJuanito.getX() + basJuanito.getAncho());
            }

            if (basJuanito.getY() >= getHeight()){
                basJuanito.setX((int)(getWidth() * (Math.random())) + 1);
                basJuanito.setY((int) (Math.random()*getHeight() - (getHeight())));
            }
        }
        
        if (iJuanitosChocados >= 5){
            iVidas --;
            iJuanitosChocados = 0;
        }
    }
    
        /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint1
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint1 (Graphics graDibujo) {
        // si la imagen ya se cargo
        if (llsJuanito != null && basMalo != null && llsFantasmas != null) {
            if (bVivo){
                //Dibuja la imagen de principal en el Applet
                for(Base basJuanito : llsJuanito){
                        basJuanito.paint(graDibujo, this);
                }
                //Dibuja la imagen de malo en el Applet
                basMalo.paint(graDibujo, this);
                //Dibujando los fantasmas
                for(Base basFantasma : llsFantasmas){
                        basFantasma.paint(graDibujo, this);
                }
                //Puntos y vidas desplegados en la esquina superior izquierda
                graDibujo.setColor(Color.red);
                graDibujo.drawString("Puntos: " + iPuntos, 15, 15);
                graDibujo.drawString ("Vidas: " + iVidas, 15, 25);
            }
            else{
                basGameOver.paint(graDibujo, this);
            }
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_W) {    //Presiono flecha arriba
            iDireccion = 1;
        } else if (ke.getKeyCode() == KeyEvent.VK_S) {    //Presiono flecha abajo
            iDireccion = 2;
        } else if (ke.getKeyCode() == KeyEvent.VK_A) {    //Presiono flecha izquierda
            iDireccion = 3;
        } else if (ke.getKeyCode() == KeyEvent.VK_D) {    //Presiono flecha derecha
            iDireccion = 4;
        }
        
        //Al presionar ESC se sale del juego
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE){
            bVivo = false;
        }
        
        //Al presionar P se pausa el juego
        if (ke.getKeyCode() == KeyEvent.VK_P){
            bPause = !bPause;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        
    }
    
    /**
     * Clase main del programa
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Jframe1 juego = new Jframe1();
        juego.setSize(iWidth, iHeight);
        juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juego.setVisible(true);
    }
    
}
