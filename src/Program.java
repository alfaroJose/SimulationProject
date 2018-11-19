/**
 *  Class that stores the information of a program
 */
public class Program {

    private int p_id; //identificador del programa
    private double tiempoSistema; //Tiempo total que pasa el programa en el sistema
    private double tiempoActual; //Tiempo en el que se va a ejecutar el evento del programa
    private double tiempoUsoCPU; //Tiempo total que el programa usa el CPU
    private double tiempoUsoES; //Tiempo total que el programa uso el dispositivo E/S
    /*Los eventos pueden ser
    1: Llegada de un programa al sistema
    2: Se libera dispositivo E/S
    3: Se libera CPU */
    private int tipoEvento;

    /*El destino posible puede ser:
    * 1: Salir del sistema (finalizar)
    * 2: Ir al dispositivo/cola E/S
    * 3: Volver al CPU/cola CPU*/
    private int destino;

    public Program(double tiempoCreacion, int id){
        p_id = id;
        tiempoSistema = 0;
        tiempoActual = tiempoCreacion;
        tiempoUsoCPU = 0;
        tiempoUsoES = 0;
    }

    public double getTiempoSistema() {
        return tiempoSistema;
    }

    public void setTiempoSistema(double tiempoSistema) {
        this.tiempoSistema = tiempoSistema;
    }

    public double getTiempoActual() {
        return tiempoActual;
    }

    public void setTiempoActual(double tiempoActual) {
        this.tiempoActual = tiempoActual;
    }

    public double getTiempoUsoCPU() {
        return tiempoUsoCPU;
    }

    public void setTiempoUsoCPU(double tiempoUsoCPU) {
        this.tiempoUsoCPU = tiempoUsoCPU;
    }

    public double getTiempoUsoES() {
        return tiempoUsoES;
    }

    public void setTiempoUsoES(double tiempoUsoES) {
        this.tiempoUsoES = tiempoUsoES;
    }

    public int getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(int tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getP_id() {
        return p_id;
    }
}