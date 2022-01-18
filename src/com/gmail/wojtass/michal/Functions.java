package com.gmail.wojtass.michal;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Functions implements Serializable {
    private static final Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
    private final String FILENAME = "plik.csv";
    List<Warehouse> warehouses = new ArrayList<>();

    //Główna pętla ze wszystkimi metodami
    public void loop(){
        final int EXIT = 0;
        final int WYPISZ = 1;
        final int ADDWAREHOUSE = 2;
        final int ADDCARGO = 3;
        final int UNSUBSCRIBECARGO = 4;
        final int UNSUBSCRIBECATEGORY = 5;
        final int UNSUBSCRIBEFULL = 6;
        final int UNSUBSCRIBEEMPTY = 7;
        final int REMOVECARGO = 8;
        final int SORTEDWAREHOUSE = 9;
        final int CARGOTODIFFERENTWAREHOUSE = 10;
        final int ALLWAREHOUSES = 11;
        final int ALLCARGOS = 12;
        System.out.println("Witam w systemie magazynu");
        odczytSerialization();
        //odczytCSV(); //Dodatkowa opcja primitywna, przy zmianie zmień też zapis i zakomentuj serializowany zapis i odczyt
        setMaxCargoNumber();
        try {
            int number = -1;
            while (number != EXIT) {
                mainMenu();
                number = option(number);
                switch (number) {
                    case WYPISZ:
                        wypisz();
                        break;
                    case ADDWAREHOUSE:
                        addWarehouse();
                        break;
                    case ADDCARGO:
                        addCargoToWarehouse();
                        break;
                    case UNSUBSCRIBECARGO:
                        unsubscribeCargo();
                        break;
                    case UNSUBSCRIBECATEGORY:
                        String category = checkIsCategory();
                        System.out.println("Podaj lokalizacje magazynu");
                        String categoryLocalization = writeString();
                        unsubscribeCategory(category,categoryLocalization);
                        break;
                    case UNSUBSCRIBEFULL:
                        unsubscribeNearlyFull();
                        break;
                    case UNSUBSCRIBEEMPTY:
                        unsubscribeNearlyEmpty();
                        break;
                    case REMOVECARGO:
                        System.out.println("Podaj numer ładunku");
                        int numb = writeInt();
                        removeCargo(numb);
                        break;
                    case SORTEDWAREHOUSE:
                        System.out.println("Podaj lokalizacje magazynu");
                        String localization = writeString();
                        wypiszPosortowaneData(localization);
                        break;
                    case CARGOTODIFFERENTWAREHOUSE:
                        System.out.println("Podaj lokalizację docelowego magazynu");
                        String cargoToDifferentWarehouseLocalization = writeString();
                        System.out.println("Podaj numer ładunku");
                        int cargoToDifferentWarehouseNumber = writeInt();
                        cargoToDifferentWarehouse(cargoToDifferentWarehouseNumber,cargoToDifferentWarehouseLocalization);
                        break;
                    case ALLWAREHOUSES:
                        unsubscribeAllWarehouses();
                        break;
                    case ALLCARGOS:
                        unsubscribeAllCargos();
                        break;
                    case EXIT:
                        break;
                    default:
                        System.out.println("Brak opcji o podanym numerze, spróbuj ponownie");
                        break;
                }
            }
        }finally {
            //zapisDoCSV(); //Dodatkowa opcja primitywna, przy zmianie zmień też odczyt i zakomentuj serializowany zapis i odczyt
            zapisSerialization();
        }
    }
    //Menu wyboru
    private void mainMenu(){
        System.out.println("\n1.Wyświetl wszystkie ładunki w podanym magazynie");
        System.out.println("2.Dodaj magazyn");
        System.out.println("3.Dodaj ładunek do magazynu");
        System.out.println("4.Wyświetl szczegóły ładunku");
        System.out.println("5.Wyświetl wszystkie ładunki z wybranej kategorii");
        System.out.println("6.Wypisz prawie pełne magazyny");
        System.out.println("7.Wypisz prawie puste magazyny");
        System.out.println("8.Usuń ładunek o wybranym numerze");
        System.out.println("9.Wypisz wszystkie ładunki wybranego magazynu posortowane według daty przybycia");
        System.out.println("10.Przeniesienie ładunku do innego magazynu");
        System.out.println("11.Wyświetl wszystkie magazyny");
        System.out.println("12.Wyświetl wszystkie ładunki");
        System.out.println("0.Exit");
        System.out.print("Wybierz podając numer:");
    }

    //Metoda opcji dla switcha
    private int option(int number){
        boolean optionOk = false;
        while(!optionOk) {
            try {
                number = writeInt();
                optionOk = true;
            }catch (InputMismatchException e){
                System.err.println("Wprowadzono wartość, która nie jest liczbą, podaj ponownie: ");
            }
        }
        return number;
    }

    //Dodaje nowy magazyn do listy, z podaniem jego lokalizacji
    private void addWarehouse(){
        System.out.println("Podaj lokalizacje nowo tworzonego magazynu");
        boolean isOk;
        String location = "";
        do{
            isOk = true;
            location = writeString();
            for (int i = 0; i < warehouses.size(); i++) {
                if (warehouses.get(i).getLocation().equalsIgnoreCase(location)){
                    isOk = false;
                    System.out.println("Magazyn o podanej lokalizacji już istnieje, spróbuj ponownie.");
                }
            }
        }while(!isOk);
        warehouses.add(new Warehouse(location));
    }

    //Wyświetlenie wszystkich magazynów
    private void unsubscribeAllWarehouses(){
        warehouses.forEach(System.out::println);
    }

    //Wypisanie wszyskich ładunków spośród wszystkich magazynów
    private void unsubscribeAllCargos(){
        //Opcja 1
        //warehouses.forEach(e -> e.getCargos().forEach(System.out::println)); //Bez sortowania według nr. ładunku

        //Opcja 2 //Posortowane według nr.ładunku, poprzez przeniesienie wszyskich ładunków do nowej listy i podmienienie indeksów na zgodne z numerem ID
        int actually = 0;
        List<Cargo> list = new ArrayList<>();

        for (int i = 0; i < warehouses.size(); i++) {
            list.addAll(warehouses.get(i).getCargos());
        }

        for (int i = 0; i < warehouses.size(); i++) {
            for (int j = 0; j < warehouses.get(i).getCargos().size(); j++) {
                actually = warehouses.get(i).getCargos().get(j).getID();
                list.set(actually,warehouses.get(i).getCargos().get(j));
            }
        }
        list.forEach(System.out::println);
    }

    // Wypisuje szczegóły wybranego ładunku
    private void unsubscribeCargo(){
        System.out.println("Podaj nr ładunku, którego chcesz wyświetlić wszystkie szczegóły");
        boolean x = false;
        int cargoNumber = writeInt();

        for (int i = 0; i < warehouses.size(); i++) {
            for (int j = 0; j < warehouses.get(i).getCargos().size(); j++) {
                if(warehouses.get(i).getCargos().get(j).getID() == cargoNumber){
                    System.out.println(warehouses.get(i).getCargos().get(j));
                    x = true;
                }
            }
        }
        if(!x){
            System.out.println("Nie ma ładunku o takim numerze.");
        }

    }

    //Podaje wszystkie ładunki znajdujące się w podanym magazynie
    private void wypisz(){
        System.out.println("Podaj lokalizację magazynu");
        boolean x = false;
        String warehouseLocalization = writeString();

        for (int i = 0; i < warehouses.size(); i++) {
            if(warehouses.get(i).getLocation().equalsIgnoreCase(warehouseLocalization)){
                warehouses.get(i).getCargos().forEach(System.out::println);
                x = true;
            }
        }
        if(!x){
            System.out.println("Nie ma magazynu w takiej lokalizacji.");
        }
    }

    // Wypisuje wszystkie ładunki o podanej kategorii spośród wszystkich magazynów
    private void unsubscribeCategory(){
        String kategoria = checkIsCategory();
        System.out.println("Lista ładunków z kategorii " + kategoria);

        for (Warehouse x: warehouses) {
            for (int i = 0; i < x.getCargos().size(); i++) {
                if (x.getCargos().get(i).getCategory().toString().equalsIgnoreCase(kategoria)){
                    System.out.println(x.getCargos().get(i));
                }
            }
        }
    }

    // Wypisuje wszystkie ładunki o podanej kategirii znajdujące się w magazynie o podanej lokalizacji
    private void unsubscribeCategory(String kategoria,String warehouseLocalization){
        System.out.println("Lista ładunków z kategorii " + kategoria);
        int i = 0;
        boolean y = false;

        for (Warehouse x: warehouses) {
            if(x.getLocation().equalsIgnoreCase(warehouseLocalization)){
                if (x.getCargos().get(i).getCategory().toString().equalsIgnoreCase(kategoria)){
                    System.out.println(x.getCargos().get(i));
                    y = true;
                }
            }
            i++;
        }
        if(!y){
            System.out.println("Nie ma magazynu w podanej lokalizacji");
        }

    }

    //Wypisuje ładunki z podanego magazynu posortowane według daty przybycia
    private void wypiszPosortowaneData(String warehouseLocalization){
        boolean x = false;
        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).getLocation().equalsIgnoreCase(warehouseLocalization)){
                warehouses.get(i).getCargos().sort(Comparator.comparing(Cargo::getArrivalDate));
                warehouses.get(i).getCargos().forEach(System.out::println);
                x = true;
            }
        }
        if(!x){
            System.out.println("Nie ma magazynu w podanej lokalizacji.");
        }
    }

    //Odczyt pliku posiadającego Stringi, a nie obiekty i na ich podstawie odtwarzanie magazynów oraz ich zawartości poprzez tworzenie ich obiektów i przekazywanie wartości
    //Uzupełnianie listy obiektów w prymitywny sposób, dla ćwiczenia
    private void odczytCSV(){
        String line = "";
        String[] zapas;
        String locationWarehouse;
        double weightWarehouse;
        int idCargo;
        String categoryCargo;
        String descriptionCargo;
        double weightPackageCargo;
        int numberOfPackagesCargo;
        String assignedWarehouseCargo;
        int dayCargo;
        int monthCargo;
        int yearCargo;


        int x = 0;
        try(FileReader fileReader = new FileReader(FILENAME);
        BufferedReader bufferedReader = new BufferedReader(fileReader)){
            if((line = bufferedReader.readLine()) != null){
                zapas = line.split(" ");
                for (int i = 0; i < zapas.length; i=i+2) {
                    locationWarehouse = zapas[i];
                    warehouses.add(new Warehouse(locationWarehouse));
                    weightWarehouse = Double.parseDouble(zapas[i+1]);
                    warehouses.get(x).setWeight(weightWarehouse);
                    x++;
                }
            }
            while((line = bufferedReader.readLine()) != null){
                zapas = line.split(" ");
                idCargo = Integer.parseInt(zapas[0]);
                categoryCargo = zapas[1];
                descriptionCargo = zapas[2];
                weightPackageCargo = Double.parseDouble(zapas[3]);
                numberOfPackagesCargo = Integer.parseInt(zapas[4]);
                assignedWarehouseCargo = zapas[5];
                dayCargo = Integer.parseInt(zapas[6]);
                monthCargo = Integer.parseInt(zapas[7]);
                yearCargo = Integer.parseInt(zapas[8]);
                for (int i = 0; i < warehouses.size(); i++) {
                    if (warehouses.get(i).getLocation().equalsIgnoreCase(assignedWarehouseCargo)){
                        Category category = Category.valueOf(categoryCargo);
                        warehouses.get(i).getCargos().add(new Cargo(category,descriptionCargo,weightPackageCargo,numberOfPackagesCargo,yearCargo,monthCargo,dayCargo,warehouses.get(i),idCargo));
                    }
                }

            }
        }catch (FileNotFoundException e){
            new File(FILENAME);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //Zapis magazynów i ich zawartości do pliku w postaci Stringów
    private void zapisDoCSV(){
        try(FileWriter fileWriter = new FileWriter(FILENAME)){
            for (Warehouse x: warehouses) {
                fileWriter.append(x.getLocation() + " " + x.getWeight() + " ");
            }
            fileWriter.append("\n");
            for (Warehouse x : warehouses) {
                fileWriter.append(x.toCSVSave());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Przenosi ładunek dodając ładunek o wybranym numerze do wybranego magazynu i usuwając z poprzedniego
    private void cargoToDifferentWarehouse(int index, String toWarehouseLocalization){
        boolean x = false;
        boolean is = false;
        int fromIndex = 0;
        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).getLocation().equalsIgnoreCase(toWarehouseLocalization)) {
                is = true;
                fromIndex = i;
            }
        }
        for (int i = 0; i < warehouses.size(); i++) {
                for (int j = 0; j < warehouses.get(i).getCargos().size(); j++) {
                    if (warehouses.get(i).getCargos().get(j).getID() == index && is) {
                        double weight = (warehouses.get(i).getCargos().get(j).getNumberOfPackages() * warehouses.get(i).getCargos().get(j).getMassOfSinglePackage());
                        if(warehouses.get(fromIndex).getCAPACITY() > warehouses.get(fromIndex).getWeight() + weight) {
                            warehouses.get(i).getCargos().get(j).setAssignedWarehouse(warehouses.get(fromIndex)); //Zmienia przypisany magazyn do ładunku na nowy
                            warehouses.get(fromIndex).getCargos().add(warehouses.get(i).getCargos().get(j)); //Dodaje w docelowym magazynie wybrany ładunek
                            warehouses.get(fromIndex).setWeight(warehouses.get(fromIndex).getWeight() + weight);
                            warehouses.get(i).setWeight(warehouses.get(i).getWeight() - weight);
                            warehouses.get(i).getCargos().remove(warehouses.get(i).getCargos().get(j)); //Usuwa ładunek z poprzedniego magazynu
                            x = true;
                            System.out.println("Przeniesiono ładunek nr." + index + " do magazynu o lokalizacji " + toWarehouseLocalization);
                        }else{
                            System.out.println("Nie można przenieść wybranego ładunku do tej lokalizacji, ponieważ pojemność tego magazynu jest niewystarczająca dla tego ładunku");
                        }
                    }
                }
        }
        if(!x && is){
            System.out.println("Nie ma ładunku z podanym numerem");
        }
        if(!is){
            System.out.println("Nie ma magazynu w podanej lokalizacji");
        }
    }

    //Odczyt pliku posiadającego obiekty, odczytuje zapisaną w pliku listę magazynów
    private void odczytSerialization(){
        final String FILENAME2 = "plikkk.csv";

        try(FileInputStream fileInputStream = new FileInputStream(FILENAME2);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ){
                warehouses = (ArrayList<Warehouse>) objectInputStream.readObject();

        } catch (FileNotFoundException ex){

        } catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }

    //Zapisuje w pliku listę magazynów
    private void zapisSerialization(){
        final String FILENAME2 = "plikkk.csv";
        try(FileOutputStream fileOutputStream = new FileOutputStream(FILENAME2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ){
            objectOutputStream.writeObject(warehouses);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //Usuwa ładunek o podanym indeksie
    private void removeCargo(int index){
        boolean x = false;
        for (int i = 0; i < warehouses.size(); i++) {
            for (int j = 0; j < warehouses.get(i).getCargos().size() ; j++) {
                if (warehouses.get(i).getCargos().get(j).getID() == index){
                    warehouses.get(i).setWeight(warehouses.get(i).getWeight() - (warehouses.get(i).getCargos().get(index).getNumberOfPackages() * warehouses.get(i).getCargos().get(index).getMassOfSinglePackage()));
                    warehouses.get(i).getCargos().remove(index);
                    x = true;
                    System.out.println("Usunięto ładunek z magazynu o numerze" + index);
                }
            }
        }
        if(!x){
            System.out.println("Nie ma ładunku o takim numerze");
        }

    }

    //Wypisuje prawie pełne magazyny
    private void unsubscribeNearlyFull(){
        System.out.println("Lista prawie pełnych magazynów powyżej 80%");
        for (Warehouse x: warehouses) {
            if (x.getWeight() > ((x.getCAPACITY()* 80)/100)){
                System.out.println(x);
            }
        }
    }

    //Wypisuje prawie puste magazyny
    private void unsubscribeNearlyEmpty(){
        System.out.println("Lista prawie pustych magazynów poniżej 20%");
        for (Warehouse x: warehouses) {
            if (x.getWeight() < ((x.getCAPACITY()* 20)/100)){
                System.out.println(x);
            }
        }
    }

    //Dodaje ładunek do magazynu
    private void addCargoToWarehouse(){
        System.out.println("Wybierz kategorię podając jej nazwę: \n1: INDUSTRIAL, \n2: RAW, \n3: MATERIAL, \n4: MERCHANDISE, \n5: OTHER");
        String kategoria = checkIsCategory();
        Category category = Category.valueOf(kategoria);
        System.out.println("Podaj szczegółowy opis produktu");
        String description = writeString();
        System.out.println("Podaj masę pojedyńczej paczki");
        double mass = writeDouble();
        System.out.println("Podaj ilość paczek");
        int numberOfPackage = writeInt();
        System.out.println("Wprowadź datę dodania ładunku");
        System.out.println("Podaj rok");
        int year = writeInt();
        System.out.println("Podaj miesiąc");
        int month = checkIsMonth();
        System.out.println("Podaj dzień");
        int day = checkIsDay(month);
        System.out.println("Lista lokalizacji dostępnych magazynów:");
        warehouses.forEach(System.out::println);
        System.out.println("Podaj lokalizację magazynu w którym ma znaleźć się ładunek");
        String magazyn = writeString();
        boolean isss = false;
        double weight;

            for (int i = 0; i < warehouses.size(); i++) {

                if (magazyn.equalsIgnoreCase(warehouses.get(i).getLocation())) {
                    weight = warehouses.get(i).getWeight() + (mass * numberOfPackage);
                    if(warehouses.get(i).getCAPACITY() > weight) {
                        warehouses.get(i).setCargos(new Cargo(category, description, mass, numberOfPackage, year, month, day, warehouses.get(i)));
                        warehouses.get(i).setWeight(weight);
                        isss = true;
                    }else{
                        System.out.println("Podany magazyn nie jest w stanie przyjąć tego ładunku z powodu niewystarczającej pojemności.");
                    }
                }
            }
            if (!isss) {
                System.out.println("Nie ma takiej lokalizacji");
            }
    }

    //Sprawdza czy podaliśmy prawidłową liczbę jako dzień czyli pomiędzy 1 a 31, w zależności od miesiąca
    private int checkIsDay(int month){
        int day = 0;
        boolean isOk;
        do {
            isOk = true;
            day = writeInt();
            if(month == 1 || month ==3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                if(day < 1 || day > 31){
                    System.out.println("Podano nieprawidłową date dla tego miesiąca, spróbuj ponownie.");
                    isOk = false;
                }
            }else if (month == 4 || month == 6 || month == 9 || month == 11){
                if(day < 1 || day > 30){
                    System.out.println("Podano nieprawidłową datę dla tego miesiąca, spróbuj ponownie.");
                    isOk = false;
                }
            }else if(month == 2){
                if (day < 1 || day > 29){
                    System.out.println("Podano nieprawidłową datę dla tego miesiąca, spróbuj ponownie.");
                    isOk = false;
                }
            }
        }while (!isOk);
        return day;
    }

    //Sprawdza czy podaliśmy prawidłową liczbę jako miesiąc czyli pomiędzy 1 a 12
    private int checkIsMonth(){
        int month = 0;
        boolean isOk;
        do{
            isOk = true;
            month = writeInt();
            if(month < 1 || month > 12){
                System.out.println("Podaj prawidłowy format miesiąca");
                isOk = false;
            }
        }while (!isOk);
        return month;
    }

    //Metoda do wpisania tekstu przez użytkownika jako String
    private String writeString(){
        boolean is = false;
        String string ="";
        while(!is) {
            string = in.nextLine().trim();
            if (string.equalsIgnoreCase("")){
                System.out.println("Wymagany jest napis, spróbuj ponownie");
            }else{
                is = true;
            }
        }
        return string;
    }

    //Metoda do wpisania tekstu przez użytkowanika jako int
    private int writeInt(){
        int x = 0;
        boolean y = false;
        while (!y) {
            try {
                x = in.nextInt();
                in.nextLine();
                y = true;
            } catch (InputMismatchException e) {
                System.out.println("Wprowadzono wartość która nie jest liczbą, podaj ponowanie");
                in.next();
            }
        }
        return x;
    }

    //Metoda do wpisania tekstu przez użytkowanika jako double
    private double writeDouble(){
        double x = 0;
        boolean y = false;
        while (!y) {
            try {
                x = in.nextDouble();
                in.nextLine();
                y = true;
            } catch (InputMismatchException e) {
                System.out.println("Wprowadzono wartość która nie jest liczbą");
            }
        }
        return x;
    }

    //Metoda ustawiająca pole statyczne ładunku na prawidłową wartość po zapisie i odczycie pliku
    private void setMaxCargoNumber(){
        int max = 0;
        int actually;
        for (int i = 0; i < warehouses.size(); i++) {
            for (int j = 0; j < warehouses.get(i).getCargos().size(); j++) {
                 actually = warehouses.get(i).getCargos().get(j).getID();
                 if(actually > max){
                     max = actually;
                 }
            }
        }
        if(max != 0) {
            Cargo.setIdNumber(max + 1);
        }
    }

    //Pobiera od użytkownika kategorię oraz sprawdza czy istnieje taka w programie
    private String checkIsCategory(){
        System.out.println("Podaj kategorie");
        String category = "";
        String wartosci = Arrays.toString(Category.values());
        boolean isOk;
        do {
            isOk = true;
            category = writeString();
            category = category.toUpperCase();
            if(!wartosci.contains(category)){
                isOk = false;
                System.out.println("Nie ma takiej kategorii, wybierz jakąś spośród: " + wartosci);
            }
        }while (!isOk);
        return category;
    }
}
