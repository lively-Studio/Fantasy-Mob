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
package com.fantasy.mob;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 库内置的「我的已驯服生物 → 打开背包」通用注册点。
 * 消费方模组（如 fantasy_the_end）在第 main 初始化时 set 自己的 provider、
 * opener，库服务端网络会自动调用它们完成列表响应与背包打开。
 */
public final class MobBackpack {

    /** 给定玩家，返回其已驯服生物列表；默认 null → 视为空列表。 */
    private static volatile Function<ServerPlayerEntity, List<MobEntry>> listProvider;

    /** 给定玩家与实体 id，打开该生物的背包/界面；默认 null → 忽略。 */
    private static volatile BiConsumer<ServerPlayerEntity, Integer> opener;

    private MobBackpack() {
    }

    public static void setListProvider(Function<ServerPlayerEntity, List<MobEntry>> provider) {
        listProvider = provider;
    }

    public static void setOpener(BiConsumer<ServerPlayerEntity, Integer> opener) {
        MobBackpack.opener = opener;
    }

    /** 收集某玩家的已驯服生物列表（无 provider 或异常时返回空）。 */
    public static List<MobEntry> listOwned(ServerPlayerEntity player) {
        Function<ServerPlayerEntity, List<MobEntry>> p = listProvider;
        if (p == null) return Collections.emptyList();
        try {
            List<MobEntry> list = p.apply(player);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /** 打开某生物的背包（无 opener 时忽略）。 */
    public static void open(ServerPlayerEntity player, int entityId) {
        BiConsumer<ServerPlayerEntity, Integer> o = opener;
        if (o != null) {
            try {
                o.accept(player, entityId);
            } catch (Exception ignored) {
            }
        }
    }
}