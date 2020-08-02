package com.marcinsi;
import com.marcinsi.warehouseapp.Basket;
import com.marcinsi.warehouseapp.StockItem;
import com.marcinsi.warehouseapp.StockList;

import java.util.Map;

public class Main {

    // Analogiczne do przykładu z ciałami niebieskimi, czy lokalizacjami
    // Jednak w tym przypadku tworzymy dodatkową klasę StockList - klasa ma tę samą funkcję co mapa we wspomnianych przykładach
    // Klasa StockList przechowuje mapę produktów (plus ma zdefinionane dodatkowe funkcjonalności)
    // Dodatkowe funkcjonalności takie jak sprzedaż, rezerwacja produktu

    private static StockList stockList = new StockList();

    public static void main(String[] args) {

        // Definicja zmiennej temp - wykorzystanie zmiennej dla dodania wszystkich produktów

       StockItem temp = new StockItem("bread", 0.86, 100);
       stockList.addStock(temp);

       temp = new StockItem("cake", 1.10, 7);
       stockList.addStock(temp);

        temp = new StockItem("car", 12.50, 2);
        stockList.addStock(temp);

        temp = new StockItem("chair", 62.0, 10);
        stockList.addStock(temp);

        temp = new StockItem("cup", 0.5, 200);
        stockList.addStock(temp);

        temp = new StockItem("cup", 0.45, 7);
        stockList.addStock(temp);

        temp = new StockItem("door", 72.95, 4);
        stockList.addStock(temp);

        temp = new StockItem("juice", 2.50, 36);
        stockList.addStock(temp);

        temp = new StockItem("towel", 2.40, 80);
        stockList.addStock(temp);

        temp = new StockItem("vase", 8.76, 40);
        stockList.addStock(temp);

        // Stworzenie nowego koszyka, koszyk nie jest zmienną statyczną, możemy stworzyć dowolną liczbę koszyków

        Basket marcinsBasket = new Basket("Marcin");

        // Metoda sprzedaży polega na dodania danego produktu do koszyka

        sellItem(marcinsBasket, "car", 1);

        System.out.println(marcinsBasket);

        sellItem(marcinsBasket, "car", 1);

        System.out.println(marcinsBasket);

        // Metoda sellItem - dodanie do koszyka, informacja o przebiegu metody - jeśli zwrócona wartość 0, to dodanie do koszyka zakończyło się niepowodzeniem

        if(sellItem(marcinsBasket, "car", 1) != 1){

            System.out.println("There are no more cars in stock");
        }

        // Próba dodania do koszyka przedmiotu, który nie istnieje

        sellItem(marcinsBasket, "spanner", 5);

        System.out.println(marcinsBasket);

        sellItem(marcinsBasket, "juice", 4);
        sellItem(marcinsBasket, "cup", 12);
        sellItem(marcinsBasket, "bread", 1);

        System.out.println(marcinsBasket);

        // Wydrukowanie listy po przeprowadzeniu wszystkich operacji (sprawdzenie, czy odpowiednie ilości zostały zarezerwowane)

        System.out.println(stockList);

        // Tworzę nowy koszyk

        Basket basket = new Basket("customer");
        sellItem(basket, "cup", 100);
        sellItem(basket, "juice", 5);
        removeItem(basket, "cup" , 1);

        System.out.println(basket);

        // Całkowicie usuwamy z koszyka car

        removeItem(marcinsBasket, "car", 1 );
        removeItem(marcinsBasket, "car", 1);
        removeItem(marcinsBasket, "cup", 9);

        // Usuniecie danego produktu z koszyka (produktu, który został całkowicie usunięty)

        System.out.println("cars removed : " + removeItem(marcinsBasket, "car", 1)); // should not remove any (ponieważ w koszyku nie mamy już samochodów)

        System.out.println(marcinsBasket);

        // remove all items from timsBasket
        // usuniecie z koszyka (nie polega po prostu na usunięciu ilości - liczba sztuk 0, ale polega na usunięciu danego produktu całkowicie)
        // Oczywiście wszystko zależy od wymagań, być może byśmy chcieli przechowywać historię zakupu
        // Wtedy moglibyśmy wprowadzić funkcjonalność polegającą na ustawieniu ilości na 0, i pozostawieniu danego produktu w koszyku

        removeItem(marcinsBasket, "bread", 1);
        removeItem(marcinsBasket, "cup", 3);
        removeItem(marcinsBasket, "juice", 4);
        removeItem(marcinsBasket, "cup", 3);

        System.out.println(marcinsBasket);

        System.out.println("\nDispley stock list before and after checkout");
        System.out.println(basket);
        System.out.println(stockList);

        // Usuwamy wszyskie produkty z koszyka i jednocześnie finalizujemy przegieg procesu

        checkOut(basket);

        System.out.println(basket);

        // Również na liście produktów powinna mieć miejsce aktualizacja - znikną zarezerwowane produkty (tylko dla koszyka dla którego wykonano tę operacje)

        System.out.println(stockList);

    }

