package cn.variZoo;

import cn.variZoo.utils.configuration.Comment;
import cn.variZoo.utils.configuration.ConfigurationFile;
import cn.variZoo.utils.configuration.ConfigurationPart;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends ConfigurationFile {

    @Comment("版本号")
    public static int version = 1;

    @Comment("总开关")
    public static boolean enabled = true;

    @Comment("动物生成相关")
    public static AnimalSpawn animalSpawn = new AnimalSpawn();
    @Comment("生育相关")
    public static Breed breed = new Breed();
    @Comment("其他相关")
    public static Other other = new Other();

    public static class AnimalSpawn extends ConfigurationPart {

        @Comment("基础配置")
        public static Basic basic = new Basic();
        @Comment("在基础上二次突变")
        public static Mutant mutant = new Mutant();
        @Comment("黑名单")
        public static BlackList blackList = new BlackList();

        public static class Basic extends ConfigurationPart {

            @Comment({"动物生成时附带体型大小的概率",
                    "设置为0或者负数禁用，最大为100"})
            public double apply = 33.0;

            @Comment({"体型变化值",
                    "degree可填写范围或者多个数字"})
            public String degree = "0.86-1.16";

        }

        public static class Mutant extends ConfigurationPart {

            @Comment("触发变异时产生的粒子特效")
            public static Particle particle = new Particle();
            @Comment({"变异的概率", "设置为0或者负数禁用，最大为100"})
            public double apply = 6.0;
            @Comment({"MULTIPLY: 简单相乘", "MORE: 自适应, 大的更大, 小的更小"})
            public String mode = "MORE";
            @Comment({"变化值",
                    "degree可填写范围或者多个数字"})
            public String degree = "0.77, 1.3";

            public static class Particle extends ConfigurationPart {

                @Comment("类型，留空禁用")
                public String type = "GLOW";

                @Comment("数量")
                public int count = 20;
            }

        }

        public static class BlackList extends ConfigurationPart {

            @Comment({"不受影响的动物",
                    "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html"})
            public List<String> animal = List.of("BEE");

            @Comment("不受影响的世界")
            public List<String> world = new ArrayList<>();

            @Comment({"不受影响的生成原因",
                    "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html"})
            public List<String> spawnReason = List.of("BREEDING", "SPAWNER_EGG");

        }

    }

    public static class Breed extends ConfigurationPart {

        @Comment("动物生育宝宝是否遗传体型")
        public static Inheritance inheritance = new Inheritance();
        @Comment("多胞胎功能")
        public static Multiple multiple = new Multiple();
        @Comment("黑名单")
        public static BlackList blackList = new BlackList();

        public static class Inheritance extends ConfigurationPart {

            @Comment({"父母对孩子体型的影响", "可使用复杂的公式，留空时禁用", "可用变量：{father}、{mother}父母的体型，{degree}对应下方比例"})
            public String finalScale = "({father} * 1.1 + {mother} * 1.2) / {degree}";

            @Comment("比例，配合上方的final-scale使用")
            public String degree = "2.0-2.6";
        }

        public static class Multiple extends ConfigurationPart {

            @Comment({"多胞胎的概率", "设置为0或负数时禁止，最大为100", "注意不可过高，每次生育都会触发多胞胎判定，过高会一直生孩子导致卡服"})
            public double apply = 9.0;

            @Comment({"每次生孩子的间隔", "单位为tick，20ticks = 1s"})
            public int delay = 3;

            @Comment({"启动多胞胎时，为了限制生育，每次生育都会扣除以下血量", "可使用复杂的公式，留空时禁用", "可用变量：{health}当前血量，{max_health}最大血量"})
            public String hurt = "{health} * 0.05";
        }

        public static class BlackList extends ConfigurationPart {

            @Comment("不受影响的动物")
            public List<String> animal = List.of("BEE");

            @Comment("不受影响的世界")
            public List<String> world = new ArrayList<>();
        }
    }

    public static class Other extends ConfigurationPart {

        @Comment("体型对最大生命的影响，会等比例变化生命值")
        public boolean effectHealth = true;

        @Comment("动物转变时是否保留体型，比如猪被雷劈变成猪灵，依旧会继承体型")
        public boolean transform = true;

        @Comment("蝾螈、鱼等动物装进桶后放出会失去原有的体型，该功能可以保留体型，会修正鱼桶和发射器")
        public boolean bucketFishFix = true;

        @Comment({"使动物的掉落物数量乘以以下值", "可使用复杂的公式，留空时禁用", "可用变量：{scale}生物体型"})
        public String increaseDrops = "cbrt({scale})";
    }
}
