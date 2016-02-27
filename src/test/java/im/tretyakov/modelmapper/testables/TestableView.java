package im.tretyakov.modelmapper.testables;

import im.tretyakov.modelmapper.MappedProperty;
import im.tretyakov.modelmapper.MappedView;

import java.io.Serializable;
import java.util.List;

/**
 * Testable view class
 *
 * Created by talbot on 27.02.16.
 */
@MappedView
public class TestableView implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String value;

    private List<String> simpleCollection;

    private AnotherTestableView otherObject;

    @MappedProperty(value = "models")
    private List<AnotherTestableView> anotherTestableViewList;

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

    public AnotherTestableView getOtherObject() {
        return otherObject;
    }

    public void setOtherObject(AnotherTestableView otherObject) {
        this.otherObject = otherObject;
    }

    public List<AnotherTestableView> getAnotherTestableViewList() {
        return anotherTestableViewList;
    }

    public void setAnotherTestableViewList(List<AnotherTestableView> anotherTestableViewList) {
        this.anotherTestableViewList = anotherTestableViewList;
    }
}
