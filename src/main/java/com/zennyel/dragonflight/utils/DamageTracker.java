package com.zennyel.dragonflight.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.util.*;

public class DamageTracker {
    private static final Map<Entity, Map<Player, Integer>> damageHashMap = new HashMap<>();
    private static final int TOP_FIVE_LIMIT = 5;

    public static void recordDamage(Player player, Entity mob, int damage) {
        damageHashMap.computeIfAbsent(mob, k -> new HashMap<>())
                .merge(player, damage, Integer::sum);
        limitToTopFive(mob);
    }

    public static int getPosition(Player player, Entity mob) {
        List<Player> topPlayers = getTopFive(mob);
        return topPlayers.indexOf(player) + 1;
    }

    public static int getDamage(Player player, Entity mob) {
        return damageHashMap.getOrDefault(mob, Collections.emptyMap())
                .getOrDefault(player, 0);
    }

    private static void limitToTopFive(Entity mob) {
        Map<Player, Integer> mobDamageMap = damageHashMap.get(mob);
        if (mobDamageMap.size() > TOP_FIVE_LIMIT) {
            List<Map.Entry<Player, Integer>> entries = new ArrayList<>(mobDamageMap.entrySet());
            entries.sort(Map.Entry.<Player, Integer>comparingByValue().reversed());
            mobDamageMap.clear();
            for (int i = 0; i < TOP_FIVE_LIMIT; i++) {
                Map.Entry<Player, Integer> entry = entries.get(i);
                mobDamageMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public static List<Player> getTopFive(Entity mob) {
        Map<Player, Integer> mobDamageMap = damageHashMap.getOrDefault(mob, Collections.emptyMap());
        List<Player> topPlayers = new ArrayList<>(mobDamageMap.keySet());
        topPlayers.sort(Comparator.comparingInt(player -> -mobDamageMap.get(player)));
        return topPlayers.subList(0, Math.min(topPlayers.size(), TOP_FIVE_LIMIT));
    }
}
