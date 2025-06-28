package com.zephbyte.streamertag.mixin;

import com.zephbyte.streamertag.StreamerTagNbt;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class StreamerTagPlayerMixin {
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeStreamerTag(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("streamerTagEnabled", StreamerTagNbt.INSTANCE.getStreamerTagEnabled((ServerPlayerEntity)(Object)this));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readStreamerTag(NbtCompound nbt, CallbackInfo ci) {
        StreamerTagNbt.INSTANCE.setStreamerTagEnabled(
                (ServerPlayerEntity)(Object)this,
                nbt.getBoolean("streamerTagEnabled").orElse(false)
        );
    }
}