import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SistemaDeInformacoesClimaticasGUI {

    public static void main(String[] args) {

        // Criação da interface gráfica
        JFrame frame = new JFrame("Sistema de Informações Climáticas");
        frame.setSize(460, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel labelCidade = new JLabel("Digite o nome da cidade:");
        labelCidade.setBounds(20, 20, 200, 30);
        frame.add(labelCidade);

        JTextField campoCidade = new JTextField();
        campoCidade.setBounds(20, 60, 250, 30);
        frame.add(campoCidade);

        JButton botaoBuscar = new JButton("Buscar");
        botaoBuscar.setBounds(280, 60, 100, 30);
        frame.add(botaoBuscar);

        JButton botaoBuscarPeloIP = new JButton("Buscar pelo IP");
        botaoBuscarPeloIP.setBounds(20, 360, 160, 30);
        frame.add(botaoBuscarPeloIP);

        // Área de texto para exibir os dados climáticos
        JTextArea areaTexto = new JTextArea();
        areaTexto.setBounds(20, 100, 400, 200);
        areaTexto.setEditable(false);
        frame.add(areaTexto);

        JLabel labelTempoRestante = new JLabel("Atualização em: 10 segundos");
        labelTempoRestante.setBounds(20, 320, 250, 30);
        frame.add(labelTempoRestante);

        // Timer para contagem regressiva de 10 segundos
        Timer timerContagem = new Timer(1000, new ActionListener() {
            int tempoRestante = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempoRestante > 0) {
                    tempoRestante--;
                    labelTempoRestante.setText("Atualização em: " + tempoRestante + " segundos");
                } else {
                    String cidade = campoCidade.getText().trim();
                    if (!cidade.isEmpty()) {
                        try {
                            String dadosClimaticos = SistemaDeInformacoesClimaticas.getDadosClimaticos(cidade);
                            SwingUtilities.invokeLater(() ->
                                    areaTexto.setText(SistemaDeInformacoesClimaticas.imprimirDadosClimaticos(dadosClimaticos))
                            );
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() ->
                                    areaTexto.setText("Erro ao atualizar dados.")
                            );
                        }
                    }
                    tempoRestante = 10;
                }
            }
        });

        botaoBuscar.addActionListener(e -> {
            String cidade = campoCidade.getText().trim();
            if (!cidade.isEmpty()) {
                try {
                    timerContagem.start();
                    String dadosClimaticos = SistemaDeInformacoesClimaticas.getDadosClimaticos(cidade);
                    areaTexto.setText(SistemaDeInformacoesClimaticas.imprimirDadosClimaticos(dadosClimaticos));
                } catch (Exception ex) {
                    areaTexto.setText("Erro: " + ex.getMessage());
                }
            } else {
                areaTexto.setText("Por favor, digite o nome da cidade.");
            }
        });

        botaoBuscarPeloIP.addActionListener(e -> {
            try {
                String cidade = SistemaDeInformacoesClimaticas.getCidadePorIP();
                campoCidade.setText(cidade); // Preenche o campo com a cidade encontrada

                String dadosClimaticos = SistemaDeInformacoesClimaticas.getDadosClimaticos(cidade);
                areaTexto.setText(SistemaDeInformacoesClimaticas.imprimirDadosClimaticos(dadosClimaticos));
            } catch (Exception ex) {
                areaTexto.setText("Erro ao buscar dados pelo IP: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}
