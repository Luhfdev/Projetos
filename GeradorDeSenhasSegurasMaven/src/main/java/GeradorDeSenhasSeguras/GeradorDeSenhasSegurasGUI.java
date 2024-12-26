package GeradorDeSenhasSeguras;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class GeradorDeSenhasSegurasGUI extends Application{
    @Override
    public void start(Stage palco) {
        palco.setTitle("Gerador de Senhas");

        Label labelLugarSenha = new Label("Aplicativo ou Programa da senha: ");
        TextField campoLugarSenha = new TextField();

        Label labelTamanhoSenha = new Label("Tamanho da senha: ");
        TextField campoTamanhoSenha = new TextField();
        campoTamanhoSenha.setText("8"); // Sugestão

        Label labelSenhaGerada = new Label("Senha gerada: ");
        TextField campoSenhaGerada = new TextField();
        campoSenhaGerada.setStyle("-fx-text-fill: cyan; -fx-background-color: black;");
        campoSenhaGerada.setEditable(false);

        Label labelSenhasSalvas = new Label("Senhas Salvas:");
        TextArea areaSenhasSalvas = new TextArea();
        areaSenhasSalvas.setEditable(false);

        Button botaoAtualizarSenhas = new Button("Atualizar Senhas");
        botaoAtualizarSenhas.setOnAction(e -> {
            areaSenhasSalvas.clear();
            for (GeradorDeSenhasSeguras.RegistroSenha linha : GeradorDeSenhasSeguras.lerSenhas()) {
                areaSenhasSalvas.appendText(linha + System.lineSeparator());
            }
        });

        Button botaoGerar = new Button("Gerar e Salvar Senha");
        botaoGerar.setOnAction(e -> {
            try {
                int tamanhoSenha = Integer.parseInt(campoTamanhoSenha.getText());
                String senha = GeradorDeSenhasSeguras.gerarSenha(tamanhoSenha);
                campoSenhaGerada.setText(senha);

                String lugar = campoLugarSenha.getText();
                if (!lugar.isBlank()) {
                    GeradorDeSenhasSeguras.salvarSenha(lugar, senha);

                    // Exibe a senha gerada em uma mensagem
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Senha Gerada");
                    alert.setHeaderText("Senha gerada com sucesso!");
                    alert.setContentText("A senha gerada é: " + senha);
                    alert.showAndWait();

                    // Atualiza automaticamente o TextArea
                    areaSenhasSalvas.clear();
                    for (GeradorDeSenhasSeguras.RegistroSenha linha : GeradorDeSenhasSeguras.lerSenhas()) {
                        areaSenhasSalvas.appendText(linha + System.lineSeparator());
                    }

                    // Limpa os campos de entrada
                    campoLugarSenha.clear();
                    campoTamanhoSenha.clear();
                    campoTamanhoSenha.setText("8"); // Sugestão padrão
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, insira o nome do lugar.");
                    alert.show();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Digite um tamanho válido.");
                alert.show();
            }
        });

        // Atualiza as senhas salvas automaticamente ao iniciar o programa
        areaSenhasSalvas.clear();
        for (GeradorDeSenhasSeguras.RegistroSenha linha : GeradorDeSenhasSeguras.lerSenhas()) {
            areaSenhasSalvas.appendText(linha + System.lineSeparator());
        }

        VBox vBox = new VBox(labelLugarSenha, campoLugarSenha, labelTamanhoSenha, campoTamanhoSenha, botaoGerar, labelSenhaGerada, campoSenhaGerada, labelSenhasSalvas, areaSenhasSalvas, botaoAtualizarSenhas);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        Scene cena = new Scene(vBox, 400, 500);
        palco.setScene(cena);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
