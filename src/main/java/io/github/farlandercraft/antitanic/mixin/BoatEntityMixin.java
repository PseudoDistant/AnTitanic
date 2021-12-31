package io.github.farlandercraft.antitanic.mixin;

import io.github.farlandercraft.antitanic.AnTitanic;
import io.github.farlandercraft.antitanic.BoatSensor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRuleManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
	@Shadow public abstract boolean damage(DamageSource source, float amount);

	@Shadow protected abstract boolean canClimb();

	public BoatEntityMixin(World world) {
		super(world);
	}
	//this.canClimb() = true

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void titanicInject(CallbackInfo ci, int i, double d, double g, int ad, int ae, int af, int ag, int ah, BlockPos blockPos) {
		if (AnTitanic.config.iceBreaksBoats) {
			BoatSensor boatSensor = new BoatSensor(blockPos, world, this.getHorizontalDirection());
			if (boatSensor.titanicMoment()) {
				if (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) >= 0.285f) {
					this.damage(DamageSource.GENERIC, 400);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void antitanicDamage(CallbackInfo ci, List list, int ao) {
		Entity entity = (Entity)list.get(ao);
		if (entity != this.rider && entity.isPushable()) {
			if (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) >= 0.23D && AnTitanic.config.boatsDealDamage) {
				if (AnTitanic.config.volatileBoats) {
					world.createExplosion(this, this.x, this.y, this.z,
							(float) (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) - 0.15D) * 55, true, true);
				} else {
					entity.damage(DamageSource.thrownProjectile(this, this.rider),
							((float) (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) - 0.15D) * 55));
				}
			}
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRuleManager;getBoolean(Ljava/lang/String;)Z"))
	public boolean cancelBoatBreak(GameRuleManager gameRuleManager, String name) {
		return false;
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V"))
	public void antitanic(BoatEntity boatEntity) {}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;move(DDD)V"))
	public void movementSpeedMultiplier(BoatEntity boatEntity, double velocityX, double velocityY, double velocityZ) {
		this.move(velocityX * AnTitanic.config.speedMultiplier,
				velocityY * AnTitanic.config.speedMultiplier,
				velocityZ * AnTitanic.config.speedMultiplier);
	}

	@Redirect(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRuleManager;getBoolean(Ljava/lang/String;)Z"))
	public boolean cancelBoatBreakTheSqueakuel(GameRuleManager gameRuleManager, String name) {
		return false;
	}

	@Redirect(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V"))
	public void runBreakBoatInFall(BoatEntity boatEntity) {
		if (this.fallDistance > 15.0F) {
			breakBoat();
			this.rider.updatePosition(this.x, this.y + 1, this.z);
			this.fallDistance = 0.0F;
		}
	}

	@Redirect(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;handleFallDamage(FF)V"))
	public void dontHandleFallDamage(BoatEntity boatEntity, float fallDistance, float damageMultiplier) {}

	public void breakBoat() {
		if (this.world.getGameRules().getBoolean("doEntityDrops") && !this.removed) {
			this.remove();
			this.dropItem(Items.BOAT, 1, 0.0F);
		}
	}

	@Shadow protected void initDataTracker() {}
	@Shadow protected void readCustomDataFromTag(CompoundTag tag) {}
	@Shadow protected void writeCustomDataToTag(CompoundTag tag) {}
}
