/*
 * Copyright (c) 2020 - 2021 Legacy Fabric
 * Copyright (c) 2016 - 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.legacyfabric.fabric.mixin.event.lifecycle;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.world.ServerChunkCache;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;

import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerChunkEvents;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
	@Final
	@Shadow
	private ServerWorld world;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkCache;scheduleUnload(Lnet/minecraft/world/chunk/Chunk;)V"), method = "unloadAll", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void chunkUnload(CallbackInfo ci, ObjectIterator<?> iterator, Chunk chunk) {
		ServerChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.world, chunk);
	}

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ChunkStorage;loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/Chunk;"), method = "loadChunk", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void chunkLoad(int i, int j, CallbackInfoReturnable<Chunk> cir, Chunk chunk) {
		ServerChunkEvents.CHUNK_LOAD.invoker().onChunkLoad(this.world, chunk);
	}
}