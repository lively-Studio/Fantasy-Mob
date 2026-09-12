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
package com.fantasy.mob.client.screen;

import com.fantasy.mob.network.MobNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

/**
 * 多只已驯服生物时的选择界面：每只一个按钮，点击打开其背包。
 * 通用，不依赖任何特定生物类型。
 */
public class MobGuiSelectorScreen extends Screen {

    private final int[] ids;
    private final String[] names;

    public MobGuiSelectorScreen(int[] ids, String[] names) {
        super(Text.literal("选择已驯服的生物"));
        this.ids = ids;
        this.names = names;
    }

    @Override
    protected void init() {
        GridWidget grid = new GridWidget();
        grid.getMainPositioner().margin(4);
        GridWidget.Adder adder = grid.createAdder(1);

        for (int i = 0; i < ids.length; i++) {
            final int entityId = ids[i];
            String label = (i < names.length && names[i] != null && !names[i].isBlank())
                    ? names[i] : "生物 #" + entityId;
            adder.add(ButtonWidget.builder(
                    Text.literal(label),
                    btn -> {
                        ClientPlayNetworking.send(new MobNetworking.OpenMobBackpackPayload(entityId));
                        if (this.client != null) this.client.setScreen(null);
                    }
            ).width(180).build());
        }

        adder.add(ButtonWidget.builder(
                ScreenTexts.CANCEL,
                btn -> this.close()
        ).width(180).build());

        grid.setPosition((this.width - 180) / 2, this.height / 2 - ids.length * 12);
        grid.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}