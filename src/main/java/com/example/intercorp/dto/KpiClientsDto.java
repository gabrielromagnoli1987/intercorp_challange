package com.example.intercorp.dto;

public class KpiClientsDto {
    private double averageAge;
    private double standardDeviation;

    public KpiClientsDto() {
    }

    public KpiClientsDto(double averageAge, double standardDeviation) {
        this.averageAge = averageAge;
        this.standardDeviation = standardDeviation;
    }

    public double getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
}
