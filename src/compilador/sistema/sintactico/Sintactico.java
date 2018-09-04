package compilador.sistema.sintactico;

import java.util.Stack;

import compilador.sistema.Compilador;
import compilador.util.DatoSimbolo;
import compilador.util.TipoSimbolo;
import compilador.util.ColumnaSimbolo;

public class Sintactico extends Thread implements TipoSimbolo,DatoSimbolo,ColumnaSimbolo{	   
	private static final int TIEMPO = 500;
	private static final int POS_DATO = 0;
	private static final int POS_TIPO = 1;
	
	private final Compilador compilador;
	
	private int fil;
	private int col;
	private boolean continuar;
    private int simboloActual;    
    private Stack<Simbolo> pilaSintactica;
    
    private static final int[] reglas = {6,2};
    private static final int[][] tablaLR = {
									{2,0,0,1},
									{0,0,-1,0},
									{0,3,-3,0},
									{2,0,0,4},
									{0,0,-2,0}
		};
    
    public Sintactico(Compilador compilador){
        this.compilador = compilador;        
    }
    
    @Override
    public void run(){
    	pausar();
    	String entrada;	
    	continuar = true;
        pilaSintactica = new Stack<Simbolo>();    	
    	pilaSintactica.push(new Terminal(PESO+""));
    	pilaSintactica.push(new Accion(COL_ID+""));
    	compilador.dameEscribano().imprimirDatosSintacticos("	Datos sintacticos \n"+mostrarPila());
        while(compilador.puedeContinuar() && continuar){
        	try {	        	
				sleep(TIEMPO);										
				fil = Integer.valueOf(pilaSintactica.peek().dameDato());		
				if(compilador.dameSimbolos().size()>simboloActual){					
					String[] simbolos = compilador.dameSimbolos().get(simboloActual).split(SEPARADOR);
					entrada = simbolos[POS_DATO];
					col =  Integer.valueOf(simbolos[POS_TIPO]);	
					castearTipoSimbolo();
				}else{
					col = COL_PESO;
					entrada = PESO+"";
				}
	        	int accion = tablaLR[fil][col];        	               		        	
	        	String datos = "\n	Entrada "+entrada+"\n	Accion >> d"+accion+"\n";
	        	compilador.dameEscribano().imprimirDatosSintacticos(datos);	        	
	        	if(accion==-1) {
	        		continuar = false;
	        	}else if(accion<0){
	        		reduccion(accion);
	        	}else if(accion>0){	        		
	        		pilaSintactica.push(new Terminal(entrada));
	        		pilaSintactica.push(new Accion(accion+""));
	        		compilador.dameEscribano().imprimirDatosSintacticos(mostrarPila());
	        	}else{
	        		if(accion==0){
	        			compilador.puedeContinuar(false);
	        		}
	        	}
			} catch (InterruptedException e) {}
        	if(compilador.dameSimbolos().size()>simboloActual && compilador.dameLexico().puedeContinuar()){
        		pausar();
        	}
        	simboloActual++;
        }
        compilador.dameEscribano().imprimirDatosSintacticos("\n	Finalizado ...");
    }
    
    private void reduccion(int tp){
    	tp = tp*-1;
    	int tamaPops = reglas[tp-COL_PESO];
    	for(int i=0; i<tamaPops; i++){
    		pilaSintactica.pop();
    	}
    	fil = Integer.valueOf(pilaSintactica.peek().dato);
    	col = COL_REGLA;
    	pilaSintactica.push(new NoTerminal("E"));
    	pilaSintactica.push(new Accion(tablaLR[fil][col]+""));
    	compilador.dameEscribano().imprimirDatosSintacticos(mostrarPila());
    }
    
    private String mostrarPila(){
    	String datosPila = "\n	Pila:\n	";
    	int tamaPila = pilaSintactica.size();
    	for(int i=0; i<tamaPila; i++){
    		datosPila += pilaSintactica.get(i).dameDato();
    	}
    	return datosPila;
    }
    
    
    private void castearTipoSimbolo(){
    	if(col==ES_IDENTIFICADOR){
    		col = COL_ID;
    	}else if(col==ES_SUM_O_RES){
    		col = COL_MAS;    		
    	}
    }
    
    public void pausar(){
    	try{
    		synchronized(this){
    			wait();
            }
        }catch(InterruptedException e){}           
    }
        
    public void renaudar(){
    	synchronized(this){
    		notify();
        }
    }
}
