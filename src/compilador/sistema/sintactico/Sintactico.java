package compilador.sistema.sintactico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import compilador.sistema.Compilador;
import compilador.util.DatoSimbolo;
import compilador.util.TipoSimbolo;
import compilador.visual.VentanaArbol;

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
        
    public Sintactico(Compilador compilador){
        this.compilador = compilador;        
    }
    
    @Override
    public void run(){
    	arbol = new Arbol();    	
    	String entrada;	
    	continuar = true;
    	simboloActual = 0;
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
	        	pilaSimbolos.push(new Terminal(entrada));
	        	pilaSimbolos.push(new Accion(accion+""));
	        }else if(accion==0){
	        	compilador.puedeContinuar(false);
	        }	        
        }
        compilador.dameEscribano().imprimirDatosSintacticos("\n\n     Finalizado ...");
        new VentanaArbol(arbol);
        arbol.mostrar();
    }       
   
    private void reduccion(int idRegla){
    	idRegla *= -1;
    	String[] regla = reglas[idRegla].split(SEPARADOR);    	
    	arbol.agregarNodos(regla,idRegla+1,Integer.valueOf(regla[POS_POPS_REGLA]));    	
    }
    
    private String mostrarPila(){
    	String datosPila = "\n     ";
    	int tamaPila = pilaSimbolos.size();    	
    	for(int i=0; i<tamaPila; i++){
    		datosPila += pilaSimbolos.get(i).dameDato()+" ";
    	}
    	return datosPila;
    } 
    
    public class Arbol {
    	private Nodo raiz;	
    	private Stack<Nodo> nodos;
    	private Stack<Nodo> defReglas; 
    	private int numHijo;
    	private boolean continuar;
    	
    	public Arbol() {		
    		raiz = null;
    		nodos = new Stack<>();
    		defReglas = new Stack<>();
    	}
    		
    	public void agregarNodos(String[] regla, int idRegla, int numElmtsRegla) {    		
    		for(int i=0; i<numElmtsRegla; i++){    			
        		pilaSimbolos.pop();
        		nodos.push(new Nodo(pilaSimbolos.peek()));
        		pilaSimbolos.pop();
        	}    	
        	int fil = Integer.valueOf(pilaSimbolos.peek().dameDato());
        	int col = Integer.valueOf(regla[Sintactico.POS_ID_REGLA]);
        	NoTerminal noTerminal = new NoTerminal(regla[Sintactico.POS_NOM_REGLA],idRegla,numElmtsRegla);
        	pilaSimbolos.push(noTerminal);    	
        	pilaSimbolos.push(new Accion(tablaLR[fil][col]+""));
        	Nodo padre = new Nodo(noTerminal);
        	for(int nodoActual=0; nodoActual<numElmtsRegla; nodoActual++) {
        		Nodo n = nodos.peek();
        		switch(nodoActual) {
        			case 0: padre.fijaPrimero(n); break;        			
        			case 1: padre.fijaSegundo(n); break;
        			case 2: padre.fijaTercero(n); break;
            		case 3: padre.fijaCuarto(n); break;
            		case 4: padre.fijaQuinto(n); break;
            		case 5: padre.fijaSexto(n); break;            		
        		}
        		padre.numHijos(1);
        		n.fijaPadre(padre);
        		nodos.pop();
        	}
        	compilador.dameEscribano().imprimirDatosSintacticos("   Padre("+padre.dameSimbolo().dameDato()+") hijos"+hijos(padre)+"\n");
        	if(defReglas.size()>0) {      		
        		continuar = true;
        		enlazarNodos(padre,defReglas.peek(),defReglas.peek().dameSimbolo().dameDato());
        		if(!continuar) {        			
        			defReglas.pop();
        		}
        	}          	
        	if(raiz!=null) {        		
        		continuar = true;
        		enlazarNodos(padre,raiz,raiz.dameSimbolo().dameDato());
        		if(continuar) defReglas.push(raiz);
        	}
/*        	
        	if(continuar) {
        		if(defReglas.size()>1) {
        			continuar = true;
            		enlazarNodos(padre,defReglas.get(defReglas.size()-2),defReglas.get(defReglas.size()-2).dameSimbolo().dameDato());
            		if(!continuar) {        			
            			defReglas.remove(defReglas.size()-2);
            		}
        		}
        	}
*/        	
        	raiz = padre;       
    	}
    	
    	private String hijos(Nodo padre) {
    		String h="";
    		if(padre.damePrimero()!=null) h += " (1)>>" +padre.damePrimero().dameSimbolo().dameDato(); 					    			
    		if(padre.dameSegundo()!=null) h += " (2)>>" +padre.dameSegundo().dameSimbolo().dameDato(); 		
    		if(padre.dameTercero()!=null) h += " (3)>>" +padre.dameTercero().dameSimbolo().dameDato(); 		
    		if(padre.dameCuarto()!=null) h += " (4)>>" +padre.dameCuarto().dameSimbolo().dameDato(); 		
    		if(padre.dameQuinto()!=null) h += " (5)>>" +padre.dameQuinto().dameSimbolo().dameDato(); 		
    		if(padre.dameSexto()!=null) h += " (6)>>" +padre.dameSexto().dameSimbolo().dameDato(); 		
    		return h;
    	}
    	
    	private void enlazarNodos(Nodo padre, Nodo hijo, String reg) {    		
    		if(continuar) buscarReglaEnNodo(padre.damePrimero(),reg,0);    					    			
    		if(continuar) buscarReglaEnNodo(padre.dameSegundo(),reg,1);
    		if(continuar) buscarReglaEnNodo(padre.dameTercero(),reg,2);
    		if(continuar) buscarReglaEnNodo(padre.dameCuarto(),reg,3);
    		if(continuar) buscarReglaEnNodo(padre.dameQuinto(),reg,4);
    		if(continuar) buscarReglaEnNodo(padre.dameSexto(),reg,5);
    		if(!continuar){
    			switch(numHijo) {    			
    				case 0: padre.fijaPrimero(hijo); break;
    				case 1: padre.fijaSegundo(hijo); break;
    				case 2: padre.fijaTercero(hijo); break;
    				case 3: padre.fijaCuarto(hijo); break;
    				case 4: padre.fijaQuinto(hijo); break;
    				case 5: padre.fijaSexto(hijo); break;
    			}
    			hijo.fijaPadre(padre);					
    		}
    	}
    	
    	private void buscarReglaEnNodo(Nodo hijo, String nomRegla, int numH) {
    		if(hijo!=null) {
    			if(hijo.dameSimbolo().dameDato().equals(nomRegla) && hijo.damePrimero()==null) {    				
    				continuar = false;
    				numHijo = numH;
    			}
    		}
    	}
    	
    	public void mostrar() {
    		mostrar(raiz);
    	}
    	
    	private void mostrar(Nodo actual) {    		
    		if(actual.damePrimero()!=null) mostrar(actual.damePrimero());
    		if(actual.dameSegundo()!=null) mostrar(actual.dameSegundo());
    		if(actual.dameTercero()!=null) mostrar(actual.dameTercero());
    		if(actual.dameCuarto()!=null) mostrar(actual.dameCuarto());
    		if(actual.dameQuinto()!=null) mostrar(actual.dameQuinto());
    		if(actual.dameSexto()!=null) mostrar(actual.dameSexto());
//    		compilador.dameEscribano().imprimirDatosSintacticos("\n     "+actual.dameSimbolo().dameDato());
//    		compilador.dameEscribano().imprimirDatosSintacticos(" >> "+actual.numHijos()+" hijos ");    		
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
