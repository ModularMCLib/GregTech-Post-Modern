package com.modularmclib.gtpm.common.machine.steam;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.gui.UITemplate;
import com.modularmclib.gtpm.api.gui.element.GTProgressBarElement;
import com.modularmclib.gtpm.api.gui.factory.MachineUIHolder;
import com.modularmclib.gtpm.api.gui.texture.ResourceTexture;
import com.modularmclib.gtpm.api.machine.steam.SteamBoilerMachine;
import com.modularmclib.gtpm.api.machine.trait.WorkLogic;
import com.modularmclib.gtpm.config.ConfigHolder;
import com.modularmclib.gtpm.utils.GTUtil;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamSolarBoiler extends SteamBoilerMachine {

    public SteamSolarBoiler(BlockEntityCreationInfo info, boolean isHighPressure) {
        super(info, isHighPressure);
    }

    @Override
    public @NotNull Direction getFrontFacing() {
        return Direction.UP;
    }

    @Override
    protected long getBaseSteamOutput() {
        return isHighPressure ? ConfigHolder.INSTANCE.machines.smallBoilers.hpSolarBoilerBaseOutput :
                ConfigHolder.INSTANCE.machines.smallBoilers.solarBoilerBaseOutput;
    }

    @Override
    protected void updateSteamSubscription() {
        if (temperatureSubs == null) {
            temperatureSubs = subscribeServerTick(null, this::updateCurrentTemperature);
        }
    }

    @Override
    protected void updateCurrentTemperature() {
        if (GTUtil.canSeeSunClearly(getLevel(), getBlockPos())) {
            getWorkLogic().setStatus(WorkLogic.Status.WORKING);
        } else {
            getWorkLogic().setStatus(WorkLogic.Status.IDLE);
        }
        super.updateCurrentTemperature();
    }

    @Override
    protected int getCooldownInterval() {
        return isHighPressure ? 50 : 45;
    }

    @Override
    protected int getCoolDownRate() {
        return 3;
    }

    @Override
    protected void addAdditionalWidgets(UIElement root, Player player, MachineUIHolder holder) {
        root.addChild(createSolarProgressBar());
    }

    private GTProgressBarElement createSolarProgressBar() {
        var progressTexture = GuiTextures.progressBar(
                (ResourceTexture) GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(isHighPressure));
        GTProgressBarElement progressBar = new GTProgressBarElement(
                () -> GTUtil.canSeeSunClearly(getLevel(), getBlockPos()) ? 1.0 : 0.0)
                .setProgressTexture(progressTexture.getEmptyBarArea(), progressTexture.getFilledBarArea());
        return UITemplate.setBounds(progressBar, 114, 44, 20, 20);
    }

    @Override
    protected void randomDisplayTick(RandomSource random, float x, float y, float z) {}
}
