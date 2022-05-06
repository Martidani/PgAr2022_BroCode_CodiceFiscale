package it.unibs.fp.codiceFiscale;

public class MainTestCodice {
    public static void main(String[] args) {
        Persona p = new Persona("STEFANO", "PALUMBO", "M", "CASERTA", "2018", "04", "30");
        p.setCodiceFiscale();
        System.out.println(p.getCodiceFiscale());
        System.out.println(p.isACodice());
    }
}
