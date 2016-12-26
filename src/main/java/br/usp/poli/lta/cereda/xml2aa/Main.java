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

import br.usp.poli.lta.cereda.aa.execution.AdaptiveAutomaton;
import br.usp.poli.lta.cereda.xml2aa.automaton.AutomatonBuilder;
import br.usp.poli.lta.cereda.xml2aa.automaton.AutomatonValidator;
import br.usp.poli.lta.cereda.xml2aa.xml.XMLTransformation;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAutomaton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.time.StopWatch;

/**
 * Classe principal.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class Main {

    /**
     * Método principal.
     *
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        
        // imprime o logotipo
        // da aplicação
        Utils.draw();
        Utils.line();
        Utils.linebreak("Laboratório de Linguagens e Técnicas Adaptativas");
        Utils.linebreak("Escola Politécnica, Universidade de São Paulo");
        Utils.linebreak("(versão 1.1)");
        Utils.line();

        try {

            // obtém o arquivo da linha de
            // comando e verifica se o
            // mesmo existe
            File file = Utils.ensure(args);

            // transforma o arquivo XML em
            // uma representação XML do
            // autômato adaptativo
            XMLTransformation transformation = new XMLTransformation();
            XMLAdaptiveAutomaton xml = transformation.get(file);

            // realiza uma validação prévia
            // da representação XML do
            // autômato adaptativo
            AutomatonValidator validator = new AutomatonValidator(xml);
            validator.validate();

            // constrói efetivamente o
            // autômato adaptativo a
            // partir da representação
            // XML avaliada anteriormente
            AutomatonBuilder builder = new AutomatonBuilder();
            AdaptiveAutomaton automaton = builder.build(xml);
            
            // realiza um reconhecimento inicial para
            // permitir a visualização inicial, ainda
            // sem cadeias previamente submetidas
            automaton.recognize(Utils.toSymbols(""));

            // imprime as informações
            // obtidas a partir da
            // representação XML do
            // autômato adaptativo
            Utils.linebreak(validator.getInformation());
            Utils.line();

            // imprime a informação do terminal,
            // incluindo a instrução para encerrar
            Utils.linebreak("Iniciando terminal, por favor, aguarde...");
            Utils.linebreak("(pressione CTRL+C or digite ':quit'"
                    + " para sair do programa)");
            Utils.line();

            // variáveis utilitárias
            // para as consultas no
            // terminal
            String query = "";
            String prompt = "[%d] consulta > ";
            
            // inicia as variáveis globais
            // da linha de resultado e o
            // contador de linhas
            String result = "[%d] resultado > ";
            int counter = 1;

            // define um cronômetro
            // para avaliar a execução
            // das consultas no autômato
            StopWatch watch = new StopWatch();

            // define o scanner para
            // obter a entrada dp usuário
            Scanner scanner = new Scanner(System.in);

            // define um mapa de ações
            // do interpretador
            Map<String, Action> actions = new HashMap<>();
            
            // adiciona a ação de verificação de uma
            // cadeia ao autômato adaptativo especificado
            actions.put(":check", (Action) (String argument) -> {
                
                // reinicia o cronômetro
                // para avaliar o tempo
                // de execução da consulta
                watch.reset();
                watch.start();
                
                // realiza a consulta
                // da cadeia ao autômato
                boolean accept = automaton.
                        recognize(Utils.toSymbols(argument));
                
                // o cronômetro é interrompido
                // após o término da execução
                watch.stop();
                
                // constrói os resultados a
                // serem impressos no terminal
                String pertinence = "cadeia ".
                        concat(accept ? "aceita" : "rejeitada");
                String type = automaton.getRecognitionPaths().
                        size() == 1 ? "(determinístico)"
                        : "(não-determinístico)";
                
                // retorna o resultado da
                // verificação
                return pertinence + " em " + watch + " " + type;
            });
            
            // adiciona a ação de visualização do autômato
            // adaptativo, de acordo com as consultas
            // submetidas previamente
            actions.put(":view", (Action) (String argument) -> {
                
                // caso o programa 'dot' não esteja
                // disponível, não será possível
                // gerar a visualização da topologia
                if (!Dot.exists()) {
                    return "a visualização da topologia do autômato adaptativo "
                            + "requer que o programa 'dot' esteja disponível "
                            + "no sistema, mas este não foi localizado. Por "
                            + "favor, verifique se o programa está disponível "
                            + "no caminho de executáveis do sistema e tente "
                            + "executar a ação ':view' novamente.";
                }
                
                // verifica se a submissão da cadeia resultou
                // em um reconhecimento não-determinístico
                if (automaton.getRecognitionPaths().size() != 1) {
                    return "a verificação da cadeia resultou em um "
                            + "reconhecimento não-determinístico. Ainda que a "
                            + "biblioteca subjacente permita a visualização de "
                            + "todas as ramificações, o interpretador, por "
                            + "razões de simplicidade, permite apenas "
                            + "visualizar topologias em reconhecimentos "
                            + "determinísticos. Utilize a biblioteca AA4J "
                            + "diretamente para ter acesso total a este "
                            + "recurso.";
                }
                
                // verifica quantas topologias estão
                // disponíveis para visualização
                int limit = automaton.getRecognitionPaths().
                        get(0).getDots().size();
                
                try {
                    
                    // obtém o índice da topologia
                    // a ser visualizada
                    int id = Integer.parseInt(argument);
                    
                    // se o intervalo é inválido, informar
                    // ao usuário
                    if (id < 0 || id > limit) {
                        return "o intervalo especificado é inválido, por "
                                + "favor, informe um intervalo válido para "
                                + "a topologia do autômato a ser visualizada.";
                    }
                    
                    // obtém o código-fonte '.dot'
                    // da topologia seleciona
                    String dot = automaton.getRecognitionPaths().
                            get(0).getDots().get(id - 1);
                    
                    try {
                        // exibe uma janela contendo
                        // a visualização da topologia
                        // selecionada no passo anterior
                        new Dot(dot).setVisible(true);
                        
                    }
                    catch (IOException nothandled) {
                        return "ocorreu um erro na visualização da topologia "
                                + "seleciona, a culpa é do autor do "
                                + "interpretador!";
                    }

                    // exibe a mensagem informando que
                    // a visualização ocorreu com sucesso
                    return "autômato visualizado em janela externa.";
                }
                catch (NumberFormatException nothandled) {
                    
                    // exibe uma mensagem de erro sobre
                    // um índice não-numérico para a
                    // seleção da topologia
                    return "o índice informado não é um valor numérico válido, "
                            + "por favor, informe um intervalo válido para "
                            + "a topologia do autômato a ser visualizada.";
      
                }
            });
            
            do {

                try {

                    // imprime a informação referente
                    // à linha corrente do terminal
                    String term = String.format(prompt, counter);
                    System.out.print(term);

                    // obtém a entrada do usuário
                    // a partir do scanner
                    query = scanner.nextLine().trim();

                    // verifica se a instrução não
                    // refere-se a sair do programa
                    if (!query.equals(":quit")) {

                        // divide a consulta em duas
                        // partes, potencialmente a ação
                        // e o argumento desta
                        String[] split = query.split(" ");
                        
                        // verifica se existe uma ação
                        // e um argumento
                        if (split.length != 2) {
                            
                            // lança uma exceção correspondente
                            throw new Exception("Uma ação do interpretador "
                                    + "(com exceção da ação ':quit') requer "
                                    + "um parâmetro correspondente. Por "
                                    + "favor, verifique a linha digitada no "
                                    + "interpretador e tente novamente.");
                        }
                        
                        // a ação não existe no
                        // mapa de ações
                        if (!actions.containsKey(split[0])) {
                            
                            // lança uma exceção correspondente
                            throw new Exception("O nome da ação informada é "
                                    + "inválido, o interpretador não entendeu "
                                    + "a entrada. As ações válidas são: "
                                    + "':check <cadeia>', ':view <índice>' e "
                                    + "':quit'. Observe que a última ação não "
                                    + "requer um parâmetro correspondente.");
                        }
                        
                        // imprime os resultados no terminal,
                        // com quebra de linha
                        Utils.linebreak(String.format(result, counter) + 
                                actions.get(split[0]).run(split[1]));
                        
                        // imprime uma linha em branco
                        // logo após o resultado
                        Utils.line();
                    }

                } catch (Exception exception) {

                    // uma exceção foi lançada durante
                    // a execução do processo de
                    // reconhecimento da cadeia
                    Utils.line();
                    Utils.center("A execução lançou uma exceção!");
                    Utils.line("-");
                    Utils.linebreak(exception.getMessage());
                    Utils.line();
                }

                // contador de linhas é
                // incrementado a cada
                // consulta no terminal
                counter++;

            } while (!query.equals(":quit"));

            // imprime mensagem de
            // despedida ao usuário
            Utils.line();
            System.out.println("Isso é tudo, pessoal!");
            
            // certifica-se que as janelas
            // existentes sejam destruídas
            System.exit(0);

        } catch (Exception exception) {

            // imprime a exceção mais geral,
            // lançada no processo de avaliação
            // e conversão da representação XML
            Utils.center("O programa encontrou uma exceção!");
            Utils.line("-");
            Utils.linebreak(Utils.maybe(exception));
        }

    }

    /**
     * Interface que define uma ação.
     */
    private interface Action {

        /**
         * Executa a ação.
         * 
         * @param argument Argumento da ação a ser executada.
         * @return Valor de retorno da ação executada;
         */
        public String run(String argument);

    }

}
