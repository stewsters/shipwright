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
//        blueprint.spec = ImageIO.read(new File("spec/fighter.png"));
        blueprint.symmetrical = true;
        blueprint.height = 32;
        blueprint.width = 32;
        blueprint.colorPalette = new ColorPalette();


        for(int i = 0; i < 10; i++){
            BufferedImage output = ShipWright.generate(blueprint);
            ImageIO.write(output, "png", new File("output/" + i + ".png") );
        }



    }

}
