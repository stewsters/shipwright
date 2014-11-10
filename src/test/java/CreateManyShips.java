import com.stewsters.shipwright.Blueprint;
import com.stewsters.shipwright.ColorPalette;
import com.stewsters.shipwright.ShipWright;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CreateManyShips {


    @Test
    public void createManyShips() throws IOException {
        Blueprint blueprint = new Blueprint();
        blueprint.symmetrical = true;
        blueprint.height = 256;
        blueprint.width = 256;

        ColorPalette totalColors = new ColorPalette();
        totalColors.generate();

        File dir = new File("input");
        for (File file : dir.listFiles()) {
            if (!file.getName().endsWith(".png"))
                continue;

            blueprint.spec = ImageIO.read(file);

            for (int i = 0; i < 10; i++) {
                blueprint.colorPalette = totalColors.sub();
                BufferedImage output = ShipWright.generate(blueprint);
                ImageIO.write(output, "png", new File("output/" + file.getName().split("\\.")[0] + i + ".png"));
            }

        }

    }

}
