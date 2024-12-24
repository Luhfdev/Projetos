package Application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProjetoCalculadoraIMC extends Application {

    @Override
    public void start(Stage palco) {
        Label etiquetaPeso = new Label("Peso");
        Label etiquetaAltura = new Label("Altura");

        TextField campoPeso = new TextField();
        campoPeso.setPromptText("Peso em Kg");
        TextField campoAltura = new TextField();
        campoAltura.setPromptText("Altura em metros");

        Label etiquetaResultado = new Label();
        Button botaoCalcular = new Button("Calcular IMC");
        botaoCalcular.setOnAction(e -> {
            try {
                double peso = Double.parseDouble(campoPeso.getText().replace(',', '.'));
                double altura = Double.parseDouble(campoAltura.getText().replace(',', '.'));

                double imc = peso / (altura * altura);
                etiquetaResultado.setText(String.format("Seu IMC é:  %.2f", imc));
            } catch (NumberFormatException ex) {
                etiquetaResultado.setText("Por favor, insira números válidos para os campos Peso e Altura.");
            }
        });

        // Layout na vertical
        VBox layout = new VBox(10, etiquetaPeso, campoPeso, etiquetaAltura, campoAltura, botaoCalcular, etiquetaResultado);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        palco.setTitle("Calculadora de IMC");
        palco.setScene(scene);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
