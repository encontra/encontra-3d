package pt.inevo.encontra.threed.descriptors.utils.view;

import pt.inevo.encontra.threed.descriptors.utils.Scene;

public class SceneRenderFactory {
	public static SceneRender create(Scene scene, Scene.Type type) {
		if(type == Scene.Type.Silhouette) {
			return new SceneRenderSilhouette(scene);
		} else if(type == Scene.Type.Contourn) {
			return new SceneRenderContourn(scene);
		} else if(type == Scene.Type.Sketch) {
			return new SceneRenderSketch(scene);
		} else {
			return new SceneRender(scene);
		}
	}
	
}