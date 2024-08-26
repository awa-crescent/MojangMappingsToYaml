# Minecraft original Mojang Mappings.txt file convert to Yaml file!

This tool is very helpful for developers to use Mappings via Reflect or Handle.

# Generate Yaml File

MojangMappingsToYaml.jar {input_original_mojang_mappings_file} {output_yaml_file}

# Use Yaml File

Just load generated Yaml file as a Config, and then use config.getString() to get obfuscated name!

This is very helpful if you use reflect or handle to modify NMS object.

Class/Record/Interface name: {class_full_package_name}.class
example: 
"net.minecraft.core.HolderSet.class" = "jq"
"net.minecraft.core.HolderSet$Named.class" = "jq$c"

Method name: {class_full_package_name}.{method_signature}
example: 
"net.minecraft.core.HolderSet.size()" = "b"
"net.minecraft.core.HolderSet$Named.contains(net.minecraft.core.Holder)" = "a"

Filed name: {class_full_package_name}.{field_name}
"net.minecraft.core.HolderSet$Named.owner" = "a"
"net.minecraft.core.HolderSet$Named.contents" = "c"