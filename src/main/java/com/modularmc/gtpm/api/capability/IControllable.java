package com.modularmc.gtpm.api.capability;

import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

/** Controls whether a machine is allowed to work. */
@TransVariants(keyPrefix = "behaviour.soft_hammer",
               value = {
                       @Variant(id = "enabled",
                                source = "Working Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已恢复工作")),
                       @Variant(id = "disabled",
                                source = "Working Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已暂停工作")),
                       @Variant(id = "disabled_cycle",
                                source = "Working Disabled after current cycle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "本运行周期后暂停工作")),
               })
public interface IControllable {

    /**
     * @return true if the controllable is allowed to work
     */
    boolean isWorkingEnabled();

    /**
     * Set if the controllable can work or not
     *
     * @param isWorkingAllowed true if the workable can work, otherwise false
     */
    @RPCMethod
    void setWorkingEnabled(boolean isWorkingAllowed);

    default void setSuspendAfterFinish(boolean suspendAfterFinish) {}

    default boolean isSuspendAfterFinish() {
        return false;
    }
}
