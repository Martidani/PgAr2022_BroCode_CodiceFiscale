package it.unibs.fp.codiceFiscale;

import javax.xml.stream.*;

/**
 * Classe persona per salvare i dati di una persona e generare il codice fiscale
 */
public class Persona {
    private static final int MAX_LETTERE = 3;

    private String nome;
    private String cognome;
    private String sesso;
    private String comuneNascita;
    private String aaDataNascita;
    private String mmDataNascita;
    private String ggDataNascita;
    private ValiditaCodici validita;
    private StringBuffer codiceFiscale = new StringBuffer();

    /**
     * Costruttore della classe persona
     *
     * @param nome nome della persona
     * @param cognome cognome della persona
     * @param sesso sesso della persona
     * @param comuneNascita comune di nascita della persona
     * @param aaDataNascita anno di nascita della persona
     * @param mmDataNascita mese di nascita della persona
     * @param ggDataNascita giorno di nascita della persona
     */
    public Persona(String nome, String cognome, String sesso, String comuneNascita, String aaDataNascita, String mmDataNascita, String ggDataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.comuneNascita = comuneNascita;
        this.aaDataNascita = aaDataNascita;
        this.mmDataNascita = mmDataNascita;
        this.ggDataNascita = ggDataNascita;
    }

    /**
     * Getter di nome
     * @return Ritorna il nome della persona
     */
    public String getNome() {
        return nome;
    }

    /**
     * Getter di cognome
     * @return Ritorna il cognome della persona
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Getter di sesso
     * @return Ritorna il sesso della persona
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Getter di comune di nascita
     * @return Ritorna il comune di nascita della persona
     */
    public String getComuneNascita() {
        return comuneNascita;
    }

    /**
     * Getter di anno di nascita
     * @return Ritorna l'anno di nascita della persona
     */
    public String getAaDataNascita() {
        return aaDataNascita;
    }

    /**
     * Getter di mese di nascita
     * @return Ritorna il mese di nascita della persona
     */
    public String getMmDataNascita() {
        return mmDataNascita;
    }

    /**
     * Getter di giorno di nascita
     * @return Ritorna il giorno di nascita della persona
     */
    public String getGgDataNascita() {
        return ggDataNascita;
    }

    /**
     * Getter di codice fiscale
     * @return Ritorna il codice fiscale della persona
     */
    public StringBuffer getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Getter di validita'
     *
     * @see ValiditaCodici
     * @return Ritorna la validita' della persona
     */
    public ValiditaCodici getValidita() {
        return validita;
    }

    /**
     * Metodo per generare il codice fiscale della persona
     * <p>Vengono chiamati vari metodi per generare le varie parti del codice fiscale
     * @see Persona#lettereNomeCognome(String)
     * @see Persona#letteraMese(String)
     * @see Persona#giornoNascita(String, String)
     * @see Persona#codiceComune()
     * @see Persona#carattereControllo(String)
     * @see Persona#setValiditaCodice()
     */
    public void setCodiceFiscale() {
        codiceFiscale.append(lettereNomeCognome(cognome));
        codiceFiscale.append(lettereNome(nome));
        codiceFiscale.append(aaDataNascita.substring(2));
        codiceFiscale.append(letteraMese(mmDataNascita));
        codiceFiscale.append(String.format("%02d", giornoNascita(sesso, ggDataNascita)));
        try {
            codiceFiscale.append(codiceComune());
        } catch (XMLStreamException e) {
            e.printStackTrace();
            codiceFiscale.append("****");
        }
        codiceFiscale.append(carattereControllo(codiceFiscale.toString()));

        try {
            validita = setValiditaCodice();
        } catch (XMLStreamException e) {
            e.printStackTrace();
            validita = ValiditaCodici.INVALIDO;
        }
    }

    /**
     * Metodo per restituire le 3 lettere del nome e cognome
     * <p>Se il nome o cognome ha meno di 3 lettere vengono aggiunte n X in base alle lettere mancanti
     * @see Persona#ordinaConsonantiEVocali(String)
     *
     * @param s nome o cognome della persona
     * @return Ritorna le 3 lettere del nome o cognome passato
     */
    public StringBuffer lettereNomeCognome(String s) {
        StringBuffer lettereOutput = new StringBuffer();
        if(s.length() < MAX_LETTERE){
            lettereOutput.append(ordinaConsonantiEVocali(s));
            lettereOutput.append("X".repeat(MAX_LETTERE - s.length()));
        }
        else
            lettereOutput.append(ordinaConsonantiEVocali(s));

        return lettereOutput;
    }

