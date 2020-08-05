import java.io.*;
import java.nio.file.*;

public class UnlaunchLauncherPatcher {
	public static final int tidLocation = 0xAE91;
	public static final byte[] newTid = {0x7F, 0x7F, 0x7F}; //yes, three bytes, because the fourth is region code and not checked
	public UnlaunchLauncherPatcher (String original, String output) {
		byte[] unlaunch = null;
		
		try {
			unlaunch = Files.readAllBytes(Paths.get(original));
		} catch (IOException e) {
			System.out.println("Error reading Unlaunch: ");
			e.printStackTrace();
			System.exit(0);
		}
		
		if (!(unlaunch[0xE616] == (byte)'2' && unlaunch[0xE617] == (byte)'.' && unlaunch[0xE618] == (byte)'0')) {
			System.out.println("'Unlaunch' file improper (maybe not version 2.0?)");
			System.exit(0);
		}
		
		System.arraycopy(newTid, 0, unlaunch, tidLocation, newTid.length);
		
		File f = new File(output);
		f.delete();
		try {
			f.createNewFile();
			Files.write(Paths.get(output), unlaunch);
		} catch (IOException e) {
			System.out.println("Error writing patched Unlaunch: ");
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Patching successful!");
	}
	public static void main (String[] args) {
		if (args.length == 2) {
			new UnlaunchLauncherPatcher(args[0], args[1]);
		}
		else {
			System.out.println("Usage: java -jar UnlaunchLauncherPatcher.jar unlaunch.dsi output.dsi");
		}
	}
}
