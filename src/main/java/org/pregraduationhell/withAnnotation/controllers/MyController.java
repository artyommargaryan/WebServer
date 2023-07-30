package org.pregraduationhell.withAnnotation.controllers;

import org.pregraduationhell.withAnnotation.annotations.Controller;
import org.pregraduationhell.withAnnotation.annotations.Get;
import org.pregraduationhell.withAnnotation.handlers.Handler;
import org.pregraduationhell.withAnnotation.handlers.MethodHandler;

@Controller
public class MyController {
    public MyController() {
    }

    @Get("/")
    public MethodHandler index() {
        return new Handler("folder1/index.html");
    }

    @Get("/page2")
    public MethodHandler page2() {
        return new Handler("folder2/index.html");
    }
}
