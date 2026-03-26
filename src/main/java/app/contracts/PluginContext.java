package app.contracts;

import java.util.HashMap;
import java.util.Map;

public class PluginContext {
    private final Map<Class<?>, Object> services = new HashMap<>();

    public <T> void registerService(Class<T> type, T instance) {
        services.put(type, instance);
    }

    public <T> T getService(Class<T> type) {
        Object service = services.get(type);
        if (service == null) {
            throw new IllegalArgumentException("Servicio no registrado: " + type.getSimpleName());
        }
        return type.cast(service);
    }
}
