package compilador.sistema;

import java.util.Stack;

import compilador.util.DatoSimbolo;
import compilador.util.TipoSimbolo;
import compilador.util.ColumnaSimbolo;

public class Sintactico extends Thread implements TipoSimbolo,DatoSimbolo,ColumnaSimbolo{	   
	private static final int TIEMPO = 500;
	private final Compilador compilador;
	
	private int fil;
	private int col;
	private String simbolo;
	private boolean continuar;
    private int simboloActual;    
    private Stack<Integer> pilaSintactica;
    
    private static final int[] reglas = {6};
    private static final int[][] tablaLR = {
									{2,0,0,1},
									{0,0,-1,0},
									{0,3,0,0},
									{4,0,0,0},
									{0,0,-2,0}
		};
    
    public Sintactico(Compilador compilador){
        this.compilador = compilador;        
    }
    
    @Override
    public void run(){
    	pausar();
    	continuar = true;
        pilaSintactica = new Stack<Integer>();    	
    	pilaSintactica.push(COL_PESO);
    	pilaSintactica.push(COL_ID);
    	compilador.dameEscribano().imprimirDatosSintacticos("	Datos sintacticos \n"+datosPila());
        while(compilador.puedeContinuar() && continuar){
        	try {	        	
				sleep(TIEMPO);
				String entrada = "";				
				fil = pilaSintactica.peek();				
				if(compilador.dameSimbolos().size()>simboloActual){
					simbolo = compilador.dameSimbolos().get(simboloActual).dameDato();
					col =  compilador.dameSimbolos().get(simboloActual).dameTipo();
					castearTipoSimbolo();
					entrada = "("+simbolo+") >> "+col;
				}else{
					col = COL_PESO;
					entrada = "("+PESO+") >> "+col;
				}
	        	int accion = tablaLR[fil][col];        	               		        	
	        	String datos = "\n	Entrada "+entrada+"\n	Accion >> d"+accion+"\n";
	        	compilador.dameEscribano().imprimirDatosSintacticos(datos);	        	
	        	if(accion==-1) {
	        		continuar = false;
	        	}else if(accion<0){
	        		reduccion(accion);
	        	}else if(accion>0){	        		
	        		pilaSintactica.push(col);
	        		pilaSintactica.push(accion);
	        		compilador.dameEscribano().imprimirDatosSintacticos(datosPila());
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
    	fil = pilaSintactica.peek();
    	col = COL_REGLA;
    	pilaSintactica.push(col);
    	pilaSintactica.push(tablaLR[fil][col]);
    	compilador.dameEscribano().imprimirDatosSintacticos(datosPila());
    }
    
    private String datosPila(){
    	String datosPila = "\n	Pila:\n	";
    	int tamaPila = pilaSintactica.size();
    	for(int i=0; i<tamaPila; i++){
    		datosPila += " "+pilaSintactica.get(i);
    	}
    	return datosPila;
    }
    
    private void castearTipoSimbolo(){
    	if(col==ES_IDENTIFICADOR){
    		col = COL_ID;
    	}else if(col==ES_SUM_O_RES){
    		if(simbolo.equals(String.valueOf(SUMA))){
    			col = COL_MAS;
    		}
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
