
package hr.infomare.joppd;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;

import java.awt.Robot;

import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author damirl
 */
public class Pregled extends javax.swing.JFrame {

    private String nazivDatoteke;
    private List<SAXParseException> greske;
    private boolean xmlIspravan;

    /** Creates new form Pregled */
    public Pregled() {
        initComponents();
        inicijalizacija();
    }


    private void inicijalizacija() {
       jEditorPane1.setEditorKitForContentType("text/xml", new XmlEditorKit());
       jEditorPane1.setContentType("text/xml");
        dragAndDrop();
    }
    // Podrška sa Drag and drop
    private void dragAndDrop() {
        TitledBorder border = new TitledBorder("Ispusti...");
        border.setTitleFont(new Font("Tahoma", 1, 14));    
        border.setTitleColor(new Color(0, 174, 24));
        new FileDrop(System.out, jEditorPane1, border, new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                //Uvijek samo jedna datoteka
                for (int i = 0; i < 1; i++) {
                    try {
                        nazivDatoteke = files[i].getCanonicalPath();                        
                    } catch (java.io.IOException e) {
                        Pomocna.greskaBox(e.getMessage());
                    }
                }
                resetCombo();
                validacijaXMLDatoteke(nazivDatoteke);
                ucitajXMLDatotekuUEditor();
                postaviTitle();
                if (!xmlIspravan) {
                    napuniCombo();
                    Pomocna.greskaBox("XML datoteka nije ispravna !", jScrollPane1);
                    staniNaRedGreske();
                } else {
                    Pomocna.infoBox("XML datoteka je ispravna!", jScrollPane1);
                }
            }
        });
    }

    private void postaviTitle(){
        this.setTitle("JOPPD validator, datoteka: "+ nazivDatoteke.trim());
    }
    private void validacijaXMLDatoteke(String xml) {
        //String xsd = "C:\\JOPPD\\ObrazacJOPPD-v1-0.xsd";
        String xsd = "xsd/ObrazacJOPPD-v1-0.xsd";
        greske = new LinkedList<SAXParseException>();

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsd));
            Validator validator = schema.newValidator();

            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    greske.add(exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    greske.add(exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    greske.add(exception);
                }
            });
            validator.validate(new StreamSource(new File(xml)));
            for (SAXParseException sAXParseException : greske) {
            }
            xmlIspravan = greske.size() == 0;
        } catch (IOException | SAXException e) {
            Pomocna.greskaBox(e.getMessage());
        }
    }

    private void ucitajXMLDatotekuUEditor() {
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(nazivDatoteke.trim()), "UTF-8");
            jEditorPane1.read(is, null);
        } catch (FileNotFoundException e) {
            Pomocna.greskaBox(e.getMessage());
        } catch (IOException e) {
            Pomocna.greskaBox(e.getMessage());
        }
    }

    private void napuniCombo() {
        for (SAXParseException sAXParseException : greske) {
            jComboBox1.addItem(sAXParseException.getLineNumber() + " " + sAXParseException.getColumnNumber() + " " +
                               sAXParseException.getMessage());

        }
        jComboBox1.setEnabled(true);
    }

    private void resetCombo() {
        jComboBox1.removeAllItems();
        jComboBox1.setEnabled(false);
    }

    private void staniNaRedGreske() {
        try {
            Robot robot = new Robot();
            String odabrano = jComboBox1.getSelectedItem().toString();
            Integer red = Integer.parseInt(odabrano.substring(0, odabrano.indexOf(" ")));
            jEditorPane1.setCaretPosition(1);
            jEditorPane1.requestFocus();
            jEditorPane1.requestFocus();
            for (int i = 1; i < red; i++) {
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
            }
        } catch (AWTException e) {
            Pomocna.greskaBox(e.getMessage(), jScrollPane1);
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JOPPD validator");
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Greške"));
        jPanel1.setPreferredSize(new java.awt.Dimension(876, 50));

        jComboBox1.setEnabled(false);
        jComboBox1.setMinimumSize(new java.awt.Dimension(848, 21));
        jComboBox1.setName(""); // NOI18N
        jComboBox1.setPreferredSize(new java.awt.Dimension(848, 21));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, 0, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dovuæi i ispustiti datoteku ovdje", 0, 0, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(49, 106, 196))); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jEditorPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
      
        if (jComboBox1.getItemCount() > 0) {
            staniNaRedGreske();      
        }        
       
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Pomocna.greskaBox(ex.getMessage());
        } catch (InstantiationException ex) {
            Pomocna.greskaBox(ex.getMessage());
        } catch (IllegalAccessException ex) {
            Pomocna.greskaBox(ex.getMessage());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            Pomocna.greskaBox(ex.getMessage());
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pregled().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
