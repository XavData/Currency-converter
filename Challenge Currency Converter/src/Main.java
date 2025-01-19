import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite sua chave da API: ");
        String key = scanner.nextLine();

        System.out.println("--[ Bem vindo ao conversor de moedas!! ]--");

        Map<Integer, String> currencies = new HashMap<>();
        currencies.put(1, "USD");
        currencies.put(2, "ARS");
        currencies.put(3, "USD");
        currencies.put(4, "BRL");
        currencies.put(5, "USD");
        currencies.put(6, "COP");

        while (true) {
            System.out.println("""
                       \s
                        ***********************************
                       \s
                        1) Dólar >> Peso argentino
                        2) Peso argentino >> Dólar
                        3) Dólar >> Real brasileiro
                        4) Real brasileiro >> Dólar
                        5) Dólar >> Peso colombiano
                        6) Peso colombiano >> Dólar
                        7) Sair
                       \s
                        ***********************************
                       \s
                        Escolha uma opção válida:\s""");
            int desiredOption = scanner.nextInt();

            if (desiredOption == 7) {
                System.out.println("Encerrando o programa. Até logo!");
                break;
            }

            if (!currencies.containsKey(desiredOption)) { //if the desired option is not among those provided
                System.out.println("Opção inválida! Tente novamente.");
                continue;
            }

            // Defines the origin currency and destination currency
            String originCurrency = currencies.get(desiredOption);
            String destinationCurrency = desiredOption % 2 == 0 ? "USD" : currencies.get(desiredOption + 1);

            // Does a request to the API to get the exchange rate
            String path = "https://v6.exchangerate-api.com/v6/" + key + "/latest/" + originCurrency;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(path))
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Convert Json to objects using Gson
            Gson gson = new Gson();
            Map responseMap = gson.fromJson(response.body(), Map.class);

            // Gets the exchanges rates
            Map<String, Double> rates = (Map<String, Double>) responseMap.get("conversion_rates");
            Double rate = rates.get(destinationCurrency);

            if (rate != null) {
                System.out.print("Digite o valor a ser convertido: ");
                double value = scanner.nextDouble();

                double converted = value * rate;
                System.out.printf("Valor convertido: %.2f %s\n", converted, destinationCurrency);
            } else {
                System.out.println("Não foi possível obter a taxa de câmbio.");
            }
        }

        scanner.close();
    }
}

