package main001.server.domain.attachment.image.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main001.server.audit.BaseTimeEntity;
import main001.server.domain.portfolio.entity.Portfolio;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImageAttachment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PORTFOLIO_ID")
    private Portfolio portfolio;

    @Builder
    public ImageAttachment(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
