package com.medco.eprescription_out_of_stock.Utills;
import com.medco.eprescription_out_of_stock.Dto.Response.User.Prescription.PrescriptionOutOfStockResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private int totalPages;
//    private long totalElements;
    private List<T> response;
}