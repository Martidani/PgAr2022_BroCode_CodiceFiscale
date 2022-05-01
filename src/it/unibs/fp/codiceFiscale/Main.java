package it.unibs.fp.codiceFiscale;

import java.io.*;
import java.sql.Array;
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
        ArrayList<Persona> persone = new ArrayList<>();
        int countDati = 0;
        boolean nextPerson = false;
        int countPersone = 0;
        String[] datiPersona = new String[5];

        while (personer.hasNext()) { // continua a leggere finché ha eventi a disposizione

            switch (personer.getEventType()) { // switch sul tipo di evento
                case XMLStreamConstants.START_DOCUMENT:                             // inizio del documento: stampa che inizia il documento
                    //System.out.println("Start Read Doc " + "inputPersone.xml");
                    break;

                case XMLStreamConstants.START_ELEMENT:                             // inizio di un elemento: stampa il nome del tag e i suoi attributi
                    //System.out.println("Tag " + personer.getLocalName());
                    //for (int i = 0; i < personer.getAttributeCount(); i++)
                    //    System.out.printf(" => attributo %s->%s%n", personer.getAttributeLocalName(i), personer.getAttributeValue(i));
                    break;

                case XMLStreamConstants.END_ELEMENT:                              // fine di un elemento: stampa il nome del tag chiuso
                    //System.out.println("END-Tag " + personer.getLocalName());

                    if(personer.getLocalName().equals("persona")) {
                        nextPerson = true;
                        //System.out.println("VERO");
                    }
                    break;

                case XMLStreamConstants.COMMENT:
                    //System.out.println("// commento " + personer.getText());break; // commento: ne stampa il contenuto
                    break;

                case XMLStreamConstants.CHARACTERS:                        // content all’interno di un elemento: stampa il testo
                    //if (personer.getText().trim().length() > 0) {       // controlla se il testo non contiene solo spazi
                        //System.out.println("-> " + personer.getText());
                    //}

                    if(nextPerson) {
                        persone.add(new Persona(datiPersona[0], datiPersona[1], datiPersona[2], datiPersona[3], datiPersona[4].split("-")[0], datiPersona[4].split("-")[1], datiPersona[4].split("-")[2]));
                        persone.get(countPersone).setCodiceFiscale();
                        countPersone++;
                        countDati = 0;
                        nextPerson = false;
                    }
                    else if (personer.getText().trim().length() > 0) {
                        datiPersona[countDati] = personer.getText();
                        countDati++;
                    }

                    break;
            }
            personer.next();
        }

        for(int i = 0; i < persone.size(); i++) {
            System.out.println(persone.get(i).stampaP() + "\n");
        }






    }
}
