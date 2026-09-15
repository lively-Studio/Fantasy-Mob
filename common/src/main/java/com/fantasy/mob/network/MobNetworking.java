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

package com.fantasy.mob.network;

import com.fantasy.mob.MobBackpack;
import com.fantasy.mob.MobEntry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * 库内置的通用网络：请求「我的已驯服生物列表」并远程打开其背包。
 * 服务端逻辑通过 {@link MobBackpack} 委托给消费方模组注册的 provider/opener。
 */
public final class MobNetworking {

    // ========== C2S: 请求已驯服生物列表 ==========
    public record RequestMobListPayload() implements CustomPayload {
        public static final CustomPayload.Id<RequestMobListPayload> ID =
                new CustomPayload.Id<>(Identifier.of("fantasy_mob", "request_mob_list"));
        public static final PacketCodec<PacketByteBuf, RequestMobListPayload> CODEC =
                PacketCodec.unit(new RequestMobListPayload());

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    // ========== S2C: 返回生物 id 与名称列表 ==========
    public record MobListResponsePayload(int[] ids, String[] names) implements CustomPayload {
        public static final CustomPayload.Id<MobListResponsePayload> ID =
                new CustomPayload.Id<>(Identifier.of("fantasy_mob", "mob_list"));
        public static final PacketCodec<PacketByteBuf, MobListResponsePayload> CODEC = PacketCodec.of(
                (value, buf) -> {
                    buf.writeIntArray(value.ids());
                    buf.writeVarInt(value.names().length);
                    for (String n : value.names()) buf.writeString(n);
                },
                buf -> {
                    int[] ids = buf.readIntArray();
                    int len = buf.readVarInt();
                    String[] names = new String[len];
                    for (int i = 0; i < len; i++) names[i] = buf.readString();
                    return new MobListResponsePayload(ids, names);
                }
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    // ========== C2S: 打开指定生物的背包 ==========
    public record OpenMobBackpackPayload(int entityId) implements CustomPayload {
        public static final CustomPayload.Id<OpenMobBackpackPayload> ID =
                new CustomPayload.Id<>(Identifier.of("fantasy_mob", "open_mob_backpack"));
        public static final PacketCodec<PacketByteBuf, OpenMobBackpackPayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeInt(value.entityId()),
                buf -> new OpenMobBackpackPayload(buf.readInt())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * 双端通用：注册 C2S/S2C payload 类型 + 服务端处理逻辑。
     * 在公共入口(onInitialize)调用，客户端与服务端都会执行 payload 类型注册。
     */
    public static void init() {
        PayloadTypeRegistry.playC2S().register(RequestMobListPayload.ID, RequestMobListPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(OpenMobBackpackPayload.ID, OpenMobBackpackPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MobListResponsePayload.ID, MobListResponsePayload.CODEC);

        // 收到请求列表：经 MobBackpack 委托给消费方 provider
        ServerPlayNetworking.registerGlobalReceiver(RequestMobListPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) context.player();
                List<MobEntry> list = MobBackpack.listOwned(player);
                int[] ids = new int[list.size()];
                String[] names = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    MobEntry e = list.get(i);
                    ids[i] = e.entityId();
                    names[i] = e.name();
                }
                ServerPlayNetworking.send(player, new MobListResponsePayload(ids, names));
            });
        });

        // 收到打开请求：经 MobBackpack 委托给消费方 opener
        ServerPlayNetworking.registerGlobalReceiver(OpenMobBackpackPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) context.player();
                MobBackpack.open(player, payload.entityId());
            });
        });
    }

    private MobNetworking() {
    }
}