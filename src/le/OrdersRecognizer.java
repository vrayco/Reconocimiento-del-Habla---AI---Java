/*
 * 
 * Escuela Tecnica Superior de Ingenieria Informatica
 * Ingenieria en Informatica
 * Asignatura de Agentes Inteligentes
 * Grupo 6: Sergio Aaron Afonso Suarez, Rayco Velazquez Gonzalez, Manuel Luis Aznar
 * 10-Diciembre-2013
 * 
 * Clase OrdersRecognizer: reconocimiento de las ordenes dadas por el usuario.
 * 
 */
 
package le;
 
import javax.speech.*;
import javax.speech.recognition.*;
import java.util.Locale;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrdersRecognizer extends ResultAdapter {
 
	static Recognizer recognizer;
	String orden;
	ManejadorOrdenes mo = new ManejadorOrdenes();
 
	@Override
	// Funcion que captura el evento cuando alguna regla
	// de nuestra gramatica ha sido reconocida.
	public void resultAccepted(ResultEvent re) {
		try {
			Result res = (Result)(re.getSource());
			ResultToken tokens[] = res.getBestTokens();
			orden = "";
			for (int i=0; i < tokens.length; i++)
				orden += tokens[i].getSpokenText();

			mo.manejarOrden(orden);

		} catch(Exception ex) {
			System.out.println("Ha ocurrido algo inesperado " + ex);
		}
	}

	public static void staticGrammarLoading() {
	    try {
	        RuleGrammar rg = recognizer.newRuleGrammar("order_grammar");
	        
	        Rule r1 = rg.ruleForJSGF("<system_termination_orders> | <process_orders> | <sound_orders> | <application_orders>");
	        rg.setRule("order_grammar", r1, true);

	        Rule r2 = rg.ruleForJSGF("<system_orders> Sistema");
	        rg.setRule("system_termination_orders", r2, true);

	        Rule r3 = rg.ruleForJSGF("Apagar | Reiniciar | Hibernar");
	        rg.setRule("system_orders", r3, true);

	        Rule r4 = rg.ruleForJSGF("(Cerrar Navegador) | (Finalizar Procesos Que No Responden)");
	        rg.setRule("process_orders", r4, true);
	        
	        Rule r5 = rg.ruleForJSGF("(<sound_basic_orders> Sonido) | (<sound_complex_orders> Volumen)");
	        rg.setRule("sound_orders", r5, true);

	        Rule r6 = rg.ruleForJSGF("Activar | Silenciar");
	        rg.setRule("sound_basic_orders", r6, true);

	        Rule r7 = rg.ruleForJSGF("Subir | Bajar");
	        rg.setRule("sound_complex_orders", r7, true);

	        Rule r8 = rg.ruleForJSGF("Cerrar Aplicacion");
	        rg.setRule("application_orders", r8, true);

	        rg.setEnabled(true);
	    } catch (GrammarException ge) {
	        ge.printStackTrace();
	    }
	}
 
	public static void main(String args[]) {
		try {
		    // Crear el motor de informacion de voz para el idioma local
		    EngineModeDesc emd = new EngineModeDesc(Locale.ROOT);
		    // Crear el reconocedor para el idioma anteriormente especificado
			recognizer = Central.createRecognizer(emd);
			// Reservar los recursos necesarios para el motor
			recognizer.allocate();

			switch (args.length) {

			    // Hay argumento de entrada, la gramatica se lee desde fichero
	            case 1:
	                // Fichero desde el que se carga la gramatica
	                FileReader grammar = new FileReader(args[0]);
	                // Asignar la gramatica al reconocedor
	                RuleGrammar rg = recognizer.loadJSGF(grammar);
	                // Hacer que la gramatica este activa
	                rg.setEnabled(true);       
	                break;

	            // No hay argumento de entrada, se utiliza la gramatica estatica que define el metodo staticGrammarLoading()
                case 0:
                    staticGrammarLoading();       
                    break;

	            default:
	                System.exit(0);
	                break;
	        }

			// Solicitud de notificaciones de todos los eventos que produce el reconocedor
			recognizer.addResultListener(new OrdersRecognizer());
			// Grabar los cambios de la gramatica cargada anteriormente
			recognizer.commitChanges();
			// Hacer que el reconocedor empiece a escuchar
			recognizer.requestFocus();
			// Poner el motor en el Estado de Reanudacion, para reanudar la transmision de audio si el motor esta
			// en el Estado de Pausa
			recognizer.resume();

	         System.out.println("Listo para recibir Ordenes...");
		} catch (Exception e) {
			System.out.println("Exception en " + e.toString());
			e.printStackTrace();
			System.exit(0);
		}
	}

}