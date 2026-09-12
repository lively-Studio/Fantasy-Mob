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
package com.fantasy.mob.client.network;

import com.fantasy.mob.client.screen.MobGuiSelectorScreen;
import com.fantasy.mob.network.MobNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * 库内置的客户端网络接收(S2C)：收到已驯服生物列表后按数量打开对应界面。
 */
public final class MobGuiNetworkingClient {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(
                MobNetworking.MobListResponsePayload.ID,
                (payload, context) -> {
                    MinecraftClient client = context.client();
                    client.execute(() -> handleList(payload, client));
                }
        );
    }

    private static void handleList(MobNetworking.MobListResponsePayload payload, MinecraftClient client) {
        int[] ids = payload.ids();
        if (ids == null || ids.length == 0) {
            if (client.player != null) {
                client.player.sendMessage(Text.literal("你还没有已驯服的生物。"), false);
            }
            return;
        }
        if (ids.length == 1) {
            ClientPlayNetworking.send(new MobNetworking.OpenMobBackpackPayload(ids[0]));
            return;
        }
        client.setScreen(new MobGuiSelectorScreen(ids, payload.names()));
    }

    private MobGuiNetworkingClient() {
    }
}