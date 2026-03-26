package app.ui;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class ResultsPanel extends JPanel {
    private final JTextArea textArea;

    public ResultsPanel() {
        setLayout(new BorderLayout());
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}
