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

import br.usp.poli.lta.cereda.aa.examples.ExampleSymbol;
import br.usp.poli.lta.cereda.aa.model.Symbol;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Classe utilitária.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class Utils {

    // largura máxima das
    // mensagens impressas
    private static final int WIDTH = 74;

    /**
     * Imprime o logotipo da aplicação.
     */
    public static void draw() {
        StringBuilder sb = new StringBuilder();
        sb.append("           _ ___           ").append("\n");
        sb.append("__ ___ __ | |_  )__ _ __ _ ").append("\n");
        sb.append("\\ \\ / '  \\| |/ // _` / _` |").append("\n");
        sb.append("/_\\_\\_|_|_|_/___\\__,_\\__,_|");
        System.out.println(sb.toString());
    }

    /**
     * Imprime o texto com quebra de linhas.
     *
     * @param text Texto a ser impresso.
     */
    public static void linebreak(String text) {
        System.out.println(WordUtils.wrap(text, WIDTH, "\n", true));
    }

    /**
     * Imprime uma linha, repetindo o símbolo informado.
     *
     * @param symbol Símbolo a ser repetido.
     */
    public static void line(String symbol) {
        System.out.println(StringUtils.repeat(symbol, WIDTH));
    }

    /**
     * Imprime o texto centralizado no terminal.
     *
     * @param text Texto a ser centralizado.
     */
    public static void center(String text) {
        System.out.println(StringUtils.center(text, WIDTH));
    }

    /**
     * Analisa os argumentos de linha de comando, retornano o arquivo referente
     * à analise ou lançando exceções de acordo com o tipo de erro encontrado.
     *
     * @param args Argumentos de linha de comando.
     * @return Arquivo XML referente à analise.
     * @throws Exception Os argumentos são inválidos ou o arquivo não existe.
     */
    public static File ensure(String[] args) throws Exception {

        // os argumentos informados
        // são inválidos
        if (args.length != 1) {
            throw new Exception("É necessário informar um arquivo contendo "
                    + "a especificação XML do autômato adaptativo. Informe "
                    + "a localização correta do arquivo e tente novamente. "
                    + "O programa será encerrado.");
        }

        // obtém a representação
        // do arquivo XML
        File file = new File(args[0]);

        // verifica se o arquivo não
        // existe, lançando uma exceção
        if (!file.exists()) {
            throw new Exception("O arquivo '" + file.getName() + "' não "
                    + "foi localizado. Verifique se o nome e o caminho do "
                    + "arquivo foram digitados corretamente e tente "
                    + "novamente. O programa será encerrado.");
        }

        // o arquivo existe,
        // mas é um diretório
        if (!file.isFile()) {
            throw new Exception("O arquivo '" + file.getName() + "' existe, "
                    + "mas é um diretório! É necessário informar um arquivo "
                    + "contendo a especificação XML do autômato adaptativo. "
                    + "Verifique a localização correta do arquivo e tente "
                    + "novamente. O programa será encerrado.");
        }

        // o arquivo finalmente
        // é retornado
        return file;
    }

    /**
     * Imprime uma linha em branco.
     */
    public static void line() {
        System.out.println();
    }

    /**
     * Retorna uma lista de símbolos a partir de uma cadeia de símbolos.
     *
     * @param string Cadeia de símbolos.
     * @return Lista de símbolos.
     */
    public static List<Symbol> toSymbols(String string) {

        List<Symbol> symbols = new ArrayList<>();

        // cada símbolo da cadeia é
        // transformado em um elemento
        // da lista de símbolos
        for (char symbol : string.toCharArray()) {
            symbols.add(new ExampleSymbol(String.valueOf(symbol)));
        }

        return symbols;
    }

    /**
     * Tenta exibir a mensagem da exceção.
     *
     * @param exception Mensagem da exceção.
     * @return Mensagem da exceção.
     */
    static String maybe(Exception exception) {
        return exception.getMessage() != null
                ? exception.getMessage()
                : "Não foi possível obter detalhes sobre a exceção lançada. "
                + "Verifique se a especificação XML do autômato adaptativo "
                + "está correta e tente novamente. Adicionalmente, o código "
                + "pode ser alterado para exibir o rastreamento da pilha.";
    }

}
