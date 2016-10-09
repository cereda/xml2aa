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
import java.util.Scanner;
import org.apache.commons.lang3.time.StopWatch;

/**
 * Classe principal.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
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
            String result = "[%d] resultado > ";
            int counter = 1;

            // define um cronômetro
            // para avaliar a execução
            // das consultas no autômato
            StopWatch watch = new StopWatch();

            // define o scanner para
            // obter a entrada dp usuário
            Scanner scanner = new Scanner(System.in);

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

                        // reinicia o cronômetro
                        // para avaliar o tempo
                        // de execução da consulta
                        watch.reset();
                        watch.start();

                        // realiza a consulta
                        // da cadeia ao autômato
                        boolean accept = automaton.
                                recognize(Utils.toSymbols(query));

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

                        // imprime os resultados
                        // no terminal
                        Utils.linebreak(String.format(result, counter)
                                + pertinence + " em " + watch + " " + type);
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
            
        } catch (Exception exception) {

            // imprime a exceção mais geral,
            // lançada no processo de avaliação
            // e conversão da representação XML
            Utils.center("O programa encontrou uma exceção!");
            Utils.line("-");
            Utils.linebreak(Utils.maybe(exception));
        }

    }

}
