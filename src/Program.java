/**
 *  Class that stores the information of a program
 */
public class Program {
    //private int p_id;
    double tiempoSistema; //Tiempo total que pasa el programa en el sistema
    double tiempoActual;
    double tiempoUsoCPU; //Tiempo total que el programa usa el CPU
    double tiempoUsoES; //Tiempo total que el programa uso el dispositivo E/S

    public Program(double tiempoCreacion){
        //p_id = 0;
        tiempoSistema = 0;
        tiempoActual = tiempoCreacion;
        tiempoUsoCPU = 0;
        tiempoUsoES = 0;
    }

}