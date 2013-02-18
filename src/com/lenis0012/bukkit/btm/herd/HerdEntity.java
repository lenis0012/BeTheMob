public class HerdEntity extends net.minecraft.server.EntityLiving {
	Player leader;
	public HerdEntity(World w, Player leader) {
		super(w);
		this.leader=leader;
	}
	@Override
	public void s_() {
		Zombie z = (Zombie)this.getBukkitEntity();
		followPlayer
		
	}
	public void followPlayer(Player p) {
		//insert code to follow player here, call often.
	}
	public void attack(Entity e) {
		this.setTarget(new CraftLivingEntity((CraftServer)server,e));
	}
}