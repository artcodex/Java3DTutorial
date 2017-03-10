package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abrenner on 3/3/17.
 */
public class OBJLoader {
    public static RawModel loadObjModel(String fileName, Loader loader) {
        FileReader objReader = null;
        try {
            objReader = new FileReader("res/"+fileName+".obj");
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (objReader != null) {
            BufferedReader reader = new BufferedReader(objReader);
            String line = null;
            List<Vector3f> vertices = new ArrayList<>();
            List<Vector3f> normals = new ArrayList<>();
            List<Vector2f> uv = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();

            float[] verticesArray = null;
            float[] uvArray = null;
            float[] normalsArray = null;
            int[] indicesArray = null;

            try {
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 0) {
                        String[] parts = line.split(" ");
                        if (line.startsWith("v ")) {
                            Vector3f vertex = new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                            vertices.add(vertex);
                        } else if (line.startsWith("vt ")) {
                            Vector2f vertex = new Vector2f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
                            uv.add(vertex);
                        } else if (line.startsWith("vn ")) {
                            Vector3f vertex = new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                            normals.add(vertex);
                        } else if (line.startsWith("f ")) {
                            for (int i=1; i <= 3; i++) {
                                if (uvArray == null) {
                                    uvArray = new float[vertices.size()*2];
                                    normalsArray = new float[vertices.size()*3];
                                    verticesArray = new float[vertices.size()*3];
                                }

                                String[] currentVertex = parts[i].split("/");
                                int vertex = Integer.parseInt(currentVertex[0]) - 1;
                                int tex = Integer.parseInt(currentVertex[1]) - 1;
                                int normal = Integer.parseInt(currentVertex[2]) - 1;

                                Vector3f vertexVector = vertices.get(vertex);
                                Vector2f uvVector = uv.get(tex);
                                Vector3f normalVector = normals.get(normal);

                                verticesArray[vertex*3] =  vertexVector.x; verticesArray[vertex*3 + 1] = vertexVector.y; verticesArray[vertex*3 + 2] = vertexVector.z;

                                // Have to flip y position difference between opengl and blender
                                uvArray[vertex*2] =  uvVector.x; uvArray[vertex*2+1] = 1 - uvVector.y;
                                normalsArray[vertex*3] =  normalVector.x; normalsArray[vertex*3+1] = normalVector.y; normalsArray[vertex*3+2] = normalVector.z;
                                indices.add(vertex);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            indicesArray = new int[indices.size()];
            for (int i=0; i < indices.size(); i++) {
                indicesArray[i] = indices.get(i);
            }

            return loader.loadToVAO(verticesArray, uvArray, normalsArray, indicesArray);
        }

        return null;
    }
}
