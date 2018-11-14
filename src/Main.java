/**
 * Driver class
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int numero, tiempo, quantum, distribucion;
        numero = tiempo = quantum = distribucion = -1;
        Scanner reader = new Scanner(System.in);

        while( numero <= 0 ) {
            System.out.println("Digite el numero maximo de corridas de la simulacion: ");
            numero = reader.nextInt();
            if( numero <= 0 ) System.out.println("El número máximo de corridas no puede ser negativo o cero");
        }

        while( tiempo <= 0 ) {
            System.out.println("Digite el tiempo total en segundos para correr cada vez la simulacion : ");
            tiempo = reader.nextInt();
            if( tiempo <= 0 ) System.out.println("El tiempo no puede ser negativo o cero");
        }

        while( quantum <= 0 ) {
            System.out.println("Digite el quantum: ");
            quantum = reader.nextInt();
            if( quantum <= 0 ) System.out.println("El quantum no puede ser negativo o cero");
        }

        while( distribucion < 1 || distribucion > 2 ) {
            System.out.println("Seleccione el tipo de distribucion para las llegadas\n" +
                    "1 para exponencial\n" +
                    "2 para normal ");
            distribucion = reader.nextInt();
            if( distribucion < 1 || distribucion > 2 ) System.out.println("Solo puede escoger entre 1 o 2");
        }

        Simulator simulator = new Simulator(numero, tiempo, quantum, distribucion);
        simulator.runSimulator();
    }
}