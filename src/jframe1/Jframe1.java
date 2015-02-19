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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Jframe1 extends JFrame implements Runnable, KeyListener{
    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maximo numero de personajes por alto
    private static final int iWidth = 800; //ancho de la pantalla
    private static final int iHeight = 500; //alto de la pantalla
    private Base basMalo;        // Objeto malo
    private LinkedList <Base> llsFantasmas;   //Linkedlist de fantasmas
    private LinkedList <Base> llsJuanito;     //LinkedList Juanito
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private SoundClip sndSonidoChimpy;   // Objeto sonido de Chimpy
    private SoundClip sndSonidoJuanito;  // Objeto sonido Juanito */
    
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
            int iPosX = ((int)(Math.random()*(iWidth)));    
            int iPosY = -(int) (Math.random() *(iHeight));
            Base basJuanito = new Base(iPosX,iPosY,iWidth/iMAXANCHO,
            iHeight/iMAXALTO,
            Toolkit.getDefaultToolkit().getImage(urlImagenJuanito));
            llsJuanito.add(basJuanito);
        }
        
        // defino la imagen del malo
	URL urlImagenMalo = this.getClass().getResource("diddy.gif");
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * iWidth / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * iHeight / iMAXALTO;        
	basMalo = new Base(iPosX,iPosY, iWidth / iMAXANCHO,
                iHeight / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
        
        //Creando los fantasmas
        URL urlImagenFantasma = this.getClass().getResource("fantasmita.gif");
        llsFantasmas = new LinkedList <Base> ();
        
        iCantFantasmas = (int) (Math.random() * 3) + 8;
        
        for (int iI = 0; iI < iCantFantasmas; iI++){
            iPosX = -((int)(Math.random()*(iWidth)));    
            iPosY = (int) (Math.random() *(iHeight));
            Base basFantasma = new Base(iPosX,iPosY,iWidth/iMAXANCHO,
            iHeight/iMAXALTO,
            Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
            llsFantasmas.add(basFantasma);
        }
        

        sndSonidoChimpy = new SoundClip("monkey2.wav");
        
        sndSonidoJuanito = new SoundClip("monkey1.wav");
        
        //Creando game over
        URL urlImagenGameOver = this.getClass().getResource("game-over.gif");
        
        basGameOver = new Base (200, 200, 750/2, 450/2, 
            Toolkit.getDefaultToolkit().getImage(urlImagenGameOver));
        
        //Inicializando el keylistener
        addKeyListener(this);
        
        //Inicializacion del Hilo
        Thread th = new Thread (this);
        th.start();
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
                basJuanito.setY(basJuanito.getY() + 6 - iVidas);
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
                    if (basMalo.getY() < 18) {
                            iDireccion = 0;
                            basMalo.setY(basMalo.getY() + 19);
                    }
                    break;    	
            }     
            case 2: { //Revisa colision cuando baja
                    if (basMalo.getY() + basMalo.getAlto() > iHeight) {
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
                    if (basMalo.getX() + basMalo.getAncho() > iWidth) {
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
                sndSonidoChimpy.play();
                basFantasmita.setX(-(int)(iWidth * (Math.random())));
                basFantasmita.setY((int) (Math.random() *(iHeight)));
            }
        }

        for (Base basFantasmita : llsFantasmas){
            if (basFantasmita.getY() + basFantasmita.getAlto() > iHeight){
                basFantasmita.setY(iHeight - basFantasmita.getAlto());
            }

            if (basFantasmita.getX() >= iWidth){
                basFantasmita.setX(-(int)(iWidth * (Math.random())));
                basFantasmita.setY((int) (Math.random() *(iHeight)));
            }
        }
        
        //Colisiones de Juanito
        for (Base basJuanito : llsJuanito){
            if (basMalo.intersecta(basJuanito)){
                iJuanitosChocados ++;
                sndSonidoJuanito.play();
                basJuanito.setX((int)(iWidth * (Math.random())));
                basJuanito.setY(-(int) (Math.random() *(iHeight)));
            }
        }

        for (Base basJuanito : llsJuanito){
            if (basJuanito.getX() + basJuanito.getAncho() > iWidth){
                basJuanito.setX(iWidth - basJuanito.getAncho());
            }
            
            if(basJuanito.getX() <= 0){
                basJuanito.setX(basJuanito.getX() + basJuanito.getAncho());
            }

            if (basJuanito.getY() >= iHeight){
                basJuanito.setX((int)(iWidth * (Math.random())) + 1);
                basJuanito.setY((int) (Math.random()*iHeight - (iHeight)));
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
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, iWidth, iHeight, this);

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
                graDibujo.drawString("Puntos: " + iPuntos, 15, 45);
                graDibujo.drawString ("Vidas: " + iVidas, 15, 60);
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
        
        //Al presionar G se guarda el juego
        if (ke.getKeyCode() == KeyEvent.VK_G){
            try {
                grabarJuego();
            } catch (IOException ex) {
                Logger.getLogger(Jframe1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Al presionar C se carga el juego
        if (ke.getKeyCode() == KeyEvent.VK_C) {
            try {
                cargarJuego();
            } catch (IOException ex) {
                Logger.getLogger(Jframe1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        
    }
    
    /**
     * Clase para guardar datos del juego actual
     * 
     * @throws IOException 
     */
    public void grabarJuego() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter("gameData.txt"));

        //se guardan variables generales
        fileOut.println(bPause); //se guarda si el juego estaba en pausa
        fileOut.println(bVivo); //Se guarda si lolita está viva
        fileOut.println(iVidas); //Se guarda vidas
        fileOut.println(iPuntos); //Se guarda el puntaje
        fileOut.println(iDireccion); //Se guarda la dirección
        
        //se guardan variables de lolita
        fileOut.println(basMalo.getX()); //Se guarda x de lolita
        fileOut.println(basMalo.getY()); //se guarda y de lolita
                
        //se guardan variables de juanito
        fileOut.println(iJuanitosChocados); //guarda cuantos juanitos chocaron
        fileOut.println(iCantJuanitos); //Se guarda la cantidad de juanitos
        for(Base basJuanito : llsJuanito){ //para todos los juanitos
                fileOut.println(basJuanito.getX()); //se guarda x de juanito
                fileOut.println(basJuanito.getY()); //se guarda y de juanito
        }
        
        //se guardan variables de fantasma
        fileOut.println(iCantFantasmas); //Se guarda la cantidad de fantasmas
        for(Base basFantasmita : llsFantasmas){ //para todos los fantasmas
                fileOut.println(basFantasmita.getX()); //se guarda x de fantasma
                fileOut.println(basFantasmita.getY()); //se guarda y de fantasma
        }
        
        fileOut.close();    //Se cierra el archivo
    }
    
    /**
     * Clase para cargar datos de un juego guardado
     * 
     * @throws IOException 
     */
    public void cargarJuego() throws IOException {
                                                                  
        BufferedReader fileIn;
        try {
                fileIn = new BufferedReader(new FileReader("gameData.txt"));
        } catch (FileNotFoundException e){
                File puntos = new File("gameData.txt");
                PrintWriter fileOut = new PrintWriter(puntos);
                fileOut.println("100,demo");
                fileOut.close();
                fileIn = new BufferedReader(new FileReader("gameData.txt"));
        }
        
        String aux = fileIn.readLine();
        bPause = (Boolean.parseBoolean(aux)); //Leo si el juego está en pausa
        
        aux = fileIn.readLine();
        bVivo = (Boolean.parseBoolean(aux)); //Leo si lolita está viva
        
        aux = fileIn.readLine();
        iVidas = (Integer.parseInt(aux)); //leo vidas
        
        aux = fileIn.readLine();
        iPuntos = (Integer.parseInt(aux)); //leo puntaje
        
        aux = fileIn.readLine();
        iDireccion = (Integer.parseInt(aux)); //leo direccion
        
        aux = fileIn.readLine();
        basMalo.setX((Integer.parseInt(aux))); //leo posicion x de lolita
        
        aux = fileIn.readLine();
        basMalo.setY((Integer.parseInt(aux))); //leo posicion y de lolita
        
        //leo variables de juanitos
        aux = fileIn.readLine();
        iJuanitosChocados = (Integer.parseInt(aux)); //cuantos juanitos chocaron
        aux = fileIn.readLine();
        
        for(Base basJuanito : llsJuanito){//Borro los Juanitos actuales
            llsJuanito.pop();
        }
               
        iCantJuanitos = (Integer.parseInt(aux)); //cuantos juanitos hay   
        
        URL urlImagenJuanito = this.getClass().getResource("juanito.gif");
        
        int iPosX;
        int iPosY;
        //Se hacen nuevos juanitos
        for (int iI = 0; iI < iCantJuanitos; iI++){
            iPosX = Integer.parseInt(fileIn.readLine());   
            iPosY = Integer.parseInt(fileIn.readLine()); 
            Base basJuanito = new Base(iPosX,iPosY,iWidth/iMAXANCHO,
            iHeight/iMAXALTO,
            Toolkit.getDefaultToolkit().getImage(urlImagenJuanito));
            llsJuanito.add(basJuanito);
        }
        
        
        for(Base basFantasmita : llsFantasmas){//Borro los fantasmas actuales
            llsFantasmas.pop();
        }
        
        //leo variables de fantasmas
        aux = fileIn.readLine();
        iCantFantasmas = (Integer.parseInt(aux)); //cuantos fantasmas hay
        
        
        URL urlImagenFantasma = this.getClass().getResource("fantasmita.gif");
        //Se crean fantasmas en posiciones del archivo
        for (int iI = 0; iI < iCantFantasmas; iI++){
            iPosX = Integer.parseInt(fileIn.readLine());   
            iPosY = Integer.parseInt(fileIn.readLine()); 
            Base basFantasma = new Base(iPosX,iPosY,iWidth/iMAXANCHO,
            iHeight/iMAXALTO,
            Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
            llsFantasmas.add(basFantasma);
        }
        
        fileIn.close();
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
