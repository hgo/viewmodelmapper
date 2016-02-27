package im.tretyakov.modelmapper.testables;

import im.tretyakov.modelmapper.MappedModel;

import java.io.Serializable;
import java.util.List;

/**
 * Testable model class
 *
 * Created by talbot on 27.02.16.
 */
@MappedModel
public class TestableModel implements Serializable {

    private static final long serialVersionUID = 3L;

    private Long id;

    private String value;

    private List<String> simpleCollection;

    private AnotherTestableModel otherObject;

    private List<AnotherTestableModel> models;

    public TestableModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getSimpleCollection() {
        return simpleCollection;
    }

    public void setSimpleCollection(List<String> simpleCollection) {
        this.simpleCollection = simpleCollection;
    }

    public AnotherTestableModel getOtherObject() {
        return otherObject;
    }

    public void setOtherObject(AnotherTestableModel otherObject) {
        this.otherObject = otherObject;
    }

    public List<AnotherTestableModel> getModels() {
        return models;
    }

    public void setModels(List<AnotherTestableModel> models) {
        this.models = models;
    }
}
