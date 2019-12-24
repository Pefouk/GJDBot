package online.playerfrais.gjdbot.ihm;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;

public class Bot {

	public static void main(String[] args) {
		Parameters parameters;
		JDA bot = null;

		try {
			parameters = getParameters();
		} catch (Exception e) {
			System.err.println("Error occured in the initialisation of the bot.");
			return;
		}
		try {
			bot = new JDABuilder(parameters.getToken()).build();
		} catch (LoginException e) {
			System.err.println("Error : Invalid token.");
			return;
		} catch (IllegalArgumentException e) {
			System.err.println("Error : Token empty or null.");
			return;
		}
		if (bot == null)
			return;
		bot.addEventListener(new Listener(parameters.getPrefix(), "0.1"));
		System.out.println("To invite your bot to you discord server, use this link\n" + bot.getInviteUrl(Permission.ADMINISTRATOR));
	}

	private static Parameters getParameters() throws InitException {
		Parameters res = new Parameters();

		try {
			XMLReader fichier = new XMLReader();
			res.setPrefix(fichier.getPrefix().getTextContent());
			res.setToken(fichier.getToken().getTextContent());
		} catch (Exception e) {
			try (Scanner s = new Scanner(System.in);) {
				System.err.println("No config.xml file detected or wrong config.xml");
				System.out.println("Please type in the token of discord bot");
				res.setToken(s.next());
				System.out.println("Now type the prefix used to call the bot");
				res.setPrefix(s.next());
			} catch (Exception e1) {
				throw new InitException(e1);
			}
		}
		if (res.getPrefix() != null && res.getToken() != null)
			return res;
		else
			throw new InitException("Void token or void prefix");
	}
}
