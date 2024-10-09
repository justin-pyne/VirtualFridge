package usfca.pyne.cs601.virtualfridge.Model;

public class Ingredient {

    //@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private String name;
    private double amount;
    private String unit;


    public Ingredient(String name) {
//        this.id = id;
        this.name = name;
    }

//    public Long getId() {
//        return id;
//    }

    public String getName() {
        return name;
    }
}