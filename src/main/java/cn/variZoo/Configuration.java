package cn.variZoo;

import cn.variZoo.utils.configuration.Comment;
import cn.variZoo.utils.configuration.ConfigurationFile;
import cn.variZoo.utils.configuration.ConfigurationPart;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends ConfigurationFile {

    @Comment(cn_value = "版本号, 请勿修改",
            value = "Do not modify this.")
    public static int version = 3;

    @Comment(cn_value = "总开关",
            value = "enabled the plugin")
    public static boolean enabled = true;

    @Comment(cn_value = "动物生成相关")
    public static AnimalSpawn animalSpawn = new AnimalSpawn();
    @Comment(cn_value = "生育相关")
    public static Breed breed = new Breed();
    @Comment(cn_value = "其他相关")
    public static Other other = new Other();

    public static class AnimalSpawn extends ConfigurationPart {

        @Comment(cn_value = "基础配置")
        public static Basic basic = new Basic();
        @Comment(cn_value = "在基础上二次突变",
                value = "Secondary mutation on the basis")
        public static Mutant mutant = new Mutant();
        @Comment(cn_value = "黑名单")
        public static BlackList blackList = new BlackList();

        public static class Basic extends ConfigurationPart {

            @Comment(cn_value = {"动物生成时附带体型大小的概率", "设置为0或者负数禁用, 最大为100"},
                    value = {"The probability of animals being generated with body size attached", "Set to 0 or a negative number to disable. The maximum is 100"})
            public double apply = 50.0;

            @Comment(cn_value = {"体型变化值", "degree可填写范围或者多个数字"},
                    value = {"Body size change value", "The 'degree' can be filled with a range or multiple numbers"})
            public String degree = "0.86-1.16";

        }

        public static class Mutant extends ConfigurationPart {

            @Comment(cn_value = "触发变异时产生的粒子特效",
                    value = "Particle effects produced when triggering mutation")
            public static Particle particle = new Particle();

            @Comment(cn_value = {"变异的概率", "设置为0或者负数禁用, 最大为100"},
                    value = {"The probability of mutation", "Set to 0 or a negative number to disable. The maximum is 100"})
            public double apply = 3.0;

            @Comment(cn_value = {"MULTIPLY: 简单相乘", "MORE: 自适应, 大的更大, 小的更小"},
                    value = {"MULTIPLY: Simple multiplication", "MORE: Adaptive. Bigger gets even bigger, smaller gets even smaller"})
            public String mode = "MORE";

            @Comment(cn_value = {"变化值", "degree可填写范围或者多个数字"},
                    value = {"Change value", "The 'degree' can be filled with a range or multiple numbers"})
            public String degree = "0.77, 1.3";

            public static class Particle extends ConfigurationPart {

                @Comment(cn_value = "类型, 留空禁用",
                        value = "Leave blank to disable")
                public String type = "GLOW";

                @Comment(cn_value = "数量",
                        value = "Particle Quantity")
                public int count = 20;
            }

        }

        public static class BlackList extends ConfigurationPart {

            @Comment(cn_value = {"不受影响的动物", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html"},
                    value = {"Animals Unaffected", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html"})
            public List<String> animal = new ArrayList<>();

            @Comment(cn_value = "不受影响的世界",
                    value = "The Worlds Unaffected")
            public List<String> world = new ArrayList<>();

            @Comment(cn_value = {"不受影响的生成原因", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html"},
                    value = {"The Uninfluenced Spawning Reasons", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html"})
            public List<String> spawnReason = List.of("SPAWNER");

        }

    }

    public static class Breed extends ConfigurationPart {

        @Comment(cn_value = "动物生育宝宝是否遗传体型",
                value = "Whether animals' babies inherit the body size when giving birth")
        public static Inheritance inheritance = new Inheritance();
        @Comment(cn_value = "多胞胎功能",
                value = "Multiple Birth")
        public static Multiple multiple = new Multiple();
        @Comment(cn_value = {"黑名单", "给予玩家varizoo.skip.breed权限可以单独关闭遗传功能, 防止一些生物牧场被破坏"},
                value = {"Blacklist", "Granting players the varizoo.skip.breed permission can separately turn off the inheritance function to prevent some biological pastures from being destroyed"})
        public static BlackList blackList = new BlackList();

        public static class Inheritance extends ConfigurationPart {

            @Comment(cn_value = {"父母对孩子体型的影响", "可使用复杂的公式, 留空时禁用", "可用变量：{father}、{mother}父母的体型, {degree}对应下方比例"},
                    value = {"The influence of parents on the body size of children", "Complex formulas can be used. Disable when left blank", "Available variables: {father}, {mother} for the body size of parents, {degree} corresponding to the proportion below"})
            public String finalScale = "({father} * 1.1 + {mother} * 1.2) / {degree}";

            @Comment(cn_value = "比例, 配合前一个final-scale使用",
                    value = "Used in conjunction with the previous 'final-scale'")
            public String degree = "2.1-2.5";

            @Comment(cn_value = "开启该选项会让宝宝不受animal-spawn的二次体型变化影响",
                    value = "Enabling this option will make the babies not be affected by the secondary body size change of animal-spawn")
            public boolean skipAnimalSpawn = false;

            @Comment(cn_value = {"当饲养出宝宝时的提示", "仅支持minimessage颜色格式, 留空时禁用", "可用变量: {scale}体型, {baby}宝宝名称, {player}玩家名称"},
                    value = {"The prompt when breeding a baby", "Only supports MiniMessage color format. Disable when left blank", "Available variables: {scale} baby scale, {baby} baby name, {player} player name"})
            public String actionbar = "<white>新生命诞生啦! 是体型为<green> {scale} </green>的{baby}宝宝~</white>";
        }

        public static class Multiple extends ConfigurationPart {

            @Comment(cn_value = {"多胞胎的概率", "设置为0或负数时禁止, 最大为100", "注意不可过高, 每次生育都会触发多胞胎判定, 过高会一直生孩子导致卡服"},
                    value = {"The probability of multiple births", "Prohibited when set to 0 or a negative number. The maximum is 100", "Note that it should not be too high. The determination of multiple births is triggered every time breeding occurs. If it is too high, it will keep giving birth to children and cause the server to lag"})
            public double apply = 9.0;

            @Comment(cn_value = {"每次生孩子的间隔", "单位为tick, 20ticks = 1s"},
                    value = {"The interval between each childbirth", "The unit is ticks, 20 ticks = 1 second"})
            public int delay = 3;

            @Comment(cn_value = {"启动多胞胎时, 为了限制生育, 每次生育都会扣除以下血量", "可使用复杂的公式, 留空时禁用", "可用变量：{health}当前血量, {max_health}最大血量"},
                    value = {"When enabling multiple births, in order to limit breeding, the following amount of health will be deducted each time breeding occurs", "Complex formulas can be used. Disable when left blank", "Available variables: {health} current health, {max_health} maximum health"})
            public String hurt = "{health} * 0.05";
        }

        public static class BlackList extends ConfigurationPart {

            @Comment(cn_value = "不受影响的动物",
                    value = "Animals Unaffected")
            public List<String> animal = List.of("BEE");

            @Comment(cn_value = "不受影响的世界",
                    value = "The Worlds Unaffected")
            public List<String> world = new ArrayList<>();
        }
    }

    public static class Other extends ConfigurationPart {

        @Comment(cn_value = "体型对最大生命的影响, 会等比例变化生命值",
                value = "The influence of scale on maximum health. Health points will change proportionally")
        public boolean effectHealth = true;

        @Comment(cn_value = "动物转变时是否保留体型, 比如猪被雷劈变成猪灵, 依旧会继承体型",
                value = {"Whether the scale is retained when the animal transforms. ", "For example, when a pig is struck by lightning and turns into a Piglin, the scale will still be inherited"})
        public boolean transform = true;

        @Comment(cn_value = {"美西螈和鱼装进桶后放出会失去原有的体型", "该功能可以保留体型, 会修正鱼桶和发射器"},
                value = {"Axolotls and fish will lose their original body size when released after being put into buckets", " This function can retain the body size and will correct fish buckets and dispensers"})
        public boolean bucketFishFix = true;

        @Comment(cn_value = {"使动物的掉落物数量乘以以下值", "可使用复杂的公式, 留空时禁用", "可用变量：{scale}生物体型"},
                value = {"Multiply the number of dropped items of animals by the following value", "Complex formulas can be used. Disable when left blank", "Available variables: {scale} Creature body size"})
        public String increaseDrops = "sqrt({scale})";
    }


}
