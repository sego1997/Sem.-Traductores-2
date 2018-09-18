package compilador.sistema.sintactico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import compilador.sistema.Compilador;
import compilador.util.DatoSimbolo;
import compilador.util.TipoSimbolo;

public class Sintactico extends Thread implements TipoSimbolo,DatoSimbolo{	   
	private static final int TIEMPO = 200;
	private static final int POS_DATO = 0;
	private static final int POS_TIPO = 1;
	private static final int POS_COL_REGLA = 0;
	private static final int POS_POPS_REGLA = 1;
	private static final int POS_NOM_REGLA = 2;
	private static final int MAX_FIL_TABLA = 95;
	private static final int MAX_COL_TABLA = 46;
	private static final int MAX_REGLAS = 53;
	private static final String RUTA_TABLA = "C:\\Users\\kikito1997\\workspace\\Sem.-Traductores-2\\src\\compilador\\sistema\\sintactico\\archivos\\tabla.txt";
	private static final String RUTA_REGLAS = "C:\\Users\\kikito1997\\workspace\\Sem.-Traductores-2\\src\\compilador\\sistema\\sintactico\\archivos\\reglas.txt";
	
	private final Compilador compilador;	
	private int fil;
	private int col;
	private boolean continuar;
    private int simboloActual;    
    private Stack<Simbolo> pilaSintactica;  
    public static String[] reglas;
    public static int[][] tablaLR;
    
    public Sintactico(Compilador compilador){
        this.compilador = compilador;        
    }
    
    @Override
    public void run(){    	    	  		
    	String entrada;	
    	continuar = true;
    	simboloActual = 0;
        pilaSintactica = new Stack<Simbolo>();    	
    	pilaSintactica.push(new Terminal(PESO+""));
    	pilaSintactica.push(new Accion(ES_IDENTIFICADOR+""));
    	cargarCompilador();    	
    	compilador.dameEscribano().imprimirDatosSintacticos("	  Datos sintacticos \n");    	
        while(compilador.puedeContinuar() && continuar){ 
        	if(compilador.dameLexico().puedeContinuar()) {
	        	pausar();
	        }
			compilador.dameEscribano().imprimirDatosSintacticos(mostrarPila());
			fil = Integer.valueOf(pilaSintactica.peek().dameDato());		
			if(compilador.dameSimbolos().size()>simboloActual){					
				String[] simbolos = compilador.dameSimbolos().get(simboloActual).split(SEPARADOR);
				entrada = simbolos[POS_DATO];
				col =  Integer.valueOf(simbolos[POS_TIPO]);	
			}else{
				col = ES_PESO;
				entrada = PESO+"";
			}
	        int accion = tablaLR[fil][col];        	
	        if(accion==-1) {
	        	continuar = false;
	        }else if(accion<0){
	        	reduccion(accion+2);
	        }else if(accion>0){	
	        	simboloActual++;
	        	pilaSintactica.push(new Terminal(entrada));
	        	pilaSintactica.push(new Accion(accion+""));
	        }else if(accion==0){
	        	compilador.puedeContinuar(false);
	        }	        
        }
        compilador.dameEscribano().imprimirDatosSintacticos("\n\n     Finalizado ...");
    }       
   
    private void reduccion(int idRegla){
    	idRegla *= -1;
    	String[] regla = reglas[idRegla].split(SEPARADOR);
    	int tamaPops = Integer.valueOf(regla[POS_POPS_REGLA]); 
    	for(int i=0; i<tamaPops; i++){
    		pilaSintactica.pop();
    	}
    	fil = Integer.valueOf(pilaSintactica.peek().dameDato());
    	col = Integer.valueOf(regla[POS_COL_REGLA]);     	
    	pilaSintactica.push(new NoTerminal(regla[POS_NOM_REGLA]));    	
    	pilaSintactica.push(new Accion(tablaLR[fil][col]+""));
    }
    
    private String mostrarPila(){
    	String datosPila = "\n     ";
    	int tamaPila = pilaSintactica.size();    	
    	for(int i=0; i<tamaPila; i++){
    		datosPila += pilaSintactica.get(i).dameDato()+" ";
    	}
    	return datosPila;
    }
    
    private void cargarCompilador() {
    	try {
    		int filT=0,filR=0;
    		String s;
    		reglas = new String[MAX_REGLAS]; 
    		tablaLR = new int[MAX_FIL_TABLA][MAX_COL_TABLA];    		
			BufferedReader tabla = new BufferedReader(new FileReader(new File(RUTA_TABLA)));
			BufferedReader regla = new BufferedReader(new FileReader(new File(RUTA_REGLAS)));
			while((s = tabla.readLine())!=null) {
				agregar(s.split("\t"),filT);				
				filT++;				
			}
			while((s = regla.readLine())!=null) {				
				reglas[filR] = s;
				filR++;	
			}
			regla.close();
			tabla.close();
		} catch (IOException e) {}
    }
    
    private void agregar(String[] s, int fil) {
    	int limite = s.length;
    	for(int j=0; j<limite; j++) {
    		tablaLR[fil][j] = Integer.valueOf(s[j]);  
    	}
    }
    
    public void pausar(){
    	try{
    		sleep(TIEMPO);    	
    	}catch(InterruptedException e){}           
    }
}
