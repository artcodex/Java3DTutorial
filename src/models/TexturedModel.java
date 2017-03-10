package models;

import textures.ModelTexture;

/**
 * Created by avibrenner on 3/2/17.
 */
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture modelTexture;

    public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }
}
