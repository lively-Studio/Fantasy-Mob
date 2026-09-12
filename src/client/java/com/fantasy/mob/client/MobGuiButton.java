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

import com.fantasy.mob.network.MobNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

/**
 * 库里「已驯服生物」入口按钮的客户端状态（静态单例）。
 * 供消费方模组控制：是否显示、按钮文案、点击行为。
 */
public final class MobGuiButton {

    private static volatile boolean visible = false;
    private static volatile Text text = Text.literal("末影人");
    private static volatile Runnable onClick = () -> ClientPlayNetworking.send(new MobNetworking.RequestMobListPayload());

    private MobGuiButton() {
    }

    /** 是否在生存/创造物品栏显示按钮。默认 false（不显示）。 */
    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean visible) {
        MobGuiButton.visible = visible;
    }

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
}