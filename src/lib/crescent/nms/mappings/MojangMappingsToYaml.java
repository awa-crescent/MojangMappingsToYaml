package lib.crescent.nms.mappings;

public class MojangMappingsToYaml {
	public static void main(String[] args) {
		if (args.length < 2)
			System.out.println("Please specify input original Mojang Mappings.txt file (arg0) and output yml file (arg1)");
		System.out.println("Converting Mojang Mappings.txt file \"" + args[0] + "\" to yml file \"" + args[1] + '\"');
		Mappings.parseMojangMappings(args[0]).writeToYamlFile(args[1]);
		System.out.println("Convert complete");
	}
}
