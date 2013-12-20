package le;

import java.io.*;

public class ManejadorOrdenes {

	private String _orden = "";		// Orden reconocida
	private String _sistema = "";	// Sistema Operativo: "Windows 7"

	public ManejadorOrdenes() {
		setOrden("");
		setSistema(System.getProperty("os.name"));
	}

	protected String getOrden() {
		return _orden;
	}

	protected void setOrden(String orden) {
		_orden = orden;
	}

	protected String getSistema() {
		return _sistema;
	}

	protected void setSistema(String sistema) {
		_sistema = sistema;
	}

	public void manejarOrden(String orden) {
		setOrden(orden);
		switch (getSistema()) {
			case "Windows 7":
				manejarOrdenWindows7();
				break;
			default:
				System.out.println("No se hace nada para el Sistema Operativo: " + getSistema());
				System.exit(0);
		}
	}

	public void manejarOrdenWindows7() {
		switch (getOrden()) {
    		case "ApagarSistema":
    			//ejecutarComando("shutdown /s /t 0");
    		    //ejecutarComando("shutdown /p /f");
    			System.out.println("Se recibio la orden de \"Apagar Sistema\"");
    			break;

    		case "ReiniciarSistema":
    			//ejecutarComando("shutdown /r /t 0");
    		    //ejecutarComando("shutdown /r /f /t 0");
    			System.out.println("Se recibio la orden de \"Reiniciar Sistema\"");
    			break;

    		case "HibernarSistema":
    			// ejecutarComando("shutdown /h"); //--> tener hibernacion activada, se
    		    // puede activar con el comando powercfg -h on
    			System.out.println("Se recibio la orden de \"Hibernar Sistema\"");
    			break;

    		case "CerrarNavegador":
    			ejecutarComando("taskkill /f /t /im chrome.exe /im iexplore.exe /im firefox.exe");
    			System.out.println("Se recibio la orden de \"Cerrar Navegador\"");
    			break;

    		case "FinalizarProcesosQueNoResponden":
    			ejecutarComando("taskkill /f /t /fi \"STATUS eq NOT RESPONDING\"");
    			System.out.println("Se recibio la orden de \"Finalizar Procesos Que No Responden\"");
    			break;

    		case "ActivarSonido":
    			ejecutarComando("nircmd.exe mutesysvolume 0");
    			System.out.println("Se recibio la orden de \"Activar Sonido\"");
    			break;

    		case "SilenciarSonido":
    			ejecutarComando("nircmd.exe mutesysvolume 1");
    			System.out.println("Se recibio la orden de \"Silenciar Sonido\"");
    			break;

            case "BajarVolumen":
                ejecutarComando("nircmd.exe changesysvolume -5000");
                System.out.println("Se recibio la orden de \"Bajar Volumen\"");
                break;

            case "SubirVolumen":
                ejecutarComando("nircmd.exe changesysvolume 5000");
                System.out.println("Se recibio la orden de \"Subir Volumen\"");
                break;

    		case "CerrarAplicacion":
    			System.out.println("Se recibio la orden de \"Cerrar Aplicacion\"");
    			System.exit(0);

    		default:
    			System.out.println("No se hace nada para la orden reconocida");
    			break;
		}
	}

	public void ejecutarComando(String comando) {
	    String s = "";
		try {
			Process p = Runtime.getRuntime().exec(comando);
			
			// Leemos la salida del comando
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println("Esta es la salida standard del comando:\n");
            while ((s = stdInput.readLine()) != null)
                System.out.println(s);

            // Leemos los errores si los hubiera
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            System.out.println("Esta es la salida standard del error del comando (si la hay):\n");
            while ((s = stdError.readLine()) != null)
                System.out.println(s);

		} catch (IOException e) {

		}
	}

}