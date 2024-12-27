package cn.scalingZoo;

import cn.scalingZoo.utils.configuration.Comment;
import cn.scalingZoo.utils.configuration.ConfigurationFile;
import cn.scalingZoo.utils.configuration.ConfigurationPart;

import java.util.List;

public class Configuration extends ConfigurationFile {

    @Comment("版本号")
    public static int version = 1;

    @Comment("总开关")
    public static boolean enabled = true;
    public static AnimalSpawn animalSpawn = new AnimalSpawn();

    @Comment("动物生成相关")
    public static class AnimalSpawn extends ConfigurationPart {

        @Comment("基础配置")
        public static Basic basic = new Basic();
        public static class Basic extends ConfigurationPart {

            @Comment({"动物生成时附带体型大小的概率",
                    "设置为0或者负数禁用，最大为100"})
            public double apply = 33.0;

            @Comment({"体型变化值",
                    "degree可填写范围或者多个数字"})
            public String degree = "0.86-1.16";

        }

        @Comment("在基础上二次突变")
        public static Mutation mutation = new Mutation();
        public static class Mutation extends ConfigurationPart {

            @Comment({"变异的概率","设置为0或者负数禁用，最大为100"})
            public double apply = 6.0;

            @Comment({"MULTIPLY: 简单相乘", "MORE: 自适应, 大的更大, 小的更小"})
            public String mode = "MULTIPLY";

            @Comment({"变化值",
                    "degree可填写范围或者多个数字"})
            public String degree = "0.77, 1.3";

            @Comment("触发变异时产生的粒子特效")
            public static Particle particle = new Particle();
            public static class Particle extends ConfigurationPart {

                @Comment("类型")
                public String type = "GLOW";

                @Comment("数量")
                public int count = 20;
            }

        }

        @Comment("黑名单")
        public static BlackList blackList = new BlackList();
        public static class BlackList extends ConfigurationPart {

            @Comment({"不受影响的动物",
                    "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html"})
            public List<String> animal = ;



        }

    }
}
