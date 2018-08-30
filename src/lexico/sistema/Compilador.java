package lexico.sistema;
import java.util.ArrayList;

import lexico.util.Escribano;

public class Compilador {   
    private boolean continuar;
    private ArrayList<Simbolo> simbolos;
	private String flujoCaracteres;     
    
	private Escribano escribano;
//	private Lexico lexico;
        
	public Compilador(Escribano escribano){
		this.escribano = escribano;
		flujoCaracteres = escribano.dameAreaCompilacion().getText();                
        continuar = !flujoCaracteres.isEmpty();
        tratarFlujoCaracteres();
        simbolos = new ArrayList<>();
	}
	
    public void inicializar(){
    	(new Lexico(this,flujoCaracteres)).start();    	
    }                
    
    public void mostrarDatosLexico(){
    	String datosLexico = "Datos de compilacion";
    	for(int i=0; i<simbolos.size(); i++){
    		datosLexico += "\n	 "+simbolos.get(i).dameDato()+" >> ("+simbolos.get(i).dameTipo()+")";
    	}
    	escribano.imprimirAreaInformador(datosLexico);
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
    
    public ArrayList<Simbolo> dameSimbolos(){
        return this.simbolos;
    }        
}
