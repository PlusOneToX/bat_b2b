package com.bat.financial.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 8:55
 */
public class DistributorNameChangeEvent extends ApplicationEvent {
    private Integer id;
    private String name;
    private String companyName;

    public DistributorNameChangeEvent(Object source) {
        super(source);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "DistributorNameChangeEvent{" + "id=" + id + ", name='" + name + '\'' + ", companyName='" + companyName
            + '\'' + ", source=" + source + '}';
    }
}
