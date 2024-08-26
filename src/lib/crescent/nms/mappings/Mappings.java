package lib.crescent.nms.mappings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Mappings {
	private KVTreeNode<String, String> root;

	public Mappings() {
		root = new KVTreeNode<>();
	}

	public Mappings(String root_key) {
		root = new KVTreeNode<>(root_key);
	}

	public String getObfuscatedName(String mapped_name) {
		KVTreeNode<String, String> target = root.findChildChainNode(mapped_name.split("\\."), false);
		if (target == null)
			return null;
		return target.value;
	}

	public KVTreeNode<String, String> setObfuscatedName(String mapped_name, String obfuscated_name) {
		KVTreeNode<String, String> target = root.findChildChainNode(mapped_name.split("\\."), true);
		target.value = obfuscated_name;
		return target;
	}

	public KVTreeNode<String, String> getRootNode() {
		return root;
	}

	public static Mappings parseMojangMappings(String mappings_file_path) {
		Mappings mappings = new Mappings();
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(mappings_file_path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		KVTreeNode<String, String> class_node = null;
		for (String line : lines) {
			if (line.startsWith("#"))
				continue;
			if (line.endsWith(":")) {
				line = line.substring(0, line.length() - 1);
				String[] class_info = line.split("->");
				class_node = mappings.setObfuscatedName(class_info[0].trim() + ".class", class_info[1].trim()).getParent();
				continue;
			}
			String[] member_info = line.split("->");
			class_node.attachChild(member_info[0].trim().split("\s+")[1], member_info[1].trim());
		}
		return mappings;
	}

	public String writeToYamlString() {
		return writeToBuffer(new StringBuilder(), root).toString();
	}

	private StringBuilder writeToBuffer(StringBuilder buffer, KVTreeNode<String, String> node) {
		int space_num = 2 * (node.getDepth() - 2);
		char[] space = null;
		if (space_num > 0) {
			space = new char[space_num];
			Arrays.fill(space, ' ');
		} else
			space = new char[] {};
		if (node.key != null) {
			buffer.append(space).append(node.key).append(": ");
			if (node.value != null)
				buffer.append(node.value);
			buffer.append('\n');
		}
		KVTreeNode<String, String>[] children = node.getChildren();
		if (children == null)
			return buffer;
		for (KVTreeNode<String, String> child : children)
			writeToBuffer(buffer, child);
		return buffer;
	}

	public void writeToYamlFile(String file_path) {
		try {
			Files.writeString(Paths.get(file_path), writeToYamlString().toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
