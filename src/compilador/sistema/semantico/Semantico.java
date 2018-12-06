package compilador.sistema.semantico;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import compilador.sistema.codigo.GenerarCodigo;
import compilador.sistema.sintactico.Nodo;
import compilador.util.Reglas;
import compilador.sistema.sintactico.Sintactico.Arbol;

public class Semantico implements Reglas{
	private static final String REGEX_ENTERO = "\\d*";
	private static final String REGEX_FLOTANTE = "\\d*\\.\\d*";
	private volatile boolean varGlobal;
	private volatile boolean varLocal;
	private volatile boolean valorRegresa;
	private volatile boolean sentencia;
//	private volatile boolean continuar;
	private ArrayList<Token> tokens;
	private ArrayList<Token> errores;	
	private Pattern patEntero;
	private Pattern patFlotante;	
	private Matcher mat;	
	
	private int numParam;
	private int numFunc;
	private String tipoFunc;
	private String idFunc;
	
	private Stack<String> operadores;
	private Stack<Boolean> condiciones;
	private String paramReturn;
	
	private String tipoVar;
	private Token tknTmp;
	private String formula;	
	private volatile boolean esRetFloat;
	private volatile boolean funcDuplicada;
	private volatile boolean condicion;
	private String opUltimo;
	private Nodo priNodoIf;
	private Stack<Nodo> nodosIf;
	
	public Semantico(Arbol arbol) {
		varGlobal = false;
		varLocal = false;
		formula = "";
//		continuar = true;
		patEntero = Pattern.compile(REGEX_ENTERO);
		patFlotante = Pattern.compile(REGEX_FLOTANTE);
		tokens = new ArrayList<>();
		errores = new ArrayList<>();
		condiciones = new Stack<>();
		nodosIf = new Stack<>();
		condiciones.add(true);
		recorrer(arbol.dameRaiz());	
		mostrar();
		new GenerarCodigo(tokens);
	}
	
	public void recorrer(Nodo nodo) {
		System.out.println("Nodo-->"+nodo.dameDato()+" condicion: "+condiciones.peek());		
		if(condiciones.peek()) {			
			tipoRegla(nodo,convertir(nodo.dameDato()));			
			if(nodo.damePri()!=null) recorrer(nodo.damePri()); 					    			
			if(nodo.dameSeg()!=null) recorrer(nodo.dameSeg()); 		
			if(nodo.dameTer()!=null) recorrer(nodo.dameTer()); 		
			if(nodo.dameCua()!=null) recorrer(nodo.dameCua()); 		
			if(nodo.dameQui()!=null) recorrer(nodo.dameQui()); 		
			if(nodo.dameSex()!=null) recorrer(nodo.dameSex());
		}else {
			condiciones.add(true);				
		}		
		return;
	}
	
	private boolean validarToken(int tipo, String t, String id, String td, String a, String err){
		boolean valido = true;
		tknTmp = new Token(t,id,td,a);					
		if(esDuplicado(tipo)) {
			tknTmp.fijaError(err);
			valido = false;			
		}
		tokens.add(tknTmp);	
		return valido;
	}
	
