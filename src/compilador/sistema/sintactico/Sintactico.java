package compilador.sistema.sintactico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import compilador.sistema.Compilador;
import compilador.sistema.sintactico.visual.Arboles;
import compilador.sistema.semantico.Semantico;
import compilador.util.DatoSimbolo;
import compilador.util.TipoSimbolo;

public class Sintactico extends Thread implements TipoSimbolo,DatoSimbolo{	   
	private static final int TIEMPO = 1000;
	private static final int POS_DATO = 0;
	private static final int POS_TIPO = 1;
	public static final int POS_ID_REGLA = 0;
	private static final int POS_POPS_REGLA = 1;
	public static final int POS_NOM_REGLA = 2;
	private static final int MAX_FIL_TABLA = 95;
	private static final int MAX_COL_TABLA = 46;
	private static final int MAX_REGLAS = 53;
	private static final String RUTA_TABLA = "C:\\Users\\kikito1997\\workspace\\Sem.-Traductores-2\\src\\compilador\\sistema\\sintactico\\archivos\\tabla.txt";
	private static final String RUTA_REGLAS = "C:\\Users\\kikito1997\\workspace\\Sem.-Traductores-2\\src\\compilador\\sistema\\sintactico\\archivos\\reglas.txt";	
	
	private Arbol arbol;
	private final Compilador compilador;		
	private int fil;
	private int col;
	private boolean continuar;
    private int simboloActual;           
    private Stack<Simbolo> pilaSimbolos;    
    public String[] reglas;
    public int[][] tablaLR;   
    int cuentaSimbolos;
        
    public Sintactico(Compilador compilador){
        this.compilador = compilador;        
    }
    
    @Override
    public void run(){
    	arbol = new Arbol();    	
    	String entrada;	
    	continuar = true;
    	simboloActual = 0; 
    	cuentaSimbolos = 0;
        pilaSimbolos = new Stack<>();    	
    	pilaSimbolos.push(new Terminal(PESO+""));
    	pilaSimbolos.push(new Accion(ES_IDENTIFICADOR+""));
    	cargarCompilador();    	
    	compilador.dameEscribano().imprimirDatosSintacticos("     Datos sintacticos");    	
        while(compilador.puedeContinuar() && continuar){ 
        	if(compilador.dameLexico().puedeContinuar()) {
	        	pausar();
	        }        	
			fil = Integer.valueOf(pilaSimbolos.peek().dameDato());		
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
//	        	compilador.dameEscribano().imprimirDatosSintacticos(mostrarPila());
	        	reduccion(accion+2);	        	
	        }else if(accion>0){	
	        	simboloActual++;
	        	cuentaSimbolos++;
	        	pilaSimbolos.push(new Terminal("("+cuentaSimbolos+") "+entrada));
	        	pilaSimbolos.push(new Accion(accion+""));
	        }else if(accion==0){
	        	compilador.puedeContinuar(false);
	        }	        
        }
        compilador.dameEscribano().imprimirDatosSintacticos("\n\n     Finalizado ...");
//        arbol.mostrar();
        new Arboles(arbol);        
        new Semantico(arbol);                         
    }       
   
    private void reduccion(int idRegla){
    	idRegla *= -1;
    	String[] regla = reglas[idRegla].split(SEPARADOR); 
    	cuentaSimbolos++;
    	arbol.agregarNodos(regla,idRegla+1,Integer.valueOf(regla[POS_POPS_REGLA]));    	
    }
