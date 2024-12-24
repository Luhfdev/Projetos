import java.security.SecureRandom;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GeradorDeSenhas {

    private static final String CARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()_+=-[],./?><";
    private static final String CAMINHO_ARQUIVO = "senhas.txt";

    public static String gerarSenha(int comprimento) {
        SecureRandom geradorDeNumeroAleatorio = new SecureRandom();
        StringBuilder senha = new StringBuilder(comprimento);

        for (int i = 0; i < comprimento; i++) {
            int indice = geradorDeNumeroAleatorio.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(indice));
        }

        return senha.toString();
    }

    public static void salvarSenha(String lugar, String senha) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            try (FileWriter escritor = new FileWriter(arquivo, true)) {
                escritor.write(lugar + ", " + senha + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar a senha: " + e.getMessage());
        }
    }

    public static List<String> lerSenhas() {
        List<String> senhas = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                senhas.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler as senhas: " + e.getMessage());
        }
        return senhas;
    }
}
