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
package br.usp.poli.lta.cereda.xml2aa.xml;

import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAction;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLActionCall;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAction;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveActions;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLAdaptiveAutomaton;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLState;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLSubmachine;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLSubmachines;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLTransition;
import br.usp.poli.lta.cereda.xml2aa.xml.model.XMLTransitions;
import com.thoughtworks.xstream.XStream;
import java.io.File;

/**
 * Implementa uma transformação XML.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class XMLTransformation {

    // atributo da classe
    private final XStream xstream;

    /**
     * Construtor.
     */
    public XMLTransformation() {

        // define o conversor da
        // especificação XML
        xstream = new XStream();

        // transições
        xstream.alias("transition", XMLTransition.class);
        xstream.aliasAttribute(XMLTransition.class, "from", "from");
        xstream.aliasAttribute(XMLTransition.class, "to", "to");
        xstream.aliasAttribute(XMLTransition.class, "symbol", "symbol");
        xstream.aliasAttribute(XMLTransition.class, "call", "call");

        // chamadas de funções adaptativas
        xstream.aliasAttribute(XMLActionCall.class, "name", "name");
        xstream.addImplicitCollection(XMLActionCall.class, "parameters",
                "parameter", String.class);

        // lista de transições
        xstream.alias("transitions", XMLTransitions.class);
        xstream.addImplicitCollection(XMLTransitions.class, "transitions");

        // lista de submáquinas
        xstream.alias("submachines", XMLSubmachines.class);
        xstream.addImplicitCollection(XMLSubmachines.class, "submachines");

        // lista de ações adaptativas
        xstream.alias("actions", XMLAdaptiveActions.class);
        xstream.addImplicitCollection(XMLAdaptiveActions.class, "actions");

        // autômato adaptativo
        xstream.alias("adaptiveAutomaton", XMLAdaptiveAutomaton.class);

        // submáquina do autômato
        xstream.alias("submachine", XMLSubmachine.class);
        xstream.aliasAttribute(XMLSubmachine.class, "name", "name");
        xstream.aliasAttribute(XMLSubmachine.class, "main", "main");
        xstream.addImplicitCollection(XMLSubmachine.class, "states");

        // estado do autômato
        xstream.alias("state", XMLState.class);
        xstream.aliasAttribute(XMLState.class, "start", "start");
        xstream.aliasAttribute(XMLState.class, "accepting", "accepting");
        xstream.aliasAttribute(XMLState.class, "name", "name");

        // ação adaptativa
        xstream.alias("adaptiveAction", XMLAdaptiveAction.class);
        xstream.aliasAttribute(XMLAdaptiveAction.class, "name", "name");
        xstream.addImplicitCollection(XMLAdaptiveAction.class, "parameters",
                "parameter", String.class);
        xstream.addImplicitCollection(XMLAdaptiveAction.class, "variables",
                "variable", String.class);
        xstream.addImplicitCollection(XMLAdaptiveAction.class, "generators",
                "generator", String.class);
        xstream.addImplicitCollection(XMLAdaptiveAction.class, "actions");

        // ação
        xstream.alias("action", XMLAction.class);
        xstream.aliasAttribute(XMLAction.class, "type", "type");
        xstream.aliasAttribute(XMLAction.class, "from", "from");
        xstream.aliasAttribute(XMLAction.class, "to", "to");
        xstream.aliasAttribute(XMLAction.class, "symbol", "symbol");
        xstream.aliasAttribute(XMLAction.class, "call", "call");
    }

    /**
     * Obtém a especificação XML do autômato adaptativo a partir do arquivo XML.
     *
     * @param file Arquivo XML.
     * @return Especificação XML do autômato adaptativo.
     * @throws Exception O arquivo está malformado.
     */
    public XMLAdaptiveAutomaton get(File file) throws Exception {
        try {

            // retorna a especificação
            // XML do autômato adaptativo
            return (XMLAdaptiveAutomaton) xstream.fromXML(file);
        } catch (Exception exception) {
            throw new Exception("O arquivo '" + file.getName() + "' não foi "
                    + "convertido para o formato intermediário de "
                    + "representação do autômato adaptativo. É possível que "
                    + "este não seja um arquivo XML válido. Por favor, "
                    + "verifique os elementos descritos no arquivo e tente "
                    + "novamente. O programa será encerrado.");
        }
    }

}
