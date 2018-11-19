/**
 * Driver class
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int numero, tiempo, quantum, distribucion;//Datos a ingresar por el usuario
        numero = tiempo = quantum = distribucion = -1;//Inicialización de datos de simulación en neutro
        Scanner reader = new Scanner(System.in);//Objeto para usar Scanner y leer input por consola
        String confirmar = "";//String auxiliar para confirmar una serie de simulaciones

        while( numero <= 0 ) {
            System.out.println("Simulación de Round Robin con un solo CPU\n" +
                    "Ingrese los datos necesarios para ejecutar las simulaciones");

            while (numero < 0) {
                System.out.println("Digite el numero maximo de corridas de la simulacion:");
                numero = reader.nextInt();
                if (numero < 0) System.out.println("El número máximo de corridas no puede ser negativo o cero");
            }

            while (tiempo < 0) {
                System.out.println("Digite el tiempo total en segundos para correr cada vez la simulacion:");
                tiempo = reader.nextInt();
                if (tiempo < 0) System.out.println("El tiempo no puede ser negativo o cero");
            }

            while (quantum < 0) {
                System.out.println("Digite el quantum del Round Robin: ");
                quantum = reader.nextInt();
                if (quantum < 0) System.out.println("El quantum no puede ser negativo o cero");
            }

            while (distribucion < 1 || distribucion > 2) {
                System.out.println("Seleccione el tipo de distribucion para las llegadas\n" +
                        "1 para exponencial\n" +
                        "2 para normal ");
                distribucion = reader.nextInt();
                if (distribucion < 1 || distribucion > 2) System.out.println("Solo puede escoger entre 1 o 2");
            }

            while (!confirmar.equals("y") && !confirmar.equals("n")) {
                System.out.println("¿Desea correr la siguiente simulacion?\n" +
                        "Número de repeticiones de la simulacion: " + numero
                        + "\nTotal de segundos por simulacion: " + tiempo
                        + "\nQuantum usado por el Round Robin: " + quantum);
                if (distribucion == 1) System.out.println("Distribución de llegadas de los programas: Exponencial");
                else System.out.println("Distribución de llegadas de los programas: Normal");
                System.out.println("Ingrese y para confirmar o n para cancelar y volver a meter datos");
                confirmar = reader.next();
                if (confirmar.equals("y")) {
                    Simulator simulator = new Simulator(numero, tiempo, quantum, distribucion);
                    simulator.runSimulator();
                } else if (confirmar.equals("n")) numero = tiempo = quantum = distribucion = -1;
            }
            confirmar = "";
        }
    }
}