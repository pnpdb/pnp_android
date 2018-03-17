package com.pnp.message;

import com.pnp.model.CommandModel;
import com.pnp.utils.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtil {

	public static XStream xstream = new XStream(new DomDriver());

	public static String CommandToXml(CommandModel obj) {
		xstream.alias("Command", CommandModel.class);
		xstream.useAttributeFor(CommandModel.class, "Guid");
		xstream.useAttributeFor(CommandModel.class, "Name");
		xstream.useAttributeFor(CommandModel.class, "Caption");
		xstream.useAttributeFor(CommandModel.class, "Route");
		xstream.useAttributeFor(CommandModel.class, "Command");
		xstream.useAttributeFor(CommandModel.class, "User");
		xstream.useAttributeFor(CommandModel.class, "From");
		xstream.useAttributeFor(CommandModel.class, "Tag");
		xstream.useAttributeFor(CommandModel.class, "ID");
		xstream.useAttributeFor(CommandModel.class, "State");
		xstream.useAttributeFor(CommandModel.class, "Value");
		xstream.useAttributeFor(CommandModel.class, "Block");
		xstream.useAttributeFor(CommandModel.class, "Object");
		xstream.useAttributeFor(CommandModel.class, "Source");
		xstream.aliasField("XML", CommandModel.class, "XML");
		return StringUtil.replaceBlank(xstream.toXML(obj));
	}

	public static CommandModel getCommandFromXML(String xml) {
		return (CommandModel) xstream.fromXML(xml);
	}

}
