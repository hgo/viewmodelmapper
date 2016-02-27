package im.tretyakov.modelmapper.testables;

import im.tretyakov.modelmapper.MappedModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Anotger testable model class
 *
 * Created by talbot on 27.02.16.
 */
@MappedModel
public class AnotherTestableModel implements Serializable {

    private static final long serialVersionUID = 4L;

    private Integer id;

    private Date date;

    private String value;

    private BigDecimal excludedValue;

    private Boolean aBoolean;

    public AnotherTestableModel() {
    }

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

    public BigDecimal getExcludedValue() {
        return excludedValue;
    }

    public void setExcludedValue(BigDecimal excludedValue) {
        this.excludedValue = excludedValue;
    }

    public Boolean isABoolean() {
        return aBoolean;
    }

    public void setABoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
}
