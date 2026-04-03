package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.order.OrderNotFoundException;
import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.ItemDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderPaymentDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderShotDTO;
import br.com.daciosoftware.shop.models.entity.order.Order;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import br.com.daciosoftware.shop.order.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final KafkaClientService kafkaClientService;
    private final EntityManager entityManager;
    private final OrderPaymentService orderPaymentService;

    public List<OrderDTO> findAllComplete() {
        List<Order> orders = orderRepository.findAll();
        return orders
                .stream()
                .map(OrderDTO::convert)
                .collect(Collectors.toList());
    }

    public List<OrderShotDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders
                .stream()
                .map(OrderShotDTO::convert)
                .collect(Collectors.toList());
    }

    public OrderDTO findById(Long id) {
        return orderRepository.findById(id).map(OrderDTO::convert).orElseThrow(OrderNotFoundException::new);
    }

    public OrderDTO findByIdAndToken(Long id, String token) {

        OrderDTO orderDTO = findById(id);

        CustomerDTO customerDTO = customerService.getCustomerAuthenticated(token);
        if (!orderDTO.getCustomer().getId().equals(customerDTO.getId())) {
            throw new OrderNotFoundException();
        }
        return orderDTO;
    }

    public List<OrderDTO> findByCustomerIndentifier(Long userId) {
        List<Order> orders = orderRepository.findByCustomerIdentifier(userId);
        return orders
                .stream()
                .map(OrderDTO::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO save(OrderDTO orderDTO, String token) {

        CustomerDTO customerDTO = customerService.getCustomerAuthenticated(token);
        List<ItemDTO> itensDTO = productService.findItens(orderDTO.getItens());
        Float total = itensDTO.stream().map(i -> (i.getPreco() * i.getQuantidade())).reduce((float) 0, Float::sum);

        orderDTO.setDateOrder(LocalDateTime.now());
        orderDTO.setTotal(total);
        orderDTO.setCustomer(customerDTO);
        orderDTO.setItens(itensDTO);
        orderDTO.setStatus(OrderStatus.PENDING);

        Order order = orderRepository.save(Order.convert(orderDTO));

        orderDTO = OrderDTO.convert(order);
        CredcardDTO credcardPrincipal = customerService.getCredcardPrincipalByToken(token);
        orderDTO.setCredcardPrincipal(credcardPrincipal);
        kafkaClientService.sendOrder(orderDTO);

        return orderDTO;
    }

    public OrderDTO update(Long id, OrderDTO orderDTO, String token) {

        OrderDTO orderUpdateDTO = findById(id);

        CustomerDTO customerDTO = customerService.getCustomerAuthenticated(token);
        if (!orderUpdateDTO.getCustomer().getId().equals(customerDTO.getId())) {
            throw new CustomerInvalidKeyException();
        }

        return update(id, orderDTO);
    }

    public OrderDTO update(Long id, OrderDTO orderDTO) {

        OrderDTO orderUpdateDTO = findById(id);

        List<ItemDTO> itensDTO = productService.findItens(orderDTO.getItens());
        Float total = itensDTO.stream().map(i -> (i.getPreco() * i.getQuantidade())).reduce((float) 0, Float::sum);
        orderUpdateDTO.setTotal(total);
        orderUpdateDTO.setItens(itensDTO);

        orderRepository.save(Order.convert(orderUpdateDTO));

        return findById(id);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        orderRepository.updateStatus(orderId, status);
    }

    public void delete(Long id, String token) {
        OrderDTO orderDeleteDTO = findById(id);
        CustomerDTO customerDTO = customerService.getCustomerAuthenticated(token);
        if (!orderDeleteDTO.getCustomer().getId().equals(customerDTO.getId())) {
            throw new CustomerInvalidKeyException();
        }
        delete(id);
    }

    public void delete(Long id) {
        OrderDTO orderDTO = findById(id);
        orderRepository.delete(Order.convert(orderDTO));
    }

    public Page<OrderDTO> findAllPageable(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderDTO::convert);
    }

    public List<OrderDTO> findOrdersByFilters(LocalDate dataInicio, LocalDate dataFim, Float valorMinimo) {

        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select o from orders o \n");
        sbSql.append("where o.data >= :dataInicio \n");

        if (dataFim != null) {
            sbSql.append("and o.data <= :dataFim \n");
        }

        if (valorMinimo != null) {
            sbSql.append("and o.total <= :valorMinimo \n");
        }

        sbSql.append("order by o.data");

        TypedQuery<Order> query = entityManager.createQuery(sbSql.toString(), Order.class);

        query.setParameter("dataInicio", dataInicio.atTime(0, 0));

        if (dataFim != null) {
            query.setParameter("dataFim", dataFim.atTime(23, 59));
        }

        if (valorMinimo != null) {
            query.setParameter("valorMinimo", valorMinimo);
        }

        List<Order> vendas = query.getResultList();

        return vendas.stream().map(OrderDTO::convert).toList();

    }

    public List<OrderDTO> findOrdersCustomerAuthenticated(String token) {
        CustomerDTO customerDTO = customerService.getCustomerAuthenticated(token);
        return findByCustomerIndentifier(customerDTO.getId());
    }

    public OrderDTO getOrderWithPayments(OrderDTO orderDTO) {
        List<OrderPaymentDTO> payments = orderPaymentService.findByOrderId(orderDTO.getId());
        orderDTO.setPayments(payments);
        return orderDTO;
    }

    public Optional<OrderDTO> findLastOrderCustomerAuthenticated(String token) {
        CustomerDTO customerDTO = customerService.getCustomerAuthenticated(token);
        return findByCustomerIndentifier(customerDTO.getId())
                .stream()
                .map(this::getOrderWithPayments)
                .max(Comparator.comparing(OrderDTO::getDateOrder));
    }

}