/*
    private String mostrarPila(){
    	String datosPila = "\n     ";
    	int tamaPila = pilaSimbolos.size();    	
    	for(int i=0; i<tamaPila; i++){
    		datosPila += pilaSimbolos.get(i).dameDato()+" ";
    	}
    	return datosPila;
    } 
*/    
    public class Arbol {
    	private Nodo raiz;	
    	private Stack<Nodo> nodos;
    	private Stack<Nodo> defReglas; 
    	private int numHijo;
    	private volatile boolean continuar;
    	
    	public Arbol() {		
    		raiz = null;
    		nodos = new Stack<>();
    		defReglas = new Stack<>();
    	}
    		
    	public void agregarNodos(String[] regla, int idRegla, int numElmtsRegla) { 
    		for(int i=0; i<numElmtsRegla; i++){    			
        		pilaSimbolos.pop();
        		nodos.push(new Nodo(pilaSimbolos.peek().dameDato()));
        		pilaSimbolos.pop();
        	}    	
        	int fil = Integer.valueOf(pilaSimbolos.peek().dameDato());
        	int col = Integer.valueOf(regla[Sintactico.POS_ID_REGLA]);
        	NoTerminal noTerminal = new NoTerminal("("+cuentaSimbolos+") "+regla[Sintactico.POS_NOM_REGLA],idRegla,numElmtsRegla);
        	pilaSimbolos.push(noTerminal);    	
        	pilaSimbolos.push(new Accion(tablaLR[fil][col]+""));
        	Nodo padre = new Nodo(noTerminal.dameDato());
        	for(int nodoActual=0; nodoActual<numElmtsRegla; nodoActual++) {
        		Nodo n = nodos.peek();
        		switch(nodoActual) {
        			case 0: padre.fijaPri(n); break;        			
        			case 1: padre.fijaSeg(n); break;
        			case 2: padre.fijaTer(n); break;
            		case 3: padre.fijaCua(n); break;
            		case 4: padre.fijaQui(n); break;
            		case 5: padre.fijaSex(n); break;            		
        		}
        		padre.numHijos(1);
        		n.fijaPadre(padre);
        		nodos.pop();
        	}
        	reglas();        
        	compilador.dameEscribano().imprimirDatosSintacticos("   Padre >> "+padre.dameDato()+" hijos "+hijos(padre)+"\n");
        	boolean esExpresion = false;
        	boolean esSentenciaBloque = false;
        	String datoNodoPadre = "";
        	String datoNodoHijo = "";
        	if(defReglas.size()>0) {          		
        		String regHijo = defReglas.peek().dameDato();
        		String regPadre = padre.dameDato();
        		datoNodoPadre = regPadre;
        		datoNodoHijo = regHijo;
        		datoNodoHijo = datoNodoHijo.substring(datoNodoHijo.indexOf(')')+2,datoNodoHijo.length());
        		datoNodoPadre = datoNodoPadre.substring(datoNodoPadre.indexOf(')')+2,datoNodoPadre.length());
        		if(datoNodoHijo.equals("Expresion")) {
    				esExpresion = true;
    			}else if(datoNodoHijo.equals("SentenciaBloque")) {
    				esSentenciaBloque = true;
    			}        		
    			if(esSentenciaBloque) {
    				if(!datoNodoPadre.equals("Otro")) {
    					continuar = true;       		
    					enlazarNodos(padre,defReglas.peek(),defReglas.peek().dameDato(),1);
    					if(!continuar) {        			
    						defReglas.pop();
    					}
    				}
    			}
    			if(esExpresion) {
    				if(!datoNodoPadre.equals("Sentencia") && defReglas.peek().numHijos()==1 ) {
    					continuar = true;       		
    					enlazarNodos(padre,defReglas.peek(),defReglas.peek().dameDato(),1);
    					if(!continuar) {        			
    						defReglas.pop();
    					}
    				}else {  
    					if(padre.numHijos()==3 || padre.numHijos()==2) {
    						continuar = true;       		
        					enlazarNodos(padre,defReglas.peek(),defReglas.peek().dameDato(),1);
        					if(!continuar) {        			
        						defReglas.pop();
        					}
    					}else {
    						if(padre.damePri()!=null){
    							String datoNodoHijoPri = padre.damePri().dameDato();
    							datoNodoHijoPri = datoNodoHijoPri.substring(datoNodoHijoPri.indexOf(')')+2,datoNodoHijoPri.length());
    							if(datoNodoHijoPri.equals("while")) {
    								continuar = true;
    								enlazarNodos(padre,defReglas.peek(),defReglas.peek().dameDato(),1);
    								if(!continuar) {        			
    	        						defReglas.pop();
    	        					}
    							}
    						}
    					}
    				}
    			}
    			
    			if(datoNodoPadre.equals("Sentencia") && padre.numHijos()==6) {
					continuar = true;       		
					enlazarNodos(padre,defReglas.peek(),defReglas.peek().dameDato(),1);
					if(!continuar) {        			
						defReglas.pop();
					}
				}
    			if(!esExpresion && !esSentenciaBloque) { 
    				for(int i=defReglas.size(); i>0; i--) {
    					continuar = true;       		
    					enlazarNodos(padre,defReglas.get(i-1),defReglas.get(i-1).dameDato(),2);
    					if(!continuar) {
    						defReglas.remove(i-1);
    					}
    				}										
    			}
    			
        	}          	
        	if(raiz!=null) {     
        		if(raiz.dameDato().equals("Expresion") && esExpresion) {
        			continuar = true;
        			enlazarNodos(padre,raiz,raiz.dameDato(),2);
        			if(continuar) defReglas.push(raiz); 
        		}else {
        			continuar = true;
        			enlazarNodos(padre,raiz,raiz.dameDato(),1);
        			if(continuar) defReglas.push(raiz);        			
        		}
        	}   	
        	raiz = padre;       
    	}
    	
    	private String hijos(Nodo padre) {
    		String h="";
    		if(padre.damePri()!=null) h += " >>" +padre.damePri().dameDato(); 					    			
    		if(padre.dameSeg()!=null) h += " >>" +padre.dameSeg().dameDato(); 		
    		if(padre.dameTer()!=null) h += " >>" +padre.dameTer().dameDato(); 		
    		if(padre.dameCua()!=null) h += " >>" +padre.dameCua().dameDato(); 		
    		if(padre.dameQui()!=null) h += " >>" +padre.dameQui().dameDato(); 		
    		if(padre.dameSex()!=null) h += " >>" +padre.dameSex().dameDato(); 		
    		return h;
    	}
    	
    	private void enlazarNodos(Nodo padre, Nodo hijo, String reg, int tipo) {    		
    		if(continuar) buscarReglaEnNodo(padre.damePri(),reg,0,tipo);    					    			
    		if(continuar) buscarReglaEnNodo(padre.dameSeg(),reg,1,tipo);
    		if(continuar) buscarReglaEnNodo(padre.dameTer(),reg,2,tipo);
    		if(continuar) buscarReglaEnNodo(padre.dameCua(),reg,3,tipo);
    		if(continuar) buscarReglaEnNodo(padre.dameQui(),reg,4,tipo);
    		if(continuar) buscarReglaEnNodo(padre.dameSex(),reg,5,tipo);
    		if(!continuar){
    			switch(numHijo) {    			
    				case 0: padre.fijaPri(hijo); break;
    				case 1: padre.fijaSeg(hijo); break;
    				case 2: padre.fijaTer(hijo); break;
    				case 3: padre.fijaCua(hijo); break;
    				case 4: padre.fijaQui(hijo); break;
    				case 5: padre.fijaSex(hijo); break;
    			}
    			hijo.fijaPadre(padre);					
    		}
    	}
    	
    	private void buscarReglaEnNodo(Nodo hijo, String nomRegla, int numH, int tipo) {
    		if(hijo!=null) {
    			if(tipo==1) {
    				if(hijo.dameDato().equals(nomRegla)) { 
    					continuar = false;
    					numHijo = numH;
    				}
    			}else {
    				if(hijo.dameDato().equals(nomRegla) && hijo.damePri()==null) { 
        				continuar = false;
        				numHijo = numH;
        			}
    			}
    		}
    	}
///*    	
    	public void reglas() {    		
    		for(int i=0; i<defReglas.size(); i++) {   		
    			compilador.dameEscribano().imprimirDatosSintacticos("   "+defReglas.get(i).dameDato()+" hijos >> "+defReglas.get(i).numHijos()+" >> "+hijos(defReglas.get(i))+"\n");
    		}
    		if(defReglas.size()!=0) {
    			compilador.dameEscribano().imprimirDatosSintacticos("\n");
    		}
    	}
//*/    	
    	public void mostrar() {
    		mostrar(raiz);
    	}
    	
    	private void mostrar(Nodo actual) { 
    		System.out.println("("+actual.dameDato()+")");
    		if(actual.damePri()!=null) mostrar(actual.damePri());
    		if(actual.dameSeg()!=null) mostrar(actual.dameSeg());
    		if(actual.dameTer()!=null) mostrar(actual.dameTer());
    		if(actual.dameCua()!=null) mostrar(actual.dameCua());
    		if(actual.dameQui()!=null) mostrar(actual.dameQui());
    		if(actual.dameSex()!=null) mostrar(actual.dameSex()); 		
    	}
    	
    	public Nodo dameRaiz() {
    		return raiz;
    	}
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
				String[] str = s.split("\t"); 
				for(int colT=0; colT<MAX_COL_TABLA; colT++) {
		    		tablaLR[filT][colT] = Integer.valueOf(str[colT]);
		    	}
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
    
    public void pausar(){
    	try{
    		sleep(TIEMPO);    	
    	}catch(InterruptedException e){}           
    }
}
