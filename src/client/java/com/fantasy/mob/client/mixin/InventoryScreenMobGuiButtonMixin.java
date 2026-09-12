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
package com.fantasy.mob.client.mixin;

import com.fantasy.mob.client.MobGuiButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在生存模式物品栏(E键)界面添加「已驯服生物」按钮（仅当 {@link MobGuiButton#isVisible()}）。
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMobGuiButtonMixin {

    @Inject(method = "init", at = @At("RETURN"))
    private void fantasy_mob_addButton(CallbackInfo ci) {
        if (!MobGuiButton.isVisible()) return;
        InventoryScreen screen = (InventoryScreen) (Object) this;
        MobGuiHandledScreenAccessor accessor = (MobGuiHandledScreenAccessor) screen;
        ScreenAccessor screenAccessor = (ScreenAccessor) screen;
        int x = accessor.getX() + accessor.getBackgroundWidth() - 80;
        int y = accessor.getY() + 5;
        Runnable onClick = MobGuiButton.getOnClick();
        screenAccessor.invokeAddDrawableChild(ButtonWidget.builder(
                MobGuiButton.getText(),
                btn -> MinecraftClient.getInstance().execute(onClick)
        ).dimensions(x, y, 78, 20).build());
    }
}