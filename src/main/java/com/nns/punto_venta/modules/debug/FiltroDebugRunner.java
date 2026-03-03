package com.nns.punto_venta.modules.debug;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
//@Component
//public class FiltroDebugRunner implements CommandLineRunner {
//
//    private final ApplicationContext context;
//
//    public FiltroDebugRunner(ApplicationContext context) {
//        this.context = context;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Ejecutando diagnóstico de filtros...");
//        System.out.println("=== LISTADO DE FILTROS REGISTRADOS ===");
//            
//            // Obtenemos los nombres de todos los beans que son Filtros
//            String[] filterNames = context.getBeanNamesForType(Filter.class);
//            
//            for (String name : filterNames) {
//                Object filter = context.getBean(name);
//                System.out.println("Filtro: " + name + " -> Tipo: " + filter.getClass().getName());
//            }
//    }
//}