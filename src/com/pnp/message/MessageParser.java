package com.pnp.message;

import com.pnp.model.CommandModel;

public class MessageParser {

	public static void parser(String message) {
		CommandModel command = XStreamUtil.getCommandFromXML(message);
		if (command.getName().equals("Login")) {
			LoginParser.parser(command);
		}
	}
}
