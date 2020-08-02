package com.marcinsi.warehouseapp;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Basket {

    private final String name;

    // Stworzenie mapy przechowującej dany produkt oraz przypisaną mu ilość
    // Przechowywanie informacji na temat ilości towaru w koszyku
    // Cel istnienia mapy w StockList - przechowywanie referencji od obiektów typu StockList

    private final Map<StockItem, Integer> list;

    public Basket(String name) {

        this.name = name;

        // Czy TreeMap potrzebuje metod equals() oraz hashCode()
        // TreeMap oraz TreeSet nie potrzebuje tych metod
        // Operacje na zbiorze HashSet używają (i wymagają) metod hashCode() i equals(), a dodawanie i wyszukiwanie elementów odbywa się w oparciu o wyniki metody equals()
        // Operacje na zbiorze TreeSet odbywają się w oparciu o wyniki metody compareTo() lub metody compare() komparatora
        // Nasza zbiory powinny być przygotowane do tego, że ich obiekty mogą znaleźć się w zbiorach typu HashSet albo TreeSet
        // Zatem powinny definiować wszystkie trzy wspomniane metody

        this.list = new TreeMap<>();
    }

    // Dodanie produktu do koszyka - dany produkt jest kluczem
    // Dodawanie do koszyka - także sprawdzenie, czy ilość, którą chcemy dodać jest większa od 0
    // Koszyk musimy traktować tak jakby istniał odzielnie (autonomiczny), niezależnie od obiektu typu StockList (i tak jest w rzeczywistości)
    // Dlatego dodanie produktu do koszyka nie jest powiązane z rezerwacją odpowiadającej ilości
    // Połączenie dodania produktu i zarezerwowanie ilości będzie miało miejsce w klasie głównej
    // Tutaj tylko piszemy cząstkowe funkcjonalności

    public int addToBasket(StockItem item, int quantity){

        if((item != null) && quantity > 0){

            // Sprawdzamy jaka była ilość danego produktu w koszyku wcześniej
            // Jeśli wcześniej danego produtku nie było - wtedy do ilości inBasket przypisujemy wartość 0

            int inBasket = list.getOrDefault(item, 0);

            // Aktalizacja produtku w koszyku
            // W tym miejscu działa już metoda hashCode() oraz equals() - czyżby?
            // Do sprawdzenia, czy w strukturze, która jest TreeMap - te metody są wykorzystywane
            // Nie muszą to być produkty równe referencyjnie

            list.put(item, inBasket + quantity);
            return inBasket;
        }

        return 0;

    }

    public int removeFromBasket(StockItem item, int quantity){

        if((item != null) && (quantity > 0)){

            // metoda getOrDefault() - zwróci ilość towaru, która była w koszyku lub wartość 0, gdy danego towaru nie było w koszyku

            int inBasket = list.getOrDefault(item, 0);

            int newQuantity = inBasket - quantity;

            // Trzy możliwości, pierwsza możliwość, w koszu został jeszcze dany produkt - wtedy dokonujemy tylko aktualizację
            // Druga możliwość - newQuantity jest równe 0, zatem możemy usunąć dany produkt
            // Trzecia możliwość - wychodzimy z tej instrukcji warunkowej i zostanie zwrócona wartość 0

            if(newQuantity > 0) {

                list.put(item, newQuantity);

                return quantity;

            } else if (newQuantity == 0){

                list.remove(item);

                return quantity;
            }

        }

        // W sytuacji, gdybyśmy chcieli usunąć z koszyka wartość ujemną, oraz w sytuacji gdy ilość, którą chcemy usunać jest większa od ilości, która znajduje się w koszyku

        return 0;
    }

    public void clearBasket(){

        // Dla mapy list (bo lista jest mapą) istnieje metoda clear() -> to rozwiązanie jest bardziej wydajne niż przechodzenie po mapie i usuwanie elementów po kolei

        this.list.clear();
    }

    public Map<StockItem, Integer> Items(){

        // Zwracamy widok na mapę produktów w koszyku
        // Błąd UnsupportedOperationException -> błąd w czasie wykonywania programu

        return Collections.unmodifiableMap(list);
    }

    @Override
    public String toString() {

        String s = "\nShopping basket " + name + " contains " + list.size() + (list.size() == 1 ? " item" : " items") + "\n";

        double totalCost = 0.0;

        for(Map.Entry<StockItem, Integer> item: list.entrySet()){

            s = s + item.getKey() + ". " + item.getValue() + " purchased\n";

            totalCost += item.getKey().getPrice() * item.getValue();
        }

        s += "Total cost : " + totalCost;

        return s;
    }
}






























