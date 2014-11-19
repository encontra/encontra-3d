package pt.inevo.encontra.threed.model;

import pt.inevo.encontra.index.annotation.Indexed;
import pt.inevo.encontra.storage.IEntity;
import pt.inevo.encontra.threed.model.BufferedModel;

public class ThreedModel implements IEntity<Long> {

    private Long id;
    private String filename;
    private transient BufferedModel model;

    public ThreedModel() {
    }

    public ThreedModel(String filename, BufferedModel model) {
        this.filename = filename;
        this.model = model;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Indexed
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Indexed
    public BufferedModel getModel() {
        return model;
    }

    public void setModel(BufferedModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "TestModel{"
                + "id=" + id
                + ", title='" + filename + '\''
                + '}';
    }
}