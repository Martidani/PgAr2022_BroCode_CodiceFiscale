package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;

public interface InputCodici {

    static boolean existCodice(StringBuffer codiceFiscale) throws XMLStreamException {
        //LETTURA FILE codiciFiscali.xml
        XMLInputFactory codiciFiscaliif = null;
        XMLStreamReader codiciFiscalir = null;
        try {
            codiciFiscaliif = XMLInputFactory.newInstance();
            codiciFiscalir = codiciFiscaliif.createXMLStreamReader("codiciFiscali.xml", new FileInputStream("codiciFiscali.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        boolean codiceValido = false;

        while (codiciFiscalir.hasNext()) {
            switch (codiciFiscalir.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    break;
                case XMLStreamConstants.COMMENT:
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if(codiciFiscalir.getText().equals(codiceFiscale.toString()))
                        codiceValido = true;
                    break;
            }
            codiciFiscalir.next();
        }

        return codiceValido;
    }

}
