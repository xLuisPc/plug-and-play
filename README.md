# IDE Unillanos 1.0 - Editor de Texto con Micro-kernel

Aplicación de escritorio en Java para procesar archivos de texto usando una arquitectura **micro-kernel** con plugins desacoplados bajo enfoque **IoC/DI** y carga dinámica de componentes desde archivos `.jar`.

## Características

- Carga y edición de archivos `.txt`.
- Carga de componentes externos en formato `.jar`.
- Ejecución de un componente cargado por selección.
- Visualización de resultados en el panel **Archivo procesado**.
- Visualización de mensajes y errores en el panel inferior.
- Resaltado de palabras reservadas de **Java**, **C++** y **SQL**.

## Arquitectura

El sistema está organizado alrededor del núcleo `IdeUnillanos`, que registra servicios y carga plugins desde JAR:

- **Core (micro-kernel):** orquesta carga y ejecución de plugins.
- **Contratos:** interfaz común `Plugin`.
- **Servicios:** tokenización, normalización, stopwords, búsqueda por fila/columna, diccionario de palabras reservadas.
- **Plugins:** `ResaltarTexto`, `ListarPalabras`, `BuscarTexto`, `ContadorPalabras`.
- **UI (IGU):**
  - Mitad izquierda: carga de componentes, lista de componentes cargados y ejecución.
  - Mitad derecha: carga de archivo, editor de texto y resultados.
  - Pie de página: mensajes y errores.

## Estructura del proyecto

```text
src/main/java/app
├── Main.java
├── contracts
├── core
├── dto
├── entities
├── persistence
├── plugins
│   ├── highlight
│   ├── search
│   ├── wordcount
│   └── wordlist
├── services
└── ui
```

## Requisitos

- Java 17 o superior
- Maven 3.8+ (recomendado)
- Sistema operativo con soporte para Swing

## Compilar y empaquetar

Desde la raíz del proyecto:

```bash
mvn clean package
```

Esto genera:

- `target/ide-unillanos-1.0.0.jar` (aplicación principal)
- `target/ide-unillanos-1.0.0-plugin-highlight.jar`
- `target/ide-unillanos-1.0.0-plugin-search.jar`
- `target/ide-unillanos-1.0.0-plugin-wordcount.jar`
- `target/ide-unillanos-1.0.0-plugin-wordlist.jar`

## Ejecutar

```bash
java -jar target/ide-unillanos-1.0.0.jar
```

Alternativa con clases compiladas:

```bash
mvn compile
java -cp target/classes app.Main
```

## Uso de la aplicación

1. Inicia la aplicación.
2. En la parte izquierda, presiona **Cargar componente**.
3. Selecciona un archivo `.jar` de componente (por ejemplo, `target/ide-unillanos-1.0.0-plugin-search.jar`).
4. Repite el paso anterior para cargar más componentes.
5. En la lista **Componentes cargados**, selecciona uno.
6. En la parte derecha, presiona **Cargar archivo** para abrir un `.txt`, o escribe texto manualmente en el editor.
7. Presiona **Ejecutar componente**.
8. Revisa:
   - resultados en **Archivo procesado**,
   - mensajes y errores en **Salida de mensajes**.

## Componentes disponibles

### 1) ResaltarTexto
- Detecta palabras reservadas de Java, C++ y SQL.
- Resalta coincidencias en el editor.

### 2) ListarPalabras
- Lista palabras encontradas en el texto sin duplicados.
- Excluye conectores (stopwords) y caracteres especiales.

### 3) BuscarTexto
- Solicita la palabra a buscar.
- Retorna coincidencias indicando **fila** y **columna**.

### 4) ContadorPalabras
- Cuenta frecuencia de aparición por palabra.
- Muestra el conteo ordenado por mayor frecuencia.

## Manejo de errores

- Si un plugin falla, el kernel captura la excepción y la reporta como mensaje de error.
- Si no hay componentes cargados, se muestra advertencia en el panel de mensajes.
- Si en `BuscarTexto` no se ingresa término, se reporta error del componente.

## Notas técnicas

- El contrato común de plugins facilita agregar nuevos componentes sin cambiar la UI.
- La búsqueda usa índices de fila/columna base 1.
- El proyecto está orientado a uso académico y demostración de patrón micro-kernel.

## Autoría

Proyecto académico - Software II.
