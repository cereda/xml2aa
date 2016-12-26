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
 * Especificação XML de uma lista de transições.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class XMLTransitions {

    // atributo da classe
    private List<XMLTransition> transitions;

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<XMLTransition> getTransitions() {
        return transitions;
    }

    /**
     * Setter.
     *
     * @param transitions Transições.
     */
    public void setTransitions(List<XMLTransition> transitions) {
        this.transitions = transitions;
    }

}
