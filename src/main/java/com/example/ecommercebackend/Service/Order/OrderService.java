package com.example.ecommercebackend.Service.Order;

import com.example.ecommercebackend.Dto.OrderDto;
import com.example.ecommercebackend.Dto.OrderItemDto;
import com.example.ecommercebackend.Enums.OrderStatus;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Cart;
import com.example.ecommercebackend.Model.Order;
import com.example.ecommercebackend.Model.OrderItem;
import com.example.ecommercebackend.Model.Product;
import com.example.ecommercebackend.Repository.Order.IOrderRepository;
import com.example.ecommercebackend.Repository.Product.ProductRepository;
import com.example.ecommercebackend.Service.Cart.ICartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService implements IOrderService{

    private final ModelMapper modelMapper;
    private IOrderRepository orderRepository;
    private ProductRepository productRepository;
    private ICartService cartService;

    @Autowired
     public OrderService(IOrderRepository orderRepository, ProductRepository productRepository, ICartService cartService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }


    @Transactional
    @Override
    public Order placeorder(Long userid) {
        Cart cart=cartService.getCartByUserId(userid);

        if(cart.getCartitem().isEmpty()){throw new ResourceNotFoundException("Please add the Items in the Cart");}
        Order order=createorder(cart);
        List<OrderItem> orderItems=createOrderItems(cart,order);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(getTotalPrice(orderItems));
        orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return order;

    }

    public Order createorder(Cart cart) {
        Order order=new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;

    }

    public List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getCartitem().stream().map(cartitem->{
           Product product= cartitem.getProduct();
           product.setInventory(product.getInventory()-cartitem.getQuantity());
           productRepository.save(product);
           return new OrderItem(order,product,cartitem.getQuantity(),cartitem.getUnitprice());
        }).toList();

    }

    public BigDecimal getTotalPrice(List<OrderItem> orderItems) {

        return orderItems.stream().
                map(item->item.getUnitPrice().
                        multiply(new BigDecimal(item.getQuantity()))).
                reduce(BigDecimal.ZERO,BigDecimal::add);

    }

    @Override
    public OrderDto getorder(Long orderid) {
        return orderRepository.findById(orderid).map(this::convertToOrderDto)
                .orElseThrow(()->new ResourceNotFoundException("Order not Found"));
    }

    public OrderDto convertToOrderDto(Order order) {

        OrderDto orderDto=modelMapper.map(order,OrderDto.class);
        orderDto.setUserid(order.getUser().getId());

        List<OrderItemDto> orderItem=order.getOrderItems().stream().map(orderitem->{
                OrderItemDto orderItemDto=new OrderItemDto();
                orderItemDto.setProductName(orderitem.getProduct().getName());
                orderItemDto.setQuantity(orderitem.getQuantity());
                orderItemDto.setProductPrice(orderitem.getUnitPrice().multiply(new  BigDecimal(orderitem.getQuantity())));
                orderItemDto.setQuantity(orderitem.getQuantity());
                orderItemDto.setBrand(orderitem.getProduct().getBrand());
                orderItemDto.setProductId(orderitem.getProduct().getId());
                return orderItemDto;
        }).toList();

        orderDto.setOrderItems(orderItem);
        return orderDto;

    }

    @Override
    public List<OrderDto> getUserorders(Long userid) {
        List<Order> orders=orderRepository.findByUserId(userid);

        if(orders.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        List<OrderDto> orderDto=orders.stream()
                .map(order->{
                    return convertToOrderDto(order);
                }).toList();


        return orderDto;

    }


    @Override
    public void deleteorder(Long orderid){

        Order order=orderRepository.findById(orderid).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);

    }
}
