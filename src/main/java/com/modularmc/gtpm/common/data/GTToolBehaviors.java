package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.item.tool.behavior.ToolBehaviorType;
import com.modularmc.gtpm.common.item.tool.behavior.*;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTToolBehaviors {

    private GTToolBehaviors() {}

    // spotless:off
    public static final ToolBehaviorType<AOEConfigUIBehavior> AOE_CONFIG_UI = REGISTRATE
            .toolBehavior("aoe_config_ui", new ToolBehaviorType<>(AOEConfigUIBehavior.CODEC, AOEConfigUIBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<BlockRotatingBehavior> BLOCK_ROTATING = REGISTRATE
            .toolBehavior("rotate_block", new ToolBehaviorType<>(BlockRotatingBehavior.CODEC, BlockRotatingBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<DisableShieldBehavior> DISABLE_SHIELD = REGISTRATE
            .toolBehavior("disable_shield", new ToolBehaviorType<>(DisableShieldBehavior.CODEC, DisableShieldBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<EntityDamageBehavior> ENTITY_DAMAGE = REGISTRATE
            .toolBehavior("entity_damage", new ToolBehaviorType<>(EntityDamageBehavior.CODEC, EntityDamageBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<GrassPathBehavior> PATH = REGISTRATE
            .toolBehavior("path", new ToolBehaviorType<>(GrassPathBehavior.CODEC, GrassPathBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<HarvestCropsBehavior> HARVEST_CROPS = REGISTRATE
            .toolBehavior("harvest_crops", new ToolBehaviorType<>(HarvestCropsBehavior.CODEC, HarvestCropsBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<HarvestIceBehavior> HARVEST_ICE = REGISTRATE
            .toolBehavior("harvest_ice", new ToolBehaviorType<>(HarvestIceBehavior.CODEC, HarvestIceBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<HoeGroundBehavior> HOE_GROUND = REGISTRATE
            .toolBehavior("hoe_ground", new ToolBehaviorType<>(HoeGroundBehavior.CODEC, HoeGroundBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<LogStripBehavior> STRIP_LOG = REGISTRATE
            .toolBehavior("strip_log", new ToolBehaviorType<>(LogStripBehavior.CODEC, LogStripBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<PlungerBehavior> PLUNGER = REGISTRATE
            .toolBehavior("plunger", new ToolBehaviorType<>(PlungerBehavior.CODEC, PlungerBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<RotateRailBehavior> ROTATE_RAIL = REGISTRATE
            .toolBehavior("rotate_rail", new ToolBehaviorType<>(RotateRailBehavior.CODEC, RotateRailBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<ScrapeBehavior> SCRAPE = REGISTRATE
            .toolBehavior("scrape", new ToolBehaviorType<>(ScrapeBehavior.CODEC, ScrapeBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<TorchPlaceBehavior> TORCH_PLACE = REGISTRATE
            .toolBehavior("torch_place", new ToolBehaviorType<>(TorchPlaceBehavior.CODEC, TorchPlaceBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<TreeFellingBehavior> TREE_FELLING = REGISTRATE
            .toolBehavior("tree_felling", new ToolBehaviorType<>(TreeFellingBehavior.CODEC, TreeFellingBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<WaxOffBehavior> WAX_OFF = REGISTRATE
            .toolBehavior("wax_off", new ToolBehaviorType<>(WaxOffBehavior.CODEC, WaxOffBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<ToolModeSwitchBehavior> MODE_SWITCH = REGISTRATE
            .toolBehavior("mode_switch", new ToolBehaviorType<>(ToolModeSwitchBehavior.CODEC, ToolModeSwitchBehavior.STREAM_CODEC)).register();
    public static final ToolBehaviorType<ProspectingBehavior> PROSPECTING = REGISTRATE
            .toolBehavior("prospecting", new ToolBehaviorType<>(ProspectingBehavior.CODEC, ProspectingBehavior.STREAM_CODEC)).register();
    // spotless:on
    public static void init() {}
}
