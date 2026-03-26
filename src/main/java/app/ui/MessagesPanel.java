package app.ui;

import app.dto.PluginMessage;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.List;

public class MessagesPanel extends JPanel {
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public MessagesPanel() {
        setLayout(new BorderLayout());
        JList<String> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void clear() {
        model.clear();
    }

    public void addMessages(List<PluginMessage> messages) {
        for (PluginMessage message : messages) {
            model.addElement(message.toString());
        }
    }
}
