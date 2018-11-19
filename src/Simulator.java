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

    private  ArrayList <Program> colaES; //almacena los programas que están en la cola de E/S esperando poder usar el dispositvo
    private ArrayList <Program> colaCPU; //almacena las programas que están en la Cola del CPU esperando poder usarlo
    private ArrayList <Program> programas; //almacena las programas de la simulacion para al final de una corrida poder calcular las estadísticas
    public static List<Program> listaEventos; //Contiene la lista de los eventos por ejecutar
    private int longitudColaCPU;
    private int longitudColaES;
    private boolean servidorOcupadoCPU; //Si es false es que está libre
    private boolean servidorOcupadoES; //Si es false es que está libre
    private int contadorProgramas; //Lleva el contador de programas que salen del sistema


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
        contadorProgramas = 0;
    }

    public void runSimulator(){

        //Promedios finales de todas las corridas.
        double tiempoSistemaFinal = 0;
        double tiempoUsoCPUFinal = 0;
        double tiempoUsoESFinal = 0;
        double tiempoOcupacionCPUFinal = 0;
        double tiempoColasFinal = 0;
        double eficienciaFinal = 0;


        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        while (corridaActual <= numCorridas){
            System.out.println("Iteración actual  : " + corridaActual);
            reloj = 0;
            longitudColaCPU = 0;
            longitudColaES = 0;
            contadorProgramas = 0;
            int id = 1;
            servidorOcupadoCPU = false;
            listaEventos = new ArrayList<>();
            Program programaActual = new Program(reloj, id);
            programaActual.setTipoEvento(1); //El primer evento del programa al ser generado es llegar al sistema en tiempo 0
            programas.add(programaActual); //Se añade a la lista de programas
            agregarEvento(programaActual); //Se mete el programa a una cola de Eventos
            int z;
            int w;
            int d;
            double y;
            double u;
            double x;

            while(reloj < tiempoTotal){

                programaActual = (Program) listaEventos.get(0);//Tomamos el primer valor de la lista
                listaEventos.remove(0); //Sacamos de la lista el primer evento
                reloj = programaActual.getTiempoActual(); //Reloj = E1 o E2 o E3 dependendiende de la lista de eventos ordenada según tiempo
                System.out.println("Reloj : " + reloj);
                System.out.println("Servidor CPU ocupado : " + servidorOcupadoCPU);
                System.out.println("Servidor ES ocupado : " + servidorOcupadoES);
                System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                System.out.println("Longitud de colaES : " + longitudColaES);

                switch (programaActual.tipoEvento) {
                    case 1: //Evento 1
                        System.out.println("Programa " + programaActual.getP_id() + " está en evento 1 :  llegada al sistema");

                        if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado
                            longitudColaCPU = longitudColaCPU + 1;
                            System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                            colaCPU.add(programaActual);

                        } else{ //El servidor está libre
                            servidorOcupadoCPU = true; //Lo ponemos en ocupado
                            System.out.println("Ahora el servidor CPU ocupado : " + servidorOcupadoCPU);
                            z = gen.generarInterrupcion();

                            if (z <= 49){ //Sí ocurre una interrupción
                                w = gen.generarTipoInterrupcion();
                                System.out.println("Sí ocurre interrupción en E1");

                                if (w <= 39){ //La interrupción es E/S
                                    programaActual.setDestino(2); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                    System.out.println("Tipo  de Interrupción en E1 es para usar dispositivo E/S");
                                } else {
                                    programaActual.setDestino(1); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                    System.out.println("Tipo  de Interrupción en E1 es para Finalizar");
                                }

                                y = gen.generarTiempoInterrupcion(quantum); // No hay que sumar q/2 porque el valor generado está entre q/2 y q
                                programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);
                                programaActual.setTipoEvento(3);
                                agregarEvento(programaActual); //Se agrega a la cola de Eventos

                            } else{ //No ocurre una interrupción
                                System.out.println("No ocurre interrupción en E1");
                                programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
                                programaActual.setTipoEvento(3);
                                programaActual.setDestino(3);  //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                agregarEvento(programaActual); //Se agrega a la cola de Eventos
                            }
                        }

                        //2. Generar tiempo entre arribos
                        if(distribuicion == 1){ //Si el tipo de llegada escogida es exponencial
                            x = gen.generarLlegadaExponencial();
                        } else { //Si el tipo de llega escogida es normal
                            x = gen.generarLlegadaNormal();
                        }

                        /*El tiempo actual de este programa nuevo es el tiempo en que se creó (x) + el reloj.
                        no hay que setearlo el tiempo actual porque el constructor ya hace eso.*/
                        id = id + 1;
                        programaActual = new Program(reloj + x, id); //Se guarda la hora de arriba del programa en tiempoActual
                        programaActual.setTipoEvento(1);
                        agregarEvento(programaActual);
                        /*Si el tiempo de llegada se excede al máximo de la simulación no hay que contarlo para las estadísticas
                        * por lo tanto no hay que agregarlo ya que nunca va a llegar al al sistema*/
                        if (programaActual.getTiempoActual() <= tiempoTotal) {
                            programas.add(programaActual); //Se añade el nuevo programa a la lista de programas
                        }

                        break;

                    case 2: //Evento 2
                        System.out.println("Programa " + programaActual.getP_id() + " está en evento 2 : liberar E/S");
                        servidorOcupadoES = false; //Se libera E/S
                        System.out.println("Servidor ES ocupado : " + servidorOcupadoES);

                        if (longitudColaES > 0){
                            System.out.println("Longitud de colaES es : " + longitudColaES);
                            longitudColaES = longitudColaES - 1;
                            Program programaSiguiente = (Program) colaES.get(0);//Tomamos el primer valor de la lista
                            colaES.remove(0); //Sacamos de la lista el primer evento
                            //El programa que estaba esperando para usar E/S ya puede usarlo y cálcula el tiempo que duro usando E/S
                            programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + (reloj - programaSiguiente.getTiempoActual()));
                            programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + (reloj - programaSiguiente.getTiempoActual()));
                            //Se calcula el tiempo que va a durar usando E/S
                            u = gen.generarTiempoUsoDispositvo();
                            //se suma tiempo de uso y tiempo en el sistema y tiempo actual del programa:
                            programaSiguiente.setTiempoActual(programaSiguiente.getTiempoActual() + u); //E2 = Reloj + Z
                            programaSiguiente.setTiempoUsoES(programaSiguiente.getTiempoUsoES() + u);
                            programaSiguiente.setTiempoSistema(programaSiguiente.getTiempoSistema() + u);
                            programaSiguiente.setTipoEvento(2);
                            agregarEvento(programaSiguiente);
                            servidorOcupadoES = true; //El programaSiguiente es ahora quien está usando E/S
                            System.out.println("Ahora servidor ES ocupado : " + servidorOcupadoES);

                            //El programa que acaba de liberar E/S debe volver al CPU
                            System.out.println("Servidor CPU ocupado : " + servidorOcupadoCPU);
                            if (servidorOcupadoCPU){ //Si está ocupado se mete en cola
                                longitudColaCPU = longitudColaCPU + 1;
                                System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                                programaActual.setTipoEvento(3);
                                colaCPU.add(programaActual);
                            } else { //No hay nadie usando CPU por lo que el programa actual que acaba de liberar E/S puede ir a a usar CPU y generamos valores para ver si ocurre interrupcion

                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                System.out.println("Ahora el servidor CPU ocupado : " + servidorOcupadoCPU);
                                z = gen.generarInterrupcion();

                                if (z <= 49){ //Sí ocurre una interrupción
                                    w = gen.generarTipoInterrupcion();
                                    System.out.println("Sí ocurre interrupción en E2");

                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                        System.out.println("Tipo  de Interrupción en E2 es para usar dispositivo E/S");
                                    } else {
                                        programaActual.setDestino(1); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                        System.out.println("Tipo  de Interrupción en E2 es para Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);

                                } else{ //No ocurre una interrupción
                                    System.out.println("No ocurre interrupción en E2");
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
                                    programaActual.setDestino(3); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                }

                                programaActual.setTipoEvento(3);
                                agregarEvento(programaActual);
                            }

                        } else { //No hay nadie esperando para usar E/S

                            /*El programa se libera de E/S hasta seg x y luego va a usar CPU entonces generamos valores aleaorios
                            * para mandar a E3 con destino correspondiente si preguntamos por el servidorCPU ocupado estaríamos preguntando en el tiempo de reloj actual
                            * no el tiempo en que E/S se libere y no sabemos si CPU va a estar ocupado en ese momento*/
                            if (servidorOcupadoCPU){ //Si el servidor del CPU está ocupado lo metemos en cola y ponemos el evento en 3
                                longitudColaCPU = longitudColaCPU + 1;
                                System.out.println("Longitud de colaCPU : " + longitudColaCPU);
                                programaActual.setTipoEvento(3);
                                colaCPU.add(programaActual); //Se mete a la cola de CPU
                            } else { //El servidorCPU está libre:*/
                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                System.out.println("Ahora el servidor CPU ocupado : " + servidorOcupadoCPU);
                                z = gen.generarInterrupcion();

                                if (z <= 49){ //Sí ocurre una interrupción
                                    w = gen.generarTipoInterrupcion();
                                    System.out.println("Sí ocurre interrupción en E2");

                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                        System.out.println("Tipo  de Interrupción en E2 es para usar dispositivo E/S");
                                    } else {
                                        programaActual.setDestino(1); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                        System.out.println("Tipo  de Interrupción en E2 es para Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);
                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la cola de Eventos

                                } else{ //No ocurre una interrupción
                                    System.out.println("No ocurre interrupción en E2");
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3); //Con esto sabemos que al programa va a liberar CPU y que es lo que hace después
                                    agregarEvento(programaActual); //Se agrega a la cola de Eventos
                                }

                            }
                        }

                        break;

                    case 3: //Evento 3
                        System.out.println("Programa " + programaActual.getP_id() + " está en evento 3 : liberar CPU");
                        d = programaActual.getDestino(); //Se sabe que si el peograma va a salir del sistema, usar E/S o usar CPU de nuevo

                        if (d == 1){ //El destino es salir del sistema
                            //Solamente libera CPU
                            System.out.println("Destino es salir de sistema");
                            System.out.println("¡Programa " + programaActual.getP_id() + " ha finalizado es decir salió del sistema!");
                            contadorProgramas++; //Se suma al contador de programas que han finalizado
                            System.out.println("Cantidad de programas que han salido del sistema : " + contadorProgramas);
                            servidorOcupadoCPU = false;
                            System.out.println("Ahora el servidor CPU ocupado : " + servidorOcupadoCPU);

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
                                System.out.println("Ahora el servidor CPU ocupado por siguiente programa en cola : " + servidorOcupadoCPU);
                                z = gen.generarInterrupcion();

                                if (z <= 49){ //Sí ocurre una interrupción
                                    System.out.println("Sí ocurre interrupción en E3");
                                    w = gen.generarTipoInterrupcion();

                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E3 es  para usar dispositivo E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E3 es  para Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);
                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    System.out.println("No ocurre interrupción en E3");
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }
                            } //else no hay cola en CPU y programa acaba de salir del sistema

                        } else if (d == 2){ //El destino es ir a E/S
                            System.out.println("Destino es usar dispositivo E/S");
                            servidorOcupadoCPU = false;
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
                                        System.out.println("Sí ocurre interrupción en E3");
                                        w = gen.generarTipoInterrupcion();
                                        if (w <= 39){ //La interrupción es E/S
                                            programaActual.setDestino(2);
                                            System.out.println("Tipo  de Interrupción después de E3 es  para usar dispositivo E/S");
                                        } else {
                                            programaActual.setDestino(1);
                                            System.out.println("Tipo  de Interrupción después de E3 es  para Finalizar");
                                        }
                                        y = gen.generarTiempoInterrupcion(quantum);
                                        programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                        programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                        programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);
                                        programaActual.setTipoEvento(3);
                                        agregarEvento(programaActual); //Se agrega a la lista
                                    } else{ //No ocurre una interrupción
                                        System.out.println("No ocurre interrupción en E3");
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
                            System.out.println("Destino es volver a usar CPU");

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
                                    System.out.println("Sí ocurre interrupción en E3");
                                    w = gen.generarTipoInterrupcion();
                                    if (w <= 39){ //La interrupción es E/S
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E3 es  para usar dispositivo E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E3 es  para Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);
                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                } else{ //No ocurre una interrupción
                                    System.out.println("No ocurre interrupción en E3");
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la lista
                                }
                            } else {
                                //Cómo no no hay nadie más que quiere usar CPU, puedo ir directamente al servidor de CPU sin hacer cola

                                //Lo mismo de E1 sin generar llegada
                                servidorOcupadoCPU = true; //Lo ponemos en ocupado
                                z = gen.generarInterrupcion();

                                if (z <= 49){ //Sí ocurre una interrupción
                                    System.out.println("Sí ocurre interrupción en E3");
                                    w = gen.generarTipoInterrupcion();

                                    if (w <= 39){ //La interrupción es E/S
                                        /*Preguntamos por el sevidor de de E/S si no hay nadie se puede mandar director a liberar E/S
                                        pero si sí hay que mandar a cola y esperar, esto lo preguntamos cuando se vuelve a liberar CPU y el destino es 2 (E/S)
                                         */
                                        programaActual.setDestino(2);
                                        System.out.println("Tipo  de Interrupción después de E3 es  para usar dispositivo E/S");
                                    } else {
                                        programaActual.setDestino(1);
                                        System.out.println("Tipo  de Interrupción después de E3 es  para Finalizar");
                                    }
                                    y = gen.generarTiempoInterrupcion(quantum);
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + y);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + y);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + y);
                                    programaActual.setTipoEvento(3);
                                    agregarEvento(programaActual); //Se agrega a la cola de Eventos

                                } else{ //No ocurre una interrupción
                                    System.out.println("No ocurre interrupción en E3");
                                    programaActual.setTiempoActual(programaActual.getTiempoActual() + quantum);
                                    programaActual.setTiempoUsoCPU(programaActual.getTiempoUsoCPU() + quantum);
                                    programaActual.setTiempoSistema(programaActual.getTiempoSistema() + quantum);
                                    programaActual.setTipoEvento(3);
                                    programaActual.setDestino(3);
                                    agregarEvento(programaActual); //Se agrega a la cola de Eventos
                                }
                            }
                        }
                        break;

                }
            }


            System.out.printf("---------PROMEDIOS DE LA CORRIDA %d--------  %n", corridaActual );
            double tiempoPromedioSistema = calcularTiempoSistema();
            System.out.println("El tiempo promedio total en el sistema para un programa es : " + tiempoPromedioSistema);
            tiempoSistemaFinal += tiempoPromedioSistema;

            double tiempoUsoCPUTotal = calcularTiempoUsoCPU();
            System.out.println("El tiempo promedio por programa de uso de CPU es : " + tiempoUsoCPUTotal);
            tiempoUsoCPUFinal += tiempoUsoCPUTotal;

            double tiempoUsoESTotal = calcularTiempoUsoES();
            System.out.println("El tiempo promedio de uso de disposotivo de E/S por cada programa es: " + tiempoUsoESTotal);
            tiempoUsoESFinal += tiempoUsoESTotal;

            System.out.println("El tiempo promedio de ocupacion del CPU es: " + calcularTiempoOcupacionCPU());
            tiempoOcupacionCPUFinal += calcularTiempoOcupacionCPU();

            System.out.println("El tiempo promedio en colas es: " + calcularTiempoColas());
            tiempoColasFinal += calcularTiempoColas();

            System.out.println("El coeficiente de eficiencia es: " + calcularEficiencia());
            eficienciaFinal += calcularEficiencia();


            corridaActual = corridaActual + 1;
        }

        System.out.printf("---------PROMEDIOS FINALES DE LAS %d CORRIDAS--------  %n", numCorridas );
        System.out.println("El tiempo promedio total en el sistema para un programa es: " + tiempoSistemaFinal / numCorridas );
        System.out.println("El tiempo promedio por programa de uso de CPU es: " + tiempoUsoCPUFinal / numCorridas );
        System.out.println("El tiempo promedio de uso de disposotivo de E/S por cada programa es: " + tiempoUsoESFinal / numCorridas );
        System.out.println("El tiempo promedio de ocupacion del CPU es: " + tiempoOcupacionCPUFinal / numCorridas );
        System.out.println("El tiempo promedio en colas es: " + tiempoColasFinal / numCorridas );
        System.out.println("El coeficiente de eficiencia es: " + eficienciaFinal / numCorridas );

    }

    /*Calcular el tiempo promedio total en el sistema para un programa*/
    public double calcularTiempoSistema(){
        /*Recorremos la lista de programas, sumamos todos los tiempos en el sitema
        y dividimos entre el tamaño de la lista (cantidad de programas)*/
        int cantidad = programas.size();
        double sumatoria = 0;
        double total = 0;
        //System.out.println("cantidad de programas :" + cantidad);
        for (int i = 0; i < cantidad; i++){
            Program e = (Program) programas.get(i);
            sumatoria = sumatoria + e.getTiempoSistema();
            //System.out.println("Programa : " +e.getP_id() + " tiempo sistema : " + e.getTiempoSistema());

        }
        total = sumatoria/cantidad;
        return total;

    }

    /*Calcular el tiempo promedio por programa de uso de CPU*/
    public double calcularTiempoUsoCPU(){
        /*Recorremos la lista de programas, sumamos todos los tiempos en el sitema
        y dividimos entre el tamaño de la lista (cantidad de programas)*/
        int cantidad = programas.size();
        double sumatoria = 0;
        double total = 0;
        //System.out.println("cantidad de programas :" + cantidad);
        for (int i = 0; i < cantidad; i++){
            Program e = (Program) programas.get(i);
            sumatoria = sumatoria + e.getTiempoUsoCPU();
            //System.out.println("Programa : " + e.getP_id() + " tiempo uso CPU : " + e.getTiempoUsoCPU());
        }
        total = sumatoria/cantidad;
        return total;
    }

    /*Calcular el tiempo promedio de uso de dispositivo de E/S por cada programa*/
    public double calcularTiempoUsoES(){
        /*Recorremos la lista de programas, sumamos todos los tiempos en el sitema
        y dividimos entre el tamaño de la lista (cantidad de programas)*/
        int cantidad = programas.size();
        double sumatoria = 0;
        double total = 0;
        //System.out.println("cantidad de programas :" + cantidad);
        for (int i = 0; i < cantidad; i++){
            Program e = (Program) programas.get(i);
            sumatoria = sumatoria + e.getTiempoUsoES();
            //System.out.println("Programa : " + e.getP_id() + " tiempo uso E/S : " + e.getTiempoUsoES());
        }
        total = sumatoria/cantidad;
        return total;
    }

    /*Calcular la tasa de ocupacion del CUP: tiempo de uso entre el tiempo total de simulacion*/
    public double calcularTiempoOcupacionCPU(){
        return  calcularTiempoUsoCPU() / tiempoTotal;
    }

    /*Calcular tiempo en colas: tiempo total - ( tiempo CPU + tiempo E/S)*/
    public double calcularTiempoColas(){
        return calcularTiempoSistema() - ( calcularTiempoUsoCPU() + calcularTiempoUsoES() );
    }

    /*Calcular coeficiente de eficiencia: tiempo en colas  / tiempo total*/
    public double calcularEficiencia(){
        return calcularTiempoColas() / calcularTiempoSistema();
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