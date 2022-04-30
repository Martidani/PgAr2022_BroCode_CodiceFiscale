package it.unibs.fp.codiceFiscale;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class Main {
    public static void main(String[] args) {

        XMLInputFactory personeif = null;
        XMLStreamReader personer = null;
        try {
            personeif = XMLInputFactory.newInstance();
            personer = personeif.createXMLStreamReader("inputPersone.xml", new FileInputStream("inputPersone.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        XMLInputFactory comuniif = null;
        XMLStreamReader comunir = null;
        try {
            comuniif = XMLInputFactory.newInstance();
            comunir = comuniif.createXMLStreamReader("comuni.xml", new FileInputStream("comuni.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        XMLInputFactory codiciFiscaliif = null;
        XMLStreamReader codiciFiscalir = null;
        try {
            codiciFiscaliif = XMLInputFactory.newInstance();
            codiciFiscalir = codiciFiscaliif.createXMLStreamReader("codiciFiscali.xml", new FileInputStream("codiciFiscali.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }



        while (personer.hasNext()) { // continua a leggere finché ha eventi a disposizione
            switch (personer.getEventType()) { // switch sul tipo di evento
                case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
                    System.out.println("Start Read Doc " + "inputPersone.xml");
                    break;
                case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
                    System.out.println("Tag " + personer.getLocalName());
                    for (int i = 0; i < personer.getAttributeCount(); i++)
                        System.out.printf(" => attributo %s->%s%n", personer.getAttributeLocalName(i), personer.getAttributeValue(i));
                    break;
                case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
                    System.out.println("END-Tag " + personer.getLocalName());
                    break;
                case XMLStreamConstants.COMMENT:
                    System.out.println("// commento " + personer.getText());
                    break; // commento: ne stampa il contenuto
                case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
                    if (personer.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
                        System.out.println("-> " + personer.getText());
                    break;
            }
            personer.next();
        }






    }
}
