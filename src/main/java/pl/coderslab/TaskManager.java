package pl.coderslab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class TaskManager {
    public static void main(String[] args) {

        selectOption();

    }

    public static void selectOption() {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean isDone = false;
        while (!isDone) {
            stringMethod("Please select an option:");
            stringMethod("add" + "\n" + "remove" + "\n" + "list" + "\n" + "exit");
            input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "add":
                    add(arrayConverter(readFile()));
                    break;
                case "remove":
                    remove(arrayConverter(readFile()));
                    break;
                case "list":
                    list();
                    break;
                case "exit":
                    isDone = true;
                    stringMethod("Bye, bye.");
                    break;
                default:
                    stringMethod("You chose incorrect option with " + input);
                    break;

            }
        }
    }

    public static void stringMethod(String string) {

        if (string.equals("Please select an option:")) {
            System.out.println(ConsoleColors.BLUE + string);
        } else if (string.toLowerCase().contains("incorrect")) {
            System.out.println(ConsoleColors.RED + string);
        } else if (string.toLowerCase().contains("bye")) {
            System.out.println(ConsoleColors.RED + string);
        } else if (string.toLowerCase().contains("please provide")) {
            System.out.println(ConsoleColors.RED + string);
        }
        else {
            System.out.println(ConsoleColors.WHITE + string);
        }
    }

    private static StringBuilder readFile() {

        File file = new File("tasks.csv");
        StringBuilder reading = new StringBuilder();
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                reading.append(scan.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            stringMethod("No file found");
        }
        return reading;
    }

    public static void add(String[][] arrayString) {
        StringBuilder fileContents = readFile();
        String description = readText("Please add task description");
        String dueDate = readText("Please add due date");
        String importance = readText("Please add importance");
        String[] rows = {description, dueDate, importance};
        String[][] tasks = Arrays.copyOf(arrayString, arrayString.length + 1);
        tasks[tasks.length - 1] = rows;
        writeFile(tasks);
    }

    private static String readText(String string) {
        stringMethod(string);
        String dueDatePattern="\\d{2}-\\d{2}-\\d{4}";
        boolean truefalse=false;

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        if (input.equals("true") || input.equals("false")) {
                        truefalse=true;
        }
        while (input.isEmpty()) {
            System.out.println(string);
            input = scanner.nextLine();
        }
        while (string.contains("due date")&&!input.matches(dueDatePattern)) {
            stringMethod("Please provide date in correct format");
            input=scanner.nextLine();
        }
        while (string.contains("importance")&&!truefalse) {
            stringMethod("Please provide true/false");
            input=scanner.nextLine();
            if (input.equals("true") || input.equals("false")) {
                truefalse=true;
            }

        }
        return input;
    }

    private static int readNumber() {
        Scanner scanner = new Scanner(System.in);
        stringMethod("Please select number to remove.");
        while (!scanner.hasNextInt()) {
            scanner.next();
            stringMethod("Incorrect argument passed. Please give number greater or equal 0");
        }
        return scanner.nextInt();
    }

    public static void writeFile(String[][] arrayString) {

        try (FileWriter writer = new FileWriter("tasks.csv")) {

            for (int i = 0; i < arrayString.length; i++) {

                for (int j = 0; j < arrayString[i].length; j++) {
                    writer.write(arrayString[i][j].trim());
                    if (j < arrayString[i].length - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            stringMethod("File has not been saved");
        }
    }

    public static String[][] arrayConverter(StringBuilder stringBuilder) {
        String[] rows = stringBuilder.toString().split("\n");
        String[][] tasks = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            tasks[i] = rows[i].split(",");
        }
        return tasks;
    }

    public static void list() {
        String[][] tasksArray = arrayConverter(readFile());

        for (int i = 0; i < tasksArray.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tasksArray[i].length; j++) {
                System.out.print(tasksArray[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void remove(String[][] arrayString) {
        list();
        ArrayList<String[]> list = new ArrayList<>((Arrays.asList(arrayString)));
        while (true) {
            try {
                int number = readNumber();
                list.remove(number);
                String[][] newArray = list.toArray(new String[list.size()][]);
                writeFile(newArray);
                stringMethod("Value was successfully deleted.");
                break;

            } catch (IndexOutOfBoundsException e) {
                stringMethod("Incorrect argument passed. Please give number greater or equal 0");

            }

        }
    }

    public static void exit(String[][] arrayString) {
        writeFile(arrayString);
    }
}




