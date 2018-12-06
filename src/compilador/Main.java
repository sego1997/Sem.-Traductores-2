/*
 * Escuela: Centro Universitario de Ciencias Exactas e Ingenierias
 * Nombre: Luis Enrique Galaviz Huerta
 * Materia: Seminario de solucion de problemas de Traductores de lenguaje 2
 * Trabajo: Analizador lexico
 */
package compilador;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import compilador.visual.VentanaPrincipal;

public class Main {	
	public static void main(String[] args){	
		new VentanaPrincipal();
//		String c="11+30+";
//		System.out.println("Letra: "+c.charAt(c.length()-2)+" >> "+c.codePointAt(c.length()-2));
/*				
		try {			
			try {
				String codigo = "\ncout<<endl<<\"hola mundo\";";
				FileWriter fichero = new FileWriter("ejecutable.cpp");
				PrintWriter pw =  new PrintWriter(fichero);
		        pw.print(strcArriba+codigo+strcAbajo);
		        pw.close();		        				
				fichero.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
			PrintWriter salida = new PrintWriter("ejecutable.bat");
			salida.println("@echo off");
			salida.println("title Ejecutable");
			salida.println("cd C:\\Users\\kikito1997\\workspace\\Sem.-Traductores-2");
			salida.println("g++ -c ejecutable.cpp");
			salida.println("g++ -o ejecutable ejecutable.cpp");
			salida.println("ejecutable.exe");
			salida.println("@ECHO.");
			salida.println("@pause");
			salida.println("exit");
			salida.close();
            Runtime rt=Runtime.getRuntime();
            try {
				rt.exec("cmd /c start \"\" ejecutable.bat");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
*/
	}
}
