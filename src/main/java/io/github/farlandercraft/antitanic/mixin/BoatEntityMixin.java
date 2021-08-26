package io.github.farlandercraft.antitanic.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
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
		this.velocityX *= 0.00005;
		this.velocityZ *= 0.00005;
	}
	// Make the boat not spawn Wooden Pickaxe recipe when it hits a photon.
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/Item;IF)Lnet/minecraft/entity/ItemEntity;"))
	public ItemEntity BoatInject2(BoatEntity boatEntity, Item item, int i, float yOffset) {
		return null;
	}

	@Shadow public void initDataTracker() {}
	@Shadow public void readCustomDataFromTag(CompoundTag tag) {}
	@Shadow public void writeCustomDataToTag(CompoundTag tag) {}
}
