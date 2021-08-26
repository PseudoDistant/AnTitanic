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
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V"))
	public void BoatInject(BoatEntity boatEntity) {
		System.out.println();
	}
	// Make the boat not spawn Wooden Pickaxe recipe when it hits a photon.
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;method_6944(Lnet/minecraft/item/Item;IF)Lnet/minecraft/entity/ItemEntity;"))
	public ItemEntity BoatInject2(BoatEntity boatEntity, Item item, int i, float f) {
		return null;
	}

	@Shadow	protected void initDataTracker() {}
	@Shadow protected void readCustomDataFromTag(CompoundTag tag) {}
	@Shadow protected void writeCustomDataToTag(CompoundTag tag) {}
}
