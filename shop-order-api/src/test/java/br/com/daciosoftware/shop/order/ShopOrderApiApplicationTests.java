package br.com.daciosoftware.shop.order;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.order.OrderNotFoundException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserKeyTokenDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.ItemDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.entity.order.Order;
import br.com.daciosoftware.shop.order.repository.OrderRepository;
import br.com.daciosoftware.shop.order.service.AuthService;
import br.com.daciosoftware.shop.order.service.CustomerService;
import br.com.daciosoftware.shop.order.service.OrderService;
import br.com.daciosoftware.shop.order.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private AuthService authService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private OrderService orderService;

    @Test
    void findByIdAndTokenReturnsOrderWhenCustomerIsAuthenticated() {
        Long orderId = 1L;
        String token = "validToken";
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer(customerDTO);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        when(authService.getUserAuthenticated(token)).thenReturn(new AuthUserKeyTokenDTO());
        when(customerService.validCustomerKeyAuth("keyToken")).thenReturn(customerDTO);

        OrderDTO result = orderService.findByIdAndToken(orderId, token);

        assertNotNull(result);
        assertEquals(customerDTO.getId(), result.getCustomer().getId());
    }

    @Test
    void findByIdAndTokenThrowsExceptionWhenCustomerIsNotAuthenticated() {
        Long orderId = 1L;
        String token = "invalidToken";
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(2L);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer(customerDTO);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        when(authService.getUserAuthenticated(token)).thenReturn(new AuthUserKeyTokenDTO());
		CustomerDTO clientDTO =  new CustomerDTO();
		clientDTO.setId(3L);
        when(customerService.validCustomerKeyAuth("keyToken")).thenReturn(clientDTO);

        assertThrows(OrderNotFoundException.class, () -> orderService.findByIdAndToken(orderId, token));
    }

    @Test
    void saveCreatesOrderWithCorrectTotal() {
        String token = "validToken";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setItens(List.of(new ItemDTO(1L, 2, 10.0f), new ItemDTO(2L, 1, 20.0f)));
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        when(authService.getUserAuthenticated(token)).thenReturn(new AuthUserKeyTokenDTO());
        when(customerService.validCustomerKeyAuth("keyToken")).thenReturn(customerDTO);
        when(productService.findItens(orderDTO.getItens())).thenReturn(orderDTO.getItens());
        when(orderRepository.save(Order.convert(orderDTO))).thenReturn(Order.convert(orderDTO));

        OrderDTO result = orderService.save(orderDTO, token);

        assertNotNull(result);
        assertEquals(40.0f, result.getTotal());
    }

    @Test
    void deleteThrowsExceptionWhenCustomerIsNotAuthorized() {
        Long orderId = 1L;
        String token = "invalidToken";
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(2L);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer(customerDTO);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        when(authService.getUserAuthenticated(token)).thenReturn(new AuthUserKeyTokenDTO());
		CustomerDTO clientDTO =  new CustomerDTO();
		clientDTO.setId(3L);
		when(customerService.validCustomerKeyAuth("keyToken")).thenReturn(clientDTO);

        assertThrows(CustomerInvalidKeyException.class, () -> orderService.delete(orderId, token));
    }

    @Test
    void findOrdersByFiltersReturnsFilteredOrders() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        Float minValue = 100.0f;

        TypedQuery<Order> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Order()));

        List<OrderDTO> result = orderService.findOrdersByFilters(startDate, endDate, minValue);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}