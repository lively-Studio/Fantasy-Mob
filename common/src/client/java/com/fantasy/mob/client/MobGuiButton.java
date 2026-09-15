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

package com.fantasy.mob.client;

import com.fantasy.mob.MobCreativeTab;
import com.fantasy.mob.network.MobNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

/**
 * 库里「生物背包」入口的客户端状态（静态单例）。
 * 供消费方模组控制：是否显示、按钮文案、图标与点击行为。
 * 生存物品栏入口与创造标签页共用此配置。
 */
public final class MobGuiButton {

    private static volatile boolean visible = false;
    private static volatile Text text = Text.translatable("itemGroup.fantasy_mob.mob_backpack");
    private static volatile Runnable onClick = () -> ClientPlayNetworking.send(new MobNetworking.RequestMobListPayload());
    private static volatile Supplier<ItemStack> icon = MobCreativeTab::getIcon;

    private MobGuiButton() {
    }

    /** 是否在生存物品栏 / 创造标签页显示入口。默认 false（不显示）。 */
    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean visible) {
        MobGuiButton.visible = visible;
    }

    /** 按钮提示 / 标签文案（默认“生物背包”）。 */
    public static Text getText() {
        return text;
    }

    public static void setText(Text text) {
        MobGuiButton.text = text != null ? text : MobGuiButton.text;
    }

    public static Runnable getOnClick() {
        return onClick;
    }

    public static void setOnClick(Runnable onClick) {
        MobGuiButton.onClick = onClick != null ? onClick : MobGuiButton.onClick;
    }

    /** 生存入口按钮渲染的图标（默认与创造标签页图标一致）。 */
    public static ItemStack getIcon() {
        Supplier<ItemStack> s = icon;
        return s != null ? s.get() : ItemStack.EMPTY;
    }

    public static void setIcon(Supplier<ItemStack> icon) {
        if (icon != null) MobGuiButton.icon = icon;
    }
}