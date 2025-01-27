package tilemap;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TileMap {

    public int[][] tileData;
    public int width, height;

    public TileMap(int width, int height, String dataPath){
        try {
            this.width = width;
            this.height = height;
            tileData = new int[height][width];
            InputStream is = getClass().getClassLoader().getResourceAsStream(dataPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null){
                int[] toAdd = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                tileData[lineCount] = toAdd.clone();
                lineCount++;
            }
            br.close();
            is.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
