package cn.variZoo;

import cn.variZoo.utils.configuration.Comment;
import cn.variZoo.utils.configuration.ConfigurationFile;

import java.util.Arrays;
import java.util.List;

public class Language extends ConfigurationFile {

    @Comment(value = "Do not modify this.", cn_value = "版本号, 请勿修改")
    public static int version = 1;

    public static List<String> help = Arrays.asList("VariZoo Help", "Commands:", "<yellow>/Varizoo reload</yellow> - Reload the plugin.", "<yellow>/Varizoo help</yellow> - Get the plugin's help.");

    public static String reload = "<green>[VariZoo]</green> Plugin is reloading...";

    public static String reloadCompleted = "<green>[VariZoo]</green> Plugin reload completed. Time taken: %s ms";


}
