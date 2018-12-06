package compilador.sistema.sintactico.visual;
import java.io.File;
import java.util.ArrayList;
import compilador.sistema.sintactico.Nodo;
import compilador.sistema.sintactico.Sintactico.Arbol;

class NodoGrafico {    
    private String dato;
    private ArrayList<NodoGrafico> hijos;
    
    NodoGrafico(String dato) {
      hijos = new ArrayList<>();
      this.dato = dato;
    }       
    public ArrayList<NodoGrafico> hijos(){
    	return hijos;
    	}
    public String dameDato() {
    	return dato;
    } 
    public void agregarHijo(NodoGrafico n){
    	hijos.add(n);
    }
  } 

public class Arboles { 
	private int tama = 0;	
    private int contadorNodos;
//    private int contadorNulos;
    private NodoGrafico raiz;
    private NodoGrafico resultado;
    private ArrayList<String> nodosVisitados;
    private GraphViz graficador;      
        
  public Arboles(Arbol abl) {
	  contadorNodos = 0;
	  graficador = new GraphViz();
	  nodosVisitados = new ArrayList<>();
	  raiz = new NodoGrafico(abl.dameRaiz().dameDato());  
	  crearArbol(raiz,abl.dameRaiz());	  
	  graficarArbol();
  } 
  
  private void crearArbol(NodoGrafico ng, Nodo n) {
	  enlazarNodos(ng.hijos(),n);
	  if(n.damePri()!=null)	crearArbol(buscarNodo(ng.hijos(),n.damePri().dameDato()),n.damePri());
	  if(n.dameSeg()!=null)	crearArbol(buscarNodo(ng.hijos(),n.dameSeg().dameDato()),n.dameSeg());
	  if(n.dameTer()!=null)	crearArbol(buscarNodo(ng.hijos(),n.dameTer().dameDato()),n.dameTer());
	  if(n.dameCua()!=null)	crearArbol(buscarNodo(ng.hijos(),n.dameCua().dameDato()),n.dameCua());
	  if(n.dameQui()!=null)	crearArbol(buscarNodo(ng.hijos(),n.dameQui().dameDato()),n.dameQui());
	  if(n.dameSex()!=null)	crearArbol(buscarNodo(ng.hijos(),n.dameSex().dameDato()),n.dameSex());
  }
  
  private void enlazarNodos(ArrayList<NodoGrafico> hijos, Nodo n) {
	  if(n.damePri()!=null) hijos.add(new NodoGrafico(n.damePri().dameDato()));
	  if(n.dameSeg()!=null) hijos.add(new NodoGrafico(n.dameSeg().dameDato()));	  
	  if(n.dameTer()!=null) hijos.add(new NodoGrafico(n.dameTer().dameDato()));	  
	  if(n.dameCua()!=null) hijos.add(new NodoGrafico(n.dameCua().dameDato()));	  
	  if(n.dameQui()!=null) hijos.add(new NodoGrafico(n.dameQui().dameDato()));	 
	  if(n.dameSex()!=null) hijos.add(new NodoGrafico(n.dameSex().dameDato()));	  	  
  }
  
  private NodoGrafico buscarNodo(ArrayList<NodoGrafico> hijos, String hijoBuscado) {
	  NodoGrafico tmp = null;
	 for(int i=0; i<hijos.size(); i++) {
		  if(hijos.get(i).dameDato().equals(hijoBuscado)) {
			  tmp = hijos.get(i);
			  break;
		  }		  
	  }
	  return tmp;
  }
  
  public NodoGrafico buscarRecursivo(String data) { 
    return(buscarRecursivo(raiz, data)); 
  }     
    
  private NodoGrafico buscarRecursivo(NodoGrafico node, String valorBuscado) { 
    if (node==null) resultado = null;          
    if (valorBuscado.equals(node.dameDato())) {
    	resultado = node;           
    }
    for(NodoGrafico tmp: node.hijos()) {    
      buscarRecursivo(tmp, valorBuscado); 
    }    
    return resultado;
  } 
  
  public int buscar(String valorNodo){      
      return nodosVisitados.indexOf(valorNodo);
  }
  
  public void finalizarGrafica(){
      graficador.addln(graficador.end_graph());
      String type = "png";
      File out = new File("arbol." + type);
      graficador.writeGraphToFile(graficador.getGraph(graficador.getDotSource(), type), out);
  }
    
  private void encontrarNodos(NodoGrafico nodoGrafico){
      if(nodoGrafico != null){
//          String nulo = "null";
          graficador.addln(String.format("%d [ label=<%s> ]", contadorNodos,nodoGrafico.dameDato()));
          nodosVisitados.add(nodoGrafico.dameDato());  
          contadorNodos++;          
          for(NodoGrafico tmp: nodoGrafico.hijos()){
/*        	  
            if(tmp == null){
                nulo = "null" + contadorNulos;
                contadorNulos++;
                graficador.addln(String.format("%s[shape=point]", nulo));                                       
            }
*/
            encontrarNodos(tmp);                                            
          }
      }
  }      
  
  public void asignarPadreHijo(NodoGrafico nodoGrafico){
	  if(nodoGrafico != null){         
          for(NodoGrafico tmp: nodoGrafico.hijos()){
              if(tmp != null) {
            	  String vn = String.format("%d -> %d", buscar(nodoGrafico.dameDato()), buscar(tmp.dameDato()));
                  graficador.addln(vn);
              }
              asignarPadreHijo(tmp);             
          }
      }
  }
  
  public void graficarArbol(){ 
	  graficador.addln(graficador.start_graph());
      graficador.addln("ordering = out");  
      encontrarNodos(raiz);                 
      asignarPadreHijo(raiz);
      finalizarGrafica();
  }
  
  public int obtenerTama(NodoGrafico nodoGrafico){
      if(nodoGrafico != null){
          tama++;
          for(NodoGrafico tmp: nodoGrafico.hijos()) {
            obtenerTama(tmp);
          }
      }
      return tama;
  }
}