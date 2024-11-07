package usfca.pyne.cs601.virtualfridge.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;


@Setter
@Getter
@Entity
@Table(name = "ingredients", uniqueConstraints = @UniqueConstraint(columnNames = {"fridge_id", "name"}))
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

    @Setter
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "fridge_id", nullable = false)
    @JsonBackReference
    private Fridge fridge;


    @JsonCreator
    public Ingredient(
            @JsonProperty("name") String name,
            @JsonProperty("amount") double amount,
            @JsonProperty("unit") String unit,
            @JsonProperty("expirationDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy") LocalDate expirationDate) {
        this.name = name.toLowerCase();
        this.amount = amount;
        this.unit = unit;
        this.expirationDate = expirationDate;
    }

    public Ingredient() {
    }

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