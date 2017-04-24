package com.msis.common.service;

import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sanjeev
 * Date: 11/17/12
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
//@XmlRootElement
public class ServiceResponse<T> extends DefServiceResponse<Collection<T>> {
    private Integer totalResults;
    private int startIndex;
    private int maxResults;    

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    
}
