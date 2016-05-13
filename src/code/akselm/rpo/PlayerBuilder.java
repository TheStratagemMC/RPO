package code.akselm.rpo;

/**
 * Created by 18AxMoreen on 3/25/2016.
 */

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_9_R2.*;

import java.net.InetSocketAddress;
import java.util.UUID;


public class PlayerBuilder {
    private MinecraftServer server;
    private WorldServer world;
    private UUID uuid;
    private GameProfile profile;
    private PlayerInteractManager manager;
    private WorldSettings.EnumGamemode mode;
    private int ping;

    private EntityPlayer player;

    public PlayerBuilder(String name) {
        this.server = MinecraftServer.getServer();
        this.world = server.getWorldServer(0);
        this.uuid = UUID.randomUUID();
        this.profile = new GameProfile(uuid, name);
        this.manager = new PlayerInteractManager(world);
        this.mode = WorldSettings.EnumGamemode.SURVIVAL;
        this.ping = 0;
    }

    public PlayerBuilder withName(String name) {
        this.profile = new GameProfile(this.profile.getId(), name);
        return this;
    }
    public PlayerBuilder withUniqueId(UUID id) {
        this.uuid = id;
        this.profile = new GameProfile(uuid, this.profile.getName());
        return this;
    }
    public PlayerBuilder withGameMode(WorldSettings.EnumGamemode mode) {
        this.mode = mode;
        return this;
    }
    public PlayerBuilder withPing(int ping) {
        this.ping = ping;
        return this;
    }

    public EntityPlayer create() {
        this.player = new EntityPlayer(this.server, this.world, this.profile, this.manager);
        this.player.playerConnection = new PlayerConnection(server, new NetworkManager(EnumProtocolDirection.SERVERBOUND), player);
        this.player.a(this.mode);
        this.player.ping = this.ping;
        this.player.playerConnection.networkManager.l = new InetSocketAddress(0);
        //this.player.playerConnection.networkManager.l = InetSocketAddress.createUnresolved()
        //new ServerBootstrap().channel(LocalServerChannel.class);

        return this.player;
    }
}