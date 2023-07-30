package org.pregraduationhell.withAnnotation.webServer;

import org.pregraduationhell.withAnnotation.annotations.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ControllerScanner {
    private ControllerScanner() {
    }

    static List<Class<?>> findAnnotatedControllers(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> controllers = new ArrayList<>();

        String classPath = System.getProperty("java.class.path");
        String[] classPathEntries = classPath.split(File.pathSeparator);

        for (String classPathEntry : classPathEntries) {
            File baseDir = new File(classPathEntry);
            findControllersInPackage(packageName, baseDir, controllers);
        }

        return controllers;
    }

    private static void findControllersInPackage(String packageName, File baseDir, List<Class<?>> controllers) throws ClassNotFoundException {
        String basePath = baseDir.getAbsolutePath();
        String packagePath = packageName.replace('.', File.separatorChar);
        File packageDir = new File(basePath + File.separator + packagePath);

        if (packageDir.exists() && packageDir.isDirectory()) {
            for (File file : Objects.requireNonNull(packageDir.listFiles())) {
                if (file.isDirectory()) {
                    String subPackageName = packageName + "." + file.getName();
                    findControllersInPackage(subPackageName, baseDir, controllers);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        controllers.add(clazz);
                    }
                }
            }
        }
    }
}
