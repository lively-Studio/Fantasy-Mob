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