package textures;

/**
 * Created by avibrenner on 3/2/17.
 */
public class ModelTexture {
    private int textureID;
    private boolean hasTransparency = false;
    private int numberOfRows = 1;

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    private boolean useFakeLighting = false;

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    private float shineDamper = 1;
    private float reflectivity = 0;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public int getID() {
        return textureID;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
