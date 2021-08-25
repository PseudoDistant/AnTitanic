package io.github.farlandercraft.antitanic.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.GameRuleManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoatEntity.class)
public class BoatEntityMixin extends Entity {
	public BoatEntityMixin(World world) {
		super(world);
	}

	// Make the boat not die when it hits a photon.
	@Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V"))
	public void BoatInject(BoatEntity boatEntity) {

	}
	// Make the boat not spawn Wooden Pickaxe recipe when it hits a photon.
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRuleManager;getBoolean(Ljava/lang/String;)Z"))
	public boolean BoatInject2(GameRuleManager gameRuleManager, String name) {
		return false;
	}

	@Shadow public void initDataTracker() {}
	@Shadow public void readCustomDataFromTag(CompoundTag tag) {}
	@Shadow public void writeCustomDataToTag(CompoundTag tag) {}
}
