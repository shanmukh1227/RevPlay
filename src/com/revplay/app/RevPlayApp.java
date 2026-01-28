package com.revplay.app;

import java.util.Scanner;

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

			int choice = sc.nextInt();
			sc.nextLine();

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

	private static void registerFlow() {
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

		System.out.println(ok ? "Registered successfully!"
				: "Registration failed!");
	}

	private static void loginFlow() {
		System.out.print("Email: ");
		String email = sc.nextLine();

		System.out.print("Password: ");
		String password = sc.nextLine();

		User user = userService.login(email, password);

		if (user == null) {
			System.out.println("Invalid credentials!");
			return;
		}

		System.out.println("Login success!");

		if ("USER".equals(user.getRole())) {
			new ListenerMenu(user).show();
		} else {
			new ArtistMenu(user).show();
		}
	}

	private static void forgotPasswordFlow() {
		System.out.print("Email: ");
		String email = sc.nextLine();

		String question = userService.getSecurityQuestion(email);

		if (question == null) {
			System.out.println("Email not found!");
			return;
		}

		System.out.println("Security Question: " + question);
		System.out.print("Answer: ");
		String ans = sc.nextLine();

		if (!userService.verifySecurityAnswer(email, ans)) {
			System.out.println("Wrong answer!");
			return;
		}

		System.out.print("New Password: ");
		String newPass = sc.nextLine();

		boolean ok = userService.updatePassword(email, newPass);

		System.out.println(ok ? "Password updated!"
				: "Failed to update password!");
	}
}
