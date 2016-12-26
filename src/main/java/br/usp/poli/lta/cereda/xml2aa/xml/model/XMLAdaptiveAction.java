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

import java.util.ArrayList;
import java.util.List;

/**
 * Especificação XML de uma ação adaptativa.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class XMLAdaptiveAction {

    // atributos da classe
    private String name;
    private List<String> parameters;
    private List<String> variables;
    private List<String> generators;
    private List<XMLAction> actions;

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<XMLAction> getActions() {
        return actions;
    }

    /**
     * Setter.
     *
     * @param actions Lista de ações.
     */
    public void setActions(List<XMLAction> actions) {
        this.actions = actions;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<String> getVariables() {
        return variables == null ? new ArrayList<>() : variables;
    }

    /**
     * Setter.
     *
     * @param variables Lista de variáveis.
     */
    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<String> getGenerators() {
        return generators == null ? new ArrayList<>() : generators;
    }

    /**
     * Setter.
     *
     * @param generators Lista de geradores.
     */
    public void setGenerators(List<String> generators) {
        this.generators = generators;
    }

    /**
     * Getter.
     *
     * @return Objeto.
     */
    public List<String> getParameters() {
        return parameters == null ? new ArrayList<>() : parameters;
    }

    /**
     * Setter.
     *
     * @param parameters Lista de parâmetros.
     */
    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
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
     * @param name Nome da ação.
     */
    public void setName(String name) {
        this.name = name;
    }

}
