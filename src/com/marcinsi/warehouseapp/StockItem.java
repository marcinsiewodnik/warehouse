package com.marcinsi.warehouseapp;

// StockItem - podstawowa składowa całej aplikacji
// Rozpoczęcie pisania kodu od tej klasy
// Klasa nie jest klasą finalną (w kontekście metody equals() oraz hashCode())
// Klasa implementuje interfejs Comparable<>
// Metody equals() oraz hashCode()

public class StockItem implements Comparable<StockItem> {

    private final String name;
    private double price;
    private int quantityInStock = 0;
    private int reserved = 0;

    public StockItem(String name, double price) {

        this.name = name;
        this.price = price;
    }

    public StockItem(String name, double price, int quantityInStock) {

        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public String getName() {

        return name;
    }

    public double getPrice() {

        return price;
    }

    // Bardzo ważna informacja do klienta - Sprawdzenie dostępności towaru
    // Metoda oblicza wartości na podstawie pól zawartych w produkcie

    public int getAvailableQuantity() {

        return quantityInStock - reserved;
    }

    public int getReservedQuantity(){

      return reserved;

    }

    public int getQuantityInStock(){

        return quantityInStock;
    }

    public void setPrice(double price) {

        if(price > 0.0){

            this.price = price;
        }

    }

    // Metoda adjustStock() - nie zwraca wartości (informacji o przebiegu procesu)
    // Moim zdaniem ta metoda także powinna zwracać wartość (informującą o przebiegu procesu)
    // Ja bym rozdzielił, tę metodę na increase oraz decrease (uzyskanie większej spójności)

    public void adjustStock(int quantity) {

        int newQuantity = this.quantityInStock + quantity;

        if(newQuantity >=0){

            this.quantityInStock = newQuantity;
        }

        // Gdybyśmy dokonali próby zmniejszenia ilości poniżej zera - nic się nie stanie
        // Po prostu wartość nie zostanie zaktualizowana
        // Metoda nie zwróci żadnej informacji, czy aktualizacja została przeprowadzona

    }

    // Moim zdaniem w tych metodach powinniśmy dokonywać walidacji pod względem wartości ujemnych -> to jest tylko model

    public int reserveStock(int quantity){

        // Wywołanie metody getAvailableQuantity() - to nie jest pole, ale metoda

        if(quantity <= this.getAvailableQuantity()){

            this.reserved += quantity;

            // Metoda zwraca informacji o zarezerwowanej ilości (w przyszłości można by stworzyć bardziej zaawansowaną aplikację)
            // Na przykład można by wprowadzić możliwość rezerwacji części towaru (jeśli nie można zarezerwować całości)

            return quantity;
        }

        return 0;

        // W przypadku gdy byśmy chcieli zarezerwować większą ilość towaru niż jest dostępna w magazynie - zostanie zwrócona wartość 0
        // Jednak gdybyśmy podali wartość ujemną - nastąpi aktualizacja danych (luka w systemie)
    }

    // Brak walidacji wartości ujemnych (ciekawy pomysł informowania o niepowodzeniu)

    public int unreserveStock(int quantity){

        if(quantity <= this.reserved){

            this.reserved -= quantity;
            return quantity;
        }

        return 0;

        // Gdybyśmy chcieli odrezerwować więcej niż zarezerwowaliśmy - wtedy zostanie zwrócona wartość 0 - informacja o niepowodzeniu
        // Jednak gdybyśmy podali wartość ujemną - nastąpi aktualizacja danych (luka w systemie)
    }

    public int finalizeStock(int quantity){

        if(quantity <= this.reserved){

            quantityInStock -=quantity;
            reserved -= quantity;
            return quantity;
        }

        return 0;

        // Tutaj również nie dokonywaliśmy walidacji pod wzlędem wartości ujemnych
    }

    @Override
    public int hashCode() {

        return this.name.hashCode() + 30;

    }

    // Metoda equals() - sprawdza tylko równość
    // Moim zdaniem metoda equals() oraz hashCode powinna być final (klasa nie jest finalna - zatem ta metoda powinna być)
    // Zwróć uwagę na przykazywany parametr - typ Object - nie możemy tego zmienić - ponieważ nadpisujemy metodę equals() - sygnatura metody musi pozostać taka sama
    // Zwróć uwagę na zwracany parametr (typ boolean)

    @Override
    public boolean equals(Object obj) {

        if(obj == this){

            return true;
        }

        // Mechanizm sprawdzania warunku

        if(obj == null || obj.getClass() != this.getClass()){

            // Gdy obiekty są różnych klas - zostaje zrócona wartość false (mimo że będą miały takie same pola)
            // To w tym miejscu jest ukryta funkcjonalność sprawdzania równości pod względem klas -> alternatywa to instanceOf

            return false;
        }

        String objName = ((StockItem) obj).getName();

        return this.getName().equals(objName);

    }

    // Zwróć uwagę na przyjmowany parametr - StockItem
    // Nie musimy sprawdzać, jakiej klasy jest przekazany parametr (tak ja w metodzie equals())
    // Zwróć uwagę na zwracany parametr (int)
    // Metoda z interfejsu Comparable<> - konieczność implementacji

    @Override
    public int compareTo(StockItem o) {

        if(this == o){

            return 0;
        }

        if(o != null){

            // Wykorzystanie funkcjonalności zdefiniowanej w klasie String

            return this.name.compareTo(o.getName());
        }

        // Metoda wyrzuca wyjątek

        throw new NullPointerException();
    }

    @Override
    public String toString() {

        return this.name + " : price " + this.price + ". Reserved : " + this.reserved;
    }
}
