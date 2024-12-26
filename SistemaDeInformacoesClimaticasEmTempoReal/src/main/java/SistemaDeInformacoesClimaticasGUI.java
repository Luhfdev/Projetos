import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SistemaDeInformacoesClimaticasGUI {

    public static void main(String[] args) {

        // Criação da interface gráfica
        JFrame frame = new JFrame("Sistema de Informações Climáticas");
        frame.setSize(400, 400);
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

        // Área de texto para exibir os dados climáticos
        JTextArea areaTexto = new JTextArea();
        areaTexto.setBounds(20, 100, 350, 200);
        areaTexto.setEditable(false);
        frame.add(areaTexto);

        JLabel labelTempoRestante = new JLabel("Tempo restante: 10 segundos");
        labelTempoRestante.setBounds(20, 320, 250, 30);
        frame.add(labelTempoRestante);

        // Timer para contagem regressiva de 10 segundos
        Timer timerContagem = new Timer(1000, new ActionListener() {
            int tempoRestante = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempoRestante > 0) {
                    tempoRestante--;
                    labelTempoRestante.setText("Tempo restante: " + tempoRestante + " segundos");
                } else {
                    // Quando o tempo zerar, atualiza os dados climáticos
                    String cidade = campoCidade.getText().trim();
                    if (!cidade.isEmpty()) {
                        try {
                            String dadosClimaticos = SistemaDeInformacoesClimaticas.getDadosClimaticos(cidade);
                            // Atualiza os dados climáticos na área de texto
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    areaTexto.setText(SistemaDeInformacoesClimaticas.imprimirDadosClimaticos(dadosClimaticos));
                                }
                            });
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    areaTexto.setText("Erro ao atualizar dados.");
                                }
                            });
                        }
                    }
                    tempoRestante = 10;
                }
            }
        });

        botaoBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cidade = campoCidade.getText().trim();
                if (!cidade.isEmpty()) {
                    try {
                        timerContagem.start();

                        // Exibe os dados climáticos inicialmente
                        String dadosClimaticos = SistemaDeInformacoesClimaticas.getDadosClimaticos(cidade);
                        areaTexto.setText(SistemaDeInformacoesClimaticas.imprimirDadosClimaticos(dadosClimaticos));
                    } catch (Exception ex) {
                        areaTexto.setText("Erro: " + ex.getMessage());
                    }
                } else {
                    areaTexto.setText("Por favor, digite o nome da cidade.");
                }
            }
        });
        
        frame.setVisible(true);
    }
}
