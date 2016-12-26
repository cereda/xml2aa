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
package br.usp.poli.lta.cereda.xml2aa.automaton;

import br.usp.poli.lta.cereda.xml2aa.model.AutomatonSpecException;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAction;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAction;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAutomaton;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLState;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLSubmachine;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLTransition;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Valida a especificação XML do autômato adaptativo.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class AutomatonValidator {

    // variáveis da classe
    private final XMLAdaptiveAutomaton automaton;
    private final Set<String> calls;

    /**
     * Construtor.
     *
     * @param automaton Especificação XML do autômato adaptativo.
     */
    public AutomatonValidator(XMLAdaptiveAutomaton automaton) {
        this.automaton = automaton;
        this.calls = new HashSet<>();
    }

    /**
     * Faz a validação da especificação XML do autômato adaptativo.
     *
     * @throws AutomatonSpecException A especificação é inválida.
     */
    public void validate() throws AutomatonSpecException {

        // lista de validações
        // a serem aplicadas
        hasTransitions();
        hasFromAndTo();
        hasSubmachines();
        hasIntegerStates();
        hasValidSubmachines();
        hasValidSymbolTransitions();
        hasValidSubmachineCalls();
        hasActionCalls();
        checkAdaptiveActions();
    }

    /**
     * Método utilitário para lançar exceções de especificação.
     *
     * @param message Mensagem da exceção.
     * @throws AutomatonSpecException Exceção parametrizada com a mensagem.
     */
    private void raise(String message) throws AutomatonSpecException {
        throw new AutomatonSpecException(message);
    }

    /**
     * Método utilitário para lançar exceções de especificação formatados.
     *
     * @param message Mensagem da exceção.
     * @param args Argumentos de formatação.
     * @throws AutomatonSpecException Exceção parametrizada com a mensagem.
     */
    private void raise(String message, Object... args)
            throws AutomatonSpecException {
        throw new AutomatonSpecException(String.format(message, args));
    }

    /**
     * Verifica se o autômato adaptativo possui transições associadas.
     *
     * @throws AutomatonSpecException Não há transições.
     */
    private void hasTransitions() throws AutomatonSpecException {
        if (automaton.getTransitions() == null
                || automaton.getTransitions().getTransitions().isEmpty()) {
            raise("O autômato adaptativo não possui transições especificadas. "
                    + "Ainda que a teoria preveja a possibilidade de um "
                    + "conjunto vazio de transições, este não faz sentido "
                    + "do ponto de vista prático geral. Por favor, adicione "
                    + "transições na especificação XML do autômato adaptativo "
                    + "e tente novamente. O programa será encerrado.");
        }
    }

    /**
     * Verifica se o autômato adaptativo possui submáquinas.
     *
     * @throws AutomatonSpecException Não há submáquinas.
     */
    private void hasSubmachines() throws AutomatonSpecException {
        if (automaton.getSubmachines() == null
                || automaton.getSubmachines().getSubmachines().isEmpty()) {
            raise("O autômato adaptativo não possui submáquinas especificadas. "
                    + "É necessário definir ao menos uma submáquina no modelo,"
                    + "incluindo todos os estados componentes e as indicações "
                    + "de entrada e retorno. Por favor, adicione submáquinas "
                    + "na especificação XML do autômato adaptativo e tente "
                    + "novamente. O programa será encerrado.");
        }
    }

    /**
     * Verifica se toda transição tem estados de origem e destino.
     *
     * @throws AutomatonSpecException Há transições com problemas.
     */
    private void hasFromAndTo() throws AutomatonSpecException {
        if (automaton.getTransitions().getTransitions().stream().
                anyMatch((XMLTransition t) -> t.getFrom() == null
                        || t.getTo() == null)) {
            raise("O autômato adaptativo possui transições sem estados de "
                    + "origem ou destino. Toda transição deve conter tais "
                    + "elementos. Por favor, adicione estados de origem e "
                    + "destino nas transições e tente novamente. O programa "
                    + "será encerrado.");

        }
    }

    /**
     * Verifica se o autômato adaptativo possui estados inteiros.
     *
     * @throws AutomatonSpecException Há estados inválidos.
     */
    private void hasIntegerStates() throws AutomatonSpecException {
        for (XMLTransition t : automaton.getTransitions().getTransitions()) {
            try {
                Integer.parseInt(t.getFrom());
                Integer.parseInt(t.getTo());
            } catch (NumberFormatException nothandled) {
                raise("O autômato adaptativo não possui estados com numeração "
                        + "inteira. O modelo de autômato aqui utilizado prevê "
                        + "um conjunto de estados representados por "
                        + "identificadores inteiros positivos, de modo a "
                        + "simplificar a geração de novos estados. Por favor, "
                        + "verifique os estados e tente novamente. O programa "
                        + "será encerrado.");
            }
        }
    }

    /**
     * Verifica se o autômato tem submáquinas válidas.
     *
     * @throws AutomatonSpecException Não há submáquinas válidas.
     */
    private void hasValidSubmachines() throws AutomatonSpecException {

        // variáveis auxiliares
        // para verificação
        int main = 0;
        int start;
        int accepting;

        // toda e qualquer submáquina da
        // especificação XML é avaliada
        for (XMLSubmachine submachine : automaton.
                getSubmachines().getSubmachines()) {

            // inicia os valores das
            // variáveis auxiliares
            start = 0;
            accepting = 0;

            // toda submáquina deve ter
            // um nome associado
            if (submachine.getName() == null
                    || submachine.getName().isEmpty()) {
                raise("O autômato adaptativo possui submáquinas anônimas. É "
                        + "necessário especificar um nome para a submáquina, "
                        + "que será utilizado como identificador em eventuais "
                        + "chamadas. Verifique a definição das submáquinas e "
                        + "tente novamente. O programa será encerrado.");
            }

            // a submáquina não possui
            // estados associados
            if (submachine.getStates() == null
                    || submachine.getStates().isEmpty()) {
                raise("O autômato adaptativo possui a submáquina '%s' sem "
                        + "estados associados. É necessário especificar "
                        + "estados para as submáquinas, incluindo eventuais "
                        + "marcações de entrada e retorno. Por favor, "
                        + "verifique a definição das submáquinas e tente "
                        + "novamente. O programa será encerrado.",
                        submachine.getName()
                );
            } else {

                // existe uma marcação
                // para a submáquina principal
                if (submachine.getMain() != null) {
                    if (!submachine.getMain().equals("true")) {
                        raise("O autômato adaptativo possui a submáquina '%s' "
                                + "com marcação principal, mas o valor "
                                + "esperado para 'main' está incorreto (o "
                                + "modelo prevê o valor 'true' para 'main'). "
                                + "Por favor, corrija o valor e tente "
                                + "novamente. O programa será encerrado.",
                                submachine.getName()
                        );
                    } else {

                        // incrementa a variável
                        // contadora de submáquinas
                        // principais do modelo
                        main++;
                    }
                }

                // caso exista mais de uma
                // submáquina assinalada como
                // principal, ocorreu um erro
                if (main > 1) {
                    raise("O autômato adaptativo possui mais do que uma "
                            + "submáquina definida como principal. O modelo "
                            + "admite apenas uma submáquina. Por favor, defina "
                            + "apenas uma submáquina como sendo principal e "
                            + "tente novamente. O programa será encerrado.");
                }

                // cada estado da submáquina
                // corrente é avaliado
                for (XMLState state : submachine.getStates()) {

                    // existem estados sem
                    // identificadores
                    if (state.getName() == null || state.getName().isEmpty()) {
                        raise("O autômato adaptativo possui a submáquina '%s' "
                                + "com estados anônimos. Por favor, inclua a "
                                + "identificação em tais estados e tente "
                                + "novamente. O programa será encerrado.",
                                submachine.getName()
                        );
                    } else {

                        // verifica se os estados
                        // das submáquinas são
                        // números inteiros
                        try {
                            Integer.parseInt(state.getName());
                        } catch (NumberFormatException nothandled) {
                            raise("O autômato adaptativo possui a submáquina"
                                    + "'%s' com o estado '%s' nomeado com um "
                                    + "valor diferente de inteiro. O modelo de "
                                    + "autômato aqui utilizado prevê um "
                                    + "conjunto de estados representados por "
                                    + "identificadores inteiros positivos, de "
                                    + "modo a simplificar a geração de novos "
                                    + "estados. Por favor, verifique os "
                                    + "estados e tente novamente. O programa "
                                    + "será encerrado.",
                                    submachine.getName(),
                                    state.getName()
                            );
                        }

                        // estado de aceitação possui
                        // um valor incorreto
                        if (state.getAccepting() != null) {
                            if (!state.getAccepting().equals("true")) {
                                raise("O autômato adaptativo possui a "
                                        + "submáquina '%s' com o estado '%s' "
                                        + "contendo a marcação de aceitação, "
                                        + "mas o valor esperado para "
                                        + "'accepting' está incorreto (o "
                                        + "modelo prevê o valor 'true' para "
                                        + "'accepting'). Por favor, corrija o "
                                        + "valor e tente novamente. O programa "
                                        + "será encerrado.",
                                        submachine.getName(),
                                        state.getName()
                                );
                            } else {

                                // incrementa a variável contadora
                                // de estados de aceitação da
                                // submáquina corrente
                                accepting++;
                            }
                        }

                        // estado de entrada possui
                        // um valor incorreto
                        if (state.getStart() != null) {
                            if (!state.getStart().equals("true")) {
                                raise("O autômato adaptativo possui a "
                                        + "submáquina '%s' com o estado '%s' "
                                        + "contendo a marcação de entrada, mas "
                                        + "o valor esperado para 'start' está "
                                        + "incorreto (o modelo prevê o valor "
                                        + "'true' para 'start'). Por favor, "
                                        + "corrija o valor e tente novamente. "
                                        + "O programa será encerrado.",
                                        submachine.getName(),
                                        state.getName()
                                );
                            } else {

                                // incrementa a variável contadora
                                // de estados de entrada da submáquina
                                start++;
                            }
                        }
                    }
                }

                // é necessário um
                // estado de entrada
                if (start != 1) {
                    raise("O autômato adaptativo possui a submáquina '%s' "
                            + "%s. Toda submáquina requer apenas um estado de "
                            + "entrada. Por favor, inclua a marcação de "
                            + "entrada em um dos estados da submáquina e tente "
                            + "novamente. O programa será encerrado.",
                            submachine.getName(),
                            (start == 0
                                    ? "sem estado de entrada definido"
                                    : "com múltiplos estados de entrada")
                    );
                }

                // a submáquina não possui
                // estados de retorno
                if (accepting == 0) {
                    raise("O autômato adaptativo possui a submáquina '%s' "
                            + "sem estados de aceitação. Ainda que a teoria "
                            + "preveja a possibilidade de um conjunto vazio "
                            + "de estados de aceitação, este não faz sentido "
                            + "do ponto de vista prático geral. Por favor, "
                            + "inclua a marcação de aceitação em pelo menos "
                            + "um dos estados da submáquina e tente novamente. "
                            + "O programa será encerrado.",
                            submachine.getName()
                    );
                }
            }
        }
    }

    /**
     * Verifica se o autômato adaptativo possui chamadas de ações adaptativas.
     *
     * @throws AutomatonSpecException Estas existem e são inválidas.
     */
    private void hasActionCalls() throws AutomatonSpecException {

        // as transições do autômato são verificadas
        // em busca de chamadas de ações adaptativas
        for (XMLTransition t : automaton.getTransitions().getTransitions()) {

            // existe a chamada anterior, mas
            // não há nome associado
            if (t.getPreAdaptiveFunction() != null) {
                if (t.getPreAdaptiveFunction().getName() == null
                        || t.getPreAdaptiveFunction().getName().isEmpty()) {
                    raise("O autômato adaptativo possui transições com "
                            + "chamadas de funções adaptativas anteriores "
                            + "anônimas. É necessário informar o nome "
                            + "associado corretamente. Por favor, corrija a "
                            + "chamada da função anterior, de forma a incluir "
                            + "seu identificador, e tente novamente. O "
                            + "programa será encerrado.");
                } else {

                    // adiciona o nome da
                    // chamada no conjunto
                    calls.add(t.getPreAdaptiveFunction().getName());
                }
            }

            // existe a chamada posterior, mas
            // não há nome associado
            if (t.getPostAdaptiveFunction() != null) {
                if (t.getPostAdaptiveFunction().getName() == null
                        || t.getPostAdaptiveFunction().getName().isEmpty()) {
                    raise("O autômato adaptativo possui transições com "
                            + "chamadas de funções adaptativas posteriores "
                            + "anônimas. É necessário informar o nome "
                            + "associado corretamente. Por favor, corrija a "
                            + "chamada da função posterior, de forma a incluir "
                            + "seu identificador, e tente novamente. O "
                            + "programa será encerrado.");
                } else {

                    // adiciona o nome da
                    // chamada no conjunto
                    calls.add(t.getPostAdaptiveFunction().getName());
                }
            }
        }
    }

    /**
     * Verifica as ações adaptativas do autômato, se existirem.
     *
     * @throws AutomatonSpecException As ações adaptativas são inválidas.
     */
    private void checkAdaptiveActions() throws AutomatonSpecException {

        // variável auxiliar que
        // armazena os identificadores
        // das ações adaptativas
        Set<String> names = new HashSet<>();

        // variável auxiliar que contém
        // os tipos de ações elementares
        Set<String> types = new HashSet<>();

        // adiciona os tipos
        // de ações elementares
        types.add("remove");
        types.add("query");
        types.add("add");

        // o autômato adaptativo
        // possui ações adaptativas
        if (automaton.getActions() != null) {

            // cada ação adaptativa
            // definida é verificada
            for (XMLAdaptiveAction action : automaton.getActions()) {

                // existem ações adaptativas
                // anônimas no modelo
                if (action.getName() == null || action.getName().isEmpty()) {
                    raise("O autômato adaptativo possui ações adaptativas "
                            + "anônimas. É necessário especificar o nome "
                            + "da ação adaptiva para que este atue como "
                            + "identificador na chamada de funções "
                            + "adaptativas. Por favor, adicione os nomes às "
                            + "ações adaptativas e tente novamente. O "
                            + "programa será encerrado.");
                } else {

                    // adiciona o nome da ação
                    // ao conjunto de nomes
                    names.add(action.getName());
                }

                // existem variáveis com
                // nomes inválidos
                if (action.getVariables() != null) {
                    for (String variable : action.getVariables()) {
                        if (!variable.startsWith("?")) {
                            raise("O autômato adaptativo possui a ação "
                                    + "adaptativa '%s' com a variável '%s' "
                                    + "contendo um nome inválido. O modelo de "
                                    + "autômato aqui utilizado prevê que as "
                                    + "variáveis sejam iniciadas por '?', por "
                                    + "exemplo, '?x'. Por favor, verifique os "
                                    + "nomes das variáveis e tente novamente. "
                                    + "O programa será encerrado.",
                                    action.getName(),
                                    variable
                            );
                        }
                    }
                }

                // existem geradores com
                // nomes inválidos
                if (action.getGenerators() != null) {
                    for (String generator : action.getGenerators()) {
                        if (!generator.endsWith("*")) {
                            raise("O autômato adaptativo possui a ação "
                                    + "adaptativa '%s' com o gerador '%s' "
                                    + "contendo um nome inválido. O modelo de "
                                    + "autômato aqui utilizado prevê que os "
                                    + "geradores sejam terminados por '*', por "
                                    + "exemplo, 'g*'. Por favor, verifique os "
                                    + "nomes dos geradores e tente novamente. "
                                    + "O programa será encerrado.",
                                    action.getName(),
                                    generator
                            );
                        }
                    }
                }

                // a ação adaptativa não
                // possui lista de ações
                // adaptativas elementares
                if (action.getActions() == null
                        || action.getActions().isEmpty()) {
                    raise("O autômato adaptativo possui a ação adaptativa '%s' "
                            + "sem uma lista de ações adaptativas elementares. "
                            + "O modelo de autômato aqui utilizado prevê uma "
                            + "lista contendo ações adaptativas elementares. "
                            + "Por favor, inclua ações adaptativas elementares "
                            + "na ação adaptativa e tente novamente. O "
                            + "programa será encerrado.",
                            action.getName()
                    );
                } else {

                    // análise da lista de ações
                    // adaptativas elementares
                    for (XMLAction elementary : action.getActions()) {

                        // toda ação deve possuir
                        // estados de origem e destino
                        if (elementary.getFrom() == null
                                || elementary.getTo() == null
                                || elementary.getFrom().isEmpty()
                                || elementary.getTo().isEmpty()) {
                            raise("O autômato adaptativo possui a ação "
                                    + "adaptativa '%s' contendo ações "
                                    + "adaptativas elementares que não possuem "
                                    + "estados de origem e/ou destino. É "
                                    + "necessário que estes sejam definidos. "
                                    + "Por favor, verifique a lista de ações "
                                    + "adaptativas elementares e tente "
                                    + "novamente. O programa será encerrado.",
                                    action.getName()
                            );
                        }

                        // toda ação deve ter
                        // o tipo definido
                        if (elementary.getType() == null) {
                            raise("O autômato adaptativo possui a ação "
                                    + "adaptativa '%s' contendo ações "
                                    + "adaptativas elementares que não possuem "
                                    + "o tipo definido ('add', 'remove' ou "
                                    + "'query'). É necessário que estes sejam "
                                    + "definidos. Por favor, verifique a "
                                    + "lista de ações adaptativas elementares "
                                    + "e tente novamente. O programa será "
                                    + "encerrado.",
                                    action.getName()
                            );
                        } else if (!types.contains(elementary.getType())) {
                            raise("O autômato adaptativo possui a ação "
                                    + "adaptativa '%s' contendo ações "
                                    + "adaptativas elementares que possuem "
                                    + "tipos inválidos. É necessário que estes "
                                    + "sejam definidos corretamente. Os tipos "
                                    + "válidos são: 'add' para inclusão, "
                                    + "'remove' para remoção, e 'query' para "
                                    + "consulta. Por favor, verifique a lista "
                                    + "de ações adaptativas elementares e "
                                    + "tente novamente. O programa será "
                                    + "encerrado.",
                                    action.getName()
                            );
                        }
                    }
                }
            }
        }

        // verifica se as chamadas de funções
        // adaptivas realmente existem
        for (String call : calls) {
            if (!names.contains(call)) {
                raise("O autômato adaptativo possui chamadas de funções "
                        + "adaptativas não existentes na especificação: '%s'. "
                        + "Por favor, verifique as chamadas correspondentes "
                        + "nas transições e tente novamente. O programa será "
                        + "encerrado.",
                        call
                );
            }
        }
    }

    /**
     * Obtém informações acerca do autômato adaptativo.
     *
     * @return Informações textuais sobre o autômato.
     */
    public String getInformation() {

        // todas as transições
        int all = automaton.getTransitions().getTransitions().size();

        // todas as transições
        // com consumo de símbolo
        int symbol = (int) automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getSymbol() != null
                        && t.getCall() == null).count();

        // todas as transiçoẽs
        // em vazio
        int empty = (int) automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getSymbol() == null
                        && t.getCall() == null).count();

        // todas as transições com
        // chamada de submáquina
        int smc = (int) automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getSymbol() == null
                        && t.getCall() != null).count();

        // todas as chamadas de
        // funções adaptativas
        int ac = (int) automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getPreAdaptiveFunction() != null
                        || t.getPostAdaptiveFunction() != null).count();

        // todas as chamadas de funções
        // adaptativas anteriores
        int prec = (int) automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getPreAdaptiveFunction() != null).
                count();

        // todas as chamadas de funções
        // adaptativas posteriores
        int postc = (int) automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getPostAdaptiveFunction() != null).
                count();

        // todas as submáquinas
        int sm = automaton.getSubmachines().getSubmachines().size();

        // todas as ações adaptativas
        int aa = automaton.getActions() != null
                ? automaton.getActions().size() : 0;

        // todos os estados
        int states = automaton.getSubmachines().getSubmachines().stream().
                map((XMLSubmachine t) -> t.getStates().stream().
                        map((XMLState t1) -> Integer.parseInt(t1.getName())).
                        collect(Collectors.toSet())).reduce((Set<Integer> t,
                Set<Integer> u) -> {
            Set<Integer> s = new HashSet<>();
            s.addAll(t);
            s.addAll(u);
            return s;
        }).get().size();

        // monta o texto das informações
        StringBuilder sb = new StringBuilder();
        sb.append("O autômato possui ").append(states).append(" ");
        sb.append(plural("estado", states)).append(", ").append(all);
        sb.append(" ").append(plural("transição", "transições", all));
        sb.append(" (").append(symbol).append(" ");
        sb.append(plural("transição", "transições", symbol));
        sb.append(" com consumo de símbolo, ");
        sb.append(empty).append(" ");
        sb.append(plural("transição", "transições", empty));
        sb.append(" em vazio, e ").append(smc);
        sb.append(" ").append(plural("chamada", "chamadas", smc));
        sb.append(" de submáquina), ").append(sm).append(" ");
        sb.append(plural("submáquina", sm)).append(", ").append(ac);
        sb.append(" ").append(plural("chamada", ac)).append(" de ");
        sb.append(plural("função", "funções", ac)).append(" ");
        sb.append(plural("adaptativa", ac)).append(" (").append(prec);
        sb.append(" ").append(plural("função", "funções", prec)).append(" ");
        sb.append(plural("adaptativa", prec)).append(" ");
        sb.append(plural("anterior", "anteriores", prec)).append(", e ");
        sb.append(postc).append(" ").append(plural("função", "funções", postc));
        sb.append(" ").append(plural("adaptativa", postc)).append(" ");
        sb.append(plural("posterior", "posteriores", postc)).append("), e ");
        sb.append(aa).append(" ").append(plural("ação", "ações", aa));
        sb.append(" ").append(plural("adaptativa", aa)).append(" ");
        sb.append(plural("definida", aa)).append(".");
        return sb.toString();
    }

    /**
     * Retorna o singular ou plural da palavra, de acordo com o valor inteiro.
     *
     * @param singular Palavra no singular.
     * @param plural Palavra no plural.
     * @param number Valor a ser verificado.
     * @return Palavra no singular ou plural.
     */
    private String plural(String singular, String plural, int number) {
        return number == 1 ? singular : plural;
    }

    /**
     * Retorna o singular ou plural da palavra, de acordo com o valor inteiro.
     *
     * @param word Palavra no singular.
     * @param number Valor a ser verificado.
     * @return Palavra no singular ou plural.
     */
    private String plural(String word, int number) {
        return plural(word, word.concat("s"), number);
    }

    /**
     * Verifica se o autômato possui chamadas válidas de submáquina.
     *
     * @throws AutomatonSpecException Existem chamadas inválidas.
     */
    private void hasValidSubmachineCalls() throws AutomatonSpecException {

        // obtém todas as
        // chamadas de submáquinas
        Set<String> smc = automaton.getTransitions().getTransitions().stream().
                filter((XMLTransition t) -> t.getCall() != null).
                map(XMLTransition::getCall).collect(Collectors.toSet());

        // obtém todas as
        // submáquinas definidas
        Set<String> sm = automaton.getSubmachines().getSubmachines().stream().
                map(XMLSubmachine::getName).collect(Collectors.toSet());

        // verifica se existem chamadas
        // a submáquinas inválidas
        smc.removeAll(sm);
        if (!smc.isEmpty()) {
            raise("O autômato adaptativo possui chamadas %s %s %s, não %s. É "
                    + "necessário que todas as chamadas de submáquinas sejam "
                    + "válidas. Por favor, verifique as transições "
                    + "correspondentes e tente novamente. O programa será "
                    + "encerrado.",
                    plural("à", smc.size()),
                    plural("submáquina", smc.size()),
                    smc,
                    plural("definida", smc.size())
            );
        }
    }

    /**
     * Verifica se o autômato possui transições com consumo de símbolo válidas.
     *
     * @throws AutomatonSpecException Existem transições com consumo de símbolo
     * inválidas.
     */
    private void hasValidSymbolTransitions() throws AutomatonSpecException {

        // verifica se existem transições com
        // consumo de símbolo e com chamadas
        // de submáquina ao mesmo tempo
        if (automaton.getTransitions().getTransitions().stream().
                anyMatch((XMLTransition t) -> (t.getSymbol() != null
                        && t.getCall() != null))) {
            raise("O autômato adaptativo possui transições com consumo de "
                    + "símbolo e chamada de submáquina ao mesmo tempo. Os dois "
                    + "tipos são mutuamente exclusivos. Por favor, verifique "
                    + "as transições e tente novamente. O programa será "
                    + "encerrado.");

        }
    }

}
