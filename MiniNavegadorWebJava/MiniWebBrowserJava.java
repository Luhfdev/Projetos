import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MiniWebBrowserJava extends Application {

	@Override
	public void start(Stage palco) {
		TextField campoUrl = new TextField();
		WebView navegador = new WebView();
		WebEngine motor = navegador.getEngine();

		//Carrega uma pagina da web quando o usuário pressionar Enter
		campoUrl.setOnAction(evento -> motor.load(formataUrl(campoUrl.getText())));
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(campoUrl, navegador);
		Scene cena = new Scene(vBox);

		palco.setTitle("Meu Browser Java");
		palco.setScene(cena);
		palco.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	//Metodo para inserir http
	public String formataUrl (String url){
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		return url;
	}
}