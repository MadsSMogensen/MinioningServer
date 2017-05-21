package minioning.minioningserver;

import java.util.Map.Entry;
import java.util.UUID;
import minioning.common.data.Entity;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import minioning.clientconnection.Installer;
import minioning.common.data.EntityType;
import minioning.common.data.Location;
import minioning.core.GameServer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Assert;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Jakob and Mads
 */
@Category(IntegrationTest.class)
public class ModuleTest {

    private Entity entity;
    private Entity enemy;
    private Entity enemyOwner;
    private final UUID entityOwnerID = UUID.randomUUID();
    private UUID enemyOwnerID;
    private final UUID enemyOwnerOwnerID = UUID.randomUUID();

    private float runTime = 0;
    private final int maxTime = 5; //seconds
    private float lastTime = System.nanoTime();

    private GameServer gs;
    private Thread t;

    @Before
    public void setData() {

        entity = new Entity(entityOwnerID, "Test", 100, 100);
        entity.setLocation(Location.wilderness);
        entity.setSpawnCount(entity.getMaxMinions());
        entity.setSkillqCurrentCD(entity.getSkillqCD() + 1);

        enemyOwner = new Entity(enemyOwnerOwnerID, "testOwner", 300, 300);
        enemyOwner.setLocation(Location.wilderness);
        enemyOwner.setType(EntityType.PORTAL);
        enemyOwner.setImmobile(true);
        enemyOwner.setSpawnCount(enemyOwner.getMaxMinions());

        enemyOwnerID = enemyOwner.getID();
        enemy = new Entity(enemyOwnerID, "TestEnemy", 113, 113);
        enemy.setLocation(Location.wilderness);
        enemy.setSpawnCount(enemy.getMaxMinions());
        enemy.setType(EntityType.ENEMY);
        enemy.setSpeed(0);
        enemy.setSkillqCD(99999999);
        enemy.setSkillqCurrentCD(0);

        String[] input = new String[]{"", "", entityOwnerID.toString(), "SKILLQ", String.valueOf(113), String.valueOf(113)};
        Installer.getActualTempData().add(input);

        gs = new GameServer();
        gs.getWorld().put(entity.getID(), entity);
        gs.getWorld().put(enemy.getID(), enemy);
        gs.getWorld().put(enemyOwner.getID(), enemyOwner);
        t = new Thread(gs);
    }

    @Test
    public void testAssertEquals() {
        t.start();

        boolean running = true;
        while (running) {
            long currentTime = System.nanoTime();
            float elapsedTime = (currentTime - lastTime);
            elapsedTime /= (float) 1000000000.0;
            lastTime = currentTime;
            runTime += elapsedTime;
            if (runTime >= maxTime) {
                running = false;
            }
        }

        assertThat(enemyHp(), not(equalTo(100)));
        Assert.assertTrue(containsTrees());

        t.interrupt();
    }

    private boolean containsTrees() {
        for (Entry<UUID, Entity> entry : gs.getWorld().entrySet()) {
            System.out.println(entry.getValue().getType());
            if (entry.getValue().getType().equals(EntityType.TREE)) {
                System.out.println("tree found!");
                return true;
            }
        }
        System.out.println("no tree found!");
        return false;
    }

    private int enemyHp() {
        Entity alteredEnemy = gs.getWorld().get(enemy.getID());
        return alteredEnemy.getHp();
    }
}
