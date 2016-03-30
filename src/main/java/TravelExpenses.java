
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TravelExpenses extends JFrame {
    private static final long serialVersionUID = 0L;

    private static final BigDecimal
        MEALS = new BigDecimal ("37.00"),
        COST_PER_MILE = new BigDecimal ("0.27"),
        PARKING_MAX = new BigDecimal ("10.00"),
        TAXI_MAX = new BigDecimal ("20.00"),
        LODGING_MAX = new BigDecimal ("95.00");

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

        @SuppressWarnings("unchecked")
        public <T extends Number> T getValue (Class <T> type)
            throws UserCancelException {
            while (true) {
                try {
                    T value;
                    if (type == Integer.class) {
                        value = (T) new Integer (field.getText ());
                    } else if (type == BigDecimal.class) {
                        value = (T) new BigDecimal (field.getText ());
                    } else {
                        throw (new RuntimeException
                               ("Don't know how to convert to " + type));
                    }
                    if (value.intValue () >= 0) {
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

        JButton resetButton = new JButton ("Reset");
        resetButton.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    for (LabeledTextField field : fields) {
                        field.reset ();
                    }
                }
            });
        buttonPanel.add (resetButton);
        JButton calculateButton = new JButton ("Calculate");
        calculateButton.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    for (LabeledTextField field : fields) {
                        field.setEditable (false);
                    }
                    try {
                        JOptionPane.showMessageDialog (null, calculate ());
                    } catch (UserCancelException cancel) {
                        return;
                    } finally {
                        for (LabeledTextField field : fields) {
                            field.setEditable (true);
                        }
                    }
                }
            });
        buttonPanel.add (calculateButton);

        pack ();
    }

    private String calculate() throws UserCancelException {
        int days = daysField.getValue (Integer.class);
        BigDecimal daysBD = new BigDecimal (days);
        BigDecimal
            airfare = airfareField.getValue (BigDecimal.class),
            rental = rentalField.getValue (BigDecimal.class),
            miles = milesField.getValue (BigDecimal.class),
            parking = parkingField.getValue (BigDecimal.class),
            taxi = taxiField.getValue (BigDecimal.class),
            registration = registrationField.getValue (BigDecimal.class),
            lodging = lodgingField.getValue (BigDecimal.class);

        BigDecimal total =
            MEALS.multiply (daysBD)
            .add (airfare)
            .add (rental)
            .add (COST_PER_MILE.multiply (miles))
            .add (parking)
            .add (taxi)
            .add (registration)
            .add (lodging.multiply (daysBD));

        BigDecimal allowable =
            MEALS.multiply (daysBD)
            .add (airfare)
            .add (rental)
            .add (rental.compareTo (BigDecimal.ZERO) > 0
                  ? BigDecimal.ZERO
                  : COST_PER_MILE.multiply (miles))
            .add (parking.compareTo (BigDecimal.ZERO) > 0
                  ? PARKING_MAX.multiply (daysBD)
                  : BigDecimal.ZERO)
            .add (taxi.compareTo (BigDecimal.ZERO) > 0
                  ? TAXI_MAX.multiply (daysBD)
                  : BigDecimal.ZERO)
            .add (registration)
            .add (lodging.compareTo (BigDecimal.ZERO) > 0
                  ? LODGING_MAX.multiply (daysBD)
                  : BigDecimal.ZERO);

        BigDecimal difference = total.subtract (allowable);

        NumberFormat currency = NumberFormat.getCurrencyInstance ();

        return (String.format ("Total Expenses : %s%n" +
                               "Allowable Expenses : %s%n" +
                               "%s : %s",
                               currency.format (total),
                               currency.format (allowable),
                               difference.compareTo (BigDecimal.ZERO) > 0
                               ? "Amount to be paid back"
                               : "Amount saved",
                               currency.format (difference.abs ())));
    }
}