    /**
     * Metodo per generare le 3 lettere del nome e cognome
     * <p>Prima vengono selezionate solo le consonanti
     * <p>Se il numero di consonanti e' minore di 3 si selezionano le vocali
     * @see Persona#isVowel(char)
     *
     * @param s nome o cognome della persona
     * @return Ritorna le lettere del nome o cognome passato, massimo 3
     */
    public StringBuffer ordinaConsonantiEVocali(String s) {
        StringBuffer lettereOutput = new StringBuffer();
        int numConsonanti = 0;

        for (int i = 0; i < s.length() && lettereOutput.length() < MAX_LETTERE; i++) {
            if (!isVowel(s.charAt(i))) {
                lettereOutput.append(s.charAt(i));
                numConsonanti++;
            }
        }

        if (numConsonanti < MAX_LETTERE) {
            for (int i = 0; i < s.length() && lettereOutput.length() < MAX_LETTERE; i++)
                if (isVowel(s.charAt(i)))
                    lettereOutput.append(s.charAt(i));
        }

        return lettereOutput;
    }


    /**
     * Metodo per generare le 3 lettere del nome
     * <p>Prima vengono selezionate contate le consonanti
     * <p>Se il numero di consonanti e' minore o uguale a 3 si usa lo stesso metodo del cognome
     * <p>Se il numero di consonanti e' maggiore di 3 si selezionano la prima, la terza e la quarta consonante del nome
     * @see Persona#isVowel(char)
     * @see Persona#lettereNomeCognome(String)
     * @see Persona#isVowel(char)
     *
     * @param s nome o cognome della persona
     * @return Ritorna le lettere del nome o cognome passato, massimo 3
     */
    public StringBuffer lettereNome(String s) {
        StringBuffer lettereOutput = new StringBuffer();
        int numConsonanti = 0;

        for(int i = 0; i < s.length(); i++) {
            if(!isVowel(s.charAt(i)))
                numConsonanti++;
        }
        if(numConsonanti < 4)
            lettereOutput = lettereNomeCognome(s);
        else {
            int posConsonante = 1;
            for (int i = 0; i < s.length() && lettereOutput.length() < MAX_LETTERE; i++) {
                if (!isVowel(s.charAt(i))) {
                    if(posConsonante != 2)
                        lettereOutput.append(s.charAt(i));
                    posConsonante++;
                }
            }
        }

        return lettereOutput;
    }

    /**
     * Metodo per controllare se una lettera sia una vocale o no
     *
     * @param c lettere da considerare
     * @return Ritorna vero se il carattere e' vocale, falso altrimenti
     */
    public boolean isVowel(char c) {
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    /**
     * Metodo per generare la lettera corrispondente al mese di nascita
     *
     * @param mm numero del mese di nascita
     * @return Ritorna lettera corrispondente al mese di nascita
     */
    public String letteraMese(String mm) {
        return switch (mm) {
            case "01" -> "A";
            case "02" -> "B";
            case "03" -> "C";
            case "04" -> "D";
            case "05" -> "E";
            case "06" -> "H";
            case "07" -> "L";
            case "08" -> "M";
            case "09" -> "P";
            case "10" -> "R";
            case "11" -> "S";
            case "12" -> "T";
            default -> "*";
        };
    }

    /**
     * Metodo per restituire il giorno di nascita
     * <p>Se il sesso e' maschio ritorna lo stesso giorno
     * <p>Se il sesso e' femmina ritorna il giorno + 40
     *
     * @param sesso sesso della persona, serve per calcolare il giorno da restituire
     * @param ggDataNascita giorno di nascita della persona
     * @return giorno di nascita
     */
    public int giornoNascita(String sesso, String ggDataNascita) {
        if(sesso.equals("M"))
            return Integer.parseInt(ggDataNascita);
        else
            return Integer.parseInt(ggDataNascita) + 40;
    }

    /**
     * Metodo per restituire il carattere di controllo
     * @see it.unibs.fp.codiceFiscale.Persona#calcolaCarattereControllo(String)
     *
     * @param codiceCalcolo String con i primi 15 caratteri del codice fiscale, necessari per il calcolo del carattere di controllo
     * @return Ritorna il carattere di controllo finale
     */
    public String carattereControllo(String codiceCalcolo) {
        return switch (calcolaCarattereControllo(codiceCalcolo)) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            case 4 -> "E";
            case 5 -> "F";
            case 6 -> "G";
            case 7 -> "H";
            case 8 -> "I";
            case 9 -> "J";
            case 10 -> "K";
            case 11 -> "L";
            case 12 -> "M";
            case 13 -> "N";
            case 14 -> "O";
            case 15 -> "P";
            case 16 -> "Q";
            case 17 -> "R";
            case 18 -> "S";
            case 19 -> "T";
            case 20 -> "U";
            case 21 -> "V";
            case 22 -> "W";
            case 23 -> "X";
            case 24 -> "Y";
            case 25 -> "Z";
            default -> "*";
        };
    }

