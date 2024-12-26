import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SistemaDeInformacoesClimaticas {

    public static String getDadosClimaticos(String cidade) throws Exception {
        var classLoader = SistemaDeInformacoesClimaticas.class.getClassLoader();
        var resource = classLoader.getResource("api-key.txt");
        if (resource == null) {
            throw new FileNotFoundException("Arquivo 'api-key.txt' não encontrado no classpath.");
        }

        // Lê o conteúdo do arquivo
        String apiKey = Files.readString(Paths.get(resource.toURI())).trim();

        String formataNomeCidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String apiUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + formataNomeCidade;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static String imprimirDadosClimaticos(String dados) {
        JSONObject dadosJson = new JSONObject(dados);
        if (!dadosJson.has("current") || !dadosJson.has("location")) {
            throw new JSONException("Dados incompletos na resposta da API.");
        }

        JSONObject informacoesMetereologicas = dadosJson.getJSONObject("current");
        String cidade = dadosJson.getJSONObject("location").getString("name");
        String estado = dadosJson.getJSONObject("location").getString("region");
        String pais = dadosJson.getJSONObject("location").getString("country");
        String dataHoraString = dadosJson.getJSONObject("location").getString("localtime");

        String condicaoTempo = informacoesMetereologicas.getJSONObject("condition").getString("text");
        int umidade = informacoesMetereologicas.getInt("humidity");
        double velocidadeVento = informacoesMetereologicas.getDouble("wind_kph");
        double pressaoAtmosferica = informacoesMetereologicas.getDouble("pressure_mb");
        double sensacaoTermica = informacoesMetereologicas.getDouble("feelslike_c");
        double temperaturaAtual = informacoesMetereologicas.getDouble("temp_c");
        double indiceUV = informacoesMetereologicas.getDouble("uv");
        double probabilidadeChuva = informacoesMetereologicas.getDouble("precip_mm");

        // Iniciar string para exibir os dados
        StringBuilder dadosString = new StringBuilder();
        dadosString.append("Informações Meteorológicas para ").append(cidade).append(" - ").append(estado).append(", ").append(pais).append("\n");
        dadosString.append("Data e Hora: ").append(dataHoraString).append("\n");
        dadosString.append("Temperatura Atual: ").append(temperaturaAtual).append("°C\n");
        dadosString.append("Sensação Termica: ").append(sensacaoTermica).append("°C\n");
        dadosString.append("Condição do Tempo: ").append(condicaoTempo).append("\n");
        dadosString.append("Umidade: ").append(umidade).append("%\n");
        dadosString.append("Velocidade do Vento: ").append(velocidadeVento).append(" Km/h\n");
        dadosString.append("Pressão Atmosférica: ").append(pressaoAtmosferica).append(" mb\n");
        dadosString.append("Índice UV: ").append(indiceUV).append("\n");
        dadosString.append("Probabilidade de Chuva: ").append(probabilidadeChuva > 0 ? "Sim (" + probabilidadeChuva + " mm)" : "Não").append("\n");

        // Verifica e exibe os alertas climáticos
        if (dadosJson.has("alerts")) {
            JSONObject alerts = dadosJson.getJSONObject("alerts");
            if (alerts.has("alert")) {
                dadosString.append("\n*** ALERTAS CLIMÁTICOS ***\n");
                var alertArray = alerts.getJSONArray("alert");
                for (int i = 0; i < alertArray.length(); i++) {
                    JSONObject alerta = alertArray.getJSONObject(i);
                    String titulo = alerta.getString("headline");
                    String mensagem = alerta.getString("description");
                    dadosString.append("ALERTA: ").append(titulo).append("\n");
                    dadosString.append("Descrição: ").append(mensagem).append("\n");
                    dadosString.append("-------------------------------\n");
                }
            } else {
                dadosString.append("\nNão há alertas climáticos no momento.\n");
            }
        } else {
            dadosString.append("\nNão há alertas climáticos no momento.\n");
        }

        return dadosString.toString();
    }
}
