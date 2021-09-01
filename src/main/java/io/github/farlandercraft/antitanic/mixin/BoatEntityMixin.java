package io.github.farlandercraft.antitanic.mixin;

import io.github.farlandercraft.antitanic.AnTitanic;
import io.github.farlandercraft.antitanic.BoatSensor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.particle.ParticleType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
	@Shadow public abstract boolean damage(DamageSource source, float amount);

	@Shadow private boolean field_8113;
	@Shadow private double field_8116;
	@Shadow private int field_8117;
	@Shadow private double field_8118;
	@Shadow private double field_8119;
	@Shadow private double field_8120;
	@Shadow private double field_8121;
	@Shadow private double field_8122;
	@Shadow private double field_8123;
	@Shadow private double field_8114;

	private double field_8115;
	@Shadow protected abstract boolean canClimb();

	public BoatEntityMixin(World world) {
		super(world);
	}
	//this.canClimb() = true

	/** @author Distant */
	@Overwrite
	public void tick() {
		super.tick();
		if (this.method_7754() > 0) {
			this.method_7750(this.method_7754() - 1);
		}

		if (this.method_7753() > 0.0F) {
			this.method_7749(this.method_7753() - 1.0F);
		}

		this.prevX = this.x;
		this.prevY = this.y;
		this.prevZ = this.z;
		int i = 5;
		double d = 0.0D;

		for(int j = 0; j < i; ++j) {
			double e = this.getBoundingBox().minY + (this.getBoundingBox().maxY - this.getBoundingBox().minY) * (double)(j) / (double)i - 0.125D;
			double f = this.getBoundingBox().minY + (this.getBoundingBox().maxY - this.getBoundingBox().minY) * (double)(j + 1) / (double)i - 0.125D;
			Box box = new Box(this.getBoundingBox().minX, e, this.getBoundingBox().minZ, this.getBoundingBox().maxX, f, this.getBoundingBox().maxZ);
			if (this.world.containsBlockWithMaterial(box, Material.WATER)) {
				d += 1.0D / (double)i;
			}
		}

		double g = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
		double ab;
		double ak;
		int af;
		if (g > 0.2975D) {
			ab = Math.cos((double)this.yaw * 3.141592653589793D / 180.0D);
			ak = Math.sin((double)this.yaw * 3.141592653589793D / 180.0D);

			for(af = 0; (double)af < 1.0D + g * 60.0D; ++af) {
				double m = (this.random.nextFloat() * 2.0F - 1.0F);
				double n = (this.random.nextInt(2) * 2 - 1) * 0.7D;
				double o;
				double p;
				if (this.random.nextBoolean()) {
					o = this.x - ab * m * 0.8D + ak * n;
					p = this.z - ak * m * 0.8D - ab * n;
				} else {
					o = this.x + ab + ak * m * 0.7D;
					p = this.z + ak - ab * m * 0.7D;
				}
				this.world.addParticle(ParticleType.WATER, o, this.y - 0.125D, p, this.velocityX, this.velocityY, this.velocityZ);
			}
		}

		double al;
		double am;
		if (this.world.isClient && this.field_8113) {
			if (this.field_8117 > 0) {
				ab = this.x + (this.field_8118 - this.x) / (double)this.field_8117;
				ak = this.y + (this.field_8119 - this.y) / (double)this.field_8117;
				al = this.z + (this.field_8120 - this.z) / (double)this.field_8117;
				am = MathHelper.wrapDegrees(this.field_8121 - (double)this.yaw);
				this.yaw = (float)((double)this.yaw + am / (double)this.field_8117);
				this.pitch = (float)((double)this.pitch + (this.field_8122 - (double)this.pitch) / (double)this.field_8117);
				--this.field_8117;
				this.updatePosition(ab, ak, al);
				this.setRotation(this.yaw, this.pitch);
			} else {
				ab = this.x + this.velocityX;
				ak = this.y + this.velocityY;
				al = this.z + this.velocityZ;
				this.updatePosition(ab, ak, al);
				if (this.onGround) {
					this.velocityX *= 0.5D;
					this.velocityY *= 0.5D;
					this.velocityZ *= 0.5D;
				}

				this.velocityX *= 0.9900000095367432D;
				this.velocityY *= 0.949999988079071D;
				this.velocityZ *= 0.9900000095367432D;
			}

		} else {
			if (d < 1.0D) {
				ab = d * 2.0D - 1.0D;
				this.velocityY += 0.03999999910593033D * ab;
			} else {
				if (this.velocityY < 0.0D) {
					this.velocityY /= 2.0D;
				}

				this.velocityY += 0.007000000216066837D;
			}

			if (this.rider instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity)this.rider;
				float aa = this.rider.yaw + -livingEntity.sidewaysSpeed * 90.0F;
				this.velocityX += -Math.sin(aa * 3.1415927F / 180.0F) * this.field_8116 * (double)livingEntity.forwardSpeed * 0.05000000074505806D;
				this.velocityZ += Math.cos(aa * 3.1415927F / 180.0F) * this.field_8116 * (double)livingEntity.forwardSpeed * 0.05000000074505806D;
			}

			ab = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
			if (ab > 0.35D) {
				ak = 0.35D / ab;
				this.velocityX *= ak;
				this.velocityZ *= ak;
				ab = 0.35D;
			}

			if (ab > g && this.field_8116 < 0.35D) {
				this.field_8116 += (0.35D - this.field_8116) / 35.0D;
				if (this.field_8116 > 0.35D) {
					this.field_8116 = 0.35D;
				}
			} else {
				this.field_8116 -= (this.field_8116 - 0.07D) / 35.0D;
				if (this.field_8116 < 0.07D) {
					this.field_8116 = 0.07D;
				}
			}

			int aj;
			for(aj = 0; aj < 4; ++aj) {
				int ae = MathHelper.floor(this.x + ((double)(aj % 2) - 0.5D) * 0.8D);
				af = MathHelper.floor(this.z + ((double)(aj / 2) - 0.5D) * 0.8D);

				for(int ag = 0; ag < 2; ++ag) {
					int ah = MathHelper.floor(this.y) + ag;
					BlockPos blockPos = new BlockPos(ae, ah, af);
					Block block = this.world.getBlockState(blockPos).getBlock();
					if (block == Blocks.SNOW_LAYER) {
						this.world.setAir(blockPos);
						this.horizontalCollision = false;
					} else if (block == Blocks.LILY_PAD) {
						this.horizontalCollision = false;
					} else if (AnTitanic.config.iceBreaksBoats) {
						BoatSensor boatSensor = new BoatSensor(blockPos, world, this.getHorizontalDirection());
						if (boatSensor.titanicMoment()) {
							if (Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityZ, 2)) >= 0.285f) {
								this.damage(DamageSource.GENERIC, 400);
							}
						}
					}
				}
			}

			if (this.onGround) {
				this.velocityX *= 0.5D;
				this.velocityY *= 0.5D;
				this.velocityZ *= 0.5D;
			}

			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.velocityX *= 0.9900000095367432D;
			this.velocityY *= 0.949999988079071D;
			this.velocityZ *= 0.9900000095367432D;

			this.pitch = 0.0F;
			ak = this.yaw;
			al = this.prevX - this.x;
			am = this.prevZ - this.z;
			if (al * al + am * am > 0.001D) {
				ak = (float)(MathHelper.atan2(am, al) * 180.0D / 3.141592653589793D);
			}

			double an = MathHelper.wrapDegrees(ak - (double)this.yaw);
			if (an > 20.0D) {
				an = 20.0D;
			}

			if (an < -20.0D) {
				an = -20.0D;
			}

			this.yaw = (float)((double)this.yaw + an);
			this.setRotation(this.yaw, this.pitch);
			if (!this.world.isClient) {
				List<Entity> list = this.world.getEntitiesIn(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
				if (list != null && !list.isEmpty()) {
					for (Entity entity : list) {
						if (entity != this.rider && entity.isPushable()) {
							if (entity instanceof BoatEntity) {
								entity.pushAwayFrom(this);
							} else {
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
					}
				}

				if (this.rider != null && this.rider.removed) {
					this.rider = null;
				}

			}
		}
	}

	public void breakBoat() {
		if (this.world.getGameRules().getBoolean("doEntityDrops") && !this.removed) {
			this.remove();
			this.dropItem(Items.BOAT, 1, 0.0F);
		}
	}

	/** @author Distant */
	@Overwrite
	public void fall(double heightDifference, boolean onGround, Block landedBlock, BlockPos landedPosition) {
		if (onGround) {
			if (this.fallDistance > 15.0F) {
				if (!this.world.isClient && !this.removed) {
					breakBoat();
					this.rider.updatePosition(this.x, this.y + 1, this.z);
				}
				this.fallDistance = 0.0F;
			}
		} else if (this.world.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.WATER && heightDifference < 0.0D) {
			this.fallDistance = (float)((double)this.fallDistance - heightDifference);
		}
	}
	@Shadow public int method_7754() {return this.dataTracker.getInt(17);}
	@Shadow public void method_7750(int i) {}
	@Shadow public float method_7753() {return this.dataTracker.getFloat(19);}
	@Shadow public void method_7749(float f) {}

	@Shadow protected void initDataTracker() {}
	@Shadow protected void readCustomDataFromTag(CompoundTag tag) {}
	@Shadow protected void writeCustomDataToTag(CompoundTag tag) {}
}
