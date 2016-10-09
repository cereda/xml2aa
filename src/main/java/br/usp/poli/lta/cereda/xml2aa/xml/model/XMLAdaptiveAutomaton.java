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
package br.usp.poli.lta.cereda.xml2aa.xml.model;

import java.util.List;

/**
 * Especificação XML de um autômato adaptativo.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class XMLAdaptiveAutomaton {

    // atributos da classe
    private XMLTransitions transitions;
    private XMLSubmachines submachines;
    private List<XMLAdaptiveAction> actions;

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<XMLAdaptiveAction> getActions() {
        return actions;
    }

    /**
     * Setter.
     *
     * @param actions Ações.
     */
    public void setActions(List<XMLAdaptiveAction> actions) {
        this.actions = actions;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public XMLSubmachines getSubmachines() {
        return submachines;
    }

    /**
     * Setter.
     *
     * @param submachines Submáquinas.
     */
    public void setSubmachines(XMLSubmachines submachines) {
        this.submachines = submachines;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public XMLTransitions getTransitions() {
        return transitions;
    }

    /**
     * Setter.
     *
     * @param transitions Transições.
     */
    public void setTransitions(XMLTransitions transitions) {
        this.transitions = transitions;
    }

}
