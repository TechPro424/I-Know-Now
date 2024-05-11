package in.techpro424.iknownow.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Shadow @Nullable public abstract LivingEntity getTarget();

    @Inject(method = "setTarget", at = @At("HEAD"))
    private void sendChatMessage(LivingEntity entity, CallbackInfo ci) {
        if(this.getTarget() != null && entity != null && this.getTarget().getUuid().equals(entity.getUuid())) return;

        if(entity instanceof ServerPlayerEntity player) {

            MobEntity mob = ((MobEntity)((Object)this));
            String mobName = Text.translatable(mob.getType().getTranslationKey()).getString();

            player.sendMessage(Text.of("A " + mobName + " is targeting you (" + (int)mob.getPos().distanceTo(player.getPos()) + " blocks away)"));

            SoundEvents.BLOCK_NOTE_BLOCK_BELL.getKey().ifPresent((key) -> {
                
                player.playSoundIfNotSilent(SoundEvent.of(key.getValue()));
            });
            
            
        }
    }
}
