package app.core;

import app.contracts.Plugin;
import app.contracts.PluginContext;
import app.dto.PluginMessage;
import app.dto.PluginRequest;
import app.dto.PluginResult;
import app.services.KeywordDictionaryService;
import app.services.PositionLocatorService;
import app.services.StopWordsService;
import app.services.TextTokenizerService;
import app.services.WordNormalizationService;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IdeUnillanos {
    private final PluginRegistry registry;
    private final PluginContext context;
    private final List<URLClassLoader> pluginLoaders;

    public IdeUnillanos() {
        this.registry = new PluginRegistry();
        this.context = new PluginContext();
        this.pluginLoaders = new ArrayList<>();
        registerServices();
    }

    private void registerServices() {
        context.registerService(TextTokenizerService.class, new TextTokenizerService());
        context.registerService(WordNormalizationService.class, new WordNormalizationService());
        context.registerService(StopWordsService.class, new StopWordsService());
        context.registerService(PositionLocatorService.class, new PositionLocatorService());
        context.registerService(KeywordDictionaryService.class, new KeywordDictionaryService());
    }

    public List<String> getPluginNames() {
        List<String> names = new ArrayList<>();
        for (Plugin plugin : registry.getAll()) {
            names.add(plugin.getName());
        }
        return names;
    }

    public List<String> loadPluginsFromJar(Path jarPath) {
        List<String> loaded = new ArrayList<>();
        try {
            URL jarUrl = jarPath.toUri().toURL();
            URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());
            pluginLoaders.add(loader);

            try (JarFile jarFile = new JarFile(jarPath.toFile())) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (!isClassFile(entry)) {
                        continue;
                    }
                    String className = entry.getName()
                            .replace('/', '.')
                            .replace(".class", "");
                    try {
                        Class<?> candidate = Class.forName(className, true, loader);
                        if (!isValidPluginClass(candidate)) {
                            continue;
                        }
                        Plugin plugin = (Plugin) candidate.getDeclaredConstructor().newInstance();
                        registry.register(plugin);
                        loaded.add(plugin.getName());
                    } catch (ReflectiveOperationException | LinkageError ignored) {
                        // Se ignoran clases no cargables para continuar con el resto del JAR.
                    }
                }
            }
        } catch (IOException ex) {
            return List.of();
        }
        return loaded;
    }

    private boolean isClassFile(JarEntry entry) {
        String name = entry.getName();
        return !entry.isDirectory()
                && name.endsWith(".class")
                && !name.equals("module-info.class")
                && !name.endsWith("package-info.class");
    }

    private boolean isValidPluginClass(Class<?> candidate) {
        return Plugin.class.isAssignableFrom(candidate)
                && !candidate.isInterface()
                && !Modifier.isAbstract(candidate.getModifiers());
    }

    public PluginResult executePlugin(String pluginName, PluginRequest request) {
        Plugin plugin = registry.get(pluginName);
        if (plugin == null) {
            return new PluginResult(pluginName, "Error", List.of(),
                    List.of(new PluginMessage(PluginMessage.Type.ERROR, "Kernel",
                            "Plugin no encontrado: " + pluginName)));
        }
        try {
            return plugin.execute(request, context);
        } catch (Exception ex) {
            return new PluginResult(pluginName, "Error", List.of(),
                    List.of(new PluginMessage(PluginMessage.Type.ERROR, pluginName,
                            "Error ejecutando plugin: " + ex.getMessage())));
        }
    }
}
