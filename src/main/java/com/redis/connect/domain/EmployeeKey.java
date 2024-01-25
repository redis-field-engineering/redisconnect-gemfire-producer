package com.redis.connect.domain;

import java.io.Serializable;

public class EmployeeKey implements Serializable {
    private static final long serialVersionUID = 160372860L;
    private final String name;
    private final int empNumber;

    public EmployeeKey(String name, int empNumber) {
        this.name = name;
        this.empNumber = empNumber;
    }

    public String getName() {
        return name;
    }

    public int getEmpNumber() {
        return empNumber;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + empNumber;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmployeeKey other = (EmployeeKey) obj;
        if (empNumber != other.empNumber)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EmployeeKey [name=" + name + ", empNumber=" + empNumber + "]";
    }
}
