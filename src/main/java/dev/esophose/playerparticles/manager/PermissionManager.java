package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public class PermissionManager extends Manager {
    
    private static final String PERMISSION_PREFIX = "playerparticles.";
    
    private enum PPermission {
        EFFECT("effect"),
        STYLE("style"),
        
        FIXED("fixed"),
        FIXED_UNLIMITED("fixed.unlimited"),
        FIXED_CLEAR("fixed.clear"),

        RELOAD("reload"),
        OVERRIDE("override"),

        GUI("gui"),
        
        PARTICLES_UNLIMITED("particles.unlimited"),
        GROUPS_UNLIMITED("groups.unlimited");
        
        private final String permissionString;
        
        PPermission(String permissionString) {
            this.permissionString = permissionString;
        }
        
        /**
         * Checks if a Permissible has a PlayerParticles permission
         * 
         * @param p The Permissible
         * @return True if the Player has permission
         */
        public boolean check(Permissible p) {
            String permission = PERMISSION_PREFIX + this.permissionString;
            return p.hasPermission(permission);
        }
        
        /**
         * Checks if a Permissible has a PlayerParticles permission with a sub-permission
         * 
         * @param p The Permissibhle
         * @param subPermission The sub-permission
         * @return True if the Player has permission
         */
        public boolean check(Permissible p, String subPermission) {
            String permission = PERMISSION_PREFIX + this.permissionString + '.' + subPermission;
            return p.hasPermission(permission);
        }
    }
    
    public PermissionManager(PlayerParticles playerParticles) {
        super(playerParticles);
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

    /**
     * Checks if the given player has reached the max number of particles in their active group
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of particles in their active group
     */
    public boolean hasPlayerReachedMaxParticles(PPlayer pplayer) {
        if (PPermission.PARTICLES_UNLIMITED.check(pplayer.getPlayer()))
            return false;

        return pplayer.getActiveParticles().size() >= Setting.MAX_PARTICLES.getInt();
    }
    
    /**
     * Checks if the given player has reached the max number of saved particle groups
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of saved particle groups
     */
    public boolean hasPlayerReachedMaxGroups(PPlayer pplayer) {
        if (PPermission.GROUPS_UNLIMITED.check(pplayer.getPlayer()))
            return false;

        return pplayer.getParticleGroups().size() - 1 >= Setting.MAX_GROUPS.getInt();
    }
    
    /**
     * Checks if the given player is able to save groups
     * 
     * @param pplayer The player to check
     * @return If the player has permission to save groups
     */
    public boolean canPlayerSaveGroups(PPlayer pplayer) {
        if (PPermission.GROUPS_UNLIMITED.check(pplayer.getPlayer()))
            return true;

        return Setting.MAX_GROUPS.getInt() != 0;
    }
    
    /**
     * Checks if the given player has reached the max number of fixed effects
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of fixed effects
     */
    public boolean hasPlayerReachedMaxFixedEffects(PPlayer pplayer) {
        if (PPermission.FIXED_UNLIMITED.check(pplayer.getPlayer()))
            return false;

        return pplayer.getFixedEffectIds().size() >= Setting.MAX_FIXED_EFFECTS.getInt();
    }

    /**
     * Gets the max distance a fixed effect can be created from the player
     * 
     * @return The max distance a fixed effect can be created from the player
     */
    public int getMaxFixedEffectCreationDistance() {
        return Setting.MAX_FIXED_EFFECT_CREATION_DISTANCE.getInt();
    }
    
    /**
     * Gets the maximum number of particles a player is allowed to use
     * 
     * @param player The player to check
     * @return The maximum number of particles based on the config.yml value, or unlimited
     */
    public int getMaxParticlesAllowed(Player player) {
        if (PPermission.PARTICLES_UNLIMITED.check(player))
            return Integer.MAX_VALUE;

        return Setting.MAX_PARTICLES.getInt();
    }

    /**
     * Checks if a world is enabled for particles to spawn in
     * 
     * @param world The world name to check
     * @return True if the world is disabled
     */
    public boolean isWorldEnabled(String world) {
        return !this.getDisabledWorlds().contains(world);
    }

    /**
     * Gets all the worlds that are disabled
     * 
     * @return All world names that are disabled
     */
    public List<String> getDisabledWorlds() {
        return Setting.DISABLED_WORLDS.getStringList();
    }

    /**
     * Checks if a player has permission to use an effect
     * 
     * @param player The player to check the permission for
     * @param effect The effect to check
     * @return True if the player has permission to use the effect
     */
    public boolean hasEffectPermission(Player player, ParticleEffect effect) {
        return PPermission.EFFECT.check(player, effect.getName());
    }

    /**
     * Checks if a player has permission to use a style
     * Always returns true for 'normal', a player needs at least one style to apply particles
     * 
     * @param player The player to check the permission for
     * @param style The style to check
     * @return If the player has permission to use the style
     */
    public boolean hasStylePermission(Player player, ParticleStyle style) {
        return PPermission.STYLE.check(player, style.getName());
    }

    /**
     * Gets a String List of all effect names a player has permission for
     * 
     * @param p The player to get effect names for
     * @return A String List of all effect names the given player has permission for
     */
    public List<String> getEffectNamesUserHasPermissionFor(Player p) {
        List<String> list = new ArrayList<>();
        for (ParticleEffect pe : ParticleEffect.getSupportedEffects())
            if (this.hasEffectPermission(p, pe))
                list.add(pe.getName());
        return list;
    }
    
    /**
     * Gets a String List of all style names a player has permission for
     * 
     * @param p The player to get style names for
     * @return A String List of all style names the given player has permission for
     */
    public List<String> getStyleNamesUserHasPermissionFor(Player p) {
        List<String> list = new ArrayList<>();
        for (ParticleStyle ps : this.playerParticles.getManager(ParticleStyleManager.class).getStyles())
            if (this.hasStylePermission(p, ps))
                list.add(ps.getName());
        return list;
    }

    /**
     * Gets a String List of all fixable style names a player has permission for
     * 
     * @param p The player to get style names for
     * @return A String List of all fixable style names the given player has permission for
     */
    public List<String> getFixableStyleNamesUserHasPermissionFor(Player p) {
        List<String> list = new ArrayList<>();
        for (ParticleStyle ps : this.playerParticles.getManager(ParticleStyleManager.class).getStyles())
            if (ps.canBeFixed() && this.hasStylePermission(p, ps))
                list.add(ps.getName());
        return list;
    }
    
    /**
     * Gets a List of all effects a player has permission for
     * 
     * @param p The player to get effects for
     * @return A List of all effects the given player has permission for
     */
    public List<ParticleEffect> getEffectsUserHasPermissionFor(Player p) {
        List<ParticleEffect> list = new ArrayList<>();
        for (ParticleEffect pe : ParticleEffect.getSupportedEffects())
            if (this.hasEffectPermission(p, pe))
                list.add(pe);
        return list;
    }

    /**
     * Gets a List of all styles a player has permission for
     * 
     * @param p The player to get styles for
     * @return A List of all styles the given player has permission for
     */
    public List<ParticleStyle> getStylesUserHasPermissionFor(Player p) {
        List<ParticleStyle> list = new ArrayList<>();
        for (ParticleStyle ps : this.playerParticles.getManager(ParticleStyleManager.class).getStyles())
            if (this.hasStylePermission(p, ps))
                list.add(ps);
        return list;
    }

    /**
     * Checks if a player has permission to created fixed effects
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission
     */
    public boolean canUseFixedEffects(Player player) {
        return PPermission.FIXED.check(player);
    }
    
    /**
     * Checks if a player has permission to clear fixed effects
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission to use /pp fixed clear
     */
    public boolean canClearFixedEffects(Player player) {
        return PPermission.FIXED_CLEAR.check(player);
    }

    /**
     * Checks if a player has permission to open the GUI
     *
     * @param player The player to check the permission for
     * @return True if the player has permission to open the GUI
     */
    public boolean canOpenGui(Player player) {
        return PPermission.GUI.check(player);
    }
    
    /**
     * Checks if a player has permission to use /pp reload
     * 
     * @param sender The sender to check the permission for
     * @return True if the sender has permission to reload the plugin's settings
     */
    public boolean canReloadPlugin(CommandSender sender) {
        return PPermission.RELOAD.check(sender);
    }

    /**
     * Checks if a player can use /ppo
     *
     * @param sender The CommandSender to check
     * @return If the player can use /ppo
     */
    public boolean canOverride(CommandSender sender) {
        if (!(sender instanceof Player))
            return true;

        return PPermission.OVERRIDE.check(sender);
    }

}
