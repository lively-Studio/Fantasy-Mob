/*
 * Copyright (C) 2026 cangcang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.fantasy.mob.client;

import com.fantasy.mob.client.network.MobGuiNetworkingClient;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fantasy: Mob 库模组客户端入口。
 */
public class FantasyMobClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("FantasyMob-Client");

    @Override
    public void onInitializeClient() {
        MobGuiNetworkingClient.init();
        LOGGER.info("[Fantasy:Mob] 库模组客户端初始化完成。");
    }
}