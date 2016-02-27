package im.tretyakov.modelmapper.testables;

import im.tretyakov.modelmapper.MappedProperty;
import im.tretyakov.modelmapper.MappedView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Another testable view class
 *
 * Created by talbot on 27.02.16.
 */
@MappedView
public class AnotherTestableView implements Serializable {

    private static final long serialVersionUID = 2L;

    private Integer id;

    private Date date;

    private String value;

    private Boolean aBoolean;

    @MappedProperty(exclude = true)
    private BigDecimal excludedValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean isABoolean() {
        return aBoolean;
    }

    public void setABoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public BigDecimal getExcludedValue() {
        return excludedValue;
    }

    public void setExcludedValue(BigDecimal excludedValue) {
        this.excludedValue = excludedValue;
    }
}
