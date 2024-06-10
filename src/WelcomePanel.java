import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class WelcomePanel extends JPanel implements ActionListener {

    private JTextField textField;
    private JButton submitButton;
    private JButton clearButton;
    private JFrame enclosingFrame;

    private Clip songClip;

    public WelcomePanel(JFrame frame) {
        enclosingFrame = frame;
        textField = new JTextField(10);
        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");
        add(textField);  // textField doesn't need a listener since nothing needs to happen when we type in text
        add(submitButton);
        add(clearButton);
        submitButton.addActionListener(this);
        clearButton.addActionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.RED);
        g.drawString("Click to start", 50, 30);
        textField.setLocation(50, 50);
        submitButton.setLocation(50, 100);
        clearButton.setLocation(150, 100);

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
                textField.setText("Welcome to snake game");
            }
        }
    }


}