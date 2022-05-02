package it.unibs.fp.codiceFiscale;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.XMLFormatter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

/**
 * Classe main per scrivere il file "codiciPersone.xml"
 */

public class Main {

    /**
     * Scrittura dei dati sul file "codiciPersone.xml"
     * Tag persone, con i dati di ogni  persona e il codice fiscale se presente, oppure ASSENTE
     * Tag codici con i codici spaiati e i codici invalidi
     *
     * @param args
     * @throws XMLStreamException
     */

    public static void main(String[] args) throws XMLStreamException {

        /**
         * Istanza oggetto inputPersone
         * @see InputPersone#InputPersone()
         * @see InputPersone#setArrayPersone()
         * @see InputPersone#divisioneCodiciErrati()
         */
        InputPersone inputPersone = new InputPersone();
        inputPersone.setArrayPersone();
        inputPersone.divisioneCodiciErrati();

        /**
         * Inizializzazione del file di scrittura
         * Saranno contenute le informazioni di ogni persona e i codici fiscali
         */
        XMLOutputFactory codiciPersoneof = null;
        XMLStreamWriter codiciPersonew = null;
        try {
            codiciPersoneof = XMLOutputFactory.newInstance();
            codiciPersonew = codiciPersoneof.createXMLStreamWriter(new FileOutputStream("codiciPersone.xml"), "utf-8");
            codiciPersonew.writeStartDocument("utf-8", "1.0");
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }

        //SCRITTURA DEL FILE codiciPersone.xml
        try { // blocco try per raccogliere eccezioni
            codiciPersonew.writeStartElement("output");
                codiciPersonew.writeStartElement("persone");
                codiciPersonew.writeAttribute("numero", Integer.toString(inputPersone.getPersone().size()));

                for (int i = 0; i < inputPersone.getPersone().size(); i++) {
                    //inizio TAG persona
                    codiciPersonew.writeStartElement("persona");
                    codiciPersonew.writeAttribute("id", Integer.toString(i));
                    //nome
                    codiciPersonew.writeStartElement("nome");
                    codiciPersonew.writeCharacters(inputPersone.getPersona(i).getNome());
                    codiciPersonew.writeEndElement();
                    //cognome
                    codiciPersonew.writeStartElement("conome");
                    codiciPersonew.writeCharacters(inputPersone.getPersona(i).getCognome());
                    codiciPersonew.writeEndElement();
                    //sesso
                    codiciPersonew.writeStartElement("sesso");
                    codiciPersonew.writeCharacters(inputPersone.getPersona(i).getSesso());
                    codiciPersonew.writeEndElement();
                    //comune di nascita
                    codiciPersonew.writeStartElement("comune_nascita");
                    codiciPersonew.writeCharacters(inputPersone.getPersona(i).getComuneNascita());
                    codiciPersonew.writeEndElement();
                    //data di nascita
                    codiciPersonew.writeStartElement("data_nascita");
                    codiciPersonew.writeCharacters(inputPersone.getPersona(i).getAaDataNascita() + "-" + inputPersone.getPersona(i).getMmDataNascita() + "-" + inputPersone.getPersona(i).getGgDataNascita());
                    codiciPersonew.writeEndElement();
                    //codice fiscale
                    codiciPersonew.writeStartElement("codice_fiscale");
                    codiciPersonew.writeCharacters(codiceFiscaleControllo(inputPersone.getPersona(i)));
                    codiciPersonew.writeEndElement();

                    //fine TAG persona
                    codiciPersonew.writeEndElement();
                }
                codiciPersonew.writeEndElement();

                codiciPersonew.writeStartElement("codici");
                    codiciPersonew.writeStartElement("invalidi");
                    codiciPersonew.writeAttribute("numero", Integer.toString(inputPersone.getCodiciInvalidi().size()));
                    for(int i =0; i < inputPersone.getCodiciInvalidi().size(); i++) {
                        codiciPersonew.writeStartElement("codice");
                        codiciPersonew.writeCharacters(inputPersone.getCodiceInvalido(i));
                        codiciPersonew.writeEndElement();
                    }
                    codiciPersonew.writeEndElement();

                    codiciPersonew.writeStartElement("spaiati");
                    codiciPersonew.writeAttribute("numero", Integer.toString(inputPersone.getCodiciSpaiati().size()));
                    for(int i =0; i < inputPersone.getCodiciSpaiati().size(); i++) {
                        codiciPersonew.writeStartElement("codice");
                        codiciPersonew.writeCharacters(inputPersone.getCodiceSpaiato(i));
                        codiciPersonew.writeEndElement();
                    }
                    codiciPersonew.writeEndElement();
                codiciPersonew.writeEndElement();


            codiciPersonew.writeEndElement();
            codiciPersonew.writeEndDocument();
            codiciPersonew.flush();
            codiciPersonew.close();

        } catch (Exception e) {
            System.out.println("Errore nella scrittura");
        }
    }

    /**
     * Metodo per inserimento del codice fiscale nel file con controllo
     *
     * @param p persona di cui si vuole controllare la validita' del codice
     * @return codiceFiscale della persona se valido, ASSENTE altrimenti
     */

    private static String codiceFiscaleControllo(Persona p) {
        if(p.getValidita() == ValiditaCodici.VALIDO)
            return p.getCodiceFiscale().toString();
        else
            return "ASSENTE";
    }


}
