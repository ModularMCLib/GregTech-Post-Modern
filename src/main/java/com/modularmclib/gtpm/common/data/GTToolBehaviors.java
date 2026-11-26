package com.modularmclib.gtpm.common.data;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.item.tool.behavior.ToolBehaviorType;
import com.modularmclib.gtpm.api.registry.GTRegistries;
import com.modularmclib.gtpm.common.item.tool.behavior.*;

public class GTToolBehaviors {

    private GTToolBehaviors() {}

    // spotless:off
    public static final ToolBehaviorType<AOEConfigUIBehavior> AOE_CONFIG_UI = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("aoe_config_ui"), new ToolBehaviorType<>(AOEConfigUIBehavior.CODEC, AOEConfigUIBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<BlockRotatingBehavior> BLOCK_ROTATING = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("rotate_block"), new ToolBehaviorType<>(BlockRotatingBehavior.CODEC, BlockRotatingBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<DisableShieldBehavior> DISABLE_SHIELD = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("disable_shield"), new ToolBehaviorType<>(DisableShieldBehavior.CODEC, DisableShieldBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<EntityDamageBehavior> ENTITY_DAMAGE = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("entity_damage"), new ToolBehaviorType<>(EntityDamageBehavior.CODEC, EntityDamageBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<GrassPathBehavior> PATH = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("path"), new ToolBehaviorType<>(GrassPathBehavior.CODEC, GrassPathBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<HarvestCropsBehavior> HARVEST_CROPS = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("harvest_crops"), new ToolBehaviorType<>(HarvestCropsBehavior.CODEC, HarvestCropsBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<HarvestIceBehavior> HARVEST_ICE = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("harvest_ice"), new ToolBehaviorType<>(HarvestIceBehavior.CODEC, HarvestIceBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<HoeGroundBehavior> HOE_GROUND = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("hoe_ground"), new ToolBehaviorType<>(HoeGroundBehavior.CODEC, HoeGroundBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<LogStripBehavior> STRIP_LOG = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("strip_log"), new ToolBehaviorType<>(LogStripBehavior.CODEC, LogStripBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<PlungerBehavior> PLUNGER = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("plunger"), new ToolBehaviorType<>(PlungerBehavior.CODEC, PlungerBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<RotateRailBehavior> ROTATE_RAIL = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("rotate_rail"), new ToolBehaviorType<>(RotateRailBehavior.CODEC, RotateRailBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<ScrapeBehavior> SCRAPE = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("scrape"), new ToolBehaviorType<>(ScrapeBehavior.CODEC, ScrapeBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<TorchPlaceBehavior> TORCH_PLACE = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("torch_place"), new ToolBehaviorType<>(TorchPlaceBehavior.CODEC, TorchPlaceBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<TreeFellingBehavior> TREE_FELLING = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("tree_felling"), new ToolBehaviorType<>(TreeFellingBehavior.CODEC, TreeFellingBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<WaxOffBehavior> WAX_OFF = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("wax_off"), new ToolBehaviorType<>(WaxOffBehavior.CODEC, WaxOffBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<ToolModeSwitchBehavior> MODE_SWITCH = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("mode_switch"), new ToolBehaviorType<>(ToolModeSwitchBehavior.CODEC, ToolModeSwitchBehavior.STREAM_CODEC));
    public static final ToolBehaviorType<ProspectingBehavior> PROSPECTING = GTRegistries.register(GTRegistries.TOOL_BEHAVIORS,
            GTPM.id("prospecting"), new ToolBehaviorType<>(ProspectingBehavior.CODEC, ProspectingBehavior.STREAM_CODEC));
    // spotless:on
    public static void init() {}
}
