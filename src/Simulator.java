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

    private  ArrayList <Program> colaES;
    private ArrayList <Program> colaCPU; //almacena las programas de la simulacion para al final de una corrida poder calcular el tiempo que pasa en el sistema promedio
    //Se ocupa??
    private ArrayList <Program> programas; //almacena las programas de la simulacion para al final de una corrida poder calcular el tiempo que pasa en el sistema promedio
    public static List<Program> listaEventos; //Contiene la lista de los eventos por ejecutar
    private int longitudColaCPU;
    private int longitudColaES;
    private boolean servidorOcupadoCPU; //Si es false es que está libre
    private boolean servidorOcupadoES;



    public Simulator(int numVeces, double tiempoMax, double quantum, int distribucion){
        numCorridas = numVeces;
        tiempoTotal = tiempoMax;
        reloj = 0; //Se inicializa el reloj en 0
        corridaActual = 1;
        this.quantum = quantum;
        this.distribuicion = distribucion;
        gen = new NumberGenerator();
        programas =  new ArrayList<Program>();
        colaCPU = new ArrayList<Program>();
        colaES = new ArrayList<Program>();
        longitudColaCPU = 0;
        longitudColaES = 0;
        servidorOcupadoCPU = false;
        servidorOcupadoES = false;
    }

    public void runSimulator(){

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        while (corridaActual <= numCorridas){
            System.out.println("iteracion actual  : " + corridaActual);
            reloj = 0;
            longitudColaCPU = 0;
            longitudColaES = 0;
            int id = 1;
            servidorOcupadoCPU = false;
            listaEventos = new ArrayList<>();
            Program programaActual = new Program(reloj, id);
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
                        System.out.println("Programa " + programaActual.getP_id() + " está en evento 1");
                        System.out.println("Servidor CPU ocupado : " + servidorOcupadoCPU);

                        if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado
                            longitudColaCPU = longitudColaCPU + 1;
                            System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                            colaCPU.add(programaActual);
                            //agregarEvento(programaActual); Se pierde el evento

                        } else{ //El servidor está libre
                            servidorOcupadoCPU = true; //Lo ponemos en ocupado
                            System.out.println("Servidor CPU ocupado : " + servidorOcupadoCPU);

                            z = gen.generarInterrupcion();
                            if (z <= 49){ //Sí ocurre una interrupción
                                w = gen.generarTipoInterrupcion();
                                System.out.println("Interrupción en E1 es : " + z + " = Sí ocurre");

                                if (w <= 39){ //La interrupción es E/S
                                    programaActual.setDestino(2);
                                    System.out.println("Tipo  de Interrupción en E1 es : " + w + " = E/S");
                                } else {
                                    programaActual.setDestino(1);
                                    System.out.println("Tipo  de Interrupción en E1 es : " + w + " = Finalizar");
                                }

                                y = gen.generarTiempoInterrupcion(quantum); // No hay que sumar q/2 porque el valor generado está entre q/2 y q
                                programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);

                                programaActual.setTipoEvento(3);
                                agregarEvento(programaActual); //Se agrega a la lista
                            } else{ //No ocurre una interrupción
                                System.out.println("Interrupción en E1 es : " + z + " = No ocurre");

                                programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
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
                        id = id + 1;
                        programaActual = new Program(reloj + x, id); //Se guarda la hora de arriba del programa en tiempoActual
                        programaActual.setTipoEvento(1);
                        agregarEvento(programaActual);


                        break;



                    case 2: //Evento 2
                        System.out.println("Programa " + programaActual.getP_id() + " está en evento 2");
                        if (longitudColaES > 0){
                            System.out.println("Longitud de colaES es : " + longitudColaES);
                            servidorOcupadoES = false;
                            longitudColaES = longitudColaES - 1;
                            Program programaSiguiente = (Program) colaES.get(0);//Tomamos el primer valor de la lista
                            colaES.remove(0); //Sacamos de la lista el primer evento
                            //El programa que estaba esperando para usar E/S ya puede usarlo y cálcula el tiempo que duro usando E/S
                            programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + (reloj - programaSiguiente.getTiempoActual()));
                            programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + (reloj - programaSiguiente.getTiempoActual()));

                            u = gen.generarTiempoUsoDispositvo();
                            //se suma tiempo de uso y tiempo en el sistema y tiempo actual del programa:
                            programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + u); //E2 = Reloj + Z
                            programaActual.setTiempoUsoES(programaSiguiente.getTiempoUsoES() + u);
                            programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + u);
                            programaSiguiente.setTipoEvento(2);
                            agregarEvento(programaSiguiente);

                            //El programa que acaba de liberar E/S debe volver al CPU
                            if (servidorOcupadoCPU){
                                System.out.println("Servidor CPU ocupado : " + servidorOcupadoCPU);
                                longitudColaCPU = longitudColaCPU + 1;
                                programaActual.setTipoEvento(3);
                                programaActual.setDestino(3);
                                colaCPU.add(programaActual);
                            } else { //No hay nadie usando CPU por lo que el programa actual que acaba de liberar E/S puede ir a liberar CPU
                                System.out.println("Servidor CPU ocupado : " + servidorOcupadoCPU);
                                programaActual.setDestino(3);
                                programaActual.setTipoEvento(3);
                                agregarEvento(programaActual);

                            }


                        } else { //No hay nadie esperando para usar E/S
                            //servidorOcupadoCPU = false; //no-> estadoServidor = libre. El de CPU o E/S?
                            servidorOcupadoES = false;

                            //u = gen.generarTiempoUsoDispositvo();
                            //se suma tiempo de uso y tiempo en el sistema y tiempo actual del programa:
                            //programaActual.setTiempoActual(programaActual.getTiempoActual() + u); //E2 = Reloj + Z
                            //programaActual.setTiempoUsoES(programaActual.getTiempoUsoES() + u);
                            //programaActual.setTiempoSistema(programaActual.getTiempoSistema() + u);
                            /*El programa se libera de E/S hasta seg x y luego va a usar CPU entonces generamos valores aleaorios
                            * para mandar a E3 con destino correspondiente si preguntamos por el servidorCPU ocupado estaríamos preguntando en el tiempo de reloj actual
                            * no el tiempo en que E/S se libere y no sabemos si CPU va a estar ocupado en ese momento*/
                            if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado lo metemos en cola y ponemos el evento en 3
                                longitudColaCPU = longitudColaCPU + 1;
                                programaActual.setTipoEvento(3);
                                colaCPU.add(programaActual);
                                //servidorOcupadoES = false;

                            } else { //El servidor está libre:*/
                                //servidorOcupadoES = false; //Ya se liberó E/S y ahora vuelve a CPU
                                //servidorOcupadoCPU = true; //Lo ponemos en ocupado
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
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);

                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);

                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }

                            }

                        }


                        break;

                    case 3: //Evento 3
                        System.out.println("Programa " + programaActual.getP_id() + " está en evento 3");
                        //Cómo saber cuando el programa libera CPU y debe volver a cola de CPU o salir o ir a E/S?
                        //Se sabe con getDestino del programa actual
                        d = programaActual.getDestino();
                        System.out.println("destino es : " + d);

                        if (d == 1){ //El destino es salir del sistema
                            //Solamente libera CPU
                            System.out.println("¡Programa " + programaActual.getP_id() + " ha finalizado es decir salió del sistema!");


                            //Donde va el if de longitudColaCPU? que hace cuando el destino es solo salir del sistema, liberar cpu pero hay gente en colade cpu
                            //no se ocupa sí se pregunta longcola>0 al final

                            servidorOcupadoCPU = false;
                            //Hay que preguntar por la cola para poner a usar CPU al siguuiente programa si es que hay
                            if (longitudColaCPU > 0){ //cuando no hay cola no pasa nada
                                System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                                longitudColaCPU = longitudColaCPU - 1;
                                Program programaSiguiente = (Program) colaCPU.get(0);//Tomamos el primer valor de la lista
                                colaCPU.remove(0); //Sacamos de la lista el primer evento
                                programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + (reloj - programaSiguiente.getTiempoActual()));
                                programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + (reloj - programaSiguiente.getTiempoActual()));
                                programaActual = programaSiguiente;

                                //Lo mismo de E1 sin generar llegada
                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                z = gen.generarInterrupcion();
                                if (z <= 49){ //Sí ocurre una interrupción
                                    System.out.println("Interrupción en E3 es : " + z + "= Sí ocurre");

                                    w = gen.generarTipoInterrupcion();
                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);


                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    System.out.println("Interrupción en E3 es : " + z + "= NO ocurre");

                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);


                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }
                            } //else no hay cola en CPU y programa acaba de salir del sistema

                        } else if (d == 2){ //El destino es ir a E/S
                            servidorOcupadoCPU = false; //?redundante
                            if (servidorOcupadoES){ /*No se puede usar E/S porque hay otro programa usándolo. Se manda a la cola y se libera el CPU*/
                                longitudColaES++;
                                colaES.add(programaActual);
                            } else { //Se puede liberar E/S
                                servidorOcupadoES = true;
                                u = gen.generarTiempoUsoDispositvo();
                                //se suma tiempo de uso y tiempo en el sistema y tiempo actual del programa:
                                programaActual.setTiempoActual(programaActual.getTiempoActual() + u); //E2 = Reloj + Z
                                programaActual.setTiempoUsoES(programaActual.getTiempoUsoES() + u);
                                programaActual.setTiempoSistema(programaActual.getTiempoSistema() + u);

                                programaActual.setTipoEvento(2);
                                agregarEvento(programaActual);



                                //se pregunta si hay cola de CPU para poner a usarlo
                                if (longitudColaCPU > 0){
                                    //sacar de en cola al siguiente para que pueda usar cpu
                                    System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                                    longitudColaCPU = longitudColaCPU - 1;
                                    Program programaSiguiente = (Program) colaCPU.get(0);//Tomamos el primer valor de la lista
                                    colaCPU.remove(0); //Sacamos de la lista el primer evento
                                    programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + (reloj - programaSiguiente.getTiempoActual()));
                                    programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + (reloj - programaSiguiente.getTiempoActual()));
                                    //El que estaba en cola empieza a usar CPU
                                    programaActual = programaSiguiente;
                                    //Lo mismo de E1 sin generar llegada
                                    servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                    z = gen.generarInterrupcion();
                                    if (z <= 49){ //Sí ocurre una interrupción
                                        System.out.println("Interrupción en E3 es : " + z + "= Sí ocurre");

                                        w = gen.generarTipoInterrupcion();
                                        if (w <= 39){ //La interrupción es E/S
                                            programaActual.setDestino(2);
                                            System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = E/S");
                                        } else {
                                            programaActual.setDestino(1);
                                            System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = Finalizar");
                                        }
                                        y = gen.generarTiempoInterrupcion(quantum);
                                        programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                        programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                        programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);


                                        programaActual.setTipoEvento(3);
                                        agregarEvento(programaActual); //Se agrega a la lista
                                    } else{ //No ocurre una interrupción
                                        System.out.println("Interrupción en E3 es : " + z + "= NO ocurre");

                                        programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                        programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                        programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);


                                        programaActual.setTipoEvento(3);
                                        programaActual.setDestino(3);
                                        agregarEvento(programaActual); //Se agrega a la lista
                                    }
                                }
                            }


                        } else { //d == 3 El destino es volver a CPU
                            //Lo mismo de E1 sin generar llegada
                            //Primero libero el CPU, luego pregunto si hay gente en cola, sino puedo volver a usarlo,
                            //pero si sí tengo que meterme a la cola y esperar.
                            servidorOcupadoCPU = false; //Se libera CPU
                            if (longitudColaCPU > 0){
                                //sacar de en cola al siguiente para que pueda usar cpu
                                System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                                longitudColaCPU = longitudColaCPU - 1;
                                Program programaSiguiente = (Program) colaCPU.get(0);//Tomamos el primer valor de la lista
                                colaCPU.remove(0); //Sacamos de la lista el primer evento
                                programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + (reloj - programaSiguiente.getTiempoActual()));
                                programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + (reloj - programaSiguiente.getTiempoActual()));
                                //Al programa actual que acaba de liberar CPU hay que mandarlo a la cola y al programaSig de la cola hay que ponerlo a uasar CPU
                                colaCPU.add(programaActual);
                                longitudColaCPU++;
                                //El que estaba en cola empieza a usar CPU
                                programaActual = programaSiguiente;
                                //Lo mismo de E1 sin generar llegada
                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                z = gen.generarInterrupcion();
                                if (z <= 49){ //Sí ocurre una interrupción
                                    System.out.println("Interrupción en E3 es : " + z + "= Sí ocurre");

                                    w = gen.generarTipoInterrupcion();
                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);


                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    System.out.println("Interrupción en E3 es : " + z + "= NO ocurre");

                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);


                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }


                            } else {
                                //Cómo no no hay nadie más que quiere usar CPU, puedo ir directamente al servidor de CPU sin hacer cola
                                System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                                //servidorOcupadoCPU = false; //no-> estadoServidor = libre

                                //Lo mismo de E1 sin generar llegada
                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                z = gen.generarInterrupcion();
                                if (z <= 49){ //Sí ocurre una interrupción
                                    System.out.println("Interrupción en E3 es : " + z + "= Sí ocurre");

                                    w = gen.generarTipoInterrupcion();
                                    if (w <= 39){ //La interrupción es E/S
                                        /*Preguntamos por el sevidor de de E/S si no hay nadie se puede mandar director a liberar E/S
                                        pero si sí hay que mandar a cola y esperar, esto lo preguntamos cuando se vuelve a liberar CPU y el destino es 2 (E/S)
                                         */
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E3 es : " + w + " = Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);


                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    System.out.println("Interrupción en E3 es : " + z + "= NO ocurre");

                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);


                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }
                            }
                        }
                        break;
                }
            }

            corridaActual = corridaActual + 1;
        }

        //imprimir datos de lista
        /*for ( Program p : listaEventos) {
            System.out.println(p.getTiempoActual());
        }*/

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