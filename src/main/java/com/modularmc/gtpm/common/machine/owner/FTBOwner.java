package com.modularmc.gtpm.common.machine.owner;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Trans(key = "gtpm.ownership.name.ftb",
       source = "FTB Teams",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "FTB团队"))
public non-sealed class FTBOwner extends MachineOwner {

    private static final Component displayName = Component.translatable("gtpm.ownership.name.ftb");

    public FTBOwner(UUID playerUUID) {
        super(playerUUID);
    }

    public @Nullable Team getPlayerTeam(UUID playerUUID) {
        if (FTBTeamsAPI.api().isManagerLoaded()) {
            return FTBTeamsAPI.api().getManager().getPlayerTeamForPlayerID(playerUUID).orElse(null);
        } else if (FTBTeamsAPI.api().isClientManagerLoaded()) {
            return FTBTeamsAPI.api().getClientManager().getTeams().stream()
                    .filter(t -> t.getMembers().contains(playerUUID))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }

    public @Nullable Team getTeam() {
        return getPlayerTeam(playerUUID);
    }

    @UnmodifiableView
    @Override
    public @NotNull Set<UUID> getMembers() {
        var team = getTeam();
        if (team == null) return Collections.emptySet();
        if (team.isPlayerTeam()) {
            return Collections.unmodifiableSet(((PlayerTeam) team).getEffectiveTeam().getMembers());
        } else if (team.isPartyTeam() || team.isServerTeam()) {
            return Collections.unmodifiableSet(team.getMembers());
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isPlayerInTeam(UUID playerUUID) {
        if (this.playerUUID.equals(playerUUID)) return true;
        if (FTBTeamsAPI.api().isManagerLoaded()) {
            return FTBTeamsAPI.api().getManager().arePlayersInSameTeam(playerUUID, this.playerUUID);
        } else if (FTBTeamsAPI.api().isClientManagerLoaded()) {
            var ownTeam = getPlayerTeam(this.playerUUID);
            if (ownTeam == null) {
                return false;
            }
            var otherTeam = getPlayerTeam(playerUUID);
            return otherTeam != null && ownTeam.getTeamId().equals(otherTeam.getTeamId());
        } else {
            return true;
        }
    }

    @Override
    public boolean isPlayerFriendly(UUID playerUUID) {
        var team = getTeam();
        if (team == null) {
            return this.playerUUID.equals(playerUUID);
        }
        return team.getRankForPlayer(playerUUID).isAllyOrBetter();
    }

    @Override
    public UUID getUUID() {
        var team = getTeam();
        return team != null ? team.getId() : EMPTY;
    }

    @Override
    public String getName() {
        var team = getTeam();
        return team != null ? team.getName().getString() :
                Component.translatable("gtpm.tooltip.status.trinary.unknown").getString();
    }

    @Override
    public Component getTypeDisplayName() {
        return displayName;
    }

    @Override
    public void displayInfo(List<Component> compList) {
        super.displayInfo(compList);
        compList.add(Component.translatable("behavior.portable_scanner.team_name", getName()));
        MachineOwner.displayPlayerInfo(compList, playerUUID);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof FTBOwner && super.equals(object);
    }

    @Trans(key = "behavior.portable_scanner.team_name",
           source = "§2Team Name: %s§r",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§2团队名称：%s§r"))
    private static final class BehaviorTranslations {}
}
