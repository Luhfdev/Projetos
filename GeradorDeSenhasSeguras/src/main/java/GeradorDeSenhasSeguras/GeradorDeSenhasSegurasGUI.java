package GeradorDeSenhasSeguras;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import javax.crypto.SecretKey;
import java.util.List;

public class GeradorDeSenhasSegurasGUI extends Application {
    @Override
    public void start(Stage palco) {
        palco.setTitle("Gerador de Senhas");

        Label labelLugarSenha = new Label("Aplicativo ou Programa da senha:");
        TextField campoLugarSenha = new TextField();

        Label labelTamanhoSenha = new Label("Tamanho da senha:");
        TextField campoTamanhoSenha = new TextField();
        campoTamanhoSenha.setText("8");

        Label labelSenhaGerada = new Label("Senha gerada:");
        TextField campoSenhaGerada = new TextField();
        campoSenhaGerada.setStyle("-fx-text-fill: cyan; -fx-background-color: black;");
        campoSenhaGerada.setEditable(false);

        Label labelSenhasSalvas = new Label("Senhas Salvas:");
        TextArea areaSenhasSalvas = new TextArea();
        areaSenhasSalvas.setEditable(false);

        Button botaoAtualizarSenhas = new Button("Atualizar Senhas");
        botaoAtualizarSenhas.setOnAction(e -> atualizarSenhas(areaSenhasSalvas));

        Button botaoGerar = new Button("Gerar e Salvar Senha");
        botaoGerar.setOnAction(e -> {
            try {
                int tamanhoSenha = Integer.parseInt(campoTamanhoSenha.getText());
                String senha = GeradorDeSenhasSeguras.gerarSenha(tamanhoSenha);
                campoSenhaGerada.setText(senha);

                String lugar = campoLugarSenha.getText();
                if (!lugar.isBlank()) {
                    GeradorDeSenhasSeguras.salvarSenha(lugar, senha);
                    mostrarAlerta("Senha gerada com sucesso!", "A senha gerada é: " + senha, Alert.AlertType.INFORMATION);
                    atualizarSenhas(areaSenhasSalvas);
                    campoLugarSenha.clear();
                    campoTamanhoSenha.setText("8");
                } else {
                    mostrarAlerta("Erro", "Por favor, insira o nome do lugar.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro", "Digite um tamanho válido.", Alert.AlertType.ERROR);
            }
        });

        atualizarSenhas(areaSenhasSalvas);

        VBox vBox = new VBox(labelLugarSenha, campoLugarSenha, labelTamanhoSenha, campoTamanhoSenha, botaoGerar, labelSenhaGerada, campoSenhaGerada, labelSenhasSalvas, areaSenhasSalvas, botaoAtualizarSenhas);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        Scene cena = new Scene(vBox, 400, 500);
        palco.setScene(cena);
        palco.show();
    }

    private void atualizarSenhas(TextArea areaSenhasSalvas) {
        areaSenhasSalvas.clear();
        try {
            List<GeradorDeSenhasSeguras.RegistroSenha> registros = GeradorDeSenhasSeguras.lerSenhas();
            SecretKey chave = GeradorDeSenhasSeguras.obterChaveSecreta();

            // Descriptografar as senhas antes de exibir
            for (GeradorDeSenhasSeguras.RegistroSenha registro : registros) {
                String senhaDescriptografada = GeradorDeSenhasSeguras.descriptografar(registro.senha, chave);
                areaSenhasSalvas.appendText("Serviço: " + registro.servico + ", Senha: " + senhaDescriptografada + System.lineSeparator());
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar as senhas: " + e.getMessage());
        }
    }


    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
