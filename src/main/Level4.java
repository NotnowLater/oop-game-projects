package main;

import entity.BossRoomEnterTriggerAction;
import entity.ChangePlayerRespawnTriggerAction;
import entity.Player;
import entity.PlayerAreaTrigger;

public class Level4 extends LevelData{

    public Level4(GameWindow gW, String tileDataPath) {
        super(gW, tileDataPath, new Vector2(340, 84));

        setLevelId(3);

        setPlayer(new Player(gW, new Vector2(1104, 2640)));
//        setPlayer(new Player(gW, new Vector2(306*48, 41*48)));

         getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(1968, 2652)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(2592, 2652)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(4992, 2556)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(6336, 2556)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(6576, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(6816, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(10560, 2604)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(12144, 1356)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(12672, 1356)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(1680, 2508)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(21, new Vector2(10320, 2748)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(22, new Vector2(3456, 2544)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(22, new Vector2(4896, 2544)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(22, new Vector2(10128, 1104)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(22, new Vector2(12144, 1680)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(22, new Vector2(11136, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(23, new Vector2(6528, 2460)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(23, new Vector2(10896, 1116)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(23, new Vector2(7392, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(23, new Vector2(13296, 1356)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(3600, 2256)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(3888, 2256)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(4176, 2256)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(10272, 1296)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(9888, 1296)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(9552, 912)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(9840, 912)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(24, new Vector2(10416, 912)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(30, new Vector2(7968, 2448)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(28, new Vector2(8352, 2450)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(30, new Vector2(7968, 2064)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(25, new Vector2(7824, 1440)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(25, new Vector2(8256, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(9936, 1824)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(28, new Vector2(2160, 2364)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(29, new Vector2(10032, 1452)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(3888, 2496)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(4464, 2400)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(12000, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(12384, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(13344, 2112)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(13584, 2112)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(26, new Vector2(10896, 2160)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(6720, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(9744, 2412)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(10656, 1836)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(9744, 1116)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(13632, 1740)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(14112, 2124)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(27, new Vector2(2928, 2556)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(29, new Vector2(8064, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(29, new Vector2(7776, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(14976, 1980)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(14640, 1980)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(6096, 1596)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(11856, 2172)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(257, new Vector2(9456, 1836)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(2976, 2544)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(4032, 2592)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(6096, 1584)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(8544, 1872)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(10704, 1632)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(9360, 1824)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(12240, 1344)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(13440, 1728)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(15024, 1968)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(15120, 1968)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(12432, 1680)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(11808, 1680)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(258, new Vector2(2400, 2640)));
        getEnemies().add(gW.getEnemyFactory().getEnemy(31, new Vector2(313 * 48, 65 * 48)));

        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(298 * 48, 64 * 48), new Vector2(22 * 48, 6 * 48), true, new BossRoomEnterTriggerAction(new Vector2(309 * 48, 64 * 48))));
        
         getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(106 * 48, 47 * 48), new Vector2(6 * 48, 8 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(115 * 48, 57 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(130 * 48, 27 * 48), new Vector2(6 * 48, 8 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(128 * 48, 33 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(187 * 48, 46 * 48), new Vector2(4 * 48, 6 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(190 * 48, 50 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(234 * 48, 23 * 48), new Vector2(4 * 48, 7 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(236 * 48, 28 * 48))));
        getEnemies().add(new PlayerAreaTrigger(gW, new Vector2(299 * 48, 35 * 48), new Vector2(8 * 48, 8 * 48), false, new ChangePlayerRespawnTriggerAction(new Vector2(301 * 48, 41 * 48))));
    }
}