	private void tipoRegla(Nodo nodo, String reg) {
		String ambito ="";
		if(varGlobal) ambito="Global";
		else if(varLocal) ambito="Local";		
		if(reg.equals(Definicion)) {
			varGL(true,false);
		}else if(reg.equals(DefLocal)) {
			varGL(false,true);
		}else if(reg.equals(DefVar) || reg.equals(ListaVar)) {
			if(nodo.numHijos()>0) {
				if(reg.equals(DefVar)) tipoVar = convertir(nodo.damePri().dameDato());					
				String id = convertir(nodo.dameSeg().dameDato());
				if(varGlobal) {
					validarToken(1,tipoVar,id,"Variable",ambito,"Variable redefinida");
				}else {
					validarToken(3,tipoVar,id,"Variable",ambito,"Variable redefinida dentro de los parametros");						
					tknTmp.fijaIDFuncion(idFunc);
				}				
			}
		}else if(reg.equals(DefFunc)) {
			paramReturn = "";
			valorRegresa = false;			
			sentencia = false;
			funcDuplicada = false;
			tipoFunc = convertir(nodo.damePri().dameDato());	
			idFunc = convertir(nodo.dameSeg().dameDato());										
			if(!validarToken(2,tipoFunc,idFunc,"Funcion",ambito,"Funcion redefinida")) {
				funcDuplicada = true;				
			}else{
				numParam = 0;
				numFunc = tokens.size()-1;			
			}
			agregarParametros(nodo);
		}else if((reg.equals(Parametros) || reg.equals(ListaParam))) {	
			if(!funcDuplicada) {
				if(nodo.numHijos()>0) {
					numParam++;
					if(reg.equals(Parametros)) {
						validarParametro(3,convertir(nodo.damePri().dameDato()),convertir(nodo.dameSeg().dameDato()));
					}else {
						validarParametro(3,convertir(nodo.dameSeg().dameDato()),convertir(nodo.dameTer().dameDato()));
					}
				}
			}			
		}else if(reg.equals(Sentencia)) {
			if(!nodo.damePri().dameDato().equals("while")) {
				condicion = condiciones.peek();
				System.out.println("Condicion:"+String.valueOf(condicion)+" Nodo ("+nodo.dameDato()+")");
				if(condicion) {
					formula = "";
					operadores = new Stack<>();
					if(nodo.numHijos()==4) {
						sentencia = true;
						tknTmp = new Token("",convertir(nodo.damePri().dameDato()),"","");
						tknTmp.fijaIDFuncion(idFunc);
					}else if(nodo.numHijos()==6) {
						condicion=true;
					}
				}
			}
		}
		else if(reg.equals(ValorRegresa)) {											
			paramReturn = "";
			esRetFloat = false;
			valorRegresa = true;
			tknTmp = new Token("","","return","Funcion");
		}else if(reg.equals(Expresion)) {
			if(valorRegresa || sentencia) {				
				if(nodo.numHijos()==3) {
					String op = convertir(nodo.dameSeg().dameDato());
					if(!op.equals(Expresion)) {
						operadores.add(op);
					}
				}			
			}
			if(!formula.isEmpty()) {
				String op = convertir(nodo.damePadre().dameSeg().dameDato());				
				switch(op) {
					case "==": formula("=="); break;
					case "!=": formula("!="); break;
					case "<=": formula("<="); break;
					case ">=": formula(">="); break;
					case "<": formula("<");	break;
					case ">": formula(">");	break;				
				}
			}
		}else if(reg.equals(Termino)) {
			if(valorRegresa || sentencia || condiciones.peek()) {					
				opUltimo = "";
				String termino=convertir(nodo.damePri().dameDato());				
				if(!operadores.empty()) opUltimo = operadores.peek();				
				mat = patEntero.matcher(termino);
				if(mat.matches()) {
					formula += termino + opUltimo;
					paramReturn += "int,";
				}else {
					mat = patFlotante.matcher(termino);
					if(mat.matches()) {
						formula += termino + opUltimo;
						paramReturn += "float,";
						esRetFloat = true;
					}else {
						Token t = null;
						if((t=buscarVarLocal(termino))!=null){							
						}else {
							t=buscarVarGlobal(termino);																			
						}
						if(t==null) {
							 tknTmp.fijaError("Variable "+termino+" nulo");
						}else {
							System.out.println("Valor de ("+termino+") >> "+t.dameValor()+" Tipo: "+t.dameTipo());
							if(t.dameValor().indexOf(".")!=-1) {
								if(t.dameTipo().equals("int")){
									t.fijaValor(t.dameValor().substring(0, t.dameValor().indexOf(".")));
								}
							}
							if(!t.dameValor().isEmpty() || !t.dameValor().equals("Error")) {
								formula += t.dameValor() + opUltimo;
								paramReturn += t.dameTipo()+",";
							}else {
								formula += "Error";
								paramReturn += "Error"+t.dameTipo();
							}													
						}
					}
				}
			}			
		}else if(reg.equals(DefLocales) || reg.equals(Sentencias)) {			
			if(valorRegresa) {
				sentencia = false;				
				String tipo = "";
				System.out.println("Formula funcion >> "+formula);
				paramReturn = paramReturn.substring(0,paramReturn.length()-1);
				tknTmp.agregarParametro(paramReturn);
				tknTmp.fijaIDFuncion(idFunc);
				if(esRetFloat){		
					tipo="float";
				}else tipo="int";			
				if(!tipoFunc.equals(tipo)) {
					tknTmp.fijaError("La funcion "+idFunc+" de tipo "+tipoFunc+" no puede retornar un tipo "+tipo);
				}
				tokens.add(tknTmp);
				tokens.get(numFunc).fijaValor(calcular());
			}
			if(sentencia) {
				if(!formula.isEmpty()) {
					valorRegresa = false;
					if(condicion) {
						Token t = null;				
						String resultado = calcular();
						System.out.println("Formula termino >> "+formula);
						System.out.println("Asignar valor "+resultado+" a ("+tknTmp.dameID()+")");
						if((t=buscarVarLocal(tknTmp.dameID()))!=null){					
						}else {
							t=buscarVarGlobal(tknTmp.dameID());
						}
						if(t!=null) {	
							if(!resultado.equals("true") && !resultado.equals("false")) {
								t.fijaValor(resultado);
							}
						}
					}
				}
			}
		}else if(reg.equals(")")){
			if(!formula.isEmpty()) {				
				formula = formula.substring(0,formula.lastIndexOf(opUltimo));			
				System.out.println("Formula condicion:"+formula);
				if(calcular().equals("false")) {					
					condiciones.add(false);					
				}else {
					condiciones.add(true);
					nodosIf.add(nodo.damePadre());
				}
				formula = "";
			}			
		}else if(reg.equals("else")){
			if(!nodosIf.isEmpty()) {
				if(nodo.damePadre().damePadre()==nodosIf.peek()) {
//				if(condiciones.get(1)) {
						condiciones.add(false);
						nodosIf.pop();
//				}
				}
			}
		}else if(reg.equals(Sentencias)) {
			Token t = null;				
			String resultado = calcular();
			System.out.println("Formula termino >> "+formula);
			System.out.println("Asignar valor "+resultado+" a ("+tknTmp.dameID()+")");
			if((t=buscarVarLocal(tknTmp.dameID()))!=null){					
			}else {
				t=buscarVarGlobal(tknTmp.dameID());
			}
			if(t!=null) {	
				if(!resultado.equals("true") && !resultado.equals("false")) {
					t.fijaValor(resultado);
				}
			}
		}
	}		
	
