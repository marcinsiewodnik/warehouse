package com.marcinsi.warehouseapp;

import java.util.*;

public class StockList {

    private final Map<String, StockItem> list;

    public StockList() {

        // Struktura LinkedHashMap<> - zachowuje porządek dodawania -> mamy możliwość zmiany implementacji

        this.list = new LinkedHashMap<>();
    }

    // Do wnętrza klasy przekazujemy referencje do obiektu

    public int addStock(StockItem item){

        if(item != null){

            StockItem inStock = list.getOrDefault(item.getName(), item);

            if(inStock != item){

                item.adjustStock(inStock.getQuantityInStock());
                item.reserveStock(inStock.getReservedQuantity());
            }

            list.put(item.getName(), item);

            // Zwrócenie ilości dostępnego towaru

            return item.getAvailableQuantity();
        }

        return 0;
    }

    public int addStockSecondVersion(StockItem item){

        if(item != null) {

            StockItem inStock = list.get(item.getName());

            if (inStock != null) {

                item.adjustStock(inStock.getAvailableQuantity());
            }

            list.put(item.getName(), item);

            return item.getAvailableQuantity();
        }

        return 0;
    }


    public int addStockThirdVersion(StockItem item){

        // Tworzenie produktów (i dodawanie tych produktów) - taką możliwość bym umieścił tylko w tej klasie (zastosowanie odpowiednich mechanizmów pozwalających na uzyskanie takiego efektu)


        if(item == null){

            return 0;
        }

        StockItem inStock = list.get(item.getName());

        if(inStock == null){

            list.put(item.getName(), item); // Jeśli wcześniej produkt nie istniał to wtedy - dodajemy go do mapy

            return item.getAvailableQuantity();
        }

        // Pobieram wartości z przekazanago towaru (które mnie interesują

        double price = item.getPrice();
        int quantity = item.getQuantityInStock();

        inStock.setPrice(price);
        inStock.adjustStock(quantity);

        return inStock.getAvailableQuantity();
    }

    // Metody sprzedające produkt, rezerwujące oraz cofące rezerwacje danego produktu - działają według schematu - podajemy nazwy produktu oraz ilość
    // Dlaczego podajemy nazwę - lista produktów przechowuje wszystkie produkty - kluczem jest nazwa produktu

    public int sellStock(String item, int quantity){

        StockItem inStock = list.get(item);

        if((inStock != null) && (quantity > 0)){

            // Zwracamy wynik działania metody finalizeStock()
            // Ja bym trochę zmodyfikował to rozwiązanie i umieścił walidację (dodatnia ilość w metodzie finalizeStock())
            // Czyli umieściłbym to poziom niżej

            return inStock.finalizeStock(quantity);

        }

        return 0;
    }


    public int reserveStock(String item, int quantity){

        StockItem inStock = list.get(item);

        if((inStock != null) && (quantity > 0)){

            // Tutaj również bym przesunął funkcjonalność walidacji (wartości dodatnie ilości)

            return inStock.reserveStock(quantity);
        }

        return 0;

    }

    public int unreservedStock(String item, int quantity){

        StockItem inStock = list.get(item);

        if((inStock != null) && (quantity >0)){

            return inStock.unreserveStock(quantity);

        }

        return 0;

    }

    public StockItem get(String key){

        return list.get(key);
    }

    // Ciekawe rozwiązanie - zwracamy mapę, której nie można modyfikować (wraz z obiektami)
    // Obiektów również nie można modyfikować, bo są immutable
    // Klasa StockItem - przechowywanie mapy StockItem
    // Jednak chcemy uniknąć zrócenia tej głównej bazowej struktury danych
    // Chcemy udostępnić te dane w innej formie

    public Map<String, Double> PriceList(){

        // Tworzę nową mapę - którą na końcu będzimy zwracać

        Map<String, Double> prices = new LinkedHashMap<>();

        // Obiekt klasy Map.Entry - to jest konstrukcja służąca do przeprowadzenia iteracji na rekordach

        for(Map.Entry<String, StockItem> item : list.entrySet()){

            // Wypełniam nowo utworzoną mapę odpowiednimi danymi pozyskanymi z głównej mapy
            // Klucz będzie ten sam, a wartość to pobrana cena

           prices.put(item.getKey(), item.getValue().getPrice());

        }

        return Collections.unmodifiableMap(prices);
    }

    public Map<String, StockItem> Items(){

        // Nie można zmieniać mapy, ale można zmieniać obiekty, które są w mapie
        // Zwrócenie nie kopii, ale widoku na naszą mapę główną
        // Dobra praktyka (brak możliwości wykonywania operacji także na mapie)

        return Collections.unmodifiableMap(list);
    }

    public Map<String, StockItem> ItemsPrevious() {

        // W tym przypadku zwróconą kopię można było modyfikować
        // Kopia była zupełnie nową mapą
        // W rozwiązaniu, w którym wykorzystujemy Collections.unmodifiableMap(list) - zwróconego obiektu nie można modyfikować

        return new HashMap<>(list);
    }

    @Override
    public String toString() {

        String s = "\nStock List\n"; // Ciekawe rozwiązania ze zmienną typu String - Będzie zwracać tekst w kilku linijkach - mamy taką możliwość

        double totalCost = 0.0;

        for(HashMap.Entry<String, StockItem> item: list.entrySet()){

            StockItem stockItem = item.getValue();

            // Decyzja o tym jaką informacje drukujemy, jak obliczamy wartość towaru w magazynie
            // W obecnym rozwiązaniu wartość jest obliczana jako iloczyn ceny oraz ilości, która jest dostępna

            double itemValue = stockItem.getPrice() * stockItem.getAvailableQuantity();

            s = s + stockItem + ". There are " + stockItem.getAvailableQuantity() + " in stock (available). Value of item : "; // nie przechodzimy jeszcze do kolejnj lini

            // Odpowiednie formatowanie (do dwóch miejsc po przecinku - częste rozwiązanie)
            // Wywołanie metody String.format("%.2f", itemValue)

            s = s + String.format("%.2f", itemValue) + "\n";

            totalCost += itemValue;
        }

        s += "Total stock value : " + String.format("%.2f", totalCost);

        return s;
    }

}
