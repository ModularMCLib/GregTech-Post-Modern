package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.common.commands.arguments.MedicalConditionArgument;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GTCommandArguments {

    private static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<MedicalConditionArgument>> MEDICAL_CONDITION_ARGUMENT_TYPE = REGISTRATE
            .generic("medical_condition", Registries.COMMAND_ARGUMENT_TYPE,
                    () -> ArgumentTypeInfos.registerByClass(MedicalConditionArgument.class,
                            SingletonArgumentInfo.contextFree(MedicalConditionArgument::medicalCondition)))
            .register();

    public static void init() {}
}
