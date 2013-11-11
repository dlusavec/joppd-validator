package hr.infomare.joppd;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Pomocna {
        
    public Pomocna() {
        super();
    }

    public static void infoBox(String poruka) {
        JOptionPane.showMessageDialog(null, poruka, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void infoBox(String poruka, Component c) {
        JOptionPane.showMessageDialog(c, poruka, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void greskaBox(String poruka) {
        JOptionPane.showMessageDialog(null, poruka, "Greška", JOptionPane.ERROR_MESSAGE);
    }
    public static void greskaBox(String poruka,Component c) {
        JOptionPane.showMessageDialog(c, poruka, "Greška", JOptionPane.ERROR_MESSAGE);
    }

    public static void upozorenjeBox(String poruka) {
        JOptionPane.showMessageDialog(null, poruka, "Upozorenje", JOptionPane.WARNING_MESSAGE);
    }   
}
