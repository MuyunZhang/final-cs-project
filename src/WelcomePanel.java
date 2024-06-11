import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel extends JPanel implements ActionListener {

    private JTextField textField;
    private JButton submitButton;
    private JButton clearButton;
    private JFrame enclosingFrame;

    public WelcomePanel(JFrame frame) {
        enclosingFrame = frame;
        textField = new JTextField(10);
        submitButton = new JButton("Submit");

        setPreferredSize(new Dimension(400, 400)); // Set preferred panel size

        setLayout(null); // Use null layout for manual component positioning
        add(submitButton);
        add(textField);
        submitButton.addActionListener(this);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.RED);
        g.drawString("Instructions", 50, 30);
        g.drawString("Welcome to the Snake Game!", 10, 50);
        g.drawString("Rules:", 10, 70);
        g.drawString("- The snake is represented by the green block.", 10, 90);
        g.drawString("- The food is represented by the red block.", 10, 110);
        g.drawString("- Blockades that block your vision will appear after time reaches 10.", 10, 130);
        g.drawString("- Click on the blockades to get rid of them.", 10, 150);
        g.drawString("- If you click on the snake or its body, you lose.", 10, 170);
        g.drawString("- If the snake's head touches its body, you lose.", 10, 190);
        g.drawString("- If the snake touches the window, you lose.", 10, 210);

        // Adjust the location of buttons
        submitButton.setBounds(50, 250, 100, 30);
    }

    // ACTIONLISTENER INTERFACE METHODS
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button == submitButton) {
                String playerName = textField.getText();
                MainFrame f = new MainFrame();
                enclosingFrame.setVisible(false);
            } else {
                textField.setText("Welcome to the snake game");
            }
        }
    }
}
