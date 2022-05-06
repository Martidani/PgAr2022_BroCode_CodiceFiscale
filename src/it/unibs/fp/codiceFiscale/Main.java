package it.unibs.fp.codiceFiscale;

import java.io.*;
import javax.xml.stream.*;

/**
 * Classe Main per scrivere il file "codiciPersone.xml"
 */
public class Main {

    /**
     * Preparazione e scrittura dei dati sul file "codiciPersone.xml"
     * <p>Tag persone, con i dati di ogni persona e il codice fiscale se presente, oppure ASSENTE
     * <p>Tag codici con i codici spaiati e i codici invalidi
     * <p>Istanza oggetto inputPersone
     * @see InputPersone#InputPersone()
     * @see InputPersone#setArrayPersone()
     * @see InputPersone#divisioneCodiciErrati()
     *
     * @param args
     * @throws XMLStreamException Se dovesse esserci un errore nel file di scrittura
     */
    public static void main(String[] args) throws XMLStreamException {
        InputPersone inputPersone = new InputPersone();
        //INIZIO DELLA LETTURA DI "inputPersone.xml" E CREAZIONE DELL'ARRAYLIST DI PERSONE
        inputPersone.setArrayPersone();
        //DIVISIONE DEI CODICI ERRATI E SPAIATI
        inputPersone.divisioneCodiciErrati();
        //SCRITTURA DEL FILE "codiciPersone.xml"
        scriviDatiSuFile(inputPersone);
    }

    /**
     * Metodo per scrivere i dati nel file "codiciPersone.xml"
     * <p>Inizializzazione del file di scrittura, in cui saranno contenute le informazioni di ogni persona e i codici fiscali
     * <p>Inserimento dei dati personali di ogni persona con inserimento del codice fiscale con controllo se corretto o no
     * <p>Inserimento dei codici invalidi
     * <p>Inserimento dei codici spaiati
     *
     * @see Main#codiceFiscaleControllo(Persona)
     * @param inputPersone Oggetto con la lista delle persone e i codici da scrivere
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
        try {
            //INIIZO TAG OUTPUT
            codiciPersonew.writeStartElement("output");
                //INIZIO TAG PERSONE
                codiciPersonew.writeStartElement("persone");
                //NUMERO DI PERSONE
                codiciPersonew.writeAttribute("numero", Integer.toString(inputPersone.getPersone().size()));

                for (int i = 0; i < inputPersone.getPersone().size(); i++) {
                    //INIZIO TAG PERSONA
                    codiciPersonew.writeStartElement("persona");
                    codiciPersonew.writeAttribute("id", Integer.toString(i));
                        //NOME
                        codiciPersonew.writeStartElement("nome");
                        codiciPersonew.writeCharacters(inputPersone.getPersona(i).getNome());
                        codiciPersonew.writeEndElement();
                        //COGNOME
                        codiciPersonew.writeStartElement("cognome");
                        codiciPersonew.writeCharacters(inputPersone.getPersona(i).getCognome());
                        codiciPersonew.writeEndElement();
                        //SESSO
                        codiciPersonew.writeStartElement("sesso");
                        codiciPersonew.writeCharacters(inputPersone.getPersona(i).getSesso());
                        codiciPersonew.writeEndElement();
                        //COMUNE DI NASCITA
                        codiciPersonew.writeStartElement("comune_nascita");
                        codiciPersonew.writeCharacters(inputPersone.getPersona(i).getComuneNascita());
                        codiciPersonew.writeEndElement();
                        //DATA DI NASCITA
                        codiciPersonew.writeStartElement("data_nascita");
                        codiciPersonew.writeCharacters(inputPersone.getPersona(i).getAaDataNascita() + "-" + inputPersone.getPersona(i).getMmDataNascita() + "-" + inputPersone.getPersona(i).getGgDataNascita());
                        codiciPersonew.writeEndElement();
                        //CODICE FISCALE
                        codiciPersonew.writeStartElement("codice_fiscale");
                        codiciPersonew.writeCharacters(codiceFiscaleControllo(inputPersone.getPersona(i)));
                        codiciPersonew.writeEndElement();
                    //FINE TAG PERSONA
                    codiciPersonew.writeEndElement();
                }
                //FINE TAG PERSONE
                codiciPersonew.writeEndElement();
                //INIZIO TAG CODICI
                codiciPersonew.writeStartElement("codici");
                    //INIZIO TAG INVALIDI
                    codiciPersonew.writeStartElement("invalidi");
                    //NUMERO DI CODICI INVALIDI
                    codiciPersonew.writeAttribute("numero", Integer.toString(inputPersone.getCodiciInvalidi().size()));
                    for(int i =0; i < inputPersone.getCodiciInvalidi().size(); i++) {
                        //CODICE
                        codiciPersonew.writeStartElement("codice");
                        codiciPersonew.writeCharacters(inputPersone.getCodiceInvalido(i));
                        codiciPersonew.writeEndElement();
                    }
                    //FINE TAG INVALIDI
                    codiciPersonew.writeEndElement();
                    //INIZIO TAG SPAIATI
                    codiciPersonew.writeStartElement("spaiati");
                    codiciPersonew.writeAttribute("numero", Integer.toString(inputPersone.getCodiciSpaiati().size()));
                    for(int i =0; i < inputPersone.getCodiciSpaiati().size(); i++) {
                        //CODICE
                        codiciPersonew.writeStartElement("codice");
                        codiciPersonew.writeCharacters(inputPersone.getCodiceSpaiato(i));
                        codiciPersonew.writeEndElement();
                    }
                    //FINE TAG SPAIATI
                    codiciPersonew.writeEndElement();
                //FINE TAG CIDICI
                codiciPersonew.writeEndElement();
            //FINE TAG OUTPUT
            codiciPersonew.writeEndElement();
            //FINE DOCUMENTO
            codiciPersonew.writeEndDocument();
            codiciPersonew.flush();
            codiciPersonew.close();

        } catch (Exception e) {
            System.out.println("Errore nella scrittura di \"codiciPersone.xml\"");
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
