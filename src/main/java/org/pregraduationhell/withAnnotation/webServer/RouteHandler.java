package org.pregraduationhell.withAnnotation.webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.pregraduationhell.withAnnotation.annotations.Get;
import org.pregraduationhell.withAnnotation.handlers.MethodHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RouteHandler {

    private static final Map<String, MethodHandler> routeMap = new HashMap<>();

    private RouteHandler() {
    }

    static List<String> getRegisteredRoutes() {
        return new ArrayList<>(routeMap.keySet());
    }
    static void registerRoute(String route, MethodHandler methodHandler) {
        routeMap.put(route, methodHandler);
    }

    static void registerRoutes() {
        try {
            List<Class<?>> controllerClasses = ControllerScanner.findAnnotatedControllers("org.pregraduationhell.withAnnotation");
            for (Class<?> controllerClass : controllerClasses) {
                registerClassRoutes(controllerClass);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void registerClassRoutes(Class<?> controllerClass) {
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Get.class)) {
                    Get getAnnotation = method.getAnnotation(Get.class);
                    String route = getAnnotation.value();
                    MethodHandler methodHandler = createMethodHandler(controllerInstance, method);
                    RouteHandler.registerRoute(route, methodHandler);
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static MethodHandler createMethodHandler(Object controllerInstance, Method method) {
        return () -> {
            try {
                Object invoke = method.invoke(controllerInstance);
                return ((MethodHandler) invoke).handle();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return "Error processing request. createMethodHandler";
            }
        };
    }

    static HttpHandler findHandler(String route) {
        MethodHandler methodHandler = routeMap.get(route);
        return methodHandler != null ? httpExchange -> handleExchange(httpExchange, methodHandler) : null;
    }

    private static void handleExchange(HttpExchange exchange, MethodHandler methodHandler) {
        try {
            String response = methodHandler.handle();
            sendResponse(exchange, response);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, "Error processing request. handleExchange");
        }
    }

    private static void sendResponse(HttpExchange exchange, String response) {
        try {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
