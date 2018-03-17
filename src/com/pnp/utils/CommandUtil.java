package com.pnp.utils;

import com.pnp.model.CommandModel;

public class CommandUtil {

	public static CommandModel getNetErrorCommand() {
		CommandModel command = new CommandModel();
		command.setState("-1");
		command.setTag("服务连接失败");
		return command;
	}

}
