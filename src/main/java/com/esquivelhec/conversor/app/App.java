package com.esquivelhec.conversor.app;

import com.esquivelhec.conversor.servicio.TasaDeCambio;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final TasaDeCambio servicio = new TasaDeCambio();

    /**
     * Inicio de la app.
     * @param args  General.
     */
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("          Conversor de Moneda            ");
        System.out.println("=========================================");
        while (true) {
            mostrarMenu();
            int opcion = leerOpcion();
            if (opcion == 0) {
                System.out.println("Gracias por usar el conversor.");
                break;
            }
            if (opcion >= 1 && opcion <= 6) {
                ejecutarConversion(opcion);
            } else if (opcion != -1) {
                System.err.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
        sc.close();
    }

    /**
     * Imprime en consola el menú.
     */
    private static void mostrarMenu() {
        System.out.println("--- Opciones de Conversión ---");
        System.out.println("1. Dólar a Euro | 2. Euro a Dólar");
        System.out.println("3. Dólar a Peso Mexicano | 4. Peso Mexicano a Dólar");
        System.out.println("5. Dólar a Real Brasileño | 6. Real Brasileño a Dólar");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    /**
     * Lee desde consola la opción ingresada.
     * @return  Devuelve el int de la opción.
     */
    private static int leerOpcion() {
        try {
            return sc.nextInt();
        } catch (InputMismatchException e) {
            sc.next(); // Limpia la entrada no válida
            return -1;
        }
    }

    /**
     * Selecciona el paso para generar el resulato.
     * @param opcion    Lee la opcion ingresada sobre el menú.
     */
    private static void ejecutarConversion(int opcion) {
        String desde;
        String a;

        switch (opcion) {
            case 1: desde = "USD"; a = "EUR"; break;
            case 2: desde = "EUR"; a = "USD"; break;
            case 3: desde = "USD"; a = "MXN"; break;
            case 4: desde = "MXN"; a = "USD"; break;
            case 5: desde = "USD"; a = "BRL"; break;
            case 6: desde = "BRL"; a = "USD"; break;
            default: return;
        }
        System.out.print("Ingresa la cantidad a convertir de " + desde + " a " + a + ": ");
        try {
            double cantidad = sc.nextDouble();
            double resultado = servicio.convertir(desde, a, cantidad);
            if (resultado > 0.0) {
                System.out.printf("Resultado: %.2f %s son %.2f %s\n",
                        cantidad, desde, resultado, a);
            } else {
                System.err.println("No se pudo realizar la conversión. Verifica tu clave API o conexión.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Error: Por favor, introduce un número válido.");
            sc.next();
        }
    }
}
