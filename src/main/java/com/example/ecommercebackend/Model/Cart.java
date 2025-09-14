package com.example.ecommercebackend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalamount=BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> cartitem;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public void updateTotalAmount(){

        if(cartitem!=null) {
            this.totalamount = this.getCartitem().stream().
                    map(item -> {
                        BigDecimal unitprice = item.getUnitprice();
                        if (unitprice == null) {
                            return BigDecimal.ZERO;
                        }
                        return unitprice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    }).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public void addItem(CartItem item){
        if(cartitem==null){cartitem=new HashSet<>();}
        this.cartitem.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item){
        if(cartitem!=null && cartitem.contains(item)){
        this.cartitem.remove(item);
        item.setCart(null);
        updateTotalAmount();
        }
    }

}
