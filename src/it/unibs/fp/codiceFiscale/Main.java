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

        //Persona[] persone = new Persona[];
        ArrayList<Persona> persone = new ArrayList<>();
        int countDati = 0;
        boolean nextPerson = false;
        int countPersone = 0;
        String[] datiPersona = new String[5];

        //LETTURA DEL FILE INPUTPERSONE
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

                    if (personer.getLocalName().equals("persona")) {
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

                    if (nextPerson) {
                        persone.add(new Persona(datiPersona[0], datiPersona[1], datiPersona[2], datiPersona[3], datiPersona[4].split("-")[0], datiPersona[4].split("-")[1], datiPersona[4].split("-")[2]));
                        persone.get(countPersone).setCodiceFiscale();
                        countPersone++;
                        countDati = 0;
                        nextPerson = false;
                    } else if (personer.getText().trim().length() > 0) {     // controlla se il testo non contiene solo spazi
                        datiPersona[countDati] = personer.getText();
                        countDati++;
                    }

                    break;
            }
            personer.next();
        }

        /*
        for(int i = 0; i < persone.size(); i++) {
            System.out.println(persone.get(i).stampaP() + "\n");
        }

         */

        ArrayList<String> codiciInvalidi = new ArrayList<>();
        ArrayList<String> codiciSpaiati = new ArrayList<>();

        for(int i = 0; i < persone.size(); i++) {
            switch(persone.get(i).getValidita()) {
                case VALIDO:
                    break;
                case INVALIDO:
                    codiciInvalidi.add(persone.get(i).getCodiceFiscale().toString());
                    break;
                case SPAIATO:
                    codiciSpaiati.add(persone.get(i).getCodiceFiscale().toString());
                    break;
                default:
                    break;
            }
        }


        //SCRITTURA DI FILE OUTPUT
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
                codiciPersonew.writeAttribute("numero", Integer.toString(persone.size()));

                for (int i = 0; i < persone.size(); i++) {
                    //inizio TAG persona
                    codiciPersonew.writeStartElement("persona");
                    codiciPersonew.writeAttribute("id", Integer.toString(i));
                    //nome
                    codiciPersonew.writeStartElement("nome");
                    codiciPersonew.writeCharacters(persone.get(i).getNome());
                    codiciPersonew.writeEndElement();
                    //cognome
                    codiciPersonew.writeStartElement("conome");
                    codiciPersonew.writeCharacters(persone.get(i).getCognome());
                    codiciPersonew.writeEndElement();
                    //sesso
                    codiciPersonew.writeStartElement("sesso");
                    codiciPersonew.writeCharacters(persone.get(i).getSesso());
                    codiciPersonew.writeEndElement();
                    //comune di nascita
                    codiciPersonew.writeStartElement("comune_nascita");
                    codiciPersonew.writeCharacters(persone.get(i).getComuneNascita());
                    codiciPersonew.writeEndElement();
                    //data di nascita
                    codiciPersonew.writeStartElement("data_nascita");
                    codiciPersonew.writeCharacters(persone.get(i).getAaDataNascita() + "-" + persone.get(i).getMmDataNascita() + "-" + persone.get(i).getGgDataNascita());
                    codiciPersonew.writeEndElement();
                    //codice fiscale
                    codiciPersonew.writeStartElement("codice_fiscale");
                    codiciPersonew.writeCharacters(codiceFiscaleControllo(persone.get(i)));
                    codiciPersonew.writeEndElement();

                    //fine TAG persona
                    codiciPersonew.writeEndElement();
                }
                codiciPersonew.writeEndElement();

                codiciPersonew.writeStartElement("codici");
                    codiciPersonew.writeStartElement("invalidi");
                    codiciPersonew.writeAttribute("numero", Integer.toString(codiciInvalidi.size()));
                    for(int i =0; i < codiciInvalidi.size(); i++) {
                        codiciPersonew.writeStartElement("codice");
                        codiciPersonew.writeCharacters(codiciInvalidi.get(i));
                        codiciPersonew.writeEndElement();
                    }
                    codiciPersonew.writeEndElement();

                    codiciPersonew.writeStartElement("spaiati");
                    codiciPersonew.writeAttribute("numero", Integer.toString(codiciSpaiati.size()));
                    for(int i =0; i < codiciInvalidi.size(); i++) {
                        codiciPersonew.writeStartElement("codice");
                        codiciPersonew.writeCharacters(codiciSpaiati.get(i));
                        codiciPersonew.writeEndElement();
                    }
                    codiciPersonew.writeEndElement();
                codiciPersonew.writeEndElement();


            codiciPersonew.writeEndElement(); // chiusura di </programmaArnaldo>
            codiciPersonew.writeEndDocument(); // scrittura della fine del documento
            codiciPersonew.flush(); // svuota il buffer e procede alla scrittura
            codiciPersonew.close(); // chiusura del documento e delle risorse impiegate

        } catch (Exception e) { // se c’è un errore viene eseguita questa parte
            System.out.println("Errore nella scrittura");
        }


    }

    private static String codiceFiscaleControllo(Persona p) {
        if(p.getValidita() == ValiditaCodici.VALIDO)
            return p.getCodiceFiscale().toString();
        else
            return "ASSENTE";
    }


}
