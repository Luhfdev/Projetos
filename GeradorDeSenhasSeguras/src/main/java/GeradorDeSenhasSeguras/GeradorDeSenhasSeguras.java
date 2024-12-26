package GeradorDeSenhasSeguras;

import java.security.SecureRandom;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class GeradorDeSenhasSeguras {

    private static final String CARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=";
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

        @Override
        public String toString() {
            return String.format("Serviço: %s, Senha: %s", servico, senha);
        }
    }

    public static String gerarSenha(int comprimento) {
        SecureRandom geradorDeNumeroAleatorio = new SecureRandom();
        StringBuilder senha = new StringBuilder(comprimento);

        for (int i = 0; i < comprimento; i++) {
            int indice = geradorDeNumeroAleatorio.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(indice));
        }

        return senha.toString();
    }

    public static String criptografar(String texto, SecretKey chave) throws Exception {
        // Gerar um IV aleatório
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16]; // Tamanho do IV é 16 bytes para AES
        secureRandom.nextBytes(iv);

        // Usar o modo CBC e padding PKCS5
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, chave, ivSpec);

        // Criptografar o texto
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());

        // Concatenar o IV com o texto criptografado (IV + texto criptografado)
        byte[] resultado = new byte[iv.length + textoCriptografado.length];
        System.arraycopy(iv, 0, resultado, 0, iv.length);
        System.arraycopy(textoCriptografado, 0, resultado, iv.length, textoCriptografado.length);

        // Codificar em Base64
        return Base64.getEncoder().encodeToString(resultado);
    }

    public static String descriptografar(String textoCriptografado, SecretKey chave) throws Exception {
        // Decodificar o texto criptografado de Base64
        byte[] dadosCriptografados = Base64.getDecoder().decode(textoCriptografado);

        // Extrair o IV (os primeiros 16 bytes)
        byte[] iv = new byte[16];
        System.arraycopy(dadosCriptografados, 0, iv, 0, iv.length);

        // Extrair o texto criptografado (o restante dos dados)
        byte[] textoCriptografadoSemIV = new byte[dadosCriptografados.length - iv.length];
        System.arraycopy(dadosCriptografados, iv.length, textoCriptografadoSemIV, 0, textoCriptografadoSemIV.length);

        // Usar o modo CBC e padding PKCS5
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, chave, ivSpec);

        // Descriptografar
        byte[] textoDescriptografado = cipher.doFinal(textoCriptografadoSemIV);
        return new String(textoDescriptografado);
    }


    public static SecretKey obterChaveSecreta() throws Exception {
        File arquivoChave = new File(CHAVE_ARQUIVO);
        if (!arquivoChave.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
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

    public static List<RegistroSenha> lerSenhas() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try {
            // Lê as senhas criptografadas do arquivo JSON
            List<RegistroSenha> registros = objectMapper.readValue(arquivo, new TypeReference<List<RegistroSenha>>() {});
            return registros;
        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            arquivo.delete();
            return new ArrayList<>();
        }
    }


    public static void salvarSenha(String lugar, String senha) {
        List<RegistroSenha> senhas = lerSenhas();
        try {
            SecretKey chave = obterChaveSecreta();
            String senhaCriptografada = criptografar(senha, chave);

            if (senhaCriptografada == null || senhaCriptografada.isBlank()) {
                throw new Exception("Senha criptografada inválida.");
            }

            senhas.add(new RegistroSenha(lugar, senhaCriptografada));

            // Verificar se os dados estão corretos antes de salvar
            for (RegistroSenha r : senhas) {
                System.out.println("Salvando serviço: " + r.servico + ", Senha: " + r.senha);
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(CAMINHO_ARQUIVO), senhas);

        } catch (Exception e) {
            System.err.println("Erro ao salvar no arquivo JSON: " + e.getMessage());
        }
    }
}
