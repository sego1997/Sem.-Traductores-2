package compilador.sistema;

import java.util.Stack;

import compilador.util.TipoSimbolo;

public class Sintactico extends Thread implements TipoSimbolo{	   
	private static final int TIEMPO = 1000;
	
	private boolean continuar;
	private final Compilador compilador;
	
	int fil;
	int col;
    private int simboloActual;    
    private Stack<Integer> simbolos;
       
    private static final int[] reglas = {2};
    private static final int[][] tablaLR = {
    								{2,0,1},
    								{0,-1,0},
    								{0,-2,0}
    							};
    /*
    private static final int[][] tablaLR_V2 = {
									{2,0,1},
									{0,-1,0},
									{0,-2,0}
		};
	*/ 
//    private int[][] tablaLR;
        
    public Sintactico(Compilador compilador /*, int tipoEjercicio*/){
        this.compilador = compilador;
        continuar = true;
        simboloActual = 0;
//        if(tipoEjercicio == 1) tablaLR = tablaLR_V1;
        simbolos = new Stack<Integer>();
    }
    
    @Override
    public void run(){
    	simbolos.push(1);
    	simbolos.push(0);
    	compilador.dameEscribano().imprimirDatosSintacticos(datosPila());
//    	int cont = 0;    	
        while(compilador.puedeContinuar() || continuar){   
//        	cont++;
//        	System.out.println("Numero entrada >> "+cont);
        	if(simbolos.size()==0){
        		pausar();
        	}
        	
//        	for(int i=0; i<compilador.dameSimbolos().size(); i++){
//        		System.out.println("Simbolo >> "+compilador.dameSimbolos().get(i).dameDato());
//        	}
        	try {	        	
				sleep(TIEMPO);
				fil = simbolos.peek();
				if(compilador.dameSimbolos().size()>simboloActual){					
					col =  compilador.dameSimbolos().get(simboloActual).dameTipo();
				}else{
					if(simbolos.size()!=0){
						col = 1;
					}else continuar = false;
				}					        	 
	        	int accion = tablaLR[fil][col];        	               	
	        	simboloActual++; 
	        	compilador.dameEscribano().imprimirDatosSintacticos("\n	Entrada >>"+col+"\n	Accion >>"+accion+"\n");
//	        	String datos;
//	        	String dtos = "\n	tablaLR["+fil+"]["+col+"] >> "+accion;
//	        	compilador.dameEscribano().imprimirDatosSintacticos(dtos);	
//	        	datos = datosPila()+"\n	Entrada >>"+col+"\n	Accion >>"+tipoAccion(accion);	        	
//	        	compilador.dameEscribano().imprimirDatosSintacticos("\n	Entrada >>"+col+"\n	Accion >>"+accion+"\n");
	        	
	        	if(accion==-1) {
	        		continuar = false;
	        	}else if(accion<0){
	        		reduccion(accion);
	        	}else if(accion>0){	        		
	        		simbolos.push(col);
	        		simbolos.push(accion);
	        		compilador.dameEscribano().imprimirDatosSintacticos(datosPila());
	        	}else{
	        		if(accion==0){
	        			compilador.puedeContinuar(false);
	        		}
	        	}
	        	
//	        	datos = datosPila()+"\n	Entrada >>"+col+"\n	Accion >>"+tipoAccion(accion);
//	        	compilador.dameEscribano().imprimirDatosSintacticos(datos);
			} catch (InterruptedException e) {}        	
        }
        System.out.println("Se detuvo el sintactico");
    }
    
    private void reduccion(int tp){
    	tp = tp*-1;
    	int tamaPops = reglas[tp-2];
    	for(int i=0; i<tamaPops; i++){
    		simbolos.pop();
    	}
    	fil = simbolos.peek();
    	col = 2;
    	simbolos.push(col);
    	simbolos.push(tablaLR[fil][col]);
    	compilador.dameEscribano().imprimirDatosSintacticos(datosPila());
    }
    
    private String datosPila(){
    	String datosPila = "\n	Datos en la pila:\n	";
    	int tamaPila = simbolos.size();
    	System.out.println("Datos de pila "+tamaPila);
    	for(int i=0; i<tamaPila; i++){
    		datosPila += " "+simbolos.get(i);
    	}
		System.out.println(datosPila);
    	return datosPila;
    }
    
    /*    
    private String tipoAccion(int accion){
    	String tipoAccion = "";
    	if(accion==-1) tipoAccion = "Estado de aceptacion";
    	else if(accion>0) tipoAccion = "Desplazamiento";
    	else if(accion<0) tipoAccion = "Reduccion";    	
    	return tipoAccion;
    }
    */
    
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
