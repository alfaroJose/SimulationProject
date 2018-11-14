/**
 *  Class that simulates the execution of a round robin algorithm
 *  with multiple variables
 */
public class Simulator {

    public Simulator(){}

    public void runSimulator(){

        NumberGenerator gen = new NumberGenerator();
        //double x = gen.generarNumeroAleatorio();
        //System.out.println("Numero: " + x);
        //double y = gen.generarLlegadaExponencial();
        //System.out.println("Tiempo de llegada exponencial es igual a : " + y);
        //double y = gen.generarLlegadaNormal();
        //System.out.println("Tiempo de llegada normal es  : " + y);
        //double y = gen.generarTiempoUsoDispositvo();
        //System.out.println("Tiempo de uso de dispositivo es  : " + y);
        int y = gen.generarInterrupcion();
        System.out.println("interrupcion si o no  : " + y);
        y = gen.generarTipoInterrupcion();
        System.out.println("tipo de interrupcion : " + y);
        double x = gen.generarTiempoInterrupcion(10);
        System.out.println("interrupcion si o no  : " + x);
    }
}