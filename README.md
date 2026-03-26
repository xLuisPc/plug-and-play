# IDE Unillanos 1.0

Aplicación de escritorio en Java (Swing) para cargar texto, ejecutar plugins y mostrar resultados bajo arquitectura micro-kernel.

## Funcionalidades actuales

- Abrir y guardar archivos `.txt`.
- Editar texto directamente en el panel principal.
- Cargar plugins desde archivos `.jar` en tiempo de ejecución.
- Ejecutar el plugin seleccionado en **Componentes cargados**.
- Mostrar salida en **Archivo procesado** y mensajes en **Salida de mensajes**.
- Resaltar tokens en el editor cuando se ejecuta `ResaltarTexto`.

## Arquitectura

- **Núcleo:** `IdeUnillanos` (carga plugins, registra servicios y ejecuta plugins).
- **Contrato:** `Plugin` y `PluginContext`.
- **Servicios:** tokenización, normalización, stopwords, localización fila/columna y diccionario de keywords.
- **Plugins incluidos:** `ResaltarTexto`, `BuscarTexto`, `ContadorPalabras`, `ListarPalabras`.
- **Interfaz:** `MainWindow` + paneles de resultados y mensajes.

## Requisitos

- Java 17+
- Maven 3.8+

## Compilar y empaquetar

```bash
mvn clean package
```

Artefactos generados en `target/`:

- `ide-unillanos-1.0.0.jar` (aplicación)
- `ide-unillanos-1.0.0-plugin-highlight.jar`
- `ide-unillanos-1.0.0-plugin-search.jar`
- `ide-unillanos-1.0.0-plugin-wordcount.jar`
- `ide-unillanos-1.0.0-plugin-wordlist.jar`

## Ejecutar

Con JAR principal:

```bash
java -jar target/ide-unillanos-1.0.0.jar
```

Con clases compiladas:

```bash
mvn compile
java -cp target/classes app.Main
```

## Uso rápido

1. Inicia la aplicación.
2. Carga uno o varios plugins con **Cargar componente** (archivos `.jar`).
3. Selecciona un plugin en **Componentes cargados**.
4. Abre un `.txt` con **Cargar archivo** o escribe en el editor.
5. Ejecuta con **Ejecutar componente**.
6. Revisa resultados en **Archivo procesado** y mensajes en **Salida de mensajes**.

## Comportamiento de plugins

### ResaltarTexto
- Busca tokens que coincidan con palabras reservadas de Java, C++ y SQL.
- Resalta en el editor por color según lenguaje.
- Reporta conteo por lenguaje en mensajes.

### BuscarTexto
- Solicita una palabra mediante diálogo.
- Si no se ingresa término, retorna error.
- Busca coincidencias de palabra completa (case-insensitive) y reporta fila/columna (base 1).

### ContadorPalabras
- Tokeniza y normaliza a minúsculas.
- Cuenta frecuencia por palabra.
- Devuelve resultados ordenados de mayor a menor frecuencia.

### ListarPalabras
- Tokeniza y normaliza a minúsculas.
- Excluye stopwords.
- Devuelve palabras únicas ordenadas alfabéticamente.

## Manejo de errores

- Si no hay plugins cargados o no hay selección, la UI muestra advertencia.
- Si un plugin falla durante ejecución, el kernel retorna mensaje de error.
- Si un JAR no contiene clases plugin válidas, se informa en el panel de mensajes.

## Autoría

Proyecto académico - Software II.
