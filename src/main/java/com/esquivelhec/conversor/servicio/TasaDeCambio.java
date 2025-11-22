package com.esquivelhec.conversor.servicio;

import com.esquivelhec.conversor.modelo.Conversion;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class TasaDeCambio {
    private static final String API_KEY = cargarApiKey();
    private static final String BASE_API_URL =
            "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private final Gson gson = new Gson();

    /**
     *  Carga la api key privada del usuario.
     * @return  Devuleve la key en formato String.
     */
    private static String cargarApiKey() {
        try (var input = TasaDeCambio.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Error: No se encontró \"config.properties\".");
                return "";
            }
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("apikey");
        } catch (java.io.IOException e) {
            System.err.println("Error al leer la clave de la API.");
            return "";
        }
    }

    /**
     * Genera las peticiones y pide el dato a la api.
     * @param desde Es la moneda desde la cual se hará la conversión.
     * @param hasta Es la moneda a la cual se hará la conversión.
     * @return  Devuleve un double con el valor en la nueva moneda.
     */
    public double obtenerTasaDeConversion(String desde, String hasta) {
    String url = BASE_API_URL + desde + "/" + hasta;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Conversion conversion = gson.fromJson(response.body(), Conversion.class);
                return conversion.conversion_rate();
            } else {
                System.err.println("Error de la API. Código HTTP: " + response.statusCode());
                return 0.0;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error de conexión al intentar obtener la tasa.");
            return 0.0;
        }
    }

    /**
     * Se en encarga de convertir la cantidad en la moneda destino.
     * @param desde La opción de moneda inicial.
     * @param hasta La opción de moneda destino.
     * @param cantidad  Cantidad a convertir.
     * @return  Devuelve la cantidad en la moneda destino.
     */
    public double convertir(String desde, String hasta, double cantidad) {
        double valor = obtenerTasaDeConversion(desde, hasta);
        if (valor > 0.0) {
            return cantidad * valor;
        } else {
            return 0.0;
        }
    }
}