    /**
     * Metodo per sommare il valore dei caratteri
     * <p>I caratteri in posizione dispari hanno un valore diverso rispetto ai caratteri in posizione pari
     * <p>Nel ciclo vengono fatte scorrere le 15 lettere della
     * <p>In base alla posizione pari o dispari si restituisce il numero corrispondente al carattere di controllo.
     * I valori restituiti vengono sommati
     * <p>Si calcola il resto della divisione tra somma dei valori e 26
     * @see it.unibs.fp.codiceFiscale.Persona#caratteriDispari(char)
     * @see it.unibs.fp.codiceFiscale.Persona#caratteriPari(char)
     *
     * @param codiceCalcolo primi 15 caratteri del codice fiscale
     * @return Ritorna il resto della divisione tra la somma dei valori e 26
     */
    public int calcolaCarattereControllo(String codiceCalcolo) {
        int sommaCaratteri = 0;
        for(int i = 0; i < codiceCalcolo.length(); i++) {
            if((i + 1) % 2 != 0)
                sommaCaratteri += caratteriDispari(codiceCalcolo.charAt(i));
            else
                sommaCaratteri += caratteriPari(codiceCalcolo.charAt(i));
        }
        return sommaCaratteri % 26;
    }

    /**
     * Metodo per restituire il valore corrispondente al carattere in posizione dispari
     *
     * @param c carattere corrispondente al valore
     * @return Ritorna il valore del carattere
     */
    public int caratteriDispari(char c) {
        return switch (c) {
            case '0' -> 1;
            case '1' -> 0;
            case '2' -> 5;
            case '3' -> 7;
            case '4' -> 9;
            case '5' -> 13;
            case '6' -> 15;
            case '7' -> 17;
            case '8' -> 19;
            case '9' -> 21;
            case 'A' -> 1;
            case 'B' -> 0;
            case 'C' -> 5;
            case 'D' -> 7;
            case 'E' -> 9;
            case 'F' -> 13;
            case 'G' -> 15;
            case 'H' -> 17;
            case 'I' -> 19;
            case 'J' -> 21;
            case 'K' -> 2;
            case 'L' -> 4;
            case 'M' -> 18;
            case 'N' -> 20;
            case 'O' -> 11;
            case 'P' -> 3;
            case 'Q' -> 6;
            case 'R' -> 8;
            case 'S' -> 12;
            case 'T' -> 14;
            case 'U' -> 16;
            case 'V' -> 10;
            case 'W' -> 22;
            case 'X' -> 25;
            case 'Y' -> 24;
            case 'Z' -> 23;
            default -> -1;
        };
    }

