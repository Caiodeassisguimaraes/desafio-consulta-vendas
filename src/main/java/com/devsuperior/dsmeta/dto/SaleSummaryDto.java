package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;

public class SaleSummaryDto {

    //OK

    String name;
    Double total;

    public SaleSummaryDto() {
    }

    public SaleSummaryDto(String name, Double total) {
        this.name = name;
        this.total = total;
    }

    public SaleSummaryDto(SaleSummaryProjection saleSummaryProjection) {
        name = saleSummaryProjection.getName();
        total = saleSummaryProjection.getTotal();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
