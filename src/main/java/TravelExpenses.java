
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TravelExpenses extends JFrame {
    private static final long serialVersionUID = 0L;

    private final LinkedList <LabeledTextField> fields = new LinkedList <> ();
    private final JPanel fieldPanel;

    private class UserCancelException extends Exception {
        private static final long serialVersionUID = 0L;
    }

    private class LabeledTextField {
        private final String labelText;
        private final JLabel label;
        private final JTextField field;

        public LabeledTextField (String labelText) {
            this.labelText = labelText + ":";
            label = new JLabel (this.labelText);
            field = new JTextField ();
            reset ();

            fieldPanel.add (label);
            fieldPanel.add (field);

            fields.add (this);
        }

        public void reset () {
            field.setText ("0");
        }

        public void setEditable (boolean editable) {
            field.setEditable (editable);
        }

        public int getValue () throws UserCancelException {
            while (true) {
                try {
                    int value = Integer.parseInt (field.getText ());
                    if (value >= 0) {
                        return (value);
                    }
                } catch (NumberFormatException ignored) {};
                String input = JOptionPane.showInputDialog
                    ("Invalid input.\n" + labelText);
                if (input == null) {
                    throw (new UserCancelException ());
                } else {
                    field.setText (input);
                }
            }
        }
    }

    private final LabeledTextField
        daysField, airfareField, rentalField, milesField, parkingField,
        taxiField, registrationField, lodgingField;

    TravelExpenses () {
        setTitle ("Travel Expenses Calculator");
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        final Container contentPane = getContentPane ();
        contentPane.setLayout (new BorderLayout ());

        fieldPanel = new JPanel ();
        contentPane.add (fieldPanel, BorderLayout.CENTER);
        fieldPanel.setLayout(new GridLayout(0, 2));

        daysField = new LabeledTextField ("Number of days on the trip");
        airfareField = new LabeledTextField ("Amount of airfare");
        rentalField = new LabeledTextField ("Amount of car rental");
        milesField = new LabeledTextField ("Miles driven");
        parkingField = new LabeledTextField ("Parking fees");
        taxiField = new LabeledTextField ("Taxi fees");
        registrationField = new LabeledTextField ("Conference registration");
        lodgingField = new LabeledTextField ("Lodging charges per night");

        JPanel buttonPanel = new JPanel ();
        contentPane.add (buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout (new FlowLayout ());

        JButton reset = new JButton ("Reset");
        reset.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    for (LabeledTextField field : fields) {
                        field.reset ();
                    }
                }
            });
        buttonPanel.add (reset);
        JButton calculate = new JButton ("Calculate");
        calculate.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    for (LabeledTextField field : fields) {
                        field.setEditable (false);
                    }
                    try {
                        int days = daysField.getValue ();
                        JOptionPane.showMessageDialog
                            (null, String.format ("Total Expenses : %s%n" +
                                                  "Allowable Expenses : %s%n" +
                                                  "Amount to be paid back: %s",
                                                  0, 0, 0));
                    } catch (UserCancelException cancel) {
                        return;
                    } finally {
                        for (LabeledTextField field : fields) {
                            field.setEditable (true);
                        }
                    }
                }
            });
        buttonPanel.add (calculate);

        pack ();
    }
}
