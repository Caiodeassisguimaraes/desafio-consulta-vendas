package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleReportDto;
import com.devsuperior.dsmeta.dto.SaleSummaryDto;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

    private static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }


    public Page<SaleReportDto> getReport(String start, String end, String name, Pageable pageable) {
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate endDate;
        LocalDate startDate;
        try {
            endDate = LocalDate.parse(end, formatDate);
        } catch (DateTimeParseException e) {
            endDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        }

        try {
            startDate = LocalDate.parse(start, formatDate);
        } catch (DateTimeParseException e) {
            startDate = endDate.minusYears(1L);
        }

        return repository.querySalesReportJpql(name, startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public List<SaleSummaryDto> saleSummaryDtoList(String startDate, String endDate) {
        //DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDate finalDate = "".equals(endDate) ? today : LocalDate.parse(endDate, formatDate);
        LocalDate initialDate = "".equals(startDate) ? finalDate.minusYears(1L) : LocalDate.parse(startDate, formatDate);

        List<SaleSummaryProjection> saleSummaryProjectionList = repository.querySalesSummarySql(initialDate, finalDate);
        List<SaleSummaryDto> convertProjectionToDto = saleSummaryProjectionList.stream().map(obj -> new SaleSummaryDto(obj)).collect(Collectors.toList());
        return convertProjectionToDto;
    }

	/*@Transactional (readOnly = true)
	public List<SaleSummaryDto> saleSummaryDtoList(String startDate, String endDate) {
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate maxDate;
		LocalDate minDate;
		if ("".equals(endDate)) {
			maxDate = LocalDate.now();
		}
		else {
			maxDate = LocalDate.parse(endDate, formatDate);
			System.out.println(maxDate);
		}
		if ("".equals(startDate)) {
			minDate = maxDate.minusYears(1);
		}
		else {
			minDate = LocalDate.parse(startDate, formatDate);
		}

		List<SaleSummaryProjection> saleSummaryProjectionList =(repository.querySalesSummarySql(minDate, maxDate));
		List<SaleSummaryDto> convertProjectionToDto = saleSummaryProjectionList.stream().map(x -> new SaleSummaryDto(x)).collect(Collectors.toList());
		return convertProjectionToDto;
	}*/

}
