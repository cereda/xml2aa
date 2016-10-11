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
package br.usp.poli.lta.cereda.xml2aa.model;

import br.usp.poli.lta.cereda.aa.examples.ExampleState;
import br.usp.poli.lta.cereda.aa.examples.ExampleSymbol;
import br.usp.poli.lta.cereda.aa.model.Action;
import br.usp.poli.lta.cereda.aa.model.Transition;
import br.usp.poli.lta.cereda.aa.model.actions.ActionQuery;
import br.usp.poli.lta.cereda.aa.model.actions.ElementaryActions;
import br.usp.poli.lta.cereda.aa.model.actions.SubmachineQuery;
import br.usp.poli.lta.cereda.aa.model.actions.Variable;
import br.usp.poli.lta.cereda.aa.model.sets.Mapping;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAction;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define uma ação adaptativa baseada em uma especificação de lista XML.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class ListAction extends Action {

    /**
     * Tipos de ações adaptativas elementares.
     */
    private enum ActionType {
        QUERY,
        ADD,
        REMOVE
    }

    // contador para o método
    // de geração de estados novos
    private static int GENERATOR_COUNTER = 0;

    // especificação XML da
    // ação adaptativa do autômato
    private final XMLAdaptiveAction xml;

    // mapa de variáveis e geradores
    private final Map<String, Object> map;

    /**
     * Construtor.
     *
     * @param name Nome da ação adaptativa.
     * @param xml Especificação XML da ação adaptativa do autômato.
     */
    public ListAction(String name, XMLAdaptiveAction xml) {
        super(name);
        this.xml = xml;
        this.map = new HashMap<>();
    }

    /**
     * Executa a ação.
     *
     * @param transitions Mapeamento do autômato.
     * @param transition Transição corrente.
     * @param parameters Parâmetros da função.
     */
    @Override
    public void execute(Mapping transitions,
            Transition transition, Object... parameters) {

        // limpa o mapa e faz novamente a ligação
        // de variáveis e geradores
        map.clear();
        bind(parameters);

        // classe responsável pela execução
        // das ações adaptativas elementares
        ElementaryActions ea = new ElementaryActions(transitions);

        // para cada ação adaptativa elementar
        // definida na ação adaptativa
        for (XMLAction action : xml.getActions()) {

            // obtém as variáveis representando
            // os estados de origem e destino
            Variable from = resolveState(action.getFrom());
            Variable to = resolveState(action.getTo());

            // faz a verificação de qual ação
            // adaptativa elementar executar
            switch (lookupAction(action)) {

                // primeiro caso -- 3 parâmetros da ação:
                // são basicamente dois padrões completos
                // e um padrão adicional considerando
                // símbolos nulos (transições em vazio)
                case 3:

                    // padrão 1: símbolos nulos que
                    // indicam uma transição em vazio
                    if (action.getSymbol() == null
                            && action.getCall() == null) {

                        // obtém o tipo da ação
                        // a ser executada
                        switch (getType(action)) {
                            case ADD:
                                ea.add(from, new Variable(null), to);
                                break;
                            case QUERY:
                                ea.query(from, new Variable(null), to);
                                break;
                            case REMOVE:
                                ea.remove(from, new Variable(null), to);
                                break;
                        }
                    } else if (action.getCall() == null) {

                        // padrão 2: consumo de símbolo
                        // (transições convencionais)
                        // obtém a variável referente
                        // ao símbolo
                        Variable symbol = resolveSymbol(action.getSymbol());

                        // obtém o tipo da ação
                        // a ser executada
                        switch (getType(action)) {
                            case ADD:
                                ea.add(from, symbol, to);
                                break;
                            case QUERY:
                                ea.query(from, symbol, to);
                                break;
                            case REMOVE:
                                ea.remove(from, symbol, to);
                                break;
                        }
                    } else {

                        // padrão 3: chamada de submáquina
                        // (transição sem consumo de símbolo)
                        // obtém a consulta à submáquina
                        // através da resolução de nome
                        SubmachineQuery call
                                = resolveSubmachine(action.getCall());

                        // obtém o tipo da ação
                        // a ser executada
                        switch (getType(action)) {
                            case ADD:
                                ea.add(from, call, to);
                                break;
                            case QUERY:
                                ea.query(from, call, to);
                                break;
                            case REMOVE:
                                ea.remove(from, call, to);
                                break;
                        }
                    }
                    break;

                // segundo caso -- 4 parâmetros da ação:
                // são basicamente quatro padrões completos
                // e um padrão adicional considerando
                // símbolos nulos (transições em vazio)
                case 4:

                    // padrões 1 e 2: existem chamadas a
                    // funções adaptativas anteriores
                    if (action.getPreAdaptiveFunction() != null) {

                        // obtém consulta de ações
                        // adaptativas através da
                        // resolução de nome e parâmetros
                        ActionQuery query = resolveAction(
                                action.getPreAdaptiveFunction().getName(),
                                action.getPreAdaptiveFunction().getParameters()
                        );

                        // símbolos nulos que indicam
                        // uma transição em vazio
                        if (action.getSymbol() == null
                                && action.getCall() == null) {

                            // obtém o tipo da ação
                            // a ser executada
                            switch (getType(action)) {
                                case ADD:
                                    ea.add(query, from,
                                            new Variable(null), to);
                                    break;
                                case QUERY:
                                    ea.query(query, from,
                                            new Variable(null), to);
                                    break;
                                case REMOVE:
                                    ea.remove(query, from,
                                            new Variable(null), to);
                                    break;
                            }
                        } else if (action.getCall() == null) {

                            // consumo de símbolo
                            // (transições convencionais) 
                            // obtém a variável referente
                            // ao símbolo
                            Variable symbol = resolveSymbol(action.getSymbol());

                            // obtém o tipo da ação
                            // a ser executada
                            switch (getType(action)) {
                                case ADD:
                                    ea.add(query, from, symbol, to);
                                    break;
                                case QUERY:
                                    ea.query(query, from, symbol, to);
                                    break;
                                case REMOVE:
                                    ea.remove(query, from, symbol, to);
                                    break;
                            }
                        } else {

                            // chamada de submáquina
                            // (transição sem consumo de símbolo)
                            // obtém a consulta à submáquina
                            // através da resolução de nome
                            SubmachineQuery call
                                    = resolveSubmachine(action.getCall());

                            // obtém o tipo da ação
                            // a ser executada
                            switch (getType(action)) {
                                case ADD:
                                    ea.add(query, from, call, to);
                                    break;
                                case QUERY:
                                    ea.query(query, from, call, to);
                                    break;
                                case REMOVE:
                                    ea.remove(query, from, call, to);
                                    break;
                            }
                        }
                    } else {

                        // padrões 3 e 4: existem chamadas a
                        // funções adaptativas posteriores
                        // obtém consulta de ações
                        // adaptativas através da
                        // resolução de nome e parâmetros
                        ActionQuery query = resolveAction(
                                action.getPostAdaptiveFunction().getName(),
                                action.getPostAdaptiveFunction().getParameters()
                        );

                        // símbolos nulos que indicam
                        // uma transição em vazio
                        if (action.getSymbol() == null
                                && action.getCall() == null) {

                            // obtém o tipo da ação
                            // a ser executada
                            switch (getType(action)) {
                                case ADD:
                                    ea.add(from, new Variable(null),
                                            to, query);
                                    break;
                                case QUERY:
                                    ea.query(from, new Variable(null),
                                            to, query);
                                    break;
                                case REMOVE:
                                    ea.remove(from, new Variable(null),
                                            to, query);
                                    break;
                            }
                        } else if (action.getCall() == null) {

                            // consumo de símbolo
                            // (transições convencionais)
                            // obtém a variável referente
                            // ao símbolo
                            Variable symbol = resolveSymbol(action.getSymbol());

                            // obtém o tipo da ação
                            // a ser executada
                            switch (getType(action)) {
                                case ADD:
                                    ea.add(from, symbol, to, query);
                                    break;
                                case QUERY:
                                    ea.query(from, symbol, to, query);
                                    break;
                                case REMOVE:
                                    ea.remove(from, symbol, to, query);
                                    break;
                            }
                        } else {

                            // chamada de submáquina
                            // (transição sem consumo de símbolo)
                            // obtém a consulta à submáquina
                            // através da resolução de nome
                            SubmachineQuery call
                                    = resolveSubmachine(action.getCall());

                            // obtém o tipo da ação
                            // a ser executada
                            switch (getType(action)) {
                                case ADD:
                                    ea.add(from, call, to, query);
                                    break;
                                case QUERY:
                                    ea.query(from, call, to, query);
                                    break;
                                case REMOVE:
                                    ea.remove(from, call, to, query);
                                    break;
                            }
                        }
                    }
                    break;

                // terceiro caso -- 5 parâmetros da ação:
                // são basicamente dois padrões completos
                // e um padrão adicional considerando
                // símbolos nulos (transições em vazio)
                case 5:

                    // obtém consulta de ações adaptativas
                    // anteriores através da resolução
                    // de nome e parâmetros
                    ActionQuery query1 = resolveAction(
                            action.getPreAdaptiveFunction().getName(),
                            action.getPreAdaptiveFunction().getParameters()
                    );

                    // obtém consulta de ações adaptativas
                    // posteriores através da resolução
                    // de nome e parâmetros
                    ActionQuery query2 = resolveAction(
                            action.getPostAdaptiveFunction().getName(),
                            action.getPostAdaptiveFunction().getParameters()
                    );

                    // padrão 1: símbolos nulos que
                    // indicam uma transição em vazio
                    if (action.getSymbol() == null
                            && action.getCall() == null) {

                        // obtém o tipo da ação
                        // a ser executada
                        switch (getType(action)) {
                            case ADD:
                                ea.add(query1, from, new Variable(null),
                                        to, query2);
                                break;
                            case QUERY:
                                ea.query(query1, from, new Variable(null),
                                        to, query2);
                                break;
                            case REMOVE:
                                ea.remove(query1, from, new Variable(null),
                                        to, query2);
                                break;
                        }
                    } else if (action.getCall() == null) {

                        // padrão 2: consumo de símbolo
                        // (transições convencionais)
                        Variable symbol = resolveSymbol(action.getSymbol());

                        // obtém o tipo da ação
                        // a ser executada
                        switch (getType(action)) {
                            case ADD:
                                ea.add(query1, from, symbol, to, query2);
                                break;
                            case QUERY:
                                ea.query(query1, from, symbol, to, query2);
                                break;
                            case REMOVE:
                                ea.remove(query1, from, symbol, to, query2);
                                break;
                        }
                    } else {

                        // padrão 3: chamada de submáquina
                        // (transição sem consumo de símbolo)
                        // obtém a consulta à submáquina
                        // através da resolução de nome
                        SubmachineQuery call
                                = resolveSubmachine(action.getCall());

                        // obtém o tipo da ação
                        // a ser executada
                        switch (getType(action)) {
                            case ADD:
                                ea.add(query1, from, call, to, query2);
                                break;
                            case QUERY:
                                ea.query(query1, from, call, to, query2);
                                break;
                            case REMOVE:
                                ea.remove(query1, from, call, to, query2);
                                break;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Define o contador do método gerador de novos estados, de acordo com o
     * valor máximo dos estados do autômato adaptativo.
     *
     * @param counter Valor inteiro.
     */
    public static void setCounter(int counter) {
        GENERATOR_COUNTER = counter;
    }

    /**
     * Gera um novo estado.
     *
     * @return Novo estado.
     */
    private String generateState() {
        int i = GENERATOR_COUNTER;
        GENERATOR_COUNTER++;
        return String.valueOf(i);
    }

    /**
     * Faz a ligação dos parâmetros da função adaptativa, das variáveis e dos
     * geradores.
     *
     * @param parameters Parâmetros da função adaptativa.
     */
    private void bind(Object... parameters) {

        // parâmetros podem ser nulos
        if (parameters != null) {
         
            // parâmetros
            for (int i = 0; i < parameters.length; i++) {
                map.put(xml.getParameters().get(i), parameters[i]);
            }
        }

        // variáveis
        xml.getVariables().stream().forEach((String t) -> {
            map.put(t, new Variable());
        });

        // geradores
        xml.getGenerators().stream().forEach((String t) -> {
            map.put(t, generateState());
        });
    }

    /**
     * Verifica qual categoria de ação elementar executar, de acordo com o
     * número de parâmetros fornecidos na especificação XML da ação.
     *
     * @param action Especificação XML da ação.
     * @return Categoria da ação.
     */
    private int lookupAction(XMLAction action) {
        int counter = 3;
        if (action.getPreAdaptiveFunction() != null) {
            counter++;
        }
        if (action.getPostAdaptiveFunction() != null) {
            counter++;
        }
        return counter;
    }

    /**
     * Obtém o tipo da ação a ser executada.
     *
     * @param action Especificação XML da ação adaptativa elementar.
     * @return Tipo da ação a ser executada.
     */
    private ActionType getType(XMLAction action) {
        switch (action.getType()) {
            case "query":
                return ActionType.QUERY;
            case "add":
                return ActionType.ADD;
            default:
                return ActionType.REMOVE;
        }
    }

    /**
     * Resolve o estado de acordo com o valor informado.
     *
     * @param value Valor a ser verificado.
     * @return Variável.
     */
    private Variable resolveState(String value) {
        Variable result;
        if (xml.getVariables().contains(value)) {
            result = (Variable) map.get(value);
        } else if (xml.getGenerators().contains(value)) {
            result = new Variable(new ExampleState(map.get(value).toString()));
        } else if (xml.getParameters().contains(value)) {
            result = new Variable(new ExampleState(map.get(value).toString()));
        } else {
            result = new Variable(new ExampleState(value));
        }
        return result;
    }

    /**
     * Resolve o símbolo de acordo com o valor informado.
     *
     * @param value Valor a ser verificado.
     * @return Variável.
     */
    private Variable resolveSymbol(String value) {
        Variable result;
        if (xml.getVariables().contains(value)) {
            result = (Variable) map.get(value);
        } else if (xml.getParameters().contains(value)) {
            result = new Variable(new ExampleSymbol(map.get(value).toString()));
        } else {
            result = new Variable(new ExampleSymbol(value));
        }
        return result;
    }

    /**
     * Resolve a submáquina de acordo com o valor informado.
     *
     * @param value Valor a ser verificado.
     * @return Resolução da consulta à submáquina.
     */
    private SubmachineQuery resolveSubmachine(String value) {
        SubmachineQuery result;
        if (xml.getVariables().contains(value)) {
            result = new SubmachineQuery((Variable) map.get(value));
        } else if (xml.getParameters().contains(value)) {
            result = new SubmachineQuery(
                    new Variable(map.get(value).toString())
            );
        } else {
            result = new SubmachineQuery(new Variable(value));
        }
        return result;
    }

    /**
     * Resolve a ação de acordo o nome da ação e a lista de parâmetros.
     *
     * @param name Nome da ação.
     * @param parameters Lista de parâmetros.
     * @return Resolução da consulta à ação.
     */
    private ActionQuery resolveAction(String name, List<String> parameters) {
        ActionQuery result;
        Variable action;

        // resolve o nome da ação
        if (xml.getVariables().contains(name)) {
            action = (Variable) map.get(name);
        } else if (xml.getParameters().contains(name)) {
            action = new Variable(map.get(name));
        } else {
            action = new Variable(name);
        }

        // resolve a lista de parâmetros,
        // se existirem na especificação
        if (parameters != null) {
            int size = parameters.size();
            Variable[] args = new Variable[size];
            for (int i = 0; i < size; i++) {
                args[i] = resolve(parameters.get(i));
            }
            result = new ActionQuery(action, args);
        } else {
            result = new ActionQuery(action);
        }

        return result;
    }

    /**
     * Resolve o valor, de acordo com o mapa.
     *
     * @param value Valor a ser verificado.
     * @return Variável.
     */
    private Variable resolve(String value) {
        Variable result;
        if (xml.getVariables().contains(value)) {
            result = (Variable) map.get(value);
        } else if (xml.getGenerators().contains(value)) {
            result = new Variable(new ExampleState(map.get(value).toString()));
        } else if (xml.getParameters().contains(value)) {
            result = new Variable(map.get(value));
        } else {
            result = new Variable(value);
        }
        return result;
    }

}
