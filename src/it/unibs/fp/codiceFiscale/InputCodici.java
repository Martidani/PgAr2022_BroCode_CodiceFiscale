package it.unibs.fp.codiceFiscale;

import javax.xml.stream.*;
import java.io.*;

/**
 * Interfaccia per confrontare il codice generato con i codici nella lista del file "codiciFiscali.xml"
 */
public interface InputCodici {

    /**
     * Metodo per controllare se il codice coincide con uno della lista
     * Viene inizializzato il file di lettura
     * <p>Nel ciclo se il codice fiscale coincide con uno dei codici presenti si termina l'iterazione
     *
     * @param codiceFiscale codice fiscale della persona da cercare
     * @return Ritorna vero se il codice esiste, altrimenti falso
     * @throws XMLStreamException Se dovesse esserci un errore nella lettura del file
     */
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

        while (codiciFiscalir.hasNext() && !codiceValido) {
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
                    //CONFRONTO DEI CODICI
                    if(codiciFiscalir.getText().equals(codiceFiscale.toString()))
                        codiceValido = true;
                    break;
            }
            codiciFiscalir.next();
        }

        return codiceValido;
    }

}
