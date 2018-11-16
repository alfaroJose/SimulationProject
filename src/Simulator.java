/**
 *  Class that simulates the execution of a round robin algorithm
 *  with multiple variables
 */

import javax.swing.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Simulator {

    private int numCorridas; //Cantidad de veces que se corre la simulacion
    private int corridaActual; //Numero de corrida por la que se va
    private double tiempoTotal; //Tiempo total que corre cada vez la simulacion
    private double reloj; //tiempo de reloj actual
    private double quantum;
    private int distribuicion;

    private NumberGenerator gen;
    //Se ocupa??
    private ArrayList <Program> programas; //almacena las programas de la simulacion para al final de una corrida poder calcular el tiempo que pasa en el sistema promedio
    public static List<Program> listaEventos; //Contiene la lista de los eventos por ejecutar
    private int tamcolaCPU;
    private int tamcolaES;
    private boolean servidorOcupadoCPU; //Si es false es que está libre



    public Simulator(int numVeces, double tiempoMax, double quantum, int distribucion){
        numCorridas = numVeces;
        tiempoTotal = tiempoMax;
        reloj = 0; //Se inicializa el reloj en 0
        corridaActual = 1;
        this.quantum = quantum;
        this.distribuicion = distribucion;
        gen = new NumberGenerator();
        programas =  new ArrayList<Program>();
        tamcolaCPU = 0;
        tamcolaES = 0;
        servidorOcupadoCPU = false;
    }

    public void runSimulator(){

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        while (corridaActual <= numCorridas){
            System.out.println("iteracion actual  : " + corridaActual);
            reloj = 0;
            tamcolaCPU = 0;
            tamcolaES = 0;
            servidorOcupadoCPU = false;
            listaEventos = new ArrayList<>();
            Program programaActual = new Program(reloj);
            programaActual.setTipoEvento(1); //El primer evento del programa al ser generado es 1 en tiempo 0
            //programas.add(programaActual);
            agregarEvento(programaActual);
            int z;
            int w;
            int d;
            double y;
            double u;
            double x;


            //System.out.println("size  : " + listaEventos.size());
            while(reloj < tiempoTotal){

                programaActual = (Program) listaEventos.get(0);//Tomamos el primer valor de la lista
                listaEventos.remove(0); //Sacamos de la lista el primer evento

                reloj = programaActual.getTiempoActual(); //Reloj = E1 o E2 o E3 dependendiende de la lista de eventos ordenada según tiempo
                System.out.println("Reloj : " + reloj);

                switch (programaActual.tipoEvento) {
                    case 1: //Evento 1
                        System.out.println("Programa está en evento 1");
                        if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado
                            System.out.println("Servidor ocupado : " + servidorOcupadoCPU);
                            tamcolaCPU = tamcolaCPU + 1;
                        } else{ //El servidor está libre
                            System.out.println("Servidor ocupado : " + servidorOcupadoCPU);
                            servidorOcupadoCPU = true; //Lo ponemos en ocupado
                            z = gen.generarInterrupcion();
                            System.out.println("Interrupción en E1 es : " + z);
                            if (z <= 49){ //Sí ocurre una interrupción
                                w = gen.generarTipoInterrupcion();
                                if (w <= 39){ //La interrupción es E/S
                                    programaActual.setDestino(2);
                                    System.out.println("Tipo  de Interrupción en E1 es : " + w + " = E/S");
                                } else {
                                    programaActual.setDestino(1);
                                    System.out.println("Tipo  de Interrupción en E1 es : " + w + " = Finalizar");
                                }

                                y = gen.generarTiempoInterrupcion(quantum); // No hay que sumar q/2 porque el valor generado está entre q/2 y q
                                programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                programaActual.setTipoEvento(3);
                                agregarEvento(programaActual); //Se agrega a la lista
                            } else{ //No ocurre una interrupción
                                programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                programaActual.setTipoEvento(3);
                                programaActual.setDestino(3);
                                agregarEvento(programaActual); //Se agrega a la lista
                            }

                        }

                        //2. Generar tiempo entre arribos
                        if(distribuicion == 1){ //Si el tipo de llegada escogida es exponencial
                            x = gen.generarLlegadaExponencial();
                        } else {
                            x = gen.generarLlegadaNormal(); //Si el tipo de llega escogida es normal
                        }

                        /*El tiempo actual de este programa nuevo es el tiempo en que se creó (x) + el reloj.
                        no hay que setearlo al inicio porque el constructor ya hace eso.*/
                        programaActual = new Program(reloj + x); //Se guarda la hora de arriba del programa en tiempoActual
                        programaActual.setTipoEvento(1);
                        agregarEvento(programaActual);





                        break;
                    case 2: //Evento 2
                        System.out.println("Programa está en evento 2");
                        if (tamcolaES >= 0){
                            tamcolaES = tamcolaES -1;
                            u = gen.generarTiempoUsoDispositvo();
                            programaActual.setTiempoActual(programaActual.getTiempoActual() + u); //E2 = Reloj + Z

                            if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado
                                tamcolaCPU = tamcolaCPU + 1;
                            } else { //El servidor está libre
                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                z = gen.generarInterrupcion();
                                System.out.println("Interrupción en E2 es : " + z);
                                if (z <= 49){ //Sí ocurre una interrupción
                                    w = gen.generarTipoInterrupcion();
                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E2 es : " + w + " = E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E2 es : " + w + " = Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }

                            }
                        } else {
                            servidorOcupadoCPU = false; //no-> estadoServidor = libre. El de CPU o E/S?
                        }


                        break;

                    case 3: //Evento 3
                        System.out.println("Programa está en evento 3");
                        if(tamcolaCPU >= 0){
                            tamcolaCPU = tamcolaCPU - 1;
                            //Cómo saber cuando el programa libera CPU y debe volver a cola de CPU?
                            //Se sabe con getDestino del programa actual
                            d = programaActual.getDestino();
                            if (d == 1){ //El destino es salir del sistema
                                System.out.println("Programa ha finalizado");
                            } else if (d == 2){ //El destino es ir a E/S
                                programaActual.setTipoEvento(2);
                                agregarEvento(programaActual);
                            } else { //d == 3 El destino es ir a CPU
                                //Lo mismo de E1 sin generar llegada

                                if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado
                                    tamcolaCPU = tamcolaCPU + 1;
                                } else { //El servidor está libre
                                    servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                    z = gen.generarInterrupcion();
                                    System.out.println("Interrupción en E2 es : " + z);
                                    if (z <= 49){ //Sí ocurre una interrupción
                                        w = gen.generarTipoInterrupcion();
                                        if (w <= 39){ //La interrupción es E/S
                                            programaActual.setDestino(2);
                                            System.out.println("Tipo  de Interrupción después de E2 es : " + w + " = E/S");
                                        } else {
                                            programaActual.setDestino(1);
                                            System.out.println("Tipo  de Interrupción después de E2 es : " + w + " = Finalizar");
                                        }
                                        y = gen.generarTiempoInterrupcion(quantum);
                                        programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                        programaActual.setTipoEvento(3);
                                        agregarEvento(programaActual); //Se agrega a la lista
                                    } else{ //No ocurre una interrupción
                                        programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                        programaActual.setTipoEvento(3);
                                        programaActual.setDestino(3);
                                        agregarEvento(programaActual); //Se agrega a la lista
                                    }

                                }

                            }

                        } else {
                            servidorOcupadoCPU = false; //no-> estadoServidor = libre
                        }
                        programaActual.setTiempoActual(programaActual.getTiempoActual() + 15); //Es solo una prueba
                        programaActual.setTipoEvento(3);
                        agregarEvento(programaActual); //Se agrega a la lista

                        break;
                }


                /*if(distribuicion == 1){ //Si el tipo de llegada escogida es exponencial
                    x = gen.generarLlegadaExponencial();
                } else {
                    x = gen.generarLlegadaNormal(); //Si el tipo de llega escogida es normal
                }*/

                //programaActual = new Program(x);
                //programaActual.setTipoEvento(1);
                //programas.add(programaActual);
                //agregarEvento(programaActual);
                //reloj = reloj + x; //solo pruebas
                //reloj = Double.parseDouble(df.format(reloj));
                //System.out.println("reloj  : " + reloj);
            }

            corridaActual = corridaActual + 1;
        }

        //imprimir datos de lista
        /*for ( Program p : listaEventos) {
            System.out.println(p.getTiempoActual());
        }*/



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