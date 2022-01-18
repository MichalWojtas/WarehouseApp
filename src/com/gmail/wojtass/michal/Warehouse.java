package com.gmail.wojtass.michal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Warehouse implements Serializable {
    private String location;
    private final double CAPACITY = 100.0;
    private double weight;
    private List<Cargo> cargos = new ArrayList<>();

    public Warehouse(String location){
        this.location = location;
    }

    public void setCargos(Cargo cargo) {
        cargos.add(cargo);
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public String getLocation() {
        return location;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public double getCAPACITY() {
        return CAPACITY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Double.compare(warehouse.CAPACITY, CAPACITY) == 0 && Double.compare(warehouse.weight, weight) == 0 && Objects.equals(location, warehouse.location) && Objects.equals(cargos, warehouse.cargos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, CAPACITY, weight, cargos);
    }

    @Override
    public String toString() {
        return location;
    }

    public String toCSVSave(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cargos.size(); i++) {
            stringBuilder.append(cargos.get(i).getID() + " " + cargos.get(i).getCategory().toString() + " " + cargos.get(i).getSpecificDescription() + " " + cargos.get(i).getMassOfSinglePackage()
             + " " + cargos.get(i).getNumberOfPackages() + " " + cargos.get(i).getAssignedWarehouse().getLocation() + " " + cargos.get(i).getArrivalDate().getDayOfMonth() + " " + cargos.get(i).getArrivalDate()
                    .getMonthValue() + " " + cargos.get(i).getArrivalDate().getYear() + "\n");
        }
        return stringBuilder.toString();
    }
}
