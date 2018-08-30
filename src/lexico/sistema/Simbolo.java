package lexico.sistema;

public class Simbolo {
    String dato;
    int tipo;
    
    public Simbolo(String dato, int tipo){
        this.dato = dato;
        this.tipo = tipo;
    }
    
    public String dameDato(){
        return dato;
    }
    
    public int dameTipo(){
        return tipo;
    }
}
