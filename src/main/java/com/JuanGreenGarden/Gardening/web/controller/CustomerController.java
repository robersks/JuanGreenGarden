package com.JuanGreenGarden.Gardening.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.JuanGreenGarden.Gardening.domain.Exceptions.DifferentDataTypeException;
import com.JuanGreenGarden.Gardening.domain.Exceptions.InvalidIdFormatException;
import com.JuanGreenGarden.Gardening.domain.Exceptions.NotFoundEndPoint;
import com.JuanGreenGarden.Gardening.domain.service.CustomerService;
import com.JuanGreenGarden.Gardening.persistence.entity.Customer;
import com.JuanGreenGarden.Gardening.persistence.entity.CustomerRepresentativeOfficeDTO;
import com.JuanGreenGarden.Gardening.persistence.entity.CustomerSalesRepDTO;
import com.JuanGreenGarden.Gardening.persistence.entity.DTO.CustomerDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/customers")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Obtiene todos los clientes.
     * 
     * @return Una respuesta con una lista de todos los clientes.
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Obtiene un cliente por su ID.
     * 
     * @param customerId El ID del cliente a obtener.
     * @return Una respuesta con el cliente correspondiente al ID especificado.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        try {
            Integer id = Integer.parseInt(customerId);
            Customer customer = customerService.getCustomerById(id);
            if (customer != null) {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            } else {
                throw new NotFoundEndPoint("Customer with ID " + id + " not found");
            }
        } catch (NumberFormatException e) {
            throw new InvalidIdFormatException("Invalid format for customer ID: " + customerId);
        }
    }

    /**
     * Obtiene clientes españoles.
     * 
     * @return Una respuesta con una lista de clientes que son de España.
     */
    @GetMapping("/spanish-customers")
    public ResponseEntity<List<CustomerDTO>> getSpanishCustomers() {
        List<CustomerDTO> customers = customerService.getSpanishCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Obtiene clientes en Madrid con representantes de ventas.
     * 
     * @return Una lista de clientes que están en Madrid y tienen representantes de ventas con los números especificados.
     */
    @GetMapping("/madrid-representatives")
    public List<Customer> getCustomersInMadridWithRepresentatives() {
        List<Integer> employeeNumbers = Arrays.asList(11, 30);
        return customerService.getCustomersInMadridWithSalesRepresentatives(employeeNumbers);
    }


    /**
     * Obtiene una lista de clientes que han realizado pagos.
     *
     * @return Lista de clientes con pagos realizados
     */
    @GetMapping("/with-payments")
    public List<Customer> getCustomersWithPayments() {
        return customerService.getCustomersWithPayments();
    }

    /**
     * Obtiene una lista de clientes que **no** han realizado pagos.
     *
     * @return Lista de clientes sin pagos realizados
     */
    @GetMapping("/without-payments")
    public List<Customer> getCustomersWithoutPayments() {
        return customerService.getCustomersWithoutPayments();
    }

    /**
     * Obtiene una lista de clientes que **no** han realizado pedidos.
     *
     * @return Lista de clientes sin pedidos realizados
     */
    @GetMapping("/without-orders")
    public ResponseEntity<List<Customer>> getCustomersWithoutOrders() {
        List<Customer> customers = customerService.getCustomersWithoutOrders();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

     /**
     * Obtiene una lista de clientes que no han realizado pedidos ni pagos.
     *
     * @return Lista de clientes sin pedidos ni pagos realizados
     */
    @GetMapping("/without-orders-and-payments")
    public ResponseEntity<List<Customer>> getCustomersWithoutOrdersAndPayments() {
        List<Customer> customers = customerService.getCustomersWithoutOrdersAndPayments();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Obtiene clientes con pedidos no pagados.
     *
     * @return Respuesta HTTP con la lista de clientes.
     */
    @GetMapping("/unpaid-orders")
    public ResponseEntity<List<Customer>> getCustomersWithUnpaidOrders() {
        List<Customer> customers = customerService.findCustomersWithOrdersButNoPayments();
        return ResponseEntity.ok(customers);
    }


    /**
     * Obtiene la cantidad de clientes por país.
     *
     * @return Respuesta HTTP con la lista de arreglos de objetos donde cada arreglo contiene el nombre del país y la cantidad de clientes.
     */
    @GetMapping("/count-by-country")
    public ResponseEntity<List<Object[]>> countCustomersByCountry() {
        List<Object[]> customerCountsByCountry = customerService.countCustomersByCountry();
        return ResponseEntity.ok(customerCountsByCountry);
    }

    /**
     * Obtiene el número total de clientes.
     *
     * @return La respuesta HTTP con el número total de clientes.
     */
    @GetMapping("/count-customers")
    public ResponseEntity<Long> getCustomerCount() {
        long customerCount = customerService.countCustomers();
        return new ResponseEntity<>(customerCount, HttpStatus.OK);
    }

    /**
     * Obtiene el número de clientes con domicilio en la ciudad de Madrid.
     *
     * @return La respuesta HTTP con el número de clientes en Madrid.
     */
    @GetMapping("/count-customers-madrid")
    public ResponseEntity<Long> getCustomerCountInMadrid() {
        long customerCount = customerService.countCustomersInMadrid();
        return new ResponseEntity<>(customerCount, HttpStatus.OK);
    }

    /**
     * Obtiene el número de clientes por ciudad que comienza con la letra "M".
     *
     * @return La respuesta HTTP con la lista de ciudades y el número de clientes en cada una.
     */
    @GetMapping("/count-by-city-starting-with-m")
    public ResponseEntity<List<Object[]>> getCustomerCountByCityStartingWithM() {
        List<Object[]> customerCounts = customerService.countCustomersByCityStartingWithM();
        return new ResponseEntity<>(customerCounts, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener el número de clientes que no tienen asignado un representante de ventas.
     *
     * @return El número de clientes sin representante de ventas asignado.
     */
    @GetMapping("/count-without-sales-representative")
    public ResponseEntity<Long> countCustomersWithoutSalesRepresentative() {
        long count = customerService.countCustomersWithoutSalesRepresentative();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


    /**
     * Obtiene la lista de clientes junto con la información de sus representantes de ventas.
     *
     * @return ResponseEntity con la lista de objetos DTO que contienen la información del cliente
     * y su representante de ventas, en caso de éxito (código de estado 200 OK).
     */
    @GetMapping("/sales-representatives")
    public ResponseEntity<List<CustomerSalesRepDTO>> getCustomersWithSalesRepresentatives() {
        List<Customer> customers = customerService.getAllCustomerss();
        List<CustomerSalesRepDTO> customerSalesRepDTOs = CustomerSalesRepDTO.fromCustomers(customers);
        return ResponseEntity.ok(customerSalesRepDTOs);
    }

    /**
     * Obtiene el nombre de los clientes que no hayan hecho pagos y el nombre de sus representantes
     * junto con la ciudad de la oficina a la que pertenece el representante.
     * @return Respuesta HTTP con la lista de objetos que contienen el nombre del cliente, nombre del representante,
     * apellido del representante y ciudad de la oficina.
     */
    @GetMapping("/without-payments-with-city-sales-representative")
    public ResponseEntity<List<Object[]>> getCustomersWithoutPaymentsAndRepresentatives() {
        List<Object[]> result = customerService.findCustomersWithoutPaymentsAndRepresentatives();
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene la lista de nombres de clientes junto con la información de sus representantes de ventas
     * y la ciudad de la oficina del representante.
     *
     * @return ResponseEntity con la lista de objetos DTO que contienen el nombre del cliente, el nombre
     * y apellido del representante de ventas, y la ciudad de la oficina del representante, en caso de éxito
     * (código de estado 200 OK).
     */
    @GetMapping("/names-representatives-customers-city")
    public ResponseEntity<List<CustomerRepresentativeOfficeDTO>> getCustomerNamesAndRepresentativesWithOfficeCity() {
        List<Object[]> results = customerService.getCustomerNamesAndRepresentativesWithOfficeCity();
        List<CustomerRepresentativeOfficeDTO> dtos = new ArrayList<>();

        for (Object[] result : results) {
            CustomerRepresentativeOfficeDTO dto = new CustomerRepresentativeOfficeDTO();
            dto.setCustomerName((String) result[0]);
            dto.setRepresentativeFirstName(String.valueOf(result[1])); // Convertir a String
            dto.setRepresentativeLastName(String.valueOf(result[2]));  // Convertir a String
            dto.setRepresentativeOfficeCity((String) result[3]);
            dtos.add(dto);
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
