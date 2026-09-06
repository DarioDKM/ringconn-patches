.class public final Lapp/morphe/patches/ringconn/RingConnSyncPatchKt;
.super Ljava/lang/Object;
.source "RingConnSyncPatch.kt"

# static fields
.field private static final ringconnSyncPatch:Lapp/morphe/patcher/patch/BytecodePatch;
.field private static final manifestPatch:Lapp/morphe/patcher/patch/ResourcePatch;

# direct methods
.method static constructor <clinit>()V
    .locals 6

    # 1. Erzeuge manifestPatch (ResourcePatch, name = null)
    new-instance v3, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt$ManifestBuilder;
    invoke-direct {v3}, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt$ManifestBuilder;-><init>()V
    const/4 v0, 0x0
    const/4 v1, 0x0
    const/4 v2, 0x1
    const/4 v4, 0x3
    const/4 v5, 0x0
    invoke-static/range {v0 .. v5}, Lapp/morphe/patcher/patch/PatchKt;->resourcePatch$default(Ljava/lang/String;Ljava/lang/String;ZLkotlin/jvm/functions/Function1;ILjava/lang/Object;)Lapp/morphe/patcher/patch/ResourcePatch;
    move-result-object v0
    sput-object v0, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt;->manifestPatch:Lapp/morphe/patcher/patch/ResourcePatch;

    # 2. Erzeuge ringconnSyncPatch (BytecodePatch)
    new-instance v3, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt$BytecodeBuilder;
    invoke-direct {v3}, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt$BytecodeBuilder;-><init>()V
    const-string v0, "RingConn Health Data Provider & Embedded Sync"
    const-string v1, "Enables debugging, injects HealthDataProvider and embedded Intervals Direct Dark Dashboard with direct sync."
    const/4 v2, 0x1
    const/4 v4, 0x0
    const/4 v5, 0x0
    invoke-static/range {v0 .. v5}, Lapp/morphe/patcher/patch/PatchKt;->bytecodePatch$default(Ljava/lang/String;Ljava/lang/String;ZLkotlin/jvm/functions/Function1;ILjava/lang/Object;)Lapp/morphe/patcher/patch/BytecodePatch;
    move-result-object v0
    sput-object v0, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt;->ringconnSyncPatch:Lapp/morphe/patcher/patch/BytecodePatch;

    return-void
.end method

.method public static final getRingconnSyncPatch()Lapp/morphe/patcher/patch/BytecodePatch;
    .locals 1
    sget-object v0, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt;->ringconnSyncPatch:Lapp/morphe/patcher/patch/BytecodePatch;
    return-object v0
.end method

.method public static final getManifestPatch()Lapp/morphe/patcher/patch/ResourcePatch;
    .locals 1
    sget-object v0, Lapp/morphe/patches/ringconn/RingConnSyncPatchKt;->manifestPatch:Lapp/morphe/patcher/patch/ResourcePatch;
    return-object v0
.end method
