package com.revplay.app;

import java.util.Scanner;

import com.revplay.exception.RevPlayException;
import com.revplay.model.User;
import com.revplay.service.UserService;

public class RevPlayApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {

        System.out.println("Welcome to RevPlay - your best music platform!");

        while (true) {
            System.out.println();
            System.out.println("REVPLAY - A place where you find peace");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // clear buffer
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Please enter a valid number");
                continue;
            }

            switch (choice) {

            case 1:
                registerFlow();
                break;

            case 2:
                loginFlow();
                break;

            case 3:
                forgotPasswordFlow();
                break;

            case 0:
                System.out.println("Application exited. Bye!");
                return;

            default:
                System.out.println("Invalid option");
            }
        }
    }

    // ================= REGISTER =================
    private static void registerFlow() {

        try {
            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            System.out.print("Role (USER/ARTIST): ");
            String role = sc.nextLine().trim().toUpperCase();

            System.out.print("Security Question: ");
            String sq = sc.nextLine();

            System.out.print("Security Answer: ");
            String sa = sc.nextLine();

            boolean ok = userService.register(name, email, password, role, sq, sa);

            if (ok) {
                System.out.println("Registered successfully!");
            }

        } catch (Exception e) {
            // ⭐ THIS PREVENTS PROGRAM STOP ⭐
            System.out.println("Registration failed  : " + e.getMessage());
        }
    }

    // ================= LOGIN =================
    private static void loginFlow() {

        try {
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            User user = userService.login(email, password);

            System.out.println("Login success!");

            try {
                if ("USER".equals(user.getRole())) {
                    new ListenerMenu(user).show();
                } else {
                    new ArtistMenu(user).show();
                }
            } catch (Exception e) {
                System.out.println("Menu error  : " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Login failed  : " + e.getMessage());
        }
    }

    // ================= FORGOT PASSWORD =================
    private static void forgotPasswordFlow() {

        try {
            System.out.print("Email: ");
            String email = sc.nextLine();

            String question = userService.getSecurityQuestion(email);
            System.out.println("Security Question: " + question);

            System.out.print("Answer: ");
            String answer = sc.nextLine();

            userService.verifySecurityAnswer(email, answer);

            System.out.print("New Password: ");
            String newPass = sc.nextLine();

            userService.updatePassword(email, newPass);

            System.out.println("Password updated successfully!");

        } catch (Exception e) {
            // ⭐ THIS PREVENTS PROGRAM FROM STOPPING ⭐
            System.out.println("Forgot Password failed  : " + e.getMessage());
        }
    }

}
