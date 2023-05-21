package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dto.SaleSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.services.SaleService;

import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

	@Autowired
	private SaleService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
		SaleMinDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/report")
	public ResponseEntity<?> getReport() {
		// TODO
		return null;
	}

	//OK
	@GetMapping(value = "/summary")
	public ResponseEntity<List<SaleSummaryDto>> getSummary(
			@RequestParam(name="minDate", defaultValue = "") String minDate,
			@RequestParam(name="maxDate", defaultValue = "") String maxDate)
	{
		return ResponseEntity.ok(service.saleSummaryDtoList(minDate,maxDate));
	}
}