	private void formula(String operador) {
		if(formula.indexOf(operador)==-1) {
			char cEnd = formula.charAt(formula.length()-1);
			if(cEnd=='+' || cEnd=='-' || cEnd=='/' || cEnd=='*') {
				formula = formula.substring(0,formula.length()-1);
			}
			formula += operador;
		}
	}
	
	private boolean esDuplicado(int tipo) {		
		boolean esDuplicado = false;					
		if(tipo==1) {
			if(buscarVarGlobal(tknTmp.dameID())!=null) esDuplicado = true;											
		}else if(tipo==2) {
			if(buscarFuncion(tknTmp.dameID(),tknTmp.dameParametros())!=null)esDuplicado = true;			
		}else if(tipo==3) {
			if(buscarVarLocal(tknTmp.dameID())!=null)esDuplicado = true;
		}
		return esDuplicado;
	}
	
	private void agregarParametros(Nodo nodo) {
		if( nodo.dameCua().numHijos()!=0) {
			Nodo aux = nodo.dameCua().dameTer();		
			tknTmp.agregarParametro(convertir(nodo.dameCua().damePri().dameDato()));
			while(aux!=null) {
				if(aux.dameSeg()!=null) {
					tknTmp.agregarParametro(","+convertir(aux.dameSeg().dameDato()));
				}
				aux = aux.dameCua();
			}
		}
	}
	
	private Token buscarVarGlobal(String id) {
		Token tkn = null;
		for(Token t: tokens) {
			if(t.dameAmbito().equals("Global") && t.dameID().equals(id)) {
				tkn= t;
				break;
			}
		}
		return tkn;
	}
	
	private Token buscarVarLocal(String id) {
		Token tkn = null;
		for(Token t: tokens) {			
			if(t.dameIDFuncion().equals(idFunc) && t.dameID().equals(id)) {
				tkn = t;
				break;
			}
		}
		return tkn;
	}
	
	private Token buscarFuncion(String id, String parms) {
		Token tkn = null;
		for(Token t: tokens) {			
			if(t.dameID().equals(id) && t.dameParametros().equals(parms)) {
				tkn = t;
				break;
			}
		}
		return tkn;
	}
	
	private String calcular() {
		String valor = "Error";
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
        	int asc = formula.codePointAt(formula.length()-1);        	
        	if(asc<48 || asc>57) {
        		formula = formula.substring(0,formula.length()-1);
        	}        	        
        	if(formula.indexOf(valor)== -1) valor = String.valueOf(engine.eval(formula));
        } catch (ScriptException e) {        	
        }        
		return valor;
	}	
	
	private String convertir(String cadena) {
		cadena = cadena.substring(cadena.indexOf(')')+2, cadena.length());
		return cadena; 
	}	
	
	private void varGL(boolean g, boolean l) {
		varLocal = l;
		varGlobal = g;
	}	
	
	private void validarParametro(int tipo, String tp, String id) {
		if(!validarToken(tipo,tp,id,"Parametro","Funcion","Parametro redefinido")) {
			tokens.get(numFunc).fijaError("Parametro "+numParam+" redefinido");
		}
		tknTmp.fijaIDFuncion(idFunc);
	}
	
	private void mostrar() {
		System.out.println();
		for(Token t: tokens) {
			String idFuncion = "";
			String valor = "";
			String error = "";
			String params = "";
			if(!t.dameIDFuncion().equals("")) idFuncion = t.dameIDFuncion()+" >> ";		
			if(!t.dameValor().equals("")) valor = t.dameValor()+" >> ";
			if(!t.dameParametros().equals("")) params = t.dameParametros()+" >> ";
			if(!t.dameError().equals("")) error = t.dameError();
			System.out.println(t.dameTipo()+" >> "+t.dameID()+" >> "+t.dameDato()+" >> "+t.dameAmbito()+" >> "+idFuncion+valor+params+error);
		}
	}	
}
