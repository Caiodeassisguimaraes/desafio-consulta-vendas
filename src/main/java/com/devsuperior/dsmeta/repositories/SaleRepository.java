package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.projections.SaleReportProjection;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

//Consulta no formato SQL do Relatorio de Vendas
    /*SELECT tb_seller.id, tb_sales.date, SUM(tb_sales.amount), tb_seller.name
       FROM tb_sales
       INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id
       WHERE tb_sales.date BETWEEN '2022-06-02' AND '2022-06-16'
       AND LOWER(tb_seller.name) LIKE LOWER('%L%')
       GROUP BY tb_seller.name, tb_sales.date*/

    @Query (nativeQuery = true, value = "SELECT tb_seller.id, tb_sales.date, SUM(tb_sales.amount), tb_seller.name " +
            "FROM tb_sales " +
            "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date BETWEEN :minimunDate AND :maximumDate " +
            "AND LOWER(tb_seller.name) LIKE LOWER(CONCAT('%',:sellerName,'%')) " +
            "GROUP BY tb_seller.name, tb_sales.date")
    Page<SaleReportProjection> querySalesReportSql(LocalDate minimunDate, LocalDate maximumDate, String sellerName, Pageable pageable);

//Consulta no formato SQL do Sumario de Vendas
    /*SELECT tb_seller.name, SUM(tb_sales.amount)
    FROM tb_sales
    INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id
    WHERE tb_sales.date BETWEEN '2022-01-01' AND '2022-06-30'
    GROUP BY tb_seller.name*/

    //OK
    @Query(nativeQuery = true, value = "SELECT tb_seller.name, SUM(tb_sales.amount) " +
            "FROM tb_sales " +
            "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date BETWEEN :minimunDate AND :maximumDate " +
            "GROUP BY tb_seller.name")
    List<SaleSummaryProjection> querySalesSummarySql(LocalDate minimunDate, LocalDate maximumDate);

}
