
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Interfaz extends Application {

    public void start(Stage primaryStage) {
        
        
        primaryStage.setTitle("JavaFX Welcome");
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        //Titulo
        Text scenetitle = new Text("simulacion");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        //Etiqueta distribucion
        Label cbLabel = new Label("distribucion:");
        grid.add(cbLabel, 0, 1);

        //Choicebox istribucion
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                "normal", "exponencial")
        );
        
        //Muestra primera opcion en choicebox
        cb.getSelectionModel().selectFirst();
        grid.add(cb, 1, 1);

        //Etiqueta tiempo quantum
        Label tiempoLabel = new Label("quantum (ms):");
        grid.add(tiempoLabel, 0, 2);

        //Slider tiempo de quantum de min 0 max 1000, colocado en 500
        Slider tiempoSlider = new Slider(0, 1000, 500);
        tiempoSlider.setShowTickMarks(true);
        tiempoSlider.setShowTickLabels(true);
        tiempoSlider.setMajorTickUnit(250);
        tiempoSlider.setMinorTickCount(0);
        tiempoSlider.setBlockIncrement(1);
        tiempoSlider.setSnapToTicks(true);
        
        grid.add(tiempoSlider, 0, 3);

        //Muestra el valor del slider
        Label valorTiempoLabel = new Label(Integer.toString((int) tiempoSlider.getValue()));
        grid.add(valorTiempoLabel, 1, 2);

        //Registra cambios en el valor del slider
        tiempoSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                //System.out.println(new_val.doubleValue());
                valorTiempoLabel.setText(String.format("%d", new_val.intValue()));
            }
        });


        //Boton aceptar
        Button btn = new Button("Aceptar");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);

        
        //Determina accion del boton
        /*
final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
		
		btn.setOnAction(new EventHandler<ActionEvent>() {
 
    @Override
    public void handle(ActionEvent e) {
        actiontarget.setFill(Color.FIREBRICK);
        actiontarget.setText("Sign in button pressed");
    }
});
         */
        
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