    // Dodając towar do koszyka dokonujemy jego rezerwacji
    // W tych metodach dokonujemy odpowiedniego połaczenia funkcjonalności (funkcjonalności z różnych klas łączymy w jedną całość)
    // Informacja o przebiegu procesu jest zwracana (wartość 0 - jeśli dodanie do koszyka się nie powiedzie)

    public static int sellItem(Basket basket, String item, int quantity){

        StockItem stockItem = stockList.get(item);

        // Pierwsze sprawdzamy, czy dany towar istnieje

        if(stockItem == null){

            System.out.println("We don't sell " + item);

            return 0;
        }

        // Następnie wykonujemy dwie metody (wykonanie drugiej metody jest uzależnione od wykonania pierwszej metody)
        // Pierwsze dokonujemy rezerwacji towaru w magazynie
        // Jeśli ta operacja zakończy się powodzeniem - wtedy dodajemy dany produkt do koszyka
        // Koszyk tak naprawdę nic nie wie o produktach w magazynie - jest niezależną klasą
        // Nie jesteśmy jeszcze na etapie wydania towarów z magazynu (ostatecznej sprzedazy)
        // Klient może jeszcze zmienić zdanie i dokonać cofnięcia rezerwacji

        if(stockList.reserveStock(item, quantity) != 0){

            return basket.addToBasket(stockItem, quantity);
        }

        return 0;
    }

    // Przy tej metodzie nie wiemy na 100 procent, czy sie wykona (zależy to od tego, czy wprowadzony przez nas produkt jest na liście oraz, czy podamy odpowiednią ilość)
    // Informacja o przebiegu procesu jest zwracana (wartość 0 - jeśli dodanie do koszyka się nie powiodło)

    public static int removeItem(Basket basket, String item, int quantity){

        StockItem stockItem = stockList.get(item);

        if(stockItem == null){

            System.out.println("We don't sell " + item);

            return 0;
        }

        // Gdy wykona się metoda removeFromBasket() na koszyku, wtedy wykonujemy metodę unreservedStock() na obiekcie typu StockList
        // Skoro towar jest w koszyku, to znaczy, że jest już zarezerwowany, inaczej, by nie znalazł się w koszyku (analiza wsześniejszego etapu dodania)
        // Równie dobrze byśmy mogli zapisać tutaj != 0

        if(basket.removeFromBasket(stockItem, quantity) == quantity){

            // Zwracamy wynik działania metody
            // W związku z zaprojektowaną architekturą naszej aplikacji - poniższa metoda zawsze się wykona (analiza wcześniejszego etapu dodania)

            return stockList.unreservedStock(item, quantity); // W jakim przypadku to się nie wykonana - jest taka możliwość, pomiędzy metodami sellItem() a removeItem() - wykonany metodę unreserveStock() - furtka w systemie
        }

        return 0;
    }

    // Jako parametr metody checkOut() wprowadzamy tylko dany koszyk
    // W koszyku znajdują się produkty, które zostały wcześniej dodane, czyli odpwiednia ilość towaru została zarezerwowana
    // Metoda sellStock na obiekcie typu stockList zawsze powinna się wykonać (wcześniejsza analiza etapu dodawania elementu)
    // Natomiast ta metoda, w przeciewieństwie do metod sellItem() oraz removeItem() nic nie zwraca

    public static void checkOut(Basket basket){

        for(Map.Entry<StockItem, Integer> item: basket.Items().entrySet()){

            // Tuaj nie sprawdzaliśmy, czy metoda nie zwróci czasami 0
            // Gdyby ta metoda zwróciła 0, oznaczałoby to, że sprzedaż towarów ze sklepu się nie powiodła
            // Jednak w związku z architekturą aplikacji - wiemy, że ta metoda zawsze powinna się wykonać

            stockList.sellStock(item.getKey().getName(), item.getValue());
        }

        basket.clearBasket();

    }
}
