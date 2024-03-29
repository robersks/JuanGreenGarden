package com.JuanGreenGarden.Gardening.web.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.JuanGreenGarden.Gardening.domain.Exceptions.DifferentDataTypeException;
import com.JuanGreenGarden.Gardening.domain.Exceptions.InvalidIdFormatException;
import com.JuanGreenGarden.Gardening.domain.Exceptions.NotFoundEndPoint;
import com.JuanGreenGarden.Gardening.domain.service.CustomerService;
import com.JuanGreenGarden.Gardening.persistence.entity.Customer;
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

    @GetMapping("/unpaid-orders")
    public ResponseEntity<List<Customer>> getCustomersWithUnpaidOrders() {
        List<Customer> customers = customerService.findCustomersWithOrdersButNoPayments();
        return ResponseEntity.ok(customers);
    }
}
