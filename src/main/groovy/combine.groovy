import javax.imageio.ImageIO
import java.awt.image.BufferedImage

println "Combining images for show"


def output = new BufferedImage(1920, 1020, BufferedImage.TYPE_INT_ARGB)
def g = output.getGraphics()

new File("/whereverYouPutCode/shipwright/output/interior").listFiles().eachWithIndex { file, i ->
    println file.name
    def input = ImageIO.read(file)

    int xPos = i % 7;
    int yPos = i / 7;

    g.drawImage(input, xPos * 256, yPos * 256, null)

}

ImageIO.write(output, "png", new File("finalOutput.png"))
