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
 * Implementa a especificação XML das submáquinas.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class XMLSubmachines {

    // atributo da classe
    private List<XMLSubmachine> submachines;

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<XMLSubmachine> getSubmachines() {
        return submachines;
    }

    /**
     * Setter.
     *
     * @param submachines Lista de submáquinas.
     */
    public void setSubmachines(List<XMLSubmachine> submachines) {
        this.submachines = submachines;
    }

}
