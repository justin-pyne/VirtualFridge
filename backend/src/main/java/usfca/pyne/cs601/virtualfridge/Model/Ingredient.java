package usfca.pyne.cs601.virtualfridge.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @NotNull
    private String unit;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
//    private LocalDate expirationDate;


    @JsonCreator
    public Ingredient(
            @JsonProperty("name") String name,
            @JsonProperty("amount") double amount,
            @JsonProperty("unit") String unit) {
//            @JsonProperty("expirationDate") LocalDate expirationDate
        this.name = name.toLowerCase();
        this.amount = amount;
        this.unit = unit;
    }

    public Ingredient() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

//    public LocalDate getExpirationDate() {
//        return expirationDate;
//    }
//
//    public void setExpirationDate(LocalDate expirationDate) {
//        this.expirationDate = expirationDate;
//    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                '}';
    }
}