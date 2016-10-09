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
 * Especificação XML de uma submáquina do autômato adaptativo.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class XMLSubmachine {

    // atributos da classe
    private String name;
    private String main;
    private List<XMLState> states;

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public String getMain() {
        return main;
    }

    /**
     * Setter.
     *
     * @param main Submáquina principal.
     */
    public void setMain(String main) {
        this.main = main;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter.
     *
     * @param name Nome da submáquina.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<XMLState> getStates() {
        return states;
    }

    /**
     * Setter.
     *
     * @param states Lista de estados.
     */
    public void setStates(List<XMLState> states) {
        this.states = states;
    }

}
