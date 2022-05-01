package it.unibs.fp.codiceFiscale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;

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

    public Persona(String nome, String cognome, String sesso, String comuneNascita, String aaDataNascita, String mmDataNascita, String ggDataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.sesso = sesso;
        this.comuneNascita = comuneNascita;
        this.aaDataNascita = aaDataNascita;
        this.mmDataNascita = mmDataNascita;
        this.ggDataNascita = ggDataNascita;
    }

    public Persona() {

    }

    //// GETTER \\\\
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getSesso() {
        return sesso;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public String getAaDataNascita() {
        return aaDataNascita;
    }

    public String getMmDataNascita() {
        return mmDataNascita;
    }

    public String getGgDataNascita() {
        return ggDataNascita;
    }

    public StringBuffer getCodiceFiscale() {
        return codiceFiscale;
    }

    public ValiditaCodici getValidita() {
        return validita;
    }

    //// SETTER \\\\
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public void setComuneNascita(String comuneNascita) {
        this.comuneNascita = comuneNascita;
    }

    public void setCodiceFiscale() {
        codiceFiscale.append(lettereNomeCognome(cognome));
        codiceFiscale.append(lettereNomeCognome(nome));
        codiceFiscale.append(aaDataNascita.substring(2));
        codiceFiscale.append(letteraMese(mmDataNascita));
        codiceFiscale.append(String.format("%02d", giornoNascita(sesso, ggDataNascita)));
        try {
            codiceFiscale.append(codiceComune());
        } catch (XMLStreamException e) {
            e.printStackTrace();
            codiceFiscale.append("A000");
        }
        codiceFiscale.append(carattereControllo(codiceFiscale.toString()));

        try {
            validita = setValiditaCodice();
        } catch (XMLStreamException e) {
            e.printStackTrace();
            validita = ValiditaCodici.INVALIDO;
        }
    }

    public StringBuffer lettereNomeCognome(String s) {
        StringBuffer lettereOutput = new StringBuffer();
        if(s.length() < MAX_LETTERE){
            lettereOutput.append(s);
            for(int i = s.length(); i < MAX_LETTERE; i++)
                lettereOutput.append("X");
        }
        else {
            int numConsonanti = 0;
            for(int i = 0; i < s.length() && lettereOutput.length() < MAX_LETTERE; i++) {
                if (!isVowel(s.charAt(i))) {
                    lettereOutput.append(s.charAt(i));
                    numConsonanti++;
                }
            }
            if(numConsonanti < MAX_LETTERE) {
                for(int i = 0; i < s.length() && lettereOutput.length() < MAX_LETTERE; i++)
                    if (isVowel(s.charAt(i)))
                        lettereOutput.append(s.charAt(i));
            }
        }

        return lettereOutput;
    }

    public boolean isVowel(char c) {
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            return true;
        }
        return false;
    }

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

    public int giornoNascita(String sesso, String ggDataNascita) {
        if(sesso.equals("M"))
            return Integer.parseInt(ggDataNascita);
        else
            return Integer.parseInt(ggDataNascita) + 40;
    }

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

    public String codiceComune() throws XMLStreamException {
        return InputComuni.codiceComune(comuneNascita);
    }

    public ValiditaCodici setValiditaCodice() throws XMLStreamException {

        if(!isACodice()) {
            return ValiditaCodici.INVALIDO;
        }

        boolean codiceValido = InputCodici.existCodice(codiceFiscale);

        if(codiceValido)
            return ValiditaCodici.VALIDO;
        else
            return ValiditaCodici.SPAIATO;
    }

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
        if(!codiceFiscale.substring(8,9).equals("B") && (Integer.parseInt(codiceFiscale.substring(9,11)) > 28 && Integer.parseInt(codiceFiscale.substring(9,11)) < 41) || Integer.parseInt(codiceFiscale.substring(9,11)) > 68)
            return false;
        //MESI DA 30 GIORNI CON MAX 30 GIORNO DI NASCITA
        if(!codiceFiscale.substring(8,9).matches("[DHPS]+") && (Integer.parseInt(codiceFiscale.substring(9,11)) > 30 && Integer.parseInt(codiceFiscale.substring(9,11)) < 41) || Integer.parseInt(codiceFiscale.substring(9,11)) > 70)
            return false;
        //CODICE PAESE 1 CARATTERE + 3 CIFRE
        if(!codiceFiscale.substring(11,12).matches("[A-Z]+") && !codiceFiscale.substring(12,15).matches("[0-9]+"))
            return false;
        //CALCOLO CORRETTEZZA CARATTERE DI CONTROLLO
        if(!codiceFiscale.substring(15).equals(carattereControllo(codiceFiscale.substring(0,15).toString())))
            return false;

        return true;
    }


}
