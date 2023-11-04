package serveur;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class CalculatorServer {
    private static int clientCount = 0; // Track the number of connected clients
    public static void main(String[] args) {
        try {
            // Create a server socket that listens on port 12345
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running.");
            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("New client connected. Client count: " + clientCount);

                // Create a new thread for each client connection to handle requests concurrently
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            // Handle any I/O exceptions
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket; // Socket for the client connection

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Set up input and output streams for communication with the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    try {
                        // Split the input into tokens to extract operands and operator
                        String[] tokens = inputLine.split(" ");
                        if (tokens.length != 3) {
                            out.println("Invalid input. Please provide input in the format: operand1 operator operand2");
                            continue;
                        }

                        double operand1 = Double.parseDouble(tokens[0]);
                        double operand2 = Double.parseDouble(tokens[2]);
                        String operator = tokens[1];

                        double result = 0;
                        // Perform the appropriate calculation based on the operator
                        switch (operator) {
                            case "+":
                                result = operand1 + operand2;
                                break;
                            case "-":
                                result = operand1 - operand2;
                                break;
                            case "*":
                                result = operand1 * operand2;
                                break;
                            case "/":
                                if (operand2 == 0) {
                                    out.println("Error: Division by zero.");
                                    continue;
                                }
                                result = operand1 / operand2;
                                break;
                            default:
                                out.println("Invalid operator. Please use one of the following: +, -, *, /");
                                continue;
                        }
                        // Send the calculation result back to the client
                        out.println("Result: " + result);
                    } catch (NumberFormatException e) {
                        out.println("Invalid input. Please provide numeric values.");
                    }
                }

                // Close the input and output streams and the client socket
                in.close();
                out.close();
                clientSocket.close();
                clientCount--; // Decrement the client count when a client disconnects
                System.out.println("Client disconnected. Client count: " + clientCount);
            } catch (IOException e) {
                // Handle any I/O exceptions
                e.printStackTrace();
            }
        }
    }
}
