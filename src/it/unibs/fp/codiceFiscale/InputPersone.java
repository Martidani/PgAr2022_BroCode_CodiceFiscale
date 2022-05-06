package it.unibs.fp.codiceFiscale;

import java.util.ArrayList;
import java.io.*;

import javax.xml.stream.*;

/**
 * Classe per input dei dati del file "inputPersone.xml" in un ArrayList di Persona
 */
public class InputPersone {

    /**
     * ArrayList di Persona, contiene tutte le persone presenti nel file "inputPersone.xml"
     * @see Persona#Persona(String, String, String, String, String, String, String)
     */
    private ArrayList<Persona> persone = new ArrayList<>();

    /**
     * ArrayList per contenere i codici invalidi
     */
    private ArrayList<String> codiciInvalidi = new ArrayList<>();

    /**
     * ArrayList per contenere i codici spaiati
     */
    private ArrayList<String> codiciSpaiati = new ArrayList<>();

    /**
     * Costruttore vuoto della classe
     */
    public InputPersone() {
    }

    //// GETTER \\\\
    /**
     * Getter di persone
     * @return Ritorna ArrayList di persone
     */
    public ArrayList<Persona> getPersone() {
        return persone;
    }

    /**
     * Getter di codiciInvalidi
     * @return Ritorna ArrayList di codiciInvalidi
     */
    public ArrayList<String> getCodiciInvalidi() {
        return codiciInvalidi;
    }

    /**
     * Getter di codiciSpaiati
     * @return Ritorna ArrayList di codiciSpaiati
     */
    public ArrayList<String> getCodiciSpaiati() {
        return codiciSpaiati;
    }

    /**
     * Metodo per ritornare una specifica persona nell'ArrayList
     * @param index indice di posizione nell'ArrayList
     * @return Ritorna la persona in posizione index nell'ArrayList
     */
    public Persona getPersona(int index) {
        return persone.get(index);
    }

    /**
     * Metodo per ritornare uno specifico codice invalido nell'ArrayList
     * @param index indice di posizione nell'ArrayList
     * @return Ritorna il codice invalido in posizione index nell'ArrayList
     */
    public String getCodiceInvalido(int index) {
        return codiciInvalidi.get(index);
    }

    /**
     * Metodo per ritornare uno specifico codice spaiato nell'ArrayList
     * @param index indice di posizione nell'ArrayList
     * @return Ritorna il codice spaiato in posizione index nell'ArrayList
     */
    public String getCodiceSpaiato(int index) {
        return codiciSpaiati.get(index);
    }

    /**
     * Metodo per inserire nell'ArrayList di persone ogni persona contenuta nel file "inputPersone.xml"
     * <p>Nel ciclo vengono salvati in un array di String i dati di una persona, fino a quando non si raggiunge il TAG di chiusura "persona"
     * <p>Quando si raggiunge tale TAG viene costruito un nuovo oggetto Persona e aggiunto nell'ArrayList
     * <p>Nel costruttore di ogni nuova Persona vengono inseriti i dati dell'Array datiPersona
     * @see Persona#Persona(String, String, String, String, String, String, String)
     *
     * @throws XMLStreamException Se dovesse esserci un errore nella lettura del file
     */
    public void setArrayPersone() throws XMLStreamException {
        //INIZIALIZZAZIONE PER LETTURA DEL FILE inputPersone.xml
        XMLInputFactory personeif = null;
        XMLStreamReader personer = null;
        try {
            personeif = XMLInputFactory.newInstance();
            personer = personeif.createXMLStreamReader("inputPersone.xml", new FileInputStream("inputPersone.xml"));
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }

        int countDati = 0;
        boolean nextPerson = false;
        int countPersone = 0;
        String[] datiPersona = new String[5];

        /*
         * LETTURA DEL FILE INPUTPERSONEA
         * GGIUNTA DELLE PERSONE NELL'ARRAYLIST
         */
        while (personer.hasNext()) {
            switch (personer.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    /*
                     * QUANDO SI CHIUDE UN TAG PERSONA
                     * NELL'ITERAZIONE SUCESSIVA SI AGGIUNGE UN NUOVO OGGETTO ALL'ARRAYLIST DI PERSONE
                     * IL CARATTERE SUCCESSIVO AL NUOVO TAG PERSONA E' DI SOLI SPAZI
                     */
                    if (personer.getLocalName().equals("persona")) {
                        nextPerson = true;
                    }
                    break;
                case XMLStreamConstants.COMMENT:
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (nextPerson) {
                        /*
                         * AGGIUNTA DELLA NUOVA Persona NELL'ARRAYLIST
                         * I DATI SONO CONTENUTI NELL'ARRAY datiPersona
                         * LA DATA DI NASCITA VIENE SEPARATA CON IL METODO split(), VIENE DIVISA AL CARATTERE "-"
                         */
                        persone.add(new Persona(datiPersona[0], datiPersona[1], datiPersona[2], datiPersona[3], datiPersona[4].split("-")[0], datiPersona[4].split("-")[1], datiPersona[4].split("-")[2]));
                        //CREAZIONE DEL CODICE FISCALE DELLA PERSONA APPENA AGGIUNTA
                        persone.get(countPersone).setCodiceFiscale();
                        countPersone++;
                        countDati = 0;
                        //VARIABILE nextPerson TORNA FALSA PER SALVARE I DATI DELLA PROSSIMA PERSONA
                        nextPerson = false;
                    } else if (personer.getText().trim().length() > 0) {
                        /*
                         * SE nextPerson E' FALSO SI SALVANO I DATI
                         * SE IL TESTO NON E' COMPOSTO DA CARATTERI NON VIENE SALVATO
                         * VENGONO SALVATI I DATI DI OGNI PERSONA IN UN ARRAY DI STRING
                         */
                        datiPersona[countDati] = personer.getText();
                        countDati++;
                    }
                    break;
            }
            personer.next();
        }
    }

    /**
     * Metodo per aggiungere negli ArrayList "codiciInvalidi" e "codiciSpaiati" i codici
     * <p>I codici vengono suddivisi in base al tipo di validità che possiedono
     *
     * @see InputPersone#codiciInvalidi
     * @see InputPersone#codiciSpaiati
     * @see Persona#setValiditaCodice()
     * @see ValiditaCodici
     */
    public void divisioneCodiciErrati() {
        for (Persona persona : persone) {
            switch (persona.getValidita()) {
                case VALIDO:
                    break;
                case INVALIDO:
                    codiciInvalidi.add(persona.getCodiceFiscale().toString());
                    break;
                case SPAIATO:
                    codiciSpaiati.add(persona.getCodiceFiscale().toString());
                    break;
                default:
                    break;
            }
        }
    }


}
