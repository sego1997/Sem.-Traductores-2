package compilador.sistema.sintactico;

public class Simbolo {
    private String dato;
    
    public Simbolo(String dato){
        this.dato = dato;
    }
    
    public String dameDato(){
        return dato;
    }
}
