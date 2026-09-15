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

package com.fantasy.mob.client.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;

/**
 * 原版风格的小号 20x20 图标按钮：保留原版按钮皮肤，中央绘制一个物品图标，
 * 可设置悬浮提示。用于生存物品栏配方书旁的「生物背包」入口。
 * <p>
 * 注意：1.21.11 起 {@link ButtonWidget} 增加抽象 {@code drawIcon}，具象子类为
 * {@link ButtonWidget.Text}；本类继承它并覆写 {@code drawIcon} 绘制图标。
 */
public class MobTabIconButton extends ButtonWidget.Text {

    private final ItemStack icon;

    public MobTabIconButton(int x, int y, ItemStack icon, net.minecraft.text.Text tooltip, PressAction onPress) {
        super(x, y, 20, 20, net.minecraft.text.Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.icon = icon;
        if (tooltip != null) {
            setTooltip(Tooltip.of(tooltip));
        }
    }

    @Override
    protected void drawIcon(DrawContext context, int x, int y, float delta) {
        if (icon != null && !icon.isEmpty()) {
            context.drawItem(icon, getX() + 2, getY() + 2);
        }
    }
}