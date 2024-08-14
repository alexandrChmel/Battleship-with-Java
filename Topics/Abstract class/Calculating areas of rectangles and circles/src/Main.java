import java.util.Scanner;

// Define the abstract Shape class with an abstract area() method
abstract class Shape {
    public abstract void area();
}

class Rectangle extends Shape {
    double length;
    double width;

    @Override
    public void area() {
        System.out.println(length * width);
    }
    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

}

class Circle extends Shape {
    double r;
    @Override

    public void area() {
        System.out.println(r * r * Math.PI);
    }
    Circle(double r) {
        this.r = r;
    }

}

// Implement the Rectangle class that extends Shape

// Implement the Circle class that extends Shape

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read the dimensions of the rectangle
        double length = scanner.nextDouble();
        double width = scanner.nextDouble();

        // Read the radius of the circle
        double radius = scanner.nextDouble();

        // Create instances of Rectangle and Circle
        Shape rec = new Rectangle(length, width);
        Shape c = new Circle(radius);


        // Calculate and print the area of the rectangle
        rec.area();

        // Calculate and print the area of the circle
        c.area();
        scanner.close();
    }
}