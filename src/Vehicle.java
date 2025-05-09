public class Vehicle {
    private int vin;
    private int year;
    private String make;
    private String model;
    private String type;
    private String color;
    private int odometer;
    private double price;

    //CONSTRUCTOR
    public Vehicle(int vin, int year, String make, String model, String Type, String color, int odometer, double price) {
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.type = Type;
        this.color = color;
        this.odometer = odometer;
        this.price = price;
    }
    //GETTERS
    public int getVin(){return this.vin;}
    public void setVin(int vin){this.vin = vin;}

    public int getYear(){return this.year;}
    public void setYear(int year){this.year = year;}

    public String getMake(){return this.make;}
    public void setMake(String make){this.make = make;}

    public String getModel(){return this.model;}
    public void getModel(String model){this.model = model;}

    public String getType(){return this.type;}
    public void setType(String type){this.type = type;}

    public String getColor(){return this.color;}
    public void setColor(String color){this.color = color;}

    public int getOdometer(){return this.odometer;}
    public void setOdometer(int odometer){this.odometer = odometer;}

    public double getPrice(){return this.price;}
    public void setPrice(double price){this.price = price;}
}