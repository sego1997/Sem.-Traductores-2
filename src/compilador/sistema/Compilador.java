package compilador.sistema;
import java.util.ArrayList;
import compilador.util.Escribano;
import compilador.sistema.lexico.Lexico;
import compilador.sistema.sintactico.Sintactico;

public class Compilador {   
    private boolean continuar;
    private ArrayList<String> simbolos;
	private String flujoCaracteres;     
    
	private Escribano escribano;
	private Lexico lexico;
	private Sintactico sintactico;
        
	public Compilador(Escribano escribano){
		this.escribano = escribano;
		flujoCaracteres = escribano.dameFlujoCaracteres();                
        continuar = !flujoCaracteres.isEmpty();
        tratarFlujoCaracteres();
        simbolos = new ArrayList<>();
	}
	
    public void inicializar(){
    	escribano.limpiarDatos();
    	(lexico = new Lexico(this,flujoCaracteres)).start();
    	(sintactico = new Sintactico(this)).start();
    }
    
    private void tratarFlujoCaracteres(){
    	String[] fcs = flujoCaracteres.split("\n");
    	flujoCaracteres = "";
    	for(int i=0; i<fcs.length; i++){
    		flujoCaracteres += fcs[i];
    	}
    }
        
    public void puedeContinuar(boolean continuar){
		this.continuar = continuar;
	}      
    public boolean puedeContinuar(){
		return continuar;
	}
    
    public Escribano dameEscribano(){
    	return escribano;
    }
   
    public Sintactico dameSintactico(){
    	return sintactico;
    }
    public Lexico dameLexico(){
    	return lexico;
    }
    
    public ArrayList<String> dameSimbolos(){
        return simbolos;
    }        
}
