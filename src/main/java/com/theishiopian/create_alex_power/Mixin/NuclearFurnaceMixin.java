/*
 * Copyright (c) theishiopian
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package com.theishiopian.create_alex_power.Mixin;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import com.theishiopian.create_alex_power.CreateAlexPower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NuclearFurnaceBlockEntity.class, remap = false)
public class NuclearFurnaceMixin
{
    @Inject(method = "tick", at = @At("HEAD"))
    private static void InjectIntoTickHead(Level level, BlockPos blockPos, BlockState state, NuclearFurnaceBlockEntity entity, CallbackInfo ci)
    {
        int fissionTime = ((INuclearFurnaceAccessor)entity).getFissionTime();

        if(fissionTime == 1)
        {
            CreateAlexPower.updateBoilers(level, blockPos);
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", ordinal = 0, remap = true))
    private static void injectIntoTickAtStart(Level level, BlockPos blockPos, BlockState state, NuclearFurnaceBlockEntity entity, CallbackInfo ci)
    {
        CreateAlexPower.updateBoilers(level, blockPos);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", ordinal = 2, remap = true))
    private static void injectIntoTickAtWasteReduced(Level level, BlockPos blockPos, BlockState state, NuclearFurnaceBlockEntity entity, CallbackInfo ci)
    {
        CreateAlexPower.updateBoilers(level, blockPos);
    }
}
