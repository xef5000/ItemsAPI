package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import org.bukkit.entity.Player;

public class XPCurrencyIntegration implements CurrencyIntegration {

    /**
     * Mojang's calculation. Refer to <a href="https://minecraft.wiki/w/Experience">wiki</a>
     * */
    @Override
    public double getBalance(Player player) {
        int level = player.getLevel();
        float progress = player.getExp();

        double experience;

        if (level <= 16) {
            experience = level * level + 6 * level;
        } else if (level <= 31) {
            experience = 2.5 * level * level - 40.5 * level + 360;
        } else {
            experience = 4.5 * level * level - 162.5 * level + 2220;
        }

        int expToNextLevel = player.getExpToLevel();

        experience += progress * expToNextLevel;

        return experience;
    }

    @Override
    public void deposit(Player player, double amount) {
        player.giveExp((int) amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        player.giveExp(-(int) amount);
    }
}
