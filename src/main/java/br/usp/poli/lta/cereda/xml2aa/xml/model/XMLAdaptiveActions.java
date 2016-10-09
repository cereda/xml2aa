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
 * Especificação XML das ações adaptativas.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class XMLAdaptiveActions {

    // atributo da classe
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
     * @param actions Lista de ações.
     */
    public void setActions(List<XMLAdaptiveAction> actions) {
        this.actions = actions;
    }

}
