/**
 * Driver class
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Digite el numero maximo de corridas de la simulacion: ");
        int n = reader.nextInt(); // Scans the next token of the input as an int.
        //once finished
        System.out.println("Digite el tiempo total en segundos para correr cada vez la simulacion : ");
        int x = reader.nextInt(); // Scans the next token of the input as an int.
        //once finished
        System.out.println("Digite el quatum: ");
        int y = reader.nextInt(); // Scans the next token of the input as an int.
        //once finished
        System.out.println("Digite el tipo de distribucion para las llegadas (exponencial o normal) : ");
        String g = reader.next(); // Get what the user types.
        //once finished
        Simulator simulator = new Simulator(n, x, y, g);
        simulator.runSimulator();

    }
}