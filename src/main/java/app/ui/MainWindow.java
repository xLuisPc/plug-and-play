package app.ui;

import app.core.IdeUnillanos;
import app.dto.PluginMessage;
import app.dto.HighlightMatchDTO;
import app.dto.PluginRequest;
import app.dto.PluginResult;
import app.entities.Documento;
import app.persistence.TextFileRepository;

import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainWindow extends JFrame {
    private final JTextPane editor = new JTextPane();
    private final ResultsPanel resultsPanel = new ResultsPanel();
    private final MessagesPanel messagesPanel = new MessagesPanel();
    private final IdeUnillanos kernel = new IdeUnillanos();
    private final TextFileRepository repository = new TextFileRepository();
    private final DefaultListModel<String> loadedPluginsModel = new DefaultListModel<>();
    private final Set<String> loadedPlugins = new LinkedHashSet<>();
    private JList<String> loadedList;
    private File currentFile;

    public MainWindow() {
        setTitle("IDE Unillanos 1.0");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        createMenu();
        createMainLayout();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem openItem = new JMenuItem("Abrir .txt");
        JMenuItem saveItem = new JMenuItem("Guardar");

        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createMainLayout() {
        JPanel leftPane = buildLeftPane();
        JPanel rightPane = buildRightPane();

        JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
        horizontalSplit.setResizeWeight(0.35);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new TitledBorder("Salida de mensajes"));
        footer.add(messagesPanel, BorderLayout.CENTER);

        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplit, footer);
        verticalSplit.setResizeWeight(0.88);

        add(verticalSplit, BorderLayout.CENTER);
    }

    private JPanel buildLeftPane() {
        JPanel leftPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel loadComponentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadComponentsPanel.setBorder(new TitledBorder("Cargar componente"));
        JButton addComponentButton = new JButton("Cargar componente");
        addComponentButton.addActionListener(e -> loadPluginJar());
        loadComponentsPanel.add(addComponentButton);

        gbc.gridy = 0;
        gbc.weighty = 0.2;
        leftPane.add(loadComponentsPanel, gbc);

        JPanel loadedPanel = new JPanel(new BorderLayout());
        loadedPanel.setBorder(new TitledBorder("Componentes cargados"));
        loadedList = new JList<>(loadedPluginsModel);
        loadedPanel.add(new JScrollPane(loadedList), BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.weighty = 0.7;
        leftPane.add(loadedPanel, gbc);

        JPanel executePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        executePanel.setBorder(new TitledBorder("Ejecutar componente"));
        JButton executeComponents = new JButton("Ejecutar componente");
        executeComponents.addActionListener(e -> executeSelectedLoadedPlugin());
        executePanel.add(executeComponents);

        gbc.gridy = 2;
        gbc.weighty = 0.1;
        leftPane.add(executePanel, gbc);

        return leftPane;
    }

    private JPanel buildRightPane() {
        JPanel rightPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.setBorder(new TitledBorder("Archivo inicial"));
        JPanel fileTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton openFileButton = new JButton("Cargar archivo");
        openFileButton.addActionListener(e -> openFile());
        fileTop.add(openFileButton);
        filePanel.add(fileTop, BorderLayout.NORTH);
        filePanel.add(new JScrollPane(editor), BorderLayout.CENTER);

        gbc.gridy = 0;
        gbc.weighty = 0.6;
        rightPane.add(filePanel, gbc);

        JPanel processedPanel = new JPanel(new BorderLayout());
        processedPanel.setBorder(new TitledBorder("Archivo procesado"));
        processedPanel.add(resultsPanel, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.weighty = 0.4;
        rightPane.add(processedPanel, gbc);

        return rightPane;
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        currentFile = chooser.getSelectedFile();
        try {
            Documento doc = repository.load(currentFile.toPath());
            editor.setText(doc.getContent());
            clearHighlighting();
            messagesPanel.clear();
            resultsPanel.setText("Archivo cargado: " + doc.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo: " + ex.getMessage());
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
            int result = chooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            currentFile = chooser.getSelectedFile();
        }
        try {
            repository.save(Path.of(currentFile.getAbsolutePath()), editor.getText());
            JOptionPane.showMessageDialog(this, "Archivo guardado correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el archivo: " + ex.getMessage());
        }
    }

    private void loadPluginJar() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos JAR", "jar"));
        chooser.setMultiSelectionEnabled(true);
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File[] selectedFiles = chooser.getSelectedFiles();
        if (selectedFiles == null || selectedFiles.length == 0) {
            File singleSelection = chooser.getSelectedFile();
            if (singleSelection == null) {
                return;
            }
            selectedFiles = new File[]{singleSelection};
        }

        List<PluginMessage> batchMessages = new ArrayList<>();
        for (File jarFile : selectedFiles) {
            List<String> foundPlugins = kernel.loadPluginsFromJar(jarFile.toPath());
            if (foundPlugins.isEmpty()) {
                batchMessages.add(new PluginMessage(PluginMessage.Type.WARN, "UI",
                        "No se encontraron componentes validos en: " + jarFile.getName()));
                continue;
            }

            int addedCount = 0;
            for (String pluginName : foundPlugins) {
                if (loadedPlugins.add(pluginName)) {
                    loadedPluginsModel.addElement(pluginName);
                    addedCount++;
                }
            }
            batchMessages.add(new PluginMessage(PluginMessage.Type.INFO, "UI",
                    "Componentes cargados desde " + jarFile.getName() + ": " + addedCount));
        }

        messagesPanel.addMessages(batchMessages);
        if (!loadedPluginsModel.isEmpty() && loadedList.getSelectedIndex() == -1) {
            loadedList.setSelectedIndex(0);
        }
    }

    private void executeSelectedLoadedPlugin() {
        if (loadedPlugins.isEmpty()) {
            messagesPanel.clear();
            messagesPanel.addMessages(List.of(
                    new PluginMessage(PluginMessage.Type.WARN, "UI",
                            "No hay componentes cargados para ejecutar.")
            ));
            return;
        }

        String pluginName = loadedList.getSelectedValue();
        if (pluginName == null) {
            messagesPanel.clear();
            messagesPanel.addMessages(List.of(
                    new PluginMessage(PluginMessage.Type.WARN, "UI",
                            "Seleccione un componente cargado para ejecutar.")
            ));
            return;
        }

        messagesPanel.clear();
        if ("ResaltarTexto".equals(pluginName)) {
            clearHighlighting();
        }

        PluginResult result = buildAndExecute(pluginName);
        resultsPanel.setText(formatResultData(result.getData()));
        messagesPanel.addMessages(result.getMessages());
        if ("ResaltarTexto".equals(pluginName)) {
            applyHighlights(result);
        }
    }

    private PluginResult buildAndExecute(String pluginName) {
        String term = null;
        if ("BuscarTexto".equals(pluginName)) {
            term = JOptionPane.showInputDialog(this, "Ingrese palabra a buscar:");
        }
        PluginRequest request = new PluginRequest(editor.getText(), term);
        return kernel.executePlugin(pluginName, request);
    }

    private String formatResultData(Object data) {
        if (data == null) {
            return "Sin resultados.";
        }
        if (data instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                return "Sin resultados.";
            }
            StringBuilder sb = new StringBuilder();
            for (Object item : collection) {
                sb.append("- ").append(item).append("\n");
            }
            return sb.toString();
        }
        return String.valueOf(data);
    }

    private void applyHighlights(PluginResult result) {
        if (!(result.getData() instanceof List<?> list)) {
            return;
        }
        Highlighter highlighter = editor.getHighlighter();
        for (Object item : list) {
            if (item instanceof HighlightMatchDTO match) {
                try {
                    highlighter.addHighlight(match.getStart(), match.getEnd(),
                            new DefaultHighlighter.DefaultHighlightPainter(colorByLanguage(match.getLanguage())));
                } catch (Exception ignored) {
                    // Se ignoran rangos invalidos para no detener la UI.
                }
            }
        }
    }

    private Color colorByLanguage(String language) {
        return switch (language) {
            case "Java" -> new Color(255, 250, 180);
            case "C++" -> new Color(200, 235, 255);
            case "SQL" -> new Color(220, 255, 220);
            default -> Color.LIGHT_GRAY;
        };
    }

    private void clearHighlighting() {
        editor.getHighlighter().removeAllHighlights();
    }
}
