/*
 * Copyright (c) 2026 lively-Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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