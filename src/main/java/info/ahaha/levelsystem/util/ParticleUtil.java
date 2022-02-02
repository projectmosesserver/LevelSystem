package info.ahaha.levelsystem.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtil {
    public ParticleUtil(){

    }

    public void createCircle(Location location , Particle particle, int size ,int count){
        for (int d = 0; d <= 90; d += count) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * size);
            particleLoc.setZ(location.getZ() + Math.sin(d) * size);
            if (particle == Particle.REDSTONE) {
                location.getWorld().spawnParticle(particle, particleLoc, 1, new Particle.DustOptions(Color.WHITE, 5));
            }else {
                location.getWorld().spawnParticle(particle, particleLoc, 1,0,0,0,0);
            }
        }
    }

    public void create3DCircle(Location location , Particle particle,int size){
        for (int i = 0; i < 360; i += 360/ size) {
            double angle = (i * Math.PI / 180);
            double x = size * Math.cos(angle);
            double z = size * Math.sin(angle);
            Location loc = location.add(x, 1, z);
            loc.getWorld().spawnParticle(particle,loc,1,0,0,0,0);
        }
    }
}
