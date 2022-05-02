package it.unibs.fp.codiceFiscale;

public class MainTestPersona {
    public static void main(String[] args) {
        Persona p = new Persona("GIUSEPPE", "MUSSO", "M", "ALCARA LI FUSI", "1940", "04", "27");
        p.setCodiceFiscale();

        System.out.println(p.getCodiceFiscale());
        System.out.println(p.getValidita());
    }
}
