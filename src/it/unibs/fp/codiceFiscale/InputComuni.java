package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;

public interface InputComuni {

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

        return "A000";
    }
}
