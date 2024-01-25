package com.redis.connect.domain;

import java.io.Serializable;

public class EmployeeData implements Serializable {
    private static final long serialVersionUID = 2095541179L;
    private final EmployeeKey nameAndNumber;
    private final int salary;
    private final int hoursPerWeek;

    public EmployeeData(EmployeeKey nameAndNumber, int salary, int hoursPerWeek) {
        this.nameAndNumber = nameAndNumber;
        this.salary = salary;
        this.hoursPerWeek = hoursPerWeek;
    }

    public EmployeeKey getNameAndNumber() {
        return nameAndNumber;
    }

    public int getSalary() {
        return salary;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    @Override
    public String toString() {
        return "EmployeeData [nameAndNumber=" + nameAndNumber + ", salary=" + salary + ", hoursPerWeek="
                + hoursPerWeek + "]";
    }
}
