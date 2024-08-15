package battleship;
import java.awt.geom.IllegalPathStateException;
import java.util.Scanner;

public class Main {
    public static boolean player = true;
    public static int playerNum = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] shipCount = new int[]{5, 4, 3, 3, 2};

        String[][] field1 = createField();
        String[][] fogField1 = createField();
        String[][] field2 = createField();
        String[][] fogField2 = createField();

        System.out.println();
        System.out.println("Player " + playerNum + ", place your ships on the game field\n");
        picking(field1);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        clearScreen();

        setPlayerNum();
        System.out.println("Player " + playerNum + ", place your ships to the game field\n");
        picking(field2);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        clearScreen();

        System.out.println("The game starts\n");

        // ------ guessing -------
        while (true) {
            if (guessing(field2, fogField2, field1) == false) {
                break;
            }
            if (guessing(field1, fogField1, field2) == false) {
                break;
            }
        }
    }

    public static boolean guessing(String[][] field, String[][] fogField, String[][] yourField) {
        Scanner scanner = new Scanner(System.in);
        player = !player;
        setPlayerNum();
        while (true) {
            try {
                printField(fogField);
                System.out.println("---------------------");
                printField(yourField);
                System.out.println();
                System.out.println("Player " + playerNum + ", it's your turn:\n");
                String shot = scanner.next();
                System.out.println();
                shoot(field, fogField, shot, yourField);
                if (lastShip(field)) {
                    return false;
                }
                System.out.println();
                break;
            } catch (Exception e) {
                System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                String shot = scanner.next();
                System.out.println();
                shoot(field, fogField, shot, yourField);
            }
        }
        return true;

    }

    public static String[][] picking(String[][] field) {
        Scanner scanner = new Scanner(System.in);
        String[] shipNoCells = new String[]{"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        String[] ship = new String[]{"Aircraft Carrier (5 cells)", "Battleship (4 cells)", "Submarine (3 cells)", "Cruiser (3 cells)", "Destroyer (2 cells)"};
        printField(field);
        System.out.println();
        int count = 0;
        int k = 0;
        boolean error = false;
        while (true) {
            try {
                if (!error) {
                    System.out.printf("Enter the coordinates of the %s:\n", ship[count]);
                    System.out.println();
                } else {
                    error = false;
                }
                String coordinates = scanner.nextLine();
                reserve(coordinates, field, count);
                count++;
                k++;
                printField(field);
                System.out.println();
            } catch (IllegalPathStateException e) {
                System.out.println("Error! Wrong ship location! Try again:");
                System.out.println();
                error = true;
            } catch (IllegalAccessError e) {
                error = true;
                System.out.println("Error! You placed it too close to another one. Try again:");
                System.out.println();
            } catch (Exception e) {
                System.out.println();
                System.out.printf("Error! Wrong length of the %s! Try again:\n", shipNoCells[count]);
                System.out.println();
                error = true;
            }
            if (count == 5) {
                break;
            }
        }
        return field;
    }

    public static boolean hitLastPart(String[][] field, String[][] fogField, int row, int column){
        boolean hitShip = false;
        boolean hitShipRU = false;
        boolean hitShipRD = false;
        boolean hitShipCL = false;
        boolean hitShipCR = false;
        if (isWithinBounds(field, row + 1, column)){
            if (field[row + 1][column].equals("X") || field[row + 1][column].equals("~")){
                hitShipRU = true;
            }
        } else{
            hitShipRU = true;
        }
        if (isWithinBounds(field, row - 1, column)){
            if (field[row - 1][column].equals("X") || field[row - 1][column].equals("~")){
                hitShipRD = true;
            }
        } else{
            hitShipRD = true;
        }
        if (isWithinBounds(field, row, column + 1)){
            if (field[row][column + 1].equals("X") || field[row][column + 1].equals("~")){
                hitShipCR = true;
            }
        } else{
            hitShipCR = true;
        }
        if (isWithinBounds(field, row, column - 1)){
            if (field[row][column - 1].equals("X") || field[row][column - 1].equals("~")){
                hitShipCL = true;
            }
        }  else{
            hitShipCL = true;
        }

        if (hitShipRU && hitShipRD && hitShipCL && hitShipCR){
            hitShip = true;
        }
        return hitShip;
    }

    public static boolean lastShip(String[][] field) {
        boolean isTrue = true;
        boolean stop = false;
        ;
        for (int i = 0; i < field.length && !stop; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].equals("o")) {
                    isTrue = false;
                    stop = true;
                }
            }
        }
        return isTrue;
    }

    public static String[][] fog(String[][] fogField, String[][] field) {
        for (int i = 0; i < fogField.length; i++) {
            for (int j = 0; j < fogField[i].length; j++) {
                if (field[i][j].equals("X")) {
                    fogField[i][j] = "X";
                }
                if (field[i][j].equals("M")) {
                    fogField[i][j] = "M";
                }
            }
        }
        return fogField;
    }

    public static String[][] shoot(String[][] field, String[][] fogField, String shot, String[][] yourField) {
        Scanner scanner = new Scanner(System.in);
        int[] shotCoordinates = inputToArray(shot);
        int row = shotCoordinates[0];
        int column = shotCoordinates[1];
        // ---- controlling -----
        if (row > 10 || column > 10) {
            throw new IllegalArgumentException();
        }
        // ----- setting -----

        if (hitLastPart(field, fogField, row, column)) {
            field[row][column] = "X";
            fog(fogField, field);
            if (lastShip(field)) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                return field;
            } else {
                System.out.println("You sank a ship!");
            }
        } else if (field[row][column].equals("o") || field[row][column].equals("X")) {
            field[row][column] = "X";
            fog(fogField, field);
            System.out.println("You hit a ship!");
        } else {
            field[row][column] = "M";
            fog(fogField, field);
            System.out.println("You missed.");
        }
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        System.out.println("\n");

        return field;
    }


    public static int[] inputToArray(String coordinates) {
        String aToJ = "ABCDEFGHIJ";
        StringBuilder coordinatesFormat = new StringBuilder();

        // ------- converting string input to array -------
        coordinatesFormat.append(coordinates);
        if (coordinates.charAt(0) == ' ') {
            coordinatesFormat.deleteCharAt(0);
        }

        for (int i = 0; i < coordinates.length(); i++) {
            if (Character.isLetter(coordinatesFormat.charAt(i))) {
                coordinatesFormat.insert(i + 1, " ");

            }
        }

        for (int i = 0; i < coordinatesFormat.length(); i++) {

            for (int j = 0; j < aToJ.length(); j++) {
                if (coordinatesFormat.charAt(i) == aToJ.charAt(j)) {
                    coordinatesFormat.deleteCharAt(i);
                    coordinatesFormat.insert(i, j + 1);
                }
            }
        }

        // ------ int array from String ------
        String[] coordinatesArr = coordinatesFormat.toString().split(" ");
        int[] coordinatesInt = new int[coordinatesArr.length];
        int[] noSwapCoordinatesInt = new int[coordinatesArr.length];

        for (int i = 0; i < coordinatesInt.length; i++) {
            coordinatesInt[i] = Integer.parseInt(coordinatesArr[i]) - 1;
            noSwapCoordinatesInt[i] = Integer.parseInt(coordinatesArr[i]) - 1;
        }
        return coordinatesInt;

    }

    public static String[][] reserve(String coordinates, String[][] field, int k) {
        // ------ swapping integers -------
        int[] coordinatesInt = inputToArray(coordinates);

        if (coordinatesInt[0] > coordinatesInt[2]) {
            int temp = coordinatesInt[0];
            coordinatesInt[0] = coordinatesInt[2];
            coordinatesInt[2] = temp;
        }
        if (coordinatesInt[1] > coordinatesInt[3]) {
            int temp = coordinatesInt[1];
            coordinatesInt[1] = coordinatesInt[3];
            coordinatesInt[3] = temp;
        }
        int firstRow = coordinatesInt[0];
        int firstColumn = coordinatesInt[1];
        int secondRow = coordinatesInt[2];
        int secondColumn = coordinatesInt[3];

        // ------ calculating length ------
        int upLength = secondRow - firstRow;
        int sideLength = secondColumn - firstColumn;
        if (upLength > 0 && sideLength > 0) {
            throw new IllegalArgumentException();
        }
        int length;
        if (Math.abs(upLength) > Math.abs(sideLength)) {
            length = Math.abs(upLength) + 1;
        } else {
            length = Math.abs(sideLength) + 1;
        }
        int shouldLength = 5 - k;

        if ((shouldLength == 2 && length == 3) || (shouldLength == 1 && length == 2)) {
            shouldLength++;
        }

        if (shouldLength != length) {
            throw new IllegalArgumentException();
        }

// ------- controlling -------
        if (upLength > 0 || sideLength > 0) {
            for (int i = 0; i < length; i++) {

                if (upLength < sideLength) {
                    if (isWithinBounds(field, firstRow, firstColumn + i - 1)) {
                        if (field[firstRow][firstColumn + i - 1].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }
                    if (isWithinBounds(field, firstRow, firstColumn + i + 1)) {
                        if (field[firstRow][firstColumn + i + 1].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }

                    if (isWithinBounds(field, firstRow + 1, firstColumn + i)) {
                        if (field[firstRow + 1][firstColumn + i].equals("o")) {


                            throw new IllegalAccessError();
                        }
                    }
                    if (isWithinBounds(field, firstRow - 1, firstColumn + i)) {
                        if (field[firstRow - 1][firstColumn + i].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }
                } else {
                    if (isWithinBounds(field, firstRow + i + 1, firstColumn)) {
                        if (field[firstRow + i + 1][firstColumn].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }
                    if (isWithinBounds(field, firstRow + i - 1, firstColumn)) {
                        if (field[firstRow + i - 1][firstColumn].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }


                    if (isWithinBounds(field, firstRow + i, firstColumn + 1)) {
                        if (field[firstRow + i][firstColumn + 1].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }

                    if (isWithinBounds(field, firstRow + i, firstColumn - 1)) {
                        if (field[firstRow + i][firstColumn - 1].equals("o")) {

                            throw new IllegalAccessError();
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        // ------ reserving -------
        for (int i = 0; i < length; i++) {
            if (upLength > sideLength) {
                field[firstRow + i][firstColumn] = "o";
            } else {
                field[firstRow][firstColumn + i] = "o";
            }
        }
        System.out.println();
        return field;
    }

    private static boolean isWithinBounds(String[][] field, int row, int col) {
        return row >= 0 && row < field.length && col >= 0 && col < field[row].length;
    }

    public static void printField(String[][] field) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        String aToJ = "ABCDEFGHIJ";
        for (int i = 0; i < 10; i++) {
            System.out.print(aToJ.charAt(i));
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + field[i][j]);
            }
            System.out.println();
        }

    }

    public static String[][] createField() {
        String[][] field = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = "~";
            }
        }
        return field;
    }

    public static void setPlayerNum() {
        if (player) {
            playerNum = 2;
        } else {
            playerNum = 1;
        }
    }

    public static void clearScreen() {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }
}
