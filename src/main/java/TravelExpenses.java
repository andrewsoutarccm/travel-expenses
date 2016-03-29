
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TravelExpenses extends JFrame {
    private final String [] LABELS = {
	"Number of days on the trip",
	"Amount of airfare",
	"Amount of car rental",
	"Miles driven",
	"Parking fees",
	"Taxi fees",
	"Conference registration",
	"Lodging charges per night",
    };
    private final HashMap <String, JTextField> fields = new HashMap <> ();
    private final Container contentPane;

    TravelExpenses () {
	setTitle ("Travel Expenses Calculator");
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	contentPane = getContentPane ();
	contentPane.setLayout (new BorderLayout ());

	JPanel fieldPanel = new JPanel ();
	contentPane.add (fieldPanel, BorderLayout.CENTER);
	fieldPanel.setLayout(new GridLayout(LABELS.length, 2));

	for (String label : LABELS) {
	    fieldPanel.add (new JLabel (label + ":"));
	    JTextField field = new JTextField ("0");
	    fieldPanel.add (field);
	    fields.put (label, field);
	    // fieldPanel.addActionListener (fieldActionListener);
	}

	JPanel buttonPanel = new JPanel ();
	contentPane.add (buttonPanel, BorderLayout.SOUTH);
	buttonPanel.setLayout (new FlowLayout ());

	JButton reset = new JButton ("Reset");
	reset.addActionListener (new ActionListener () {
		public void actionPerformed (ActionEvent e) {
		    for (JTextField field : fields.values ()) {
			field.setText ("0");
		    }
		}
	    });
	buttonPanel.add (reset);
	JButton calculate = new JButton ("Calculate");
	calculate.addActionListener (new ActionListener () {
		public void actionPerformed (ActionEvent e) {
		    /* Do the calculation */
		    JOptionPane.showMessageDialog (null, "I didn't do this yet");
		}
	    });
	buttonPanel.add (calculate);

	pack ();
    }
}
