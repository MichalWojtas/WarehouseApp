package com.gmail.wojtass.michal;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Cargo  implements Serializable {
    private static int idNumber = 0;
    private final int ID;
    private Category category;
    private String specificDescription;
    private double massOfSinglePackage;
    private int numberOfPackages;
    private Warehouse assignedWarehouse;
    private LocalDate arrivalDate;

    public Cargo(Category category, String specificDescription, double massOfSinglePackage, int numberOfPackages, int year, int month, int day, Warehouse assignedWarehouse){
        this.category = category;
        this.specificDescription = specificDescription;
        this.massOfSinglePackage = massOfSinglePackage;
        this.numberOfPackages = numberOfPackages;
        this.arrivalDate = LocalDate.of(year,month,day);
        this.ID = idNumber;
        idNumber++;
        this.assignedWarehouse = assignedWarehouse;
    }

    public Cargo(Category category, String specificDescription, double massOfSinglePackage, int numberOfPackages, int year, int month, int day, Warehouse assignedWarehouse, int ID){
        this.category = category;
        this.specificDescription = specificDescription;
        this.massOfSinglePackage = massOfSinglePackage;
        this.numberOfPackages = numberOfPackages;
        this.arrivalDate = LocalDate.of(year,month,day);
        this.ID = ID;
        idNumber++;
        this.assignedWarehouse = assignedWarehouse;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public Category getCategory() {
        return category;
    }

    public String getSpecificDescription() {
        return specificDescription;
    }

    public double getMassOfSinglePackage() {
        return massOfSinglePackage;
    }

    public int getNumberOfPackages() {
        return numberOfPackages;
    }

    public int getID() {
        return ID;
    }

    public Warehouse getAssignedWarehouse() {
        return assignedWarehouse;
    }

    public void setAssignedWarehouse(Warehouse assignedWarehouse) {
        this.assignedWarehouse = assignedWarehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return ID == cargo.ID && Double.compare(cargo.massOfSinglePackage, massOfSinglePackage) == 0 && numberOfPackages == cargo.numberOfPackages && category == cargo.category && Objects.equals(specificDescription, cargo.specificDescription) && Objects.equals(assignedWarehouse, cargo.assignedWarehouse) && Objects.equals(arrivalDate, cargo.arrivalDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, category, specificDescription, massOfSinglePackage, numberOfPackages, assignedWarehouse, arrivalDate);
    }

    @Override
    public String toString() {
        return "Nr ładunku = " + ID + "\nKategoria = " + category + ", opis = " + specificDescription +
                ", waga pojedyńczej paczki = " + massOfSinglePackage + ", ilość paczek = " + numberOfPackages +
                ",\nData przybycia ładunku do magazynu = " + arrivalDate + ", przypisany magazyn = " + assignedWarehouse;
    }

    public static void setIdNumber(int idNumber) {
        Cargo.idNumber = idNumber;
    }
}
