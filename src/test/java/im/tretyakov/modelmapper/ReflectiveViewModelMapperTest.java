package im.tretyakov.modelmapper;

import im.tretyakov.modelmapper.testables.AnotherTestableModel;
import im.tretyakov.modelmapper.testables.AnotherTestableView;
import im.tretyakov.modelmapper.testables.TestableModel;
import im.tretyakov.modelmapper.testables.TestableView;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static junit.framework.TestCase.*;

/**
 * Unit tests on mapping
 *
 * Created by talbot on 27.02.16.
 */
public class ReflectiveViewModelMapperTest {

    /**
     * Basic marshalling test
     *
     * @throws Exception
     */
    @Test
    public void testMarshall() throws Exception {
        final ViewModelMapper<TestableView, TestableModel> mapper = new ReflectiveViewModelMapper<>(TestableView.class, TestableModel.class);
        final TestableModel model = new TestableModel();
        model.setId(1000L);
        model.setValue("A Value");
        model.setSimpleCollection(Arrays.asList("A", "B", "C"));
        final AnotherTestableModel anotherModel = new AnotherTestableModel();
        anotherModel.setId(100);
        anotherModel.setValue("Another value");
        anotherModel.setABoolean(true);
        anotherModel.setDate(new Date());
        anotherModel.setExcludedValue(BigDecimal.valueOf(99.95f));
        final AnotherTestableModel anotherModelForCollection = new AnotherTestableModel();
        anotherModelForCollection.setId(101);
        anotherModelForCollection.setValue("Value in collection");
        anotherModelForCollection.setDate(new Date());
        anotherModelForCollection.setABoolean(false);
        model.setOtherObject(anotherModel);
        model.setModels(Collections.singletonList(anotherModelForCollection));

        final TestableView result = mapper.marshall(model);
        assertNotNull(result);
        assertNotNull(result.getOtherObject());
        assertNotNull(result.getAnotherTestableViewList());
        assertFalse(result.getAnotherTestableViewList().isEmpty());
        assertNotNull(result.getAnotherTestableViewList().get(0));
        assertEquals(model.getId(), result.getId());
        assertEquals(model.getValue(), result.getValue());
        assertEquals(model.getModels().get(0).getId(), result.getAnotherTestableViewList().get(0).getId());
        assertEquals(model.getModels().get(0).getValue(), result.getAnotherTestableViewList().get(0).getValue());
        assertEquals(model.getModels().get(0).getDate(), result.getAnotherTestableViewList().get(0).getDate());
        assertEquals(model.getModels().get(0).isABoolean(), result.getAnotherTestableViewList().get(0).isABoolean());
        assertNull(result.getAnotherTestableViewList().get(0).getExcludedValue());
        assertNotNull(result.getOtherObject());
        assertEquals(model.getOtherObject().getId(), result.getOtherObject().getId());
        assertEquals(model.getOtherObject().getValue(), result.getOtherObject().getValue());
        assertEquals(model.getOtherObject().getDate(), result.getOtherObject().getDate());
        assertEquals(model.getOtherObject().isABoolean(), result.getOtherObject().isABoolean());
        assertNull(result.getOtherObject().getExcludedValue());
        assertNotNull(result.getSimpleCollection());
        assertFalse(result.getSimpleCollection().isEmpty());
    }

    /**
     * Basic unmarshalling test
     *
     * @throws Exception
     */
    @Test
    public void testUnMarshall() throws Exception {
        final TestableView view = new TestableView();
        view.setId(10_000L);
        view.setValue("View value");
        view.setSimpleCollection(Arrays.asList("z", "y", "x"));
        final AnotherTestableView otherView = new AnotherTestableView();
        otherView.setId(200);
        otherView.setValue("Another view value");
        otherView.setDate(new Date());
        otherView.setABoolean(false);
        otherView.setExcludedValue(BigDecimal.valueOf(-1L));
        final AnotherTestableView viewForCollection = new AnotherTestableView();
        viewForCollection.setId(201);
        viewForCollection.setValue("Collection view value");
        viewForCollection.setABoolean(true);
        viewForCollection.setDate(new Date());
        viewForCollection.setExcludedValue(BigDecimal.valueOf(1000L));
        view.setOtherObject(otherView);
        view.setAnotherTestableViewList(Collections.singletonList(viewForCollection));

        final ViewModelMapper<TestableView, TestableModel> mapper = new ReflectiveViewModelMapper<>(TestableView.class, TestableModel.class);
        final TestableModel result = mapper.unMarshall(view);
        assertNotNull(result);
        assertNotNull(result.getOtherObject());
        assertNotNull(result.getSimpleCollection());
        assertNotNull(result.getModels());
        assertEquals(view.getId(), result.getId());
        assertEquals(view.getValue(), result.getValue());
        assertFalse(result.getSimpleCollection().isEmpty());
        assertEquals(view.getOtherObject().getId(), result.getOtherObject().getId());
        assertEquals(view.getOtherObject().getValue(), result.getOtherObject().getValue());
        assertEquals(view.getOtherObject().getDate(), result.getOtherObject().getDate());
        assertEquals(view.getOtherObject().isABoolean(), result.getOtherObject().isABoolean());
        assertNull(result.getOtherObject().getExcludedValue());
        assertEquals(view.getAnotherTestableViewList().get(0).getId(), result.getModels().get(0).getId());
        assertEquals(view.getAnotherTestableViewList().get(0).getDate(), result.getModels().get(0).getDate());
        assertEquals(view.getAnotherTestableViewList().get(0).getValue(), result.getModels().get(0).getValue());
        assertEquals(view.getAnotherTestableViewList().get(0).isABoolean(), result.getModels().get(0).isABoolean());
        assertNull(result.getModels().get(0).getExcludedValue());
    }
}