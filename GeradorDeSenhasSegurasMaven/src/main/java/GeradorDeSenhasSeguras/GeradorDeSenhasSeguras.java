package GeradorDeSenhasSeguras;

import java.security.SecureRandom;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class GeradorDeSenhasSeguras {

    private static final String CARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()_+=-[],./?><";
    private static final String CAMINHO_ARQUIVO = "senhas.json";
    private static final String CHAVE_ARQUIVO = "chave.aes";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Modelo de senha
    public static class RegistroSenha {
        public String servico;
        public String senha;

        public RegistroSenha() {} // Construtor padrão para o Jackson

        public RegistroSenha(String servico, String senha) {
            this.servico = servico;
            this.senha = senha;
        }
    }

    // Gera uma senha aleatoria
    public static String gerarSenha(int comprimento) {
        SecureRandom geradorDeNumeroAleatorio = new SecureRandom();
        StringBuilder senha = new StringBuilder(comprimento);

        for (int i = 0; i < comprimento; i++) {
            int indice = geradorDeNumeroAleatorio.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(indice));
        }

        return senha.toString();
    }

    // Criptografa uma senha
    public static String criptografar(String texto, SecretKey chave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }

    // Descriptografa uma senha
    public static String descriptografar(String textoCriptografado, SecretKey chave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] textoDescriptografado = cipher.doFinal(Base64.getDecoder().decode(textoCriptografado));
        return new String(textoDescriptografado);
    }

    // Gera ou le a chave secreta para criptografia
    public static SecretKey obterChaveSecreta() throws Exception {
        File arquivoChave = new File(CHAVE_ARQUIVO);
        if (!arquivoChave.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // Tamanho da chave
            SecretKey chave = keyGen.generateKey();
            try (FileOutputStream fos = new FileOutputStream(arquivoChave)) {
                fos.write(chave.getEncoded());
            }
            return chave;
        } else {
            byte[] chaveBytes = new byte[16];
            try (FileInputStream fis = new FileInputStream(arquivoChave)) {
                fis.read(chaveBytes);
            }
            return new SecretKeySpec(chaveBytes, "AES");
        }
    }

    // Lê o arquivo JSON e retorna a lista de senhas descriptografadas
    public static List<RegistroSenha> lerSenhas() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try {
            SecretKey chave = obterChaveSecreta();
            List<RegistroSenha> registros = objectMapper.readValue(arquivo, new TypeReference<List<RegistroSenha>>() {});
            for (RegistroSenha registro : registros) {
                registro.senha = descriptografar(registro.senha, chave);
            }
            return registros;
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Salva uma senha criptografada no arquivo JSON
    public static void salvarSenha(String lugar, String senha) {
        List<RegistroSenha> senhas = lerSenhas(); // Carrega as senhas existentes
        try {
            SecretKey chave = obterChaveSecreta();
            String senhaCriptografada = criptografar(senha, chave);
            senhas.add(new RegistroSenha(lugar, senhaCriptografada)); // Adiciona a nova senha
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), senhas); // Salva a lista no arquivo JSON
        } catch (Exception e) {
            System.out.println("Erro ao salvar no arquivo JSON: " + e.getMessage());
        }
    }
}
