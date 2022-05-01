package it.unibs.fp.codiceFiscale;

public class MainTestPersona {
    public static void main(String[] args) {
        Persona p = new Persona("MARIA", "CERRATO", "F", "POLONGHERA", "1939", "03", "04");
        p.setCodiceFiscale();

        System.out.println(p.getCodiceFiscale());
        System.out.println(p.getValidita());
    }
}
