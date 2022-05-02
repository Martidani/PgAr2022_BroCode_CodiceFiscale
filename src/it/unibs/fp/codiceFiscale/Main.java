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
 * Classe Main per scrivere il file "codiciPersone.xml"
 */
public class Main {

    /**
     * Scrittura dei dati sul file "codiciPersone.xml"
     * Tag persone, con i dati di ogni  persona e il codice fiscale se presente, oppure ASSENTE
     * Tag codici con i codici spaiati e i codici invalidi
     * Istanza oggetto inputPersone
     * @see InputPersone#InputPersone()
     * @see InputPersone#setArrayPersone()
     * @see InputPersone#divisioneCodiciErrati()
     *
     * @param args
     * @throws XMLStreamException
     */
    public static void main(String[] args) throws XMLStreamException {
        InputPersone inputPersone = new InputPersone();
        inputPersone.setArrayPersone();
        inputPersone.divisioneCodiciErrati();
        scriviDatiSuFile(inputPersone);
    }

    /**
     * Metodo per scrivere i dati nel file "codiciPersone.xml"
     * <p>Inizializzazione del file di scrittur, in cui saranno contenute le informazioni di ogni persona e i codici fiscali
     * <p>Inserimento dei dati personali di ogni persona,
     * con inserimento del codice fiscale con controllo se corretto o no
     * @see Main#codiceFiscaleControllo(Persona)
     * Inserimento dei codici invalidi
     * Inserimento dei codici spaiati
     */
    private static void scriviDatiSuFile(InputPersone inputPersone) {
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
     * Metodo per controllo sull'inserimento del codice fiscale nel file
     * <p>Se il codice e' valido si ritorna il codice fiscale
     * <p>Se il codice non e' valido si ritorna "ASSENTE"
     *
     * @param p persona di cui si vuole controllare la validita' del codice
     * @return Ritorna codiceFiscale della persona se valido, ASSENTE altrimenti
     */
    public static String codiceFiscaleControllo(Persona p) {
        if(p.getValidita() == ValiditaCodici.VALIDO)
            return p.getCodiceFiscale().toString();
        else
            return "ASSENTE";
    }


}
