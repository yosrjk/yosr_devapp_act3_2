package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // Create a socket connection to the server on the specified port
            Socket socket = new Socket("localhost", 12345);

            // Initialize input and output streams for communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            // Prompt the user to enter the first number
            System.out.println("Welcome to the Calculator Client!");
            System.out.println("Enter the first number:");
            String number1 = stdIn.readLine();

            // Prompt the user to enter the desired operation
            System.out.println("Enter the operation (+, -, *, /):");
            String operation = stdIn.readLine();

            // Prompt the user to enter the second number
            System.out.println("Enter the second number:");
            String number2 = stdIn.readLine();

            // Combine the user inputs into a single string to send to the server
            String userInput = number1 + " " + operation + " " + number2;
            out.println(userInput);

            // Receive and display the result from the server
            System.out.println("Result: " + in.readLine());

            // Close the input and output streams and the socket
            out.close();
            in.close();
            stdIn.close();
            socket.close();
        } catch (IOException e) {
            // Handle any input/output exceptions
            e.printStackTrace();
        }
    }
}
