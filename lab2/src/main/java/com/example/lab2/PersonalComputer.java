package com.example.lab2;

import java.io.Serializable;

public class PersonalComputer implements Serializable {
    private Integer id;
    private Double cpuSpeedGHz;
    private Integer ramSizeGB;
    private RAM_TYPE ramType;
    private Boolean isLaptop;
    private CPU cpuBrand;
    private Integer storageGB;
    private GPU graphicCard;
    private Boolean hasWifi;
    private STORAGE_TYPE storageType;
    private Integer powerConsumption;
    private Double price;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != null) {
            this.id = id;
        }
    }

    public double getCpuSpeedGHz() {
        return cpuSpeedGHz;
    }

    public void setCpuSpeedGHz(Double cpuSpeedGHz) {
        if (cpuSpeedGHz != null && !cpuSpeedGHz.equals(-1.0)) {
            if (cpuSpeedGHz > 0.1 && cpuSpeedGHz < 10) {
                this.cpuSpeedGHz = cpuSpeedGHz;
            } else {
                throw new RuntimeException("Such CPU Frequency is not possible: " + cpuSpeedGHz);
            }
        }

    }

    public int getRamSizeGB() {
        return ramSizeGB;
    }

    public void setRamSizeGB(Integer ramSizeGB) {
        if (ramSizeGB != null && !ramSizeGB.equals(-1)) {
            if (ramSizeGB > 1 && ramSizeGB < 256) {
                this.ramSizeGB = ramSizeGB;
            } else {
                throw new RuntimeException("Such RAM capacity is not possible: " + ramSizeGB);
            }
        }

    }

    public RAM_TYPE getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        if (ramType != null && !ramType.equals("/")) {
            try {
                this.ramType = RAM_TYPE.valueOf(ramType.toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("No such RAM type: " + ramType);
            }
        }
    }

    public boolean isLaptop() {
        return isLaptop;
    }

    public void setLaptop(Boolean laptop) {
        if (laptop != null) {
            isLaptop = laptop;
        }
    }

    public CPU getCpuBrand() {
        return cpuBrand;
    }

    public void setCpuBrand(String cpuBrand) {
        if (cpuBrand != null && !cpuBrand.equals("/")) {
            try {
                this.cpuBrand = CPU.valueOf(cpuBrand.toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("No such CPU brand: " + cpuBrand);
            }
        }
    }

    public int getStorageGB() {
        return storageGB;
    }

    public void setStorageGB(Integer storageGB) {
        if (storageGB != null && !storageGB.equals(-1)) {
            if (storageGB > 10 && storageGB < 100000) {
                this.storageGB = storageGB;
            } else {
                throw new RuntimeException("Such storage capacity is not possible: " + storageGB);
            }
        }
    }

    public GPU getGraphicCard() {
        return graphicCard;
    }

    public void setGraphicCard(String graphicCard) {
        if (graphicCard != null && !graphicCard.equals("/")) {
            try {
                this.graphicCard = GPU.valueOf(graphicCard.toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("No such GPU brand: " + graphicCard);
            }
        }
    }

    public boolean isHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        if (hasWifi != null) {
            this.hasWifi = hasWifi;
        }
    }

    public STORAGE_TYPE getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        if (storageType != null && !storageType.equals("/")) {
            try {
                this.storageType = STORAGE_TYPE.valueOf(storageType.toUpperCase());
            } catch (Exception e) {
                throw new RuntimeException("No such Storage type: " + storageType);
            }
        }
    }

    public int getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(Integer powerConsumption) {
        if (powerConsumption != null && !powerConsumption.equals(-1)) {
            if (powerConsumption > 100 && powerConsumption < 2000) {
                this.powerConsumption = powerConsumption;
            } else {
                throw new RuntimeException("Such power consumption is not possible: " + powerConsumption);
            }
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        if (price != null && !price.equals(-1.0)) {
            if (price > 100) {
                this.price = price;
            } else {
                throw new RuntimeException("Such price is not possible: " + price);
            }
        }
    }

    @Override
    public String toString() {
        return "PersonalComputer{" +
                "\n id=" + id +
                ",\n cpuSpeedGHz=" + cpuSpeedGHz +
                ",\n ramSizeGB=" + ramSizeGB +
                ",\n ramType=" + ramType +
                ",\n isLaptop=" + isLaptop +
                ",\n cpuBrand=" + cpuBrand +
                ",\n storageGB=" + storageGB +
                ",\n graphicCard=" + graphicCard +
                ",\n hasWifi=" + hasWifi +
                ",\n storageType=" + storageType +
                ",\n powerConsumption=" + powerConsumption +
                ",\n price=" + price +
                "\n }";
    }
}

enum CPU {
    INTEL,
    AMD,
    APPLE
}

enum GPU {
    NVIDIA,
    AMD,
    INTEL
}

enum STORAGE_TYPE {
    SSD,
    HDD,
    COMBINED
}

enum RAM_TYPE {
    DDR3,
    DDR4,
    DDR5
}




