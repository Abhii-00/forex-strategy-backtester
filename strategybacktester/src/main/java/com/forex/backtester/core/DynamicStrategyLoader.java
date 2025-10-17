package com.forex.backtester.core;

import com.forex.backtester.strategy.Strategy;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicStrategyLoader {

    public static Strategy loadStrategyFromCodeString(String javaCode) throws Exception {
        String className = parseClassName(javaCode);
        if (className == null) {
            throw new IllegalArgumentException("Could not find a 'public class...' declaration in the code.");
        }
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("user_strategy_");
            File sourceFile = new File(tempDir.toFile(), className + ".java");
            Files.write(sourceFile.toPath(), javaCode.getBytes(StandardCharsets.UTF_8));

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new IllegalStateException("Cannot find system Java compiler. Ensure you are running on a full JDK, not just a JRE.");
            }
            if (compiler.run(null, null, null, sourceFile.getPath()) != 0) {
                throw new Exception("Strategy compilation failed. Please check the code for errors.");
            }

            // --- CORRECTED SECTION ---
            // Use try-with-resources to ensure the classLoader is closed automatically.
            try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{tempDir.toUri().toURL()})) {
                Class<?> strategyClass = Class.forName(className, true, classLoader);
                return (Strategy) strategyClass.getDeclaredConstructor().newInstance();
            }
            // The classLoader is now guaranteed to be closed here.

        } finally {
            // This will now succeed because the lock from the classLoader has been released.
            if (tempDir != null) {
                deleteDirectory(tempDir.toFile());
            }
        }
    }

    private static String parseClassName(String code) {
        Matcher matcher = Pattern.compile("public class\\s+([A-Za-z0-9_]+)").matcher(code);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static void deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}