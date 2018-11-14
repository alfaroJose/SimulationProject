/**
 * Library with methods for the generation of numbers following some distributions
 */

import java.util.Random;

public class NumberGenerator {

    Random r;
    public  NumberGenerator(){
        r = new Random(); //Genera un número entre  0 y 1
    }

    //Genera un número entre 0 y 1 y lo redondea
    public double generarNumeroAleatorio(){
        double x = r.nextDouble();
        //System.out.println("Numero: " + x);
        x = Math.round(x * Math.pow(10, 4)); //Redondear el valor a 4 decimales
        x = x/Math.pow(10, 4);
        return x;
    }

    //Genera el tiempo de llegada de los programas al sistema cuando la dsitribución es exponencial. lamda = 1/30
    public double generarLlegadaExponencial(){
        double nuevoValor;
        double r = generarNumeroAleatorio(); //Se genera un número random r entra 0 y 1
        if (r != 1) { //Hay que verificar que r no sea 1 para que no se caiga la fórmula
            //System.out.println("Numero aleatorio es : " + r);
            nuevoValor = (-30) * (Math.log(1 - r)); //La distribución exponencial tiene una media de 30 segundos
            nuevoValor = Math.round(nuevoValor * Math.pow(10, 4)); //Redondear el valor a 4 decimales
            nuevoValor = nuevoValor / Math.pow(10, 4);
            return nuevoValor;
        } else {
            return generarLlegadaExponencial(); //Se vuelve a generar un nuevo número
        }
    }

    //Genera el tiempo de llegada de los programas al sistema cuando la dsitribución es normal. varianza = 4seg^2, media = 25seg
    public double generarLlegadaNormal(){
        double nuevoValor = 0;
        for(int i = 1; i <= 12; i++){
            double r = generarNumeroAleatorio();
            //System.out.println("El random generado es : " + r);
            nuevoValor += r;
            //System.out.println("El random sumado es : " + nuevoValor);
        }
        nuevoValor = nuevoValor - 6; //La sumatoria de los r sumados se les resta un 6.

        double x  = (2*nuevoValor) + 25;
        x = Math.round(x * Math.pow(10, 4)); //Redondear el valor a 4 decimales
        x = x/Math.pow(10, 4);
        return x;
    }

    //Genera el tiempo de uso del dispositivo E/S con una función de densidad.
    public double generarTiempoUsoDispositvo(){
        double x;
        double r = generarNumeroAleatorio(); //Se genera un número random r entra 0 y 1
        x = 20*(Math.sqrt(3*r+1));
        return x;
    }

    //Genera un valor para ver si ocurre o no interrupción. 0-29 Sí ocurre. 30-99 No ocurre
    public int generarInterrupcion(){
        int x;
        x = r.nextInt(100);
        return x;
    }
    //Genera un valor para saber cuál tipo de interrupción es. 0-79 E/S. 80-99 Finaliza.
    public int generarTipoInterrupcion(){
        int x;
        x = r.nextInt(100);
        return x;
    }

    //Genera el tiempo en el cuál ocurre la interrupción.
    public double generarTiempoInterrupcion(double b){
        double x;
        double r = generarNumeroAleatorio(); //Se genera un número random r entra 0 y 1
        x = (b*r + b)/2;
        return x;
    }
}
