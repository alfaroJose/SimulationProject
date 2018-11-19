/**
 * Library with methods for the generation of numbers following some distributions
 */

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class NumberGenerator {

    //Usado para recortar los decimales a los deseados, en este caso 4
    private Random randomizer;
    DecimalFormat df;

    public  NumberGenerator(){
        randomizer = new Random(); //Genera un número random entre  0 y 1
        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
    }

    //Genera el tiempo de llegada de los programas al sistema con distribución exponencial.
    //lambda = 1/30 y lo redondea a 4 decimales
    //La distribución exponencial tiene una media de 30 segundos
    public double generarLlegadaExponencial(){
        double nuevoValor;
        double r = randomizer.nextDouble(); //Se genera un número random r entra 0 y 1
        while (r == 1 ){
            r = randomizer.nextDouble();
        }
        return Double.parseDouble(df.format((-30) * (Math.log(1 - r))));
    }

    //Genera el tiempo de llegada de los programas al sistema con distribución normal.
    // varianza = 4seg^2, media = 25seg y lo redondea a 4 decimales
    public double generarLlegadaNormal(){
        double nuevoValor = 0;
        for(int i = 1; i <= 12; i++)
            nuevoValor += randomizer.nextDouble();
        nuevoValor = nuevoValor - 6; //La sumatoria de los r sumados se les resta un 6.
        return Double.parseDouble(df.format((2*nuevoValor) + 25));
    }

    //Genera el tiempo de uso del dispositivo E/S con una función de densidad.
    public double generarTiempoUsoDispositvo(){
        double x = 20*(Math.sqrt(3*randomizer.nextDouble()+1));
        return Double.parseDouble(df.format(x));
    }

    //Genera un valor para ver si ocurre o no interrupción. 0-49 Sí ocurre. 50-99 No ocurre
    public int generarInterrupcion(){
        return randomizer.nextInt(100);
    }

    //Genera un valor para saber cuál tipo de interrupción es. 0-39 E/S. 40-99 Finaliza.
    public int generarTipoInterrupcion(){ return randomizer.nextInt(100); }

    //Genera el tiempo en el cuál ocurre la interrupción.
    public double generarTiempoInterrupcion(double b){ return Double.parseDouble(df.format((b*randomizer.nextDouble() + b)/2)); }
}