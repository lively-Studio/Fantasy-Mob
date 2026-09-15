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

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

/**
 * Mob 库内置的「生物背包」创造标签页。
 * 该标签页代表玩家已驯服的所有生物（不限种类），本身不展示物品种类，
 * 点击后由客户端 mixin 拦截并打开「选择已驯服生物 → 打开其背包」的通用界面。
 * 图标默认使用末影人刷怪蛋，消费方模组可用 {@link #setIcon} 覆盖。
 */
public final class MobCreativeTab {

    public static final RegistryKey<ItemGroup> KEY =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("fantasy_mob", "mob_backpack"));

    /** 已注册的 ItemGroup，供客户端 mixin 判断“是否点击了本标签页”。 */
    private static volatile ItemGroup group;

    /** 标签页 / 生存入口按钮的图标。 */
    private static volatile Supplier<ItemStack> icon = () -> new ItemStack(Items.ENDERMAN_SPAWN_EGG);

    /** 标签页显示名（翻译键）。 */
    private static final String TITLE_KEY = "itemGroup.fantasy_mob.mob_backpack";

    private MobCreativeTab() {
    }

    public static void register() {
        group = Registry.register(
                Registries.ITEM_GROUP,
                KEY,
                ItemGroup.create(ItemGroup.Row.TOP, 9)
                        .displayName(Text.translatable(TITLE_KEY))
                        .icon(() -> icon.get())
                        .entries((displayContext, entries) -> {
                            // 内容由实体列表动态生成，这里不注入任何静态条目。
                        })
                        .build()
        );
    }

    /** 当前已注册的 ItemGroup；未注册时为 null。 */
    public static ItemGroup getGroup() {
        return group;
    }

    /** 判断给定对象是否为本库的「生物背包」标签页。 */
    public static boolean isMobTab(ItemGroup target) {
        return group != null && target == group;
    }

    public static ItemStack getIcon() {
        Supplier<ItemStack> s = icon;
        return s != null ? s.get() : ItemStack.EMPTY;
    }

    /** 覆盖标签页图标（如主材的专属图标）。 */
    public static void setIcon(Supplier<ItemStack> icon) {
        if (icon != null) MobCreativeTab.icon = icon;
    }

    /** 供语言文件一致使用的翻译键。 */
    public static String getTitleKey() {
        return TITLE_KEY;
    }
}