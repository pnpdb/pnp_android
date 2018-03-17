package com.pnp.message;

import com.pnp.LoginActivity;
import com.pnp.PNPApplication;
import com.pnp.model.CommandModel;

public class LoginParser {

	public static void parser(CommandModel command) {
		if (command.getCaption().equals("Cloud")) {
			String innerMessage = command.getCommand();
			

		}

		if (PNPApplication.getCurrentActivity().equals(
				LoginActivity.class.getName())) {
			System.out.println("888888888");
			LoginActivity.reflaction(command);
		}
	}

}
