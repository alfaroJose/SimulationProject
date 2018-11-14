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
    int distribuicion;

    NumberGenerator gen;
    //Se ocupa??
    ArrayList <Program> programas; //almacena las programas de la simulacion para al final de una corrida poder calcular el tiempo que pasa en el sistema promedio
    public static List<Program> listaEventos; //Contiene la lista de los eventos por ejecutar



    public Simulator(int numVeces, double tiempoMax, double quantum, int distribucion){
        numCorridas = numVeces;
        tiempoTotal = tiempoMax;
        reloj = 0; //Se inicializa el reloj en 0
        corridaActual = 1;
        this.quantum = quantum;
        this.distribuicion = distribucion;
        gen = new NumberGenerator();
        programas =  new ArrayList<Program>();
    }

    public void runSimulator(){

        while (corridaActual <= numCorridas){
            reloj = 0;
            listaEventos = new ArrayList<>();
            Program programaActual = new Program(reloj);
            programaActual.setTipoEvento(1); //El primer evento del programa al ser generado es 1
            //programas.add(programaActual);
            agregarEvento(programaActual);
            double x;
            do{
                if(distribuicion == 1){ //Si el tipo de llegada escogida es exponencial
                    x = gen.generarLlegadaExponencial();
                } else {
                    x = gen.generarLlegadaNormal(); //Si el tipo de llega escogida es normal
                }
                //if (reloj+x < tiempoTotal) {
                    programaActual = new Program(x);
                    programaActual.setTipoEvento(1);
                    //programas.add(programaActual);
                    agregarEvento(programaActual);
                    reloj = reloj + x; //solo pruebas
                    System.out.println("reloj  : " + reloj);
                //} else {
                    //reloj = tiempoTotal;
                //}
            } while(reloj < tiempoTotal);

            System.out.println("iteracion actual  : " + corridaActual);
            corridaActual = corridaActual + 1;
        }



        //double x = gen.generarNumeroAleatorio();
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

    public void generarEstadisticas(){

    }

    public static void agregarEvento(Program p) {
        if (listaEventos.isEmpty()) {//En caso que la cola esté vacía
            listaEventos.add(0,p);
        } else {
            Iterator it = listaEventos.iterator();
            Program aux;
            boolean campo = false;
            int espacio = 0;
            while (it.hasNext() && !campo) {
                aux = (Program) it.next();
                if (aux.getTiempoActual() <= p.getTiempoActual()) {
                    ++espacio;
                } else {
                    campo = true;
                }
            }
            listaEventos.add(espacio, p);
        }
    }
}