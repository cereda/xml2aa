/**
 * ------------------------------------------------------
 *    Laboratório de Linguagens e Técnicas Adaptativas
 *       Escola Politécnica, Universidade São Paulo
 * ------------------------------------------------------
 *
 * This program is free software: you can redistribute it
 * and/or modify  it under the  terms of the  GNU General
 * Public  License  as  published by  the  Free  Software
 * Foundation, either  version 3  of the License,  or (at
 * your option) any later version.
 *
 * This program is  distributed in the hope  that it will
 * be useful, but WITHOUT  ANY WARRANTY; without even the
 * implied warranty  of MERCHANTABILITY or FITNESS  FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 *
 */
package br.usp.poli.lta.cereda.xml2aa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Exibe a imagem correspondente ao código-fonte em formato DOT.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class Dot extends JFrame {

    // rótulo que conterá a imagem
    // gerada pelo programa 'dot'
    private final JLabel lblImage;
    
    // medidas máximas, em pixels,
    // da altura e largura da imagem
    private static final int MAX_HEIGHT = 700;
    private static final int MAX_WIDTH = 700;
    
    // medida, em pixels, para o espaçamento
    // entre a imagem e a borda da janela
    private static final int PAD = 50;

    /**
     * Construtor.
     * 
     * @param value Código-fonte do código DOT a ser gerado.
     * @throws UnsupportedEncodingException Codificação não suportada.
     * @throws IOException Erro de entrada e saída.
     */
    public Dot(String value) throws UnsupportedEncodingException, IOException {
        
        // define o título da janela
        super("Visualização do autômato");
        
        // define a operação padrão
        // quando a janela é fechada
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // define o layout da janela
        setLayout(new BorderLayout());

        // define o plano de fundo da
        // janela na cor branca
        getContentPane().setBackground(Color.WHITE);

        // define um novo rótulo que
        // conterá a imagem a ser gerada
        lblImage = new JLabel();
        
        // define o alinhamento do rótulo
        // para que a imagem fique centralizada
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        
        // adiciona o componente
        // na janela corrente
        add(lblImage, BorderLayout.CENTER);

        // define um novo comando,
        // referenciando o programa 'dot'
        CommandLine command = new CommandLine("dot");
        
        // define o parâmetro do programa,
        // neste caso, a geração será em
        // formato PNG
        command.addArgument("-Tpng");

        // define o formato a ser submetido
        // ao programa, neste caso, a representação
        // em bytes do código-fonte informado
        ByteArrayInputStream input = new ByteArrayInputStream(value.
                getBytes("UTF-8"));
        
        // cria um fluxo de saída e tenta
        // obter a imagem correspondente
        // à execução do programa 'dot'
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            // cria um novo executor
            Executor executor = new DefaultExecutor();
            
            // define o manipulador de fluxos, informando
            // os fluxos de saída e entrada correspondentes
            executor.setStreamHandler(new PumpStreamHandler(output,
                    null, input));
            
            // executa o comando 'dot'
            // propriamente dito
            executor.execute(command);

            // cria uma imagem temporária
            // com o fluxo de bytes obtidos
            // a partir da execução de 'dot'
            ImageIcon image = new ImageIcon(output.toByteArray());

            // altura e largura da
            // imagem são obtidas
            int height = image.getIconHeight();
            int width = image.getIconWidth();

            // novos valores para
            // a altura e largura
            int newheight;
            int newwidth;

            // realiza cálculos de redimensionamento,
            // caso a imagem obtida seja maior do que
            // as dimensões máximas definidas
            if (height > width) {        
                
                // define a nova altura e calcula
                // a largura correspondente
                newheight = height > MAX_HEIGHT ? MAX_HEIGHT : height;
                newwidth = newheight * width / height;
                
            } else {
                
                // define a nova largura e calcula
                // a altura correspondente
                newwidth = width > MAX_WIDTH ? MAX_WIDTH : width;
                newheight = newwidth * height / width;
                
            }

            // define o novo tamanho da janela
            // de acordo com os novos valores
            // calculados anteriormente
            setPreferredSize(new Dimension(newwidth + PAD, newheight + PAD));

            // define a nova imagem, devidamente
            // redimensionada, como exibição do
            // rótulo da janela corrente
            lblImage.setIcon(new ImageIcon(image.getImage().
                    getScaledInstance(newwidth,
                            newheight, Image.SCALE_SMOOTH)));

        }

        // empacota a janela,
        // ajustando os componentes
        pack();
        
        // centraliza a posição da janela
        // em relação à tela do usuário
        setLocationRelativeTo(null);

    }

    /**
     * Verifica se o programa 'dot' existe.
     * 
     * @return Valor lógico indicando se o programa 'dot' existe.
     */
    public static boolean exists() {
        
        try {
            
            // cria um novo comando
            CommandLine command = new CommandLine("dot");
            
            // define um novo executor
            Executor executor = new DefaultExecutor();
            
            // executa o comando
            executor.execute(command);
            
            // o comando foi executado com
            // sucesso, indicando que este
            // existe no caminho do sistema
            return true;
            
        } catch (IOException nothandled) {
            
            // no caso de uma exceção de
            // entrada e saída, o comando
            // 'dot' não existe
            return false;
        }
    }

}
