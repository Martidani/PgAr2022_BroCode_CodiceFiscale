package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;

/**
 * Interfaccia per restituire il codice del comune presente nel file "comuni.xml"
 */

public interface InputComuni {

    /**
     * Metodo per ritornare il codice del comune, dato il nome del comune, presente nel file "comuni.xml"
     * Se il comune non e' presente si ritorna un codice non esistente "****", in modo da rendere il codice fiscale invalido
     *
     * @param comuneNascita comune di nascita di cui si necessita il codice
     * @return ritorna il codice del comune
     * @throws XMLStreamException
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

        boolean restituisciCodice = false;
        boolean nextCodice = false;

        while (comunir.hasNext()) { // continua a leggere finché ha eventi a disposizione

            switch (comunir.getEventType()) { // switch sul tipo di evento
                case XMLStreamConstants.START_DOCUMENT:
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    if(nextCodice)
                        restituisciCodice = true;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    break;
                case XMLStreamConstants.COMMENT:
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if(comunir.getText().equals(comuneNascita))
                        nextCodice = true;
                    if(restituisciCodice)
                        return comunir.getText();
                    break;
            }
            comunir.next();
        }

        return "****";
    }
}
