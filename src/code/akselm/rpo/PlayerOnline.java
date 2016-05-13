package code.akselm.rpo;


import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.EnumProtocolDirection;
import net.minecraft.server.v1_9_R2.NetworkManager;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;


import java.util.UUID;

/**
 * Created by 18AxMoreen on 3/25/2016.
 */
public class PlayerOnline {
    private String name;
    private UUID id;
    private EntityPlayer ep;

    private long timeJoined;

    public PlayerOnline(String name, UUID id) {
        this.name = name;
        this.id = id;
        this.ep  = new PlayerBuilder(name).withUniqueId(id).create();
    }

    public PlayerOnline(String name){
        this.name = name;
        this.ep = new PlayerBuilder(name).create();
        this.id = ep.getUniqueID();
    }

    public void join(CraftServer server){
        NetworkManager nm = new NetworkManager(EnumProtocolDirection.SERVERBOUND);
        server.getHandle().a(nm, ep);
        timeJoined = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void disconnect(){
        ep.playerConnection.disconnect("Bye");
    }
    public UUID getId() {
        return id;
    }

    public EntityPlayer getEp() {
        return ep;
    }

    public long getTimeJoined() {
        return timeJoined;
    }
}
