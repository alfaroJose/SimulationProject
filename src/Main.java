/**
 * Driver class
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int numero, tiempo, quantum, distribucion;
        numero = tiempo = quantum = distribucion = -1;
        Scanner reader = new Scanner(System.in);
        String confirmar = "";

        while( numero <= 0 ) {

            System.out.println("Digite 1 para 3/600/6/Exponencial o 2 para 3/600/6/Normal o lo que sea para manualmente ");
            numero = reader.nextInt();
            if (numero == 1){
                Simulator simulator = new Simulator(3, 600, 6, 1);
                simulator.runSimulator();
                break;
            } else if (numero == 2){
                Simulator simulator = new Simulator(3, 600, 6, 2);
                simulator.runSimulator();
                break;
            }

            while (numero <= 0) {
                System.out.println("Digite el numero maximo de corridas de la simulacion: ");
                numero = reader.nextInt();
                if (numero <= 0) System.out.println("El número máximo de corridas no puede ser negativo o cero");
            }

            while (tiempo <= 0) {
                System.out.println("Digite el tiempo total en segundos para correr cada vez la simulacion : ");
                tiempo = reader.nextInt();
                if (tiempo <= 0) System.out.println("El tiempo no puede ser negativo o cero");
            }

            while (quantum <= 0) {
                System.out.println("Digite el quantum: ");
                quantum = reader.nextInt();
                if (quantum <= 0) System.out.println("El quantum no puede ser negativo o cero");
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