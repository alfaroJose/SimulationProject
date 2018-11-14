/**
 *  Class that simulates the execution of a round robin algorithm
 *  with multiple variables
 */

import javax.swing.*;
import java.util.*;

public class Simulator {

    int numCorridas; //Cantidad de veces que se corre la simulacion
    int corridaActual; //Numero de corrida por la que se va
    double tiempoTotal; //Tiempo total que corre cada vez la simulacion
    double reloj; //tiempo de reloj actual
    double quantum;
    String distribuicion;

    NumberGenerator gen;

    public Simulator(int numVeces, double tiempoMax, double q, String h){
        numCorridas = numVeces;
        tiempoTotal = tiempoMax;
        reloj = 0; //Se inicializa el reloj en 0
        corridaActual = 1;
        quantum = q;
        distribuicion = h;
        gen = new NumberGenerator();
    }

    public void runSimulator(){

        while (corridaActual <= numCorridas){
            reloj = 0;

            while(reloj < tiempoTotal){
                reloj = reloj + 1; //solo pruebas
                //System.out.println("reloj  : " + reloj);
            }

            //System.out.println("iteracion actual  : " + corridaActual);
            corridaActual = corridaActual + 1;
        }
        //ouble x = gen.generarNumeroAleatorio();
        //System.out.println("Numero   es : " + x);
        //double y = gen.generarLlegadaExponencial();
        //System.out.println("Tiempo de llegada exponencial es igual a : " + y);
        //double y = gen.generarLlegadaNormal();
        //System.out.println("Tiempo de llegada normal es  : " + y);
        //double y = gen.generarTiempoUsoDispositvo();
        //System.out.println("Tiempo de uso de dispositivo es  : " + y);
        /*int y = gen.generarInterrupcion();
        System.out.println("interrupcion si o no  : " + y);
        y = gen.generarTipoInterrupcion();
        System.out.println("tipo de interrupcion : " + y);
        double x = gen.generarTiempoInterrupcion(10);
        System.out.println("interrupcion si o no  : " + x);*/

        //System.out.println("Num de iteraciones  : " + numCorridas);

        //System.out.println("Tiempo máximo es  : " + tiempoTotal);
        //System.out.println("Tiempo del quantum es  : " + quantum);
        //System.out.println("Distribucion escogida  : " + distribuicion);
    }
}