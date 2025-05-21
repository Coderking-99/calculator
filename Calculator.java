import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel; // For better layout management
import java.awt.BorderLayout; // For main frame layout
import java.awt.GridLayout; // For button layout
import java.awt.Font; // For text field font
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {

	private JTextField displayField;
	private double num1 = 0;
	private String operator = "";
	private boolean newNumber = true; // Flag to indicate if a new number is being entered

	public Calculator() {
		// --- JFrame Setup ---
		setTitle("Calculator");
		setSize(300, 450); // Adjusted size for better appearance
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout()); // Use BorderLayout for overall frame

		// --- Display Field ---
		displayField = new JTextField("0");
		displayField.setEditable(false); // User can't type directly
		displayField.setHorizontalAlignment(JTextField.RIGHT); // Align text to the right
		displayField.setFont(new Font("Arial", Font.BOLD, 30)); // Larger font for readability
		add(displayField, BorderLayout.NORTH); // Place at the top of the frame

		// --- Buttons Panel ---
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(6, 4, 10, 10)); // 6 rows, 4 columns, with gaps

		// Define button labels in order for the grid
		String[] buttonLabels = {
				"C", "DEL", "%", "/", // Row 1
				"7", "8", "9", "*", // Row 2
				"4", "5", "6", "-", // Row 3
				"1", "2", "3", "+", // Row 4
				"00", "0", ".", "=" // Row 5 (adjusting for the provided buttons)
		};

		// Create and add buttons to the panel
		for (String label : buttonLabels) {
			JButton button = new JButton(label);
			button.setFont(new Font("Arial", Font.PLAIN, 20)); // Font for buttons
			button.addActionListener(this); // Add action listener to each button
			buttonPanel.add(button);
		}

		add(buttonPanel, BorderLayout.CENTER); // Place button panel in the center

		setVisible(true); // Make the frame visible
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand(); // Get the text of the clicked button

		if (command.matches("[0-9]")) { // If a number button is pressed
			if (newNumber) {
				displayField.setText(command);
				newNumber = false;
			} else {
				if (displayField.getText().equals("0") && command.equals("0")) {
					// Do nothing if current display is "0" and "0" is pressed
				} else if (displayField.getText().equals("0") && !command.equals("0")) {
					displayField.setText(command); // Replace "0" with the new digit
				} else {
					displayField.setText(displayField.getText() + command);
				}
			}
		} else if (command.equals(".")) { // If decimal point is pressed
			if (newNumber) {
				displayField.setText("0.");
				newNumber = false;
			} else if (!displayField.getText().contains(".")) {
				displayField.setText(displayField.getText() + ".");
			}
		} else if (command.equals("00")) { // Handle "00" button
			if (newNumber) {
				displayField.setText("0");
			} else if (!displayField.getText().equals("0")) {
				displayField.setText(displayField.getText() + "00");
			}
		} else if (command.matches("[+\\-*/%]")) { // If an operator button is pressed
			num1 = Double.parseDouble(displayField.getText());
			operator = command;
			newNumber = true; // Ready for the next number
		} else if (command.equals("=")) { // If equals button is pressed
			if (!operator.isEmpty()) {
				double num2 = Double.parseDouble(displayField.getText());
				double result = 0;

				switch (operator) {
					case "+":
						result = num1 + num2;
						break;
					case "-":
						result = num1 - num2;
						break;
					case "*":
						result = num1 * num2;
						break;
					case "/":
						if (num2 != 0) {
							result = num1 / num2;
						} else {
							displayField.setText("Error: Div by 0");
							return; // Exit to prevent further calculations
						}
						break;
					case "%":
						result = num1 % num2;
						break;
				}
				// Display result, handle whole numbers to avoid ".0"
				if (result == (long) result) {
					displayField.setText(String.format("%d", (long) result));
				} else {
					displayField.setText(String.format("%.4f", result)); // Format to 4 decimal places
				}
				num1 = result; // Set result as the first number for chaining operations
				operator = ""; // Clear operator
				newNumber = true; // Ready for a new calculation
			}
		} else if (command.equals("C")) { // Clear button
			displayField.setText("0");
			num1 = 0;
			operator = "";
			newNumber = true;
		} else if (command.equals("DEL")) { // Delete (backspace) button
			String currentText = displayField.getText();
			if (currentText.length() > 1) {
				displayField.setText(currentText.substring(0, currentText.length() - 1));
			} else {
				displayField.setText("0");
				newNumber = true;
			}
		}
	}

	public static void main(String[] args) {
		// Create the calculator GUI on the Event Dispatch Thread (EDT)
		// This is important for Swing applications for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Calculator();
			}
		});
	}
}