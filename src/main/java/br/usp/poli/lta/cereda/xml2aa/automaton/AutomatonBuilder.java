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

import br.usp.poli.lta.cereda.xml2aa.model.ListAction;
import br.usp.poli.lta.cereda.aa.examples.ExampleState;
import br.usp.poli.lta.cereda.aa.examples.ExampleSymbol;
import br.usp.poli.lta.cereda.aa.execution.AdaptiveAutomaton;
import br.usp.poli.lta.cereda.aa.model.State;
import br.usp.poli.lta.cereda.aa.model.Submachine;
import br.usp.poli.lta.cereda.aa.model.Transition;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAction;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAutomaton;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLState;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLSubmachine;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementa um construtor de uma instância do autômato adaptativo de acordo
 * com a especificação XML.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class AutomatonBuilder {

    /**
     * Constrói um autômato adaptativo a partir da especificação XML.
     *
     * @param xml Especificação XML do autômato adaptativo.
     * @return Autômato adaptativo.
     */
    public AdaptiveAutomaton build(XMLAdaptiveAutomaton xml) {

        // conjunto de estados
        Set<String> states = new HashSet<>();

        // obtém todos os estados
        // a partir das transições
        xml.getTransitions().getTransitions().stream().map((xt) -> {
            states.add(xt.getFrom());
            return xt;
        }).forEach((xt) -> {
            states.add(xt.getTo());
        });

        // obtém todos os estados
        // a partir das submáquinas
        xml.getSubmachines().getSubmachines().stream().
                forEach((XMLSubmachine t) -> {
                    states.addAll(t.getStates().stream().
                            map(XMLState::getName).
                            collect(Collectors.toSet()));
                });

        // constrói o autômato adaptativo
        // propriamente dito, conforme a
        // especificação XML
        AdaptiveAutomaton automaton = new AdaptiveAutomaton() {
            
            @Override
            public void setup() {

                // cria as transições do
                // autômato adaptativo
                xml.getTransitions().getTransitions().stream().map((xt) -> {
                    Transition t = new Transition();

                    // define os estados
                    // de origem e destino
                    t.setSourceState(new ExampleState(xt.getFrom()));
                    t.setTargetState(new ExampleState(xt.getTo()));

                    // define o tipo de transição
                    // (consumo de símbolo, chamada
                    // de submáquina ou em vazio)
                    if (xt.getCall() != null) {
                        t.setSubmachineCall(xt.getCall());
                    } else if (xt.getSymbol() != null) {
                        t.setSymbol(new ExampleSymbol(xt.getSymbol()));
                    }

                    // adiciona a ação adaptativa
                    // anterior, se existir
                    if (xt.getPreAdaptiveFunction() != null) {
                        t.setPriorActionCall(xt.getPreAdaptiveFunction().getName());
                        if (xt.getPreAdaptiveFunction().
                                getParameters() != null) {
                            t.setPriorActionArguments(xt.getPreAdaptiveFunction().
                                    getParameters().toArray());
                        }
                    }
                    
                    // adiciona a ação adaptativa
                    // posterior, se existir
                    if (xt.getPostAdaptiveFunction() != null) {
                        t.setPostActionCall(xt.getPostAdaptiveFunction().getName());
                        if (xt.getPostAdaptiveFunction().
                                getParameters() != null) {
                            t.setPostActionArguments(xt.getPostAdaptiveFunction().
                                    getParameters().toArray());
                        }
                    }

                    return t;
                }).forEach((t) -> {
                    
                    // a transição recém-construída
                    // é adiciona no modelo do
                    // autômato adaptativo
                    transitions.add(t);
                });

                // se existem ações adaptativas na
                // especificação XML, estas são
                // adicionadas no modelo do
                // autômato adaptativo
                if (xml.getActions() != null) {
                    xml.getActions().stream().forEach((XMLAdaptiveAction t) -> {
                        actions.add(new ListAction(t.getName(), t));
                    });
                }

                // obtém as submáquinas da
                // especificação XML e faz
                // o mapeamento correspondente
                xml.getSubmachines().getSubmachines().stream().map((xs) -> {

                    // obtém todos os estados
                    // da submáquina
                    Set<State> all = xs.getStates().stream().
                            map((XMLState t) -> new ExampleState(t.getName())).
                            collect(Collectors.toSet());
                    
                    // obtém todos os estados
                    // de aceitação da submáquina
                    Set<State> accepting = xs.getStates().stream().
                            filter((XMLState t) -> t.getAccepting() != null).
                            map((XMLState t) -> new ExampleState(t.getName())).
                            collect(Collectors.toSet());
                    
                    // obtém o estado inicial
                    // da submáquina
                    State initial = xs.getStates().stream().
                            filter((XMLState t) -> t.getStart() != null).
                            map((XMLState t) -> new ExampleState(t.getName())).
                            collect(Collectors.toList()).get(0);

                    // adiciona a submáquina corrente
                    // no conjunto de submáquinas do
                    // modelo do autômato adaptativo
                    submachines.add(new Submachine(xs.getName(), all,
                            initial, accepting));

                    // retorna a submáquina para
                    // uma nova consulta
                    return xs;

                }).filter((xs) -> (xs.getMain() != null)).forEach((xs) -> {
                    
                    // se a submáquina corrente está
                    // marcada como principal, esta
                    // é definida também no modelo do
                    // autômato adaptativo
                    setMainSubmachine(xs.getName());
                });

            }
        };

        // o método gerador de estados é atualizado
        // para conter o próximo inteiro do conjunto
        // de estados inteiros
        ListAction.setCounter(states.stream().mapToInt(Integer::parseInt).
                max().getAsInt() + 1);

        // o autômato adaptativo
        // é efetivamente retornado
        return automaton;
    }

}
