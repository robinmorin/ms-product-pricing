package org.test.between.msproductpricing.infrastructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRICES")
public class Price implements Serializable {

    private static final long serialVersionUID = 7480742869731054793L;

    @EmbeddedId
    private PricePK pricePK;

    @Column(name = "START_DATE", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private OffsetDateTime endDate;

    @Column(name = "PRIORITY", nullable = false)
    private Short priority;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal vPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ISO", referencedColumnName = "CURRENCY_ISO")
    private Currency currency;

}
