import jakarta.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelogioDigital extends Application{

    final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void start(Stage palco) throws Exception {
        Label rotuloTempo =  new Label();
        rotuloTempo.setStyle("-fx-font-size: 24pt; -fx-text-fill: yellow;");

        // Atualizador
        KeyFrame keyFramaAtualizar = new KeyFrame(Duration.ZERO, e -> {
            rotuloTempo.setText(LocalDateTime.now().format(FORMATADOR));
        });

        // Tempo de intervalor do Atualizador
        KeyFrame keyFrameIntervalo = new KeyFrame(Duration.seconds(1));

        Timeline relogio = new Timeline();
        relogio.getKeyFrames().addAll(keyFramaAtualizar, keyFrameIntervalo);

        relogio.setCycleCount(Animation.INDEFINITE);
        relogio.play(); // Inicia a Timeline

        VBox vBoxLayout = new VBox(rotuloTempo);
        vBoxLayout.setAlignment(Pos.CENTER);
        vBoxLayout.setStyle("-fx-background-color: black;");

        Scene cena = new Scene(vBoxLayout, 210, 100);

        palco.setTitle("Relógio Digital");
        palco.setScene(cena);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