    /**
     * Metodo per ritornare il valore corrispondente al carattere in posizione pari
     *
     * @param c carattere corrispondente al valore
     * @return Ritorna il valore del carattere
     */
    public int caratteriPari(char c) {
        return switch (c) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            case 'I' -> 8;
            case 'J' -> 9;
            case 'K' -> 10;
            case 'L' -> 11;
            case 'M' -> 12;
            case 'N' -> 13;
            case 'O' -> 14;
            case 'P' -> 15;
            case 'Q' -> 16;
            case 'R' -> 17;
            case 'S' -> 18;
            case 'T' -> 19;
            case 'U' -> 20;
            case 'V' -> 21;
            case 'W' -> 22;
            case 'X' -> 23;
            case 'Y' -> 24;
            case 'Z' -> 25;
            default -> -1;
        };
    }

    /**
     * Metodo per richiamare interfaccia InputComune
     * <p>Serve per ritornare il codice del comune di nascita
     * @see InputComuni#codiceComune(String)
     *
     * @return Ritorna il codice relativo al comune di nascita
     * @throws XMLStreamException Se dovesse esserci un errore nella lettura del file
     */
    public String codiceComune() throws XMLStreamException {
        return InputComuni.codiceComune(comuneNascita);
    }

    /**
     * Metodo per impostare la validita' del codice fiscale generato
     * <p>Se il codice e' sintatticamente o logicamente scorretto si considera invalido
     * <p>Se il codice risulta valido si considera valido, altrimenti spaiato
     *
     * @see Persona#isCodiceValido()
     * @return Ritorna la validita' del codice
     * @throws XMLStreamException Se dovesse esserci un errore nella lettura del file
     */
    public ValiditaCodici setValiditaCodice() throws XMLStreamException {
        if(!isACodice()) {
            return ValiditaCodici.INVALIDO;
        }
        boolean codiceValido = isCodiceValido();

        if(codiceValido)
            return ValiditaCodici.VALIDO;
        else
            return ValiditaCodici.SPAIATO;
    }

    /**
     * Metodo per richiamare interfaccia InputCodici
     * <p>Serve per controllare il codice generato con tutti quelli presenti nel file "codiciFiscali.zml"
     *
     * @see InputCodici#existCodice(StringBuffer)
     * @return ritorna vero se il codice e' valido, falso altrimenti
     * @throws XMLStreamException Se dovesse esserci un errore nella lettura del file
     */
    public boolean isCodiceValido() throws XMLStreamException {
        return InputCodici.existCodice(codiceFiscale);
    }

    /**
     * Metodo per controllare se il codice generato e' un codice fiscale
     * <p>Se non ha 16 caratteri, non e' un codice fiscale
     * <p>Se i primi 6 caratteri non sono lettere, non e' un codice fiscale
     * <p>Se i caratteri numero 7 e 8 non sono numerici, non e' un codice fiscale
     * <p>Se il carattere del mese (9) non corrisponde a nessun mese, non e' un codice fiscale
     * <p>Se il giorno di nascita (cifre 10-11) non sono numeriche e non e' compreso tra 1 e 31 o 41-71, non e' un codice fiscale
     * <p>Se il mese di nascita e' febbraio ("B") e il giorno di nascita e' maggiore di 28, non e' un codice fiscale
     * <p>Se per i mesi di nascita di 30 giorni e il giorno di nascita e' maggiore di 30, non e' un codice fiscale
     * <p>Se il carattere numero 12 non e' un carattere e le cifre numero 13-14-15 non sono numeriche, non e' un codice fiscale
     * <p>Se il carattere di controllo generato e' scorretto, non e' un codice fiscale
     *
     * @return vero se e' un codice fiscale corretto, falso se si verifica una delle situazioni controllate
     */
    public boolean isACodice() {
        if(codiceFiscale.length() != 16)
            return false;
        //COGNOME SOLO CARATTERI
        if(!codiceFiscale.substring(0,3).matches("[A-Z]+"))
            return false;
        //NOME SOLO CARATTERI
        if(!codiceFiscale.substring(3,6).matches("[A-Z]+"))
            return false;
        //ANNO SOLO CIFRE
        if(!codiceFiscale.substring(6,8).matches("[0-9]+"))
            return false;
        //MESE SOLO LETTERE DEI MESI
        if(!codiceFiscale.substring(8,9).matches("[ABCDEHLMPRST]+"))
            return false;
        //GIORNI SOLO CIFRE ACCETABBILI
        if(!codiceFiscale.substring(9,11).matches("[0-9]+"))
            return false;
        else {
            int gg = Integer.parseInt(codiceFiscale.substring(9, 11));
            if (gg < 1 || (gg > 31 && gg < 41) || gg > 71)
                return false;
        }
        //GIORNI DI FEBBRAIO MAX 28
        if(codiceFiscale.substring(8,9).equals("B")) {
            int gg = Integer.parseInt(codiceFiscale.substring(9, 11));
            if ((gg > 28 && gg < 41) || gg > 68)
                return false;
        }
        //MESI DA 30 GIORNI CON MAX 30 GIORNO DI NASCITA
        if(codiceFiscale.substring(8,9).matches("[DHPS]+")) {
            int gg = Integer.parseInt(codiceFiscale.substring(9, 11));
            if ((gg > 30 && gg < 41) || gg > 70)
                return false;
        }
        //CODICE PAESE 1 CARATTERE + 3 CIFRE
        if(!codiceFiscale.substring(11,12).matches("[A-Z]+") && !codiceFiscale.substring(12,15).matches("[0-9]+"))
            return false;
        //CALCOLO CORRETTEZZA CARATTERE DI CONTROLLO
        if(!codiceFiscale.substring(15).equals(carattereControllo(codiceFiscale.substring(0,15))))
            return false;

        return true;
    }


}
