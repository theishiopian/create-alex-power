/*
 * Copyright (c) theishiopian
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package com.theishiopian.create_alex_power.Mixin;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = NuclearFurnaceBlockEntity.class, remap = false)
public interface INuclearFurnaceAccessor
{
    @Accessor()
    int getFissionTime();
}
