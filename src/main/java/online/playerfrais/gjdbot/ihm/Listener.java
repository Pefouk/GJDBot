package online.playerfrais.gjdbot.ihm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {

	private final String version;
	private final String prefix;
	private final Map<String, String> commands;

	public Listener(String prefix, String version) {
		this.prefix = prefix;
		this.commands = new HashMap<String, String>();
		this.commands.put("help", "Displays help message.");
		this.commands.put("ping", "Answers with pong.");
		this.commands.put("avatar", "Get avatar url.");
		this.version = version;
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		event.getGuild().getTextChannels().get(0).sendMessage("Hello, use " + this.prefix + "help to learn what i can do!\nYou can also go to https://github.com/PlayerFre/GJDBot to learn more about me").queue();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;
		if (event.getMessage().getContentRaw().startsWith(this.prefix))
			commands(event);
	}

	private void commands(MessageReceivedEvent event) {
		String brut = event.getMessage().getContentRaw().replace(prefix, "");

		if (brut.startsWith("help")) {
			helper(event, brut);
			return;
		}
		if (brut.startsWith("ping")) {
			ping(event, brut);
			return;
		}
		if (brut.startsWith("avatar")) {
			avatar(event, brut);
			return;
		}
	}

	private void avatar(MessageReceivedEvent event, String brut) {
		List<String> split = Arrays.asList(brut.split(" "));
		
		if (split.size() > 1)
			pingedAvatar(event);
		else
			authorAvatar(event);
	}

	private void pingedAvatar(MessageReceivedEvent event) {
		MessageBuilder msgb = new MessageBuilder("Avatar from pinged users :\n");
		List<Member> pinged = event.getMessage().getMentionedMembers();

		if (pinged.size() == 0)
			event.getChannel().sendMessage("Please ping one or multiple users to get their avatars URL").queue();
		else {
			for (Member m : pinged) {
				msgb.append(m.getUser().getAvatarUrl() + " ");
			}
			event.getChannel().sendMessage(msgb.build()).queue();
		}
	}

	private void authorAvatar(MessageReceivedEvent event) {
		event.getChannel().sendMessage(event.getAuthor().getAvatarUrl()).queue();
	}

	private void ping(MessageReceivedEvent event, String message) {
		event.getChannel().sendMessage("Pong").queue();
	}

	private void helper(MessageReceivedEvent event, String message) {
		List<String> splitted = Arrays.asList(message.split(" "));

		if (splitted.size() == 1)
			fullhelp(event);
		else
			helpcommand(event, splitted.get(1));
	}

	private void helpcommand(MessageReceivedEvent event, String string) {
		if (this.commands.get(string.toLowerCase()) == null) 
			event.getChannel().sendMessage("Command not found !\nTry " + this.prefix + "help to see every commands avaible").queue();
		else {
			event.getChannel().sendMessage(string.toLowerCase() + " : " + this.commands.get(string).toLowerCase()).queue();;
		}
	}

	private void fullhelp(MessageReceivedEvent event) {
		MessageBuilder msgb = new MessageBuilder("Generic Discord Java Bot " + this.version + " : avaible commands\n");

		for (Map.Entry<String, String> entry : this.commands.entrySet()) {
			String command = entry.getKey();
			String description = entry.getValue();
			
			msgb.append(" " + command + " : " + description + "\n");
		}
		msgb.append("Prefix : " + this.prefix + "\n");
		msgb.append("Made on GitHub by PlayerFre (https://github.com/PlayerFre/GJDBot)");
		event.getChannel().sendMessage(msgb.build()).queue();
	}
}