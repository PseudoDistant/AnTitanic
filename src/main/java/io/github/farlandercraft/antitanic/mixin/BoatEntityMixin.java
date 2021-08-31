package io.github.farlandercraft.antitanic.mixin;

import io.github.farlandercraft.antitanic.AnTitanic;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.List;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
	@Shadow protected abstract boolean canClimb();

	public BoatEntityMixin(World world) {
		super(world);
	}
	//this.canClimb() = true
	@Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V"))
	public void PreventBoatDisintegration(BoatEntity boatEntity) {
		this.velocityX *= 0.9900000095367432D;
		this.velocityY *= 0.949999988079071D;
		this.velocityZ *= 0.9900000095367432D;
	}


	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/Item;IF)Lnet/minecraft/entity/ItemEntity;"))
	public ItemEntity RemoveItemDrops(BoatEntity boatEntity, Item item, int i, float yOffset) {
		return null;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesIn(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
	public void BoatRammer(CallbackInfo ci) {
		List<Entity> list = this.world.getEntitiesIn(this, this.getBoundingBox().expand(0.20000000298023224D,
				0.0D, 0.20000000298023224D));
		if (list != null && !list.isEmpty()) {
			for(int ao = 0; ao < list.size(); ++ao) {
				Entity entity = (Entity)list.get(ao);
				if (entity != this.rider && entity.isPushable()) {
					if (entity instanceof BoatEntity) {
						entity.pushAwayFrom(this);
					} else {
						if (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) >= 0.23D && AnTitanic.config.boatsDealDamage) {
							if (true) {
								world.createExplosion(this, this.x, this.y, this.z,
										(float) (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) - 0.15D) * 55, true, true);
							} else {
								entity.damage(DamageSource.thrownProjectile(this, this.rider),
									((float) (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) - 0.15D) * 55));
							}

						}
					}
				}
			}
		}
	}


	/** @author Distant */
	@Overwrite
	public void fall(double heightDifference, boolean onGround, Block landedBlock, BlockPos landedPosition) {
		if (onGround) {
			if (this.fallDistance > 15.0F) {
				if (!this.world.isClient && !this.removed) {
					this.remove();
					if (this.world.getGameRules().getBoolean("doEntityDrops")) {
						this.dropItem(Items.BOAT, 1, 0.0F);
					}
					this.rider.updatePosition(this.x, this.y + 1, this.z);
				}
				this.fallDistance = 0.0F;
			}
		} else if (this.world.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.WATER && heightDifference < 0.0D) {
			this.fallDistance = (float)((double)this.fallDistance - heightDifference);
		}
	}

	@Shadow public void initDataTracker() {}
	@Shadow public void readCustomDataFromTag(CompoundTag tag) {}
	@Shadow public void writeCustomDataToTag(CompoundTag tag) {}
}
