package it.unibs.fp.codiceFiscale;

import javax.xml.stream.*;
import java.io.*;

/**
 * Interfaccia per restituire il codice del comune presente nel file "comuni.xml"
 */
public interface InputComuni {

    /**
     * Metodo per ritornare il codice del comune nel file "comuni.xml", dato il nome del comune
     * <p>Se il comune non e' presente si ritorna un codice non esistente "****",
     * in modo da rendere il codice fiscale invalido
     *
     * @param comuneNascita comune di nascita di cui si necessita il codice
     * @return ritorna il codice del comune
     * @throws XMLStreamException Se dovesse esserci un errore nella lettura del file
     */
    static String codiceComune(String comuneNascita) throws XMLStreamException {
        //LETTURA FILE comuni.xml
        XMLInputFactory comuniif = null;
        XMLStreamReader comunir = null;
        try {
            comuniif = XMLInputFactory.newInstance();
            comunir = comuniif.createXMLStreamReader("comuni.xml", new FileInputStream("comuni.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        //DIVENTA VERO QUANDO SI TROVA IL COMUNE CON IL CODICE DA RESTUIRE
        boolean nextCodice = false;

        while (comunir.hasNext()) {
            switch (comunir.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    break;
                case XMLStreamConstants.COMMENT:
                    break;
                case XMLStreamConstants.CHARACTERS:
                    /*
                     * CONTROLLO SE E' L'ELEMENTO DA RESTITUIRE
                     * E' DA RESTITUIRE SE L'ELEMENTO PRECEDENTE ERA IL COMUNE DI NASCITA E NON E' VUOTO
                     * VIENE RESTITUITO IL CODICE DEL COMUNE DI NASCITA DELLA PERSONA
                     */
                    if(nextCodice && comunir.getText().trim().length() > 0)
                        return comunir.getText();
                    /*
                     * CONTROLLO SUL NOME DEI COMUNI PRESENTI NEL FILE E IL COMUNE DI NASCITA DELLA PERSONA
                     * SE IL NOME DEL COMUNE COINCIDE, ALLORA SI DEVE RESTITUIRE IL CODICE CHE LO SEGUE, QUINDI IL PROSSIMO CODICE E' CORRETTO
                     */
                    if(comunir.getText().equals(comuneNascita))
                        nextCodice = true;
                    break;
            }
            comunir.next();
        }

        //SE NON ESISTE IL COMUNE SI RESTITUISCE UN CODICE CHE RENDE IL CODICE FISCALE ERRATO
        return "****";
    }
}
