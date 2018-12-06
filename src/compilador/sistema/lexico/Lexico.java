package compilador.sistema.lexico;
import compilador.sistema.Compilador;
import compilador.util.DatoSimbolo;
import compilador.util.TipoSimbolo;

public class Lexico extends Thread implements DatoSimbolo,TipoSimbolo{		
	private static final int ASCII_A = 65;
	private static final int ASCII_a = 97;
	private static final int ASCII_0 = 48;
	private static final int MAX_LETRAS = 26;
	private static final int MAX_DIGITOS = 9;
	private final Compilador compilador;	
    private int estado;
    private String palabra;
    private boolean continuar;
    private int caracterActual;   
    private int tamaFlujoCaracteres;    
    private final String flujoCaracteres;
        
	public Lexico(Compilador compilador, String flujoCaracteres){                
		this.compilador = compilador;
        this.flujoCaracteres = flujoCaracteres;         
	}
	
    private char sigCaracter(){
    	char c = 0;		
        if(caracterActual>=tamaFlujoCaracteres){
        	continuar = false;
        }else{
        	c = flujoCaracteres.charAt(caracterActual);
        }
        caracterActual++;
        return c;
    }
        
    @Override
    public void run(){
    	estado = 0;
        palabra = "";
        continuar = true;
        caracterActual = 0;
        tamaFlujoCaracteres = flujoCaracteres.length();       
    	compilador.dameEscribano().imprimirDatosLexicos("     Datos lexicos");
        while(compilador.puedeContinuar() && continuar){            
        	char c = sigCaracter();
        	palabra += c;
            switch(estado){
            	case 0: 
            		if(ESPACIO==c || "	"==String.valueOf(c)){
            			palabra = "";
            		}else if(COMILLA==c){
						estado = 5;
					}else if(SUMA==c || RESTA==c){
						agregarSimbolo(palabra,ES_SUM_O_RES);
					}else if(DIVISION==c || MULTIPLICACION==c){
						agregarSimbolo(palabra,ES_MUL_O_DIV);
					}else if(MAYOR==c || MENOR==c){
						estado = 10;
					}else if(IGUAL==c){
						estado = 11;
					}else if(OR==c){
						estado = 13;
					}else if(AND==c){
						estado = 15;
					}else if(NOT==c){
						estado = 17;
					}else if(PUNTO_Y_COMA==c){
						agregarSimbolo(palabra,ES_PUNTO_Y_COMA);
					}else if(COMA==c){
						agregarSimbolo(palabra,ES_COMA);
					}else if(ABRIR_PARENTESIS==c){
						agregarSimbolo(palabra,ES_ABRIR_PARENTESIS);
					}else if(CERRAR_PARENTESIS==c){
						agregarSimbolo(palabra,ES_CERRAR_PARENTESIS);
					}else if(ABRIR_CORCHETE==c){
						agregarSimbolo(palabra,ES_ABRIR_CORCHETE);
					}else if(CERRAR_CORCHETE==c){
						agregarSimbolo(palabra,ES_CERRAR_CORCHETE);
					}else if(PESO==c){
						agregarSimbolo(palabra,ES_PESO);
					}else if(esDigito(c)){
						estado = 1;			
					}else if(esLetra(c) || c==GUION_BAJO){
						estado = 4;
					}
				break;
					
				case 1: 
					if(!esDigito(c)){
						if(c==PUNTO){
							estado = 2;
						}else{
							agregarSimbolo(palabra.substring(0,palabra.length()-1),ES_DATO_ENTERO);
							caracterActual--;
						}
					}
				break;
						
				case 2:
					if(esDigito(c)){
						estado = 3;
					}else{
						estado = 0;						
						palabra = "";
					}
				break;
						
				case 3:
					if(!esDigito(c)){
						agregarSimbolo(palabra.substring(0,palabra.length()-1),ES_DATO_REAL);
						caracterActual--;
					}
				break;
						
				case 4: 
					if(esLetra(c) || esDigito(c)){
						if(INT.equals(palabra) || FLOAT.equals(palabra) || VOID.equals(palabra)){
							agregarSimbolo(palabra,ES_TIPO);
						}else if(IF.equals(palabra)){
							agregarSimbolo(palabra,ES_IF);
						}else if(ELSE.equals(palabra)){
							agregarSimbolo(palabra,ES_ELSE);
						}else if(WHILE.equals(palabra)){
							agregarSimbolo(palabra,ES_WHILE);
						}else if(RETURN.equals(palabra)){
							agregarSimbolo(palabra,ES_RETURN);
						}
					}else{
						int tamaID = palabra.length()-1;
						agregarSimbolo(palabra.substring(0,tamaID),ES_IDENTIFICADOR);						
						caracterActual--;
					}
				break;
				
				case 5: 
					if(c==COMILLA){
						agregarSimbolo(palabra,ES_DATO_CADENA);
					}							
				break;
																			
				case 10: 
					if(c!=IGUAL){					
						agregarSimbolo(""+palabra.charAt(0),ES_OP_RELACION);
						caracterActual--;
					}else{
						agregarSimbolo(palabra,ES_OP_RELACION);
					}
				break;						
				
				case 11: 
					if(c!=IGUAL){					
						agregarSimbolo(""+palabra.charAt(0),ES_IGUAL);
						caracterActual--;
					}else{
						agregarSimbolo(palabra,ES_OP_IGUALDAD);
					}
				break;						
				
				case 13: 
					if(c==OR){					
						agregarSimbolo(palabra,ES_OR);
					}else{
						estado = 0;
						caracterActual--;
					}
				break;						
				
				case 15: 
					if(c==AND){					
						agregarSimbolo(palabra,ES_AND);
					}else{
						estado = 0;
						caracterActual--;
					}
				break;				
				
				case 17: 
				if(c!=IGUAL){					
					agregarSimbolo(""+palabra.charAt(0),ES_NOT);
					caracterActual--;
				}else{
					agregarSimbolo(palabra,ES_OP_IGUALDAD);
				}
				break;
			}            
        }
        compilador.dameEscribano().imprimirDatosLexicos("\n\n     Finalizado ...");
        continuar = false;
    }
    
    private void agregarSimbolo(String dato, int tipo){
        	estado = 0;
            palabra = "";
            compilador.dameSimbolos().add(dato+SEPARADOR+tipo);
            compilador.dameEscribano().imprimirDatosLexicos("\n     "+dato+" >> ("+tipo+")");
    }
    
	private boolean esLetra(char c){
		boolean esLetra = false;
		int ascii_c = (int)c;
		if((ascii_c>=ASCII_A && ascii_c<=(ASCII_A+MAX_LETRAS)) ||
				(ascii_c>=ASCII_a && ascii_c<=(ASCII_a+MAX_LETRAS))){
			esLetra = true;
		}
		return esLetra;
	}
	
	private boolean esDigito(char c){
		boolean esDigito = false;
		int ascii_c = (int)c;
		if(ascii_c>=ASCII_0 && ascii_c<=(ASCII_0+MAX_DIGITOS)){
			esDigito = true;
		}
		return esDigito;
	}
	
	public boolean puedeContinuar() {
		return continuar;
	}
}