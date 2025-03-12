/*
 * Copyright (c) theishiopian
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package com.theishiopian.create_alex_power;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.NuclearFurnaceComponentBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(CreateAlexPower.MODID)
public class CreateAlexPower
{
    public static final String MODID = "create_alex_power";
    public static BoilerHeater NUCLEAR_FURNACE = CreateAlexPower::nuclearFurnace;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SOUL_GOOP = MOD_ITEMS.register("soul_goop", () -> new Item(new Item.Properties()));

    public CreateAlexPower()
    {
        @SuppressWarnings("removal") IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onCreativeTab);

        MOD_ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        BoilerHeater.REGISTRY.register(ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get(), NUCLEAR_FURNACE);
    }

    private void onCreativeTab(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == ACCreativeTabRegistry.TOXIC_CAVES.getKey())
        {
            event.accept(SOUL_GOOP);
        }
    }

    public static int nuclearFurnace(Level level, BlockPos pos, BlockState state)
    {
        if(!state.is(ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get())) return -1;
        if(!state.getValue(ACTIVE)) return -1;

        BlockPos corner = NuclearFurnaceComponentBlock.getCornerForFurnace(level, pos, true);

        if(corner == null) return -1;

        NuclearFurnaceBlockEntity be = (NuclearFurnaceBlockEntity) level.getBlockEntity(corner);

        if(be == null) return -1;

        if(!be.isUndergoingFission()) return -1;

        return 5 + be.getCriticality();
    }

    public static void updateBoilers(Level level, BlockPos pos)
    {
        BlockPos upperCorner = pos.above();

        BlockPos[] checkPoints = new BlockPos[]
        {
            upperCorner,
            upperCorner.north(),
            upperCorner.east(),
            upperCorner.south(),
            upperCorner.west(),
            upperCorner.north().east(),
            upperCorner.south().east(),
            upperCorner.south().west(),
            upperCorner.north().west()
        };

        for(BlockPos checkPos : checkPoints)
        {
            BlockPos boilerPos = checkPos.above();

            if(level.getBlockEntity(boilerPos) instanceof FluidTankBlockEntity tank)
            {
                tank.boiler.needsHeatLevelUpdate = true;
            }
        }
    }
}
