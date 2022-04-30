package it.unibs.fp.codiceFiscale;

import java.io.*;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) throws XMLStreamException {

        //LETTURA FILE inputPersone.xml
        XMLInputFactory personeif = null;
        XMLStreamReader personer = null;
        try {
            personeif = XMLInputFactory.newInstance();
            personer = personeif.createXMLStreamReader("inputPersone.xml", new FileInputStream("inputPersone.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

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

        //SCRITTURA DI FILE OUTPUT
        XMLOutputFactory codiciGeneratiof = null;
        XMLStreamWriter codiciGeneratiw = null;
        try {
            codiciGeneratiof = XMLOutputFactory.newInstance();
            codiciGeneratiw = codiciGeneratiof.createXMLStreamWriter(new FileOutputStream("codiciPersone.xml"), "utf-8");
            codiciGeneratiw.writeStartDocument("utf-8", "1.0");
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }

        //Persona[] persone = new Persona[];


        while (personer.hasNext()) { // continua a leggere finché ha eventi a disposizione
            String lastTag;

            switch (personer.getEventType()) { // switch sul tipo di evento
                case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
                    //System.out.println("Start Read Doc " + "inputPersone.xml");
                    break;
                case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
                    //System.out.println("Tag " + personer.getLocalName());
                    //for (int i = 0; i < personer.getAttributeCount(); i++)
                    //    System.out.printf(" => attributo %s->%s%n", personer.getAttributeLocalName(i), personer.getAttributeValue(i));
                    break;
                case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
                    //System.out.println("END-Tag " + personer.getLocalName());
                    break;
                case XMLStreamConstants.COMMENT:
                    //System.out.println("// commento " + personer.getText());break; // commento: ne stampa il contenuto
                    break;
                case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
                    if (personer.getText().trim().length() > 0) {   // controlla se il testo non contiene solo spazi
                        System.out.println("-> " + personer.getText());
                    }
                    break;
            }

            personer.next();
        }



    }
}
