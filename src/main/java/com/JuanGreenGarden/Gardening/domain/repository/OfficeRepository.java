package com.JuanGreenGarden.Gardening.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JuanGreenGarden.Gardening.persistence.entity.Office;

/**
 * Repositorio para acceder a la tabla de oficinas.
 */
@Repository
public interface OfficeRepository extends JpaRepository<Office, String> {
    
    /**
     * Consulta para obtener los códigos de oficina y las ciudades.
     * 
     * @return Una lista de arreglos de objetos donde cada arreglo contiene el código de la oficina y la ciudad.
     */
    @Query("SELECT o.officeCode, o.city FROM Office o")
    List<Object[]> OfficeCodeAndCity();

    /**
     * Consulta para obtener las oficinas en España.
     * 
     * @return Una lista de arreglos de objetos donde cada arreglo contiene la ciudad y el teléfono de las oficinas en España.
     */
    @Query("SELECT o.city, o.phone FROM Office o WHERE o.country = 'España'")
    List<Object[]> findOfficesInSpain();

    /**
     * Busca las direcciones de las oficinas que tienen clientes en Fuenlabrada.
     * 
     * @return Lista de direcciones de las oficinas con clientes en Fuenlabrada.
     */
    @Query("SELECT DISTINCT o.addressLine1 FROM Office o " +
       "JOIN o.offices e " +
       "JOIN e.employees c " + 
       "WHERE c.city = 'Fuenlabrada'")
    List<String> findOfficeAddressesWithCustomersInFuenlabrada();

    /**
     * Busca las oficinas donde no hay representantes de ventas para productos de la gama 'Frutales'.
     *
     * @return Lista de oficinas que no tienen representantes de ventas para productos de la gama 'Frutales'.
     */
    @Query("SELECT o FROM Office o WHERE o.officeCode NOT IN " +
            "(SELECT e.officeField.officeCode FROM Employee e WHERE e.employeeNumber IN " +
            "(SELECT c.employeeField.employeeNumber FROM Customer c WHERE c.customerNumber IN " +
            "(SELECT od.orderField.customerField.customerNumber FROM OrderDetail od " +
            "WHERE od.productField.productLineField.productLine = 'Frutales')))")
    List<Office> findOfficesWhereNoSalesRepresentativesForFruitProducts();
}
