package cn.variZoo.managers;

import cn.variZoo.Configuration;
import cn.variZoo.Main;
import cn.variZoo.utils.XLogger;
import cn.variZoo.utils.configuration.ConfigurationManager;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigManager {
    private final File configFile;
    private final Main plugin;
    public boolean animalSpawnMutantParticle;
    public Set<EntityType> animalSpawnBlacklistAnimal;
    public Set<String> animalSpawnBlacklistWorld;
    public Set<String> animalSpawnBlacklistSpawnReason;
    public boolean breedActionbar;
    public boolean breedMultipleHurt;
    public Set<EntityType> breedBlacklistAnimal;
    public Set<String> breedBlacklistWorld;
    public boolean otherIncreaseDrops;

    public ConfigManager(Main main) {
        this.plugin = main;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        load();

    }

    public void load() {

        try {
            ConfigurationManager.load(Configuration.class, configFile, "version");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        animalSpawnMutantParticle = !(Configuration.AnimalSpawn.Mutant.particle.type.isEmpty() && Configuration.AnimalSpawn.Mutant.particle.count < 1);
        animalSpawnBlacklistAnimal = Configuration.AnimalSpawn.blackList.animal.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return EntityType.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + "is not a valid entity type.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        animalSpawnBlacklistWorld = new HashSet<>(Configuration.AnimalSpawn.blackList.world);
        animalSpawnBlacklistSpawnReason = new HashSet<>(Configuration.AnimalSpawn.blackList.spawnReason);

        breedActionbar = !Configuration.Breed.inheritance.actionbar.isEmpty();
        breedMultipleHurt = !Configuration.Breed.multiple.hurt.isEmpty();
        breedBlacklistAnimal = Configuration.Breed.blackList.animal.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return EntityType.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + "is not a valid entity type.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        breedBlacklistWorld = new HashSet<>(Configuration.Breed.blackList.world);

        otherIncreaseDrops = !Configuration.other.increaseDrops.isEmpty();
    }


}
